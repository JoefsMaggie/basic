package joe.tags.modal.query;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.collect.Lists;
import joe.elasticsearch.helper.EsQueryHelper;
import joe.tags.common.CusTagField;
import joe.tags.modal.bean.request.CrowdQueryModel;
import joe.tags.modal.bean.request.CrowdQueryReq;
import joe.tags.modal.utils.CrowdQueryHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author : Joe
 * @version V1.0


 * @note: 人群查询
 * Date Date : 2018年09月03日 15:44
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CrowdQuery {
    private final static Logger LOGGER = LoggerFactory.getLogger(CrowdQuery.class);

    private final static String INDEX_KEY = "Joe.es.tags.index";
    @Value("${" + INDEX_KEY + ":customertags}")
    private String customerTags;

    private final TransportClient client;

    @ApolloConfigChangeListener
    public void listen(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged(INDEX_KEY)) {
            final ConfigChange change = changeEvent.getChange(INDEX_KEY);
            LOGGER.info("{} is change to {}, ole value is {} ", INDEX_KEY, change.getNewValue(), change.getOldValue());
            customerTags = change.getNewValue();
        }
    }

    /**
     * 人群详情查询
     *
     * @param crowdQueryModels 人群查询模型列表
     * @param aggs             聚合列表
     * @return 查询结果
     */
    public SearchResponse crowdDetail(List<CrowdQueryModel> crowdQueryModels, List<AggregationBuilder> aggs) {
        BoolQueryBuilder queryBuilder = CrowdQueryHelper.create(crowdQueryModels);
        return crowdDetail(aggs, queryBuilder, Lists.newArrayList(customerTags));
    }

    /**
     * 人群详情查询
     *
     * @param crowdQueryReq 查询关系
     * @param aggs          聚合列表
     * @return 查询结果
     */
    public SearchResponse crowdDetail(CrowdQueryReq crowdQueryReq, List<AggregationBuilder> aggs) {
        QueryBuilder queryBuilder = EsQueryHelper.create(crowdQueryReq.getQueryRelation());
        final List<String> indices = CollectionUtils.isEmpty(crowdQueryReq.getIndices()) ? Lists.newArrayList(customerTags) : crowdQueryReq.getIndices();
        return crowdDetail(aggs, queryBuilder, indices);
    }

    /**
     * 人群详情查询
     *
     * @param aggs         聚合列表
     * @param queryBuilder 查询方式
     * @param indices      索引列表
     * @return 查询结果
     */
    private SearchResponse crowdDetail(List<AggregationBuilder> aggs, QueryBuilder queryBuilder, List<String> indices) {
        LOGGER.info("\nquery: \n{}", queryBuilder);
        SearchRequestBuilder searchRequestBuilder = searchRequestBuilder(indices).setQuery(queryBuilder)
                                                                                 .setFetchSource(false);
        for (AggregationBuilder agg : aggs) {
            LOGGER.info("\nagg: \n{}", agg);
            searchRequestBuilder.addAggregation(agg);
        }
        LOGGER.info("\nquery and aggs: \n{}", searchRequestBuilder);
        return searchRequestBuilder.get();
    }

    public SearchResponse crowd30DaysVolume(List<CrowdQueryModel> crowdQueryModels) {
        BoolQueryBuilder queryBuilder = CrowdQueryHelper.create(crowdQueryModels);
        queryBuilder.must(QueryBuilders.rangeQuery(CusTagField.VOLUME_30DAYS_COUNT).gt(0));
        return searchRequestBuilder(Lists.newArrayList(customerTags))
                .setQuery(queryBuilder)
                .addAggregation(AggregationBuilders.count(CusTagField.VOLUME_30DAYS_COUNT).field(CusTagField.VOLUME_30DAYS_COUNT))
                .get();
    }

    public SearchResponse crowdBoughtOrNot(List<CrowdQueryModel> crowdQueryModels) {
        BoolQueryBuilder queryBuilder = CrowdQueryHelper.create(crowdQueryModels);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        queryBuilder.must(boolQuery);
        boolQuery.should(QueryBuilders.rangeQuery(CusTagField.WSC_COUNT).gt(0))
                 .should(QueryBuilders.rangeQuery(CusTagField.JF_PRODUCT_COUNT).gt(0))
                 .should(QueryBuilders.rangeQuery(CusTagField.PT_COUNT).gt(0))
                 .should(QueryBuilders.rangeQuery(CusTagField.AWARD_ORDER_COUNT).gt(0));
        return searchRequestBuilder(Lists.newArrayList(customerTags))
                .setQuery(queryBuilder)
                .addAggregation(AggregationBuilders.count(CusTagField.ROW_KEY_KEYWORD).field(CusTagField.ROW_KEY_KEYWORD))
                .get();
    }

    public SearchResponse crowdScanOrNot(List<CrowdQueryModel> crowdQueryModels) {
        BoolQueryBuilder queryBuilder = CrowdQueryHelper.create(crowdQueryModels);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        queryBuilder.must(boolQuery);
        boolQuery.should(QueryBuilders.existsQuery(CusTagField.CODE_FWM_STAT))
                 .should(QueryBuilders.existsQuery(CusTagField.CODE_XM_STAT))
                 .should(QueryBuilders.existsQuery(CusTagField.CODE_DGM_STAT))
                 .should(QueryBuilders.existsQuery(CusTagField.CODE_WXM_STAT))
                 .should(QueryBuilders.existsQuery(CusTagField.CODE_WSM_STAT));
        return searchRequestBuilder(Lists.newArrayList(customerTags))
                .setQuery(queryBuilder)
                .addAggregation(AggregationBuilders.count(CusTagField.ROW_KEY_KEYWORD).field(CusTagField.ROW_KEY_KEYWORD))
                .get();
    }

    private SearchRequestBuilder searchRequestBuilder(List<String> indices) {
        return client.prepareSearch(indices.toArray(new String[0]))
                     .setSearchType(SearchType.QUERY_THEN_FETCH)
                     .setFetchSource(false);
    }

    /**
     * 查询人群详情报表时间
     *
     * @return 报表时间
     */
    public String reportDate(List<String> indices) {
        final List<String> indies = CollectionUtils.isEmpty(indices) ? Lists.newArrayList(customerTags) : indices;
        SearchResponse response = searchRequestBuilder(indies)
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(AggregationBuilders.max(CusTagField.LATEST_UPDATE_TIME).field(CusTagField.LATEST_UPDATE_TIME))
                .get();
        Max max = response.getAggregations().get(CusTagField.LATEST_UPDATE_TIME);
        return LocalDateTime.ofEpochSecond((long) (max.getValue() / 1000), 0, ZoneOffset.ofHours(8))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

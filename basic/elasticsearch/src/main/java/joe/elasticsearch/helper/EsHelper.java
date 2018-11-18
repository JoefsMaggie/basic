package joe.elasticsearch.helper;

import com.google.common.collect.Lists;
import joe.elasticsearch.model.aggs.GeneralAgg;
import joe.elasticsearch.model.querys.GeneralQueryAggReq;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.StopWatch;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: ES 帮助类
 * Date Date : 2018年09月20日 14:25
 */
@Component
public class EsHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(EsHelper.class);

    private static TransportClient client;

    /**
     * 线程池，连接数固定为系统 CPU 核数
     */
    private final static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * ES 滚动查询
     *
     * @param scrollId 滚动查询ID
     * @return 查询结果
     */
    public static SearchResponse queryScroll(String scrollId) {
        return client.prepareSearchScroll(scrollId)
                     .setScroll(TimeValue.timeValueMinutes(1))
                     .get();
    }

    /**
     * 清除滚动查询ID
     *
     * @param scrollId 滚动查询ID
     */
    public static boolean cleanScrollId(String scrollId) {
        return cleanScrollIds(Lists.newArrayList(scrollId));
    }

    /**
     * 清除滚动查询ID
     *
     * @param scrollIds 滚动查询ID列表
     */
    public static boolean cleanScrollIds(List<String> scrollIds) {
        Runnable runnable = () -> {
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.scrollIds(scrollIds);
            client.clearScroll(clearScrollRequest).actionGet();
        };
        executor.execute(runnable);
        return true;
    }

    public static Map<String, Object> execute(GeneralQueryAggReq generalQueryAggReq) {
        QueryBuilder query = EsQueryHelper.create(generalQueryAggReq.getConditions());
        List<GeneralAgg> aggs = generalQueryAggReq.getAggs();
        List<AggregationBuilder> aggregationBuilders = EsAggHelper.create(aggs);
        SearchResponse response = queryAggs(query, aggregationBuilders, generalQueryAggReq.getIndices());
        return EsAggHelper.parseAgg(aggs, response.getAggregations());
    }

    public static SearchResponse query(QueryBuilder query, String... indices) {
        return query(query, -1, -1, indices);
    }

    public static SearchResponse query(QueryBuilder query, int size, int from, String... indices) {
        return queryAggs(query, size, from, Collections.emptyList(), indices);
    }

    public static SearchResponse query(QueryBuilder query, int size, int from, List<String> indices) {
        return queryAggs(query, size, from, Collections.emptyList(), indices);
    }

    public static SearchResponse agg(AggregationBuilder agg, String... indices) {
        return aggs(Collections.singletonList(agg), indices);
    }

    public static SearchResponse aggs(List<AggregationBuilder> aggs, String... indices) {
        return queryAggs(null, aggs, indices);
    }

    public static SearchResponse queryAgg(QueryBuilder query, AggregationBuilder agg, String... indices) {
        return queryAggs(query, Collections.singletonList(agg), indices);
    }

    public static SearchResponse queryAgg(QueryBuilder query, AggregationBuilder agg, List<String> indices) {
        return queryAggs(query, Collections.singletonList(agg), indices);
    }

    private static SearchResponse queryAggs(QueryBuilder query, List<AggregationBuilder> aggs, String... indices) {
        return queryAggs(query, 0, 1, aggs, indices);
    }

    private static SearchResponse queryAggs(QueryBuilder query, List<AggregationBuilder> aggs, List<String> indices) {
        return queryAggs(query, 0, 1, aggs, indices);
    }

    private static SearchResponse queryAggs(QueryBuilder query, int size, int from, List<AggregationBuilder> aggs, List<String> indices) {
        return queryAggs(query, size, from, aggs, ArrayUtils.toStringArray(indices.toArray()));
    }

    private static SearchResponse queryAggs(QueryBuilder query, int size, int from, List<AggregationBuilder> aggs, String... indices) {
        if (ArrayUtils.isEmpty(indices)) {
            LOGGER.error("indices 参数异常，不能为空");
            throw new IllegalArgumentException("indices 参数异常，不能为空");
        }
        SearchRequestBuilder esBuilder = client.prepareSearch(indices)
                                               .setSearchType(SearchType.QUERY_THEN_FETCH)
                                               .setSize(size)
                                               .setFrom(from);
        if (Objects.nonNull(query)) {
            LOGGER.info("查询参数： \r\n{}", query);
            esBuilder.setQuery(query);
        }
        if (CollectionUtils.isNotEmpty(aggs)) {
            // 有聚合则不需要返回的 hits
            esBuilder.setFetchSource(false);
            aggs.stream()
                .peek(agg -> LOGGER.info("聚合格式：\r\n{}", agg))
                .forEach(esBuilder::addAggregation);
        }
        LOGGER.info("查询聚合形式：\r\n{}", esBuilder);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SearchResponse searchResponse = esBuilder.get();
        stopWatch.stop();
        TimeValue took = searchResponse.getTook();
        LOGGER.info("ES 查询聚合用时: {} ms, took: {}", stopWatch.totalTime().millis(), took.toString());
        return searchResponse;
    }

    @Autowired
    public void setClient(TransportClient client) {
        EsHelper.client = client;
    }

}

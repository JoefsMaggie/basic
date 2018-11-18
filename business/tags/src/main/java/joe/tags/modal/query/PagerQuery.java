package joe.tags.modal.query;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import joe.elasticsearch.helper.EsQueryHelper;
import joe.tags.modal.bean.request.FieldPagerReq;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 分页 ES 查询层
 * Date Date : 2018年09月20日 12:05
 */
@Component
public class PagerQuery {

    private final static Logger LOGGER = LoggerFactory.getLogger(CrowdQuery.class);
    private final static String INDEX_KEY = "Joe.es.advancedSearch.index";
    private final TransportClient client;

    @Value("${" + INDEX_KEY + ":customertags}")
    private String customerTags;

    @Autowired
    public PagerQuery(TransportClient client) {
        this.client = client;
    }

    @ApolloConfigChangeListener
    public void listen(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged(INDEX_KEY)) {
            final ConfigChange change = changeEvent.getChange(INDEX_KEY);
            LOGGER.info("{} is change to {}, ole value is {} ", INDEX_KEY, change.getNewValue(), change.getOldValue());
            customerTags = change.getNewValue();
        }
    }

    private SearchRequestBuilder searchRequestBuilder(FieldPagerReq fieldPagerReq) {
        final boolean isCustomIndices = ArrayUtils.isEmpty(fieldPagerReq.getIndices());
        return client.prepareSearch(isCustomIndices ? new String[]{customerTags} : fieldPagerReq.getIndices())
                     .setSearchType(SearchType.QUERY_THEN_FETCH);
    }

    /**
     * 不去重分页查询
     *
     * @param fieldPagerReq 根据字段做分页的请求模型
     * @return 查询结果
     */
    public SearchResponse fieldValues(FieldPagerReq fieldPagerReq) {
        QueryBuilder queryBuilder = EsQueryHelper.create(fieldPagerReq.getQueryRelation());
        SearchRequestBuilder searchRequestBuilder = searchRequestBuilder(fieldPagerReq)
                .setFetchSource(fieldPagerReq.getFields(), null)
                .setQuery(queryBuilder);
        if (fieldPagerReq.getPageIndex() == 0) {
            searchRequestBuilder.setScroll(TimeValue.timeValueMinutes(1));
        } else {
            searchRequestBuilder.setFrom(fieldPagerReq.getPageSize() * (fieldPagerReq.getPageIndex() - 1))
                                .setSize(fieldPagerReq.getPageSize());
        }
        if (StringUtils.isNotBlank(fieldPagerReq.getOrderBy())) {
            searchRequestBuilder.addSort(fieldPagerReq.getOrderBy(), SortOrder.DESC);
        }
        LOGGER.info("query: \n{}", searchRequestBuilder);
        return searchRequestBuilder.get();
    }

    /**
     * 去重分页查询
     *
     * @param fieldPagerReq 根据字段做分页的请求模型
     * @return 查询结果
     */
    public SearchResponse distinctFieldValues(FieldPagerReq fieldPagerReq) {
        QueryBuilder queryBuilder = EsQueryHelper.create(fieldPagerReq.getQueryRelation());
//        int size = fieldPagerReq.getPageIndex() == 0 ? Integer.MAX_VALUE : fieldPagerReq.getPageIndex() * fieldPagerReq.getPageSize();
        String distinctField = fieldPagerReq.getFields()[0];
        String[] strings = new String[fieldPagerReq.getFields().length];
        /*
             处理掉聚合需要keyword的字段，
             es聚合遇到分词类型的字段时会根据分词后的结果做聚合，会导致数据不准确，需要使用其keyword字段
             但是需要查询可以不需要keyword
          */
        String[] fields = Arrays.stream(fieldPagerReq.getFields())
                                .parallel()
                                .map(field -> {
                                    int i = field.indexOf(".keyword");
                                    if (i > 0) {
                                        return field.substring(0, i);
                                    } else {
                                        return field;
                                    }
                                })
                                .collect(Collectors.toList())
                                .toArray(strings);
        return searchRequestBuilder(fieldPagerReq)
                .setFetchSource(false)
                .setQuery(queryBuilder)
                .addAggregation(AggregationBuilders.terms(distinctField).field(distinctField).size(Integer.MAX_VALUE)
                                                   .subAggregation(AggregationBuilders.topHits("top")
                                                                                      .fetchSource(fields, null)
                                                                                      .size(1)))
                .get();
    }

    public static void main(String[] args) {

        System.out.println("abc".indexOf(".keyword"));
        String s = "abc.keyword";
        int x = s.indexOf(".keyword");
        System.out.println(s.substring(0, x));
    }
}

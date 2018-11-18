package joe.elasticsearch.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joe.elasticsearch.helper.factory.agg.AggFactories;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper
 * @Description : ES 聚合帮助类
 * @date : 2018年09月30日 11:56
 */
public class EsAggHelper {

    public static List<AggregationBuilder> create(List<GeneralAgg> aggs) {
        List<AggregationBuilder> aggBuilders = Lists.newArrayListWithExpectedSize(aggs.size());
        aggs.forEach(agg -> {
            AggregationBuilder aggBuilder = AggFactories.create(agg);
            aggBuilders.add(aggBuilder);
        });
        return aggBuilders;
    }

    public static Map<String, Object> parseAgg(List<GeneralAgg> aggs, Aggregations aggregations) {
        if (CollectionUtils.isEmpty(aggs)) {
            return Collections.emptyMap();
        }
        Map<String, Object> resMap = Maps.newHashMapWithExpectedSize(aggs.size());
        for (GeneralAgg agg : aggs) {
            Object data = AggFactories.parseAgg(agg, aggregations);
            resMap.put(agg.getReturnName(), data);
        }
        return resMap;
    }
}

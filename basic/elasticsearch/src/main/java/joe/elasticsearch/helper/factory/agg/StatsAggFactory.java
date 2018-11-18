package joe.elasticsearch.helper.factory.agg;

import com.google.common.collect.Maps;
import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * STATS AggregationBuilder Factory
 * Date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.STATS)
public class StatsAggFactory implements AggFactory {
    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        return AggregationBuilders.stats(agg.getAggName()).field(agg.getField());
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        Stats stats = aggregations.get(agg.getAggName());
        Map<String, Object> statsMap = Maps.newHashMapWithExpectedSize(5);
        statsMap.put("avg", stats.getAvg());
        statsMap.put("min", stats.getMin());
        statsMap.put("max", stats.getMax());
        statsMap.put("sum", stats.getSum());
        statsMap.put("count", stats.getCount());
        return statsMap;
    }
}

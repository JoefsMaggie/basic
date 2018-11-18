package joe.elasticsearch.helper.factory.agg;

import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper.factory.agg
 * @Description : DATE_RANGE AggregationBuilder Factory
 * @date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.DATE_RANGE)
public class DateRangeAggFactory implements AggFactory {
    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        // TODO 因时间类型暂未使用，未实现
        return AggregationBuilders.dateRange(agg.getAggName()).field(agg.getField());
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        return null;
    }

}

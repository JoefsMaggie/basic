package joe.elasticsearch.helper.factory.agg;

import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper.factory.agg
 * @Description : MISSING AggregationBuilder Factory
 * @date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.MISSING)
public class MissingAggFactory implements AggFactory {
    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        return AggregationBuilders.missing(agg.getAggName()).field(agg.getField());
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        Missing missing = aggregations.get(agg.getAggName());
        return parseSingleBucketAgg(agg, missing);
    }
}

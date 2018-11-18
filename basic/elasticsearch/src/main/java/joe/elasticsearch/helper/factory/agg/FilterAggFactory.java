package joe.elasticsearch.helper.factory.agg;

import joe.elasticsearch.common.AggType;
import joe.elasticsearch.helper.EsQueryHelper;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * FILTER AggregationBuilder Factory
 * Date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.FILTER)
public class FilterAggFactory implements AggFactory {
    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        QueryBuilder queryBuilder = EsQueryHelper.create(agg.getCondition());
        return AggregationBuilders.filter(agg.getAggName(), queryBuilder);
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        Filter filter = aggregations.get(agg.getAggName());
        return parseSingleBucketAgg(agg, filter);
    }
}

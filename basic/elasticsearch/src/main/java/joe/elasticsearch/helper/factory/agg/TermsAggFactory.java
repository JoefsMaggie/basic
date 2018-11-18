package joe.elasticsearch.helper.factory.agg;

import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * TERMS AggregationBuilder Factory
 * Date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.TERMS)
public class TermsAggFactory implements AggFactory {
    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        TermsAggregationBuilder terms = AggregationBuilders.terms(agg.getAggName());
        fieldOrScript(terms, agg);
        return terms.size(agg.getSize());
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        Terms terms = aggregations.get(agg.getAggName());
        return parseMultiBucketAgg(agg, terms);
    }

}

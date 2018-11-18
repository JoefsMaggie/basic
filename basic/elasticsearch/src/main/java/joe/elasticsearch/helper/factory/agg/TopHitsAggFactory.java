package joe.elasticsearch.helper.factory.agg;

import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper.factory.agg
 * @Description : TOP_HITS AggregationBuilder Factory
 * @date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.TOP_HITS)
public class TopHitsAggFactory implements AggFactory {
    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        return AggregationBuilders.topHits(agg.getAggName()).size(agg.getSize());
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        TopHits topHits = aggregations.get(agg.getAggName());
        return Stream.of(topHits.getHits().getHits())
                     .map(SearchHit::getSourceAsMap)
                     .collect(Collectors.toList());
    }
}

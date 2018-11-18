package joe.elasticsearch.helper.factory.agg;

import joe.core.exception.CommonRuntimeException;
import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * HISTOGRAM AggregationBuilder Factory
 * Date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.HISTOGRAM)
public class HistogramAggFactory implements AggFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(HistogramAggFactory.class);

    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        if (CollectionUtils.isEmpty(agg.getValues())) {
            this.error("AggType 为 HISTOGRAM 时，必须穿入一个值在 values 中");
        }
        if (!NumberUtils.isCreatable(agg.getValues().get(0))) {
            this.error("values 第一个值必须是数值类型");
        }
        HistogramAggregationBuilder builder = AggregationBuilders.histogram(agg.getAggName())
                                                                 .field(agg.getField())
                                                                 .interval(Double.valueOf(agg.getValues().get(0)))
                                                                 .minDocCount(agg.getMinDocCount())
                                                                 .offset(agg.getOffset());
        if (CollectionUtils.isEmpty(agg.getExtendedBounds())) {
            return builder;
        }
        if (agg.getExtendedBounds().stream().allMatch(NumberUtils::isCreatable)) {
            this.error("extendedBounds 的值必须是数值类型");
        }
        return builder.extendedBounds(Double.valueOf(agg.getExtendedBounds().get(0)),
                                      Double.valueOf(agg.getExtendedBounds().get(1)));
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        Histogram histogram = aggregations.get(agg.getAggName());
        return parseMultiBucketAgg(agg, histogram);
    }

    private void error(String msg) {
        CommonRuntimeException commonRuntimeException = new CommonRuntimeException("400", msg);
        LOGGER.error(msg, commonRuntimeException);
        throw commonRuntimeException;
    }
}

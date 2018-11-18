package joe.elasticsearch.helper.factory.agg;

import joe.core.exception.CommonRuntimeException;
import joe.elasticsearch.common.AggType;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * RANGE AggregationBuilder Factory
 * Date : 2018年09月30日 16:29
 */
@Component
@AggFactoryType(AggType.RANGE)
public class RangeAggFactory implements AggFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(RangeAggFactory.class);

    /**
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    @Override
    public AggregationBuilder createAgg(GeneralAgg agg) {
        RangeAggregationBuilder builder = AggregationBuilders.range(agg.getAggName())
                                                             .field(agg.getField());
        List<String> values = agg.getValues();
        if (CollectionUtils.isEmpty(values)) {
            this.error("AggType 为 RANGE 时，values 不能为空");
        }
        int size = values.size();
        if (size % 2 == 1) {
            size = size - 1;
        }
        if (!values.stream().allMatch(NumberUtils::isCreatable)) {
            this.error("values 的值必须是数值类型");
        }
        for (int i = 0; i < size; i = i + 2) {
            String from = values.get(i);
            String to = values.get(++i);
            /*if (Objects.isNull(from)) {
                builder.addUnboundedTo(Double.valueOf(to));
            } else if (Objects.isNull(to)) {
                builder.addUnboundedFrom(Double.valueOf(from));
            } else {
                builder.addRange(Double.valueOf(from), Double.valueOf(to));
            }*/
            builder.addRange(Objects.isNull(from) ? null : Double.valueOf(from), Objects.isNull(to) ? null : Double.valueOf(to));
        }
        return builder;
    }

    @Override
    public Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        Range range = aggregations.get(agg.getAggName());
        return parseMultiBucketAgg(agg, range);
    }

    private void error(String msg) {
        CommonRuntimeException commonRuntimeException = new CommonRuntimeException("400", msg);
        LOGGER.error(msg, commonRuntimeException);
        throw commonRuntimeException;
    }
}

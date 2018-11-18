package joe.elasticsearch.helper.factory.agg;

import com.google.common.collect.Maps;
import joe.elasticsearch.helper.EsAggHelper;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * ES AggregationBuilder Factory
 * Date : 2018年09月30日 14:15
 */
public interface AggFactory {

    /**
     * 生成特定类型的 AGG
     *
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    AggregationBuilder createAgg(GeneralAgg agg);

    /**
     * 解析 AGG 数据
     *
     * @param agg          通用 AGG 模型
     * @param aggregations ES 返回的 AGG
     * @return 解析的数据
     */
    Object parseAgg(GeneralAgg agg, Aggregations aggregations);

    /**
     * 获取聚合名称
     *
     * @param agg 通用AGG模型
     * @return 聚合名称
     */
    default String createAggName(GeneralAgg agg) {
        if (StringUtils.isBlank(agg.getReturnName())) {
            agg.setReturnName(agg.getField() + "." + agg.getType().name().toLowerCase());
        }
        return agg.getReturnName();
    }

    /**
     * 生成 AGG
     *
     * @param agg 通用AGG模型
     * @return AggregationBuilder
     */
    default AggregationBuilder create(GeneralAgg agg) {
        String aggName = createAggName(agg);
        agg.setAggName(aggName);
        AggregationBuilder topAgg = createAgg(agg);
        List<GeneralAgg> subAggs = agg.getSubAggs();
        if (CollectionUtils.isEmpty(subAggs)) {
            return topAgg;
        }
        List<AggregationBuilder> subAggregations = EsAggHelper.create(subAggs);
        subAggregations.forEach(topAgg::subAggregation);
        return topAgg;
    }

    default ValuesSourceAggregationBuilder fieldOrScript(ValuesSourceAggregationBuilder aggregationBuilder, GeneralAgg agg) {
        if (Objects.isNull(agg.getField())) {
            return aggregationBuilder.script(new Script(agg.getScript()));
        } else {
            return aggregationBuilder.field(agg.getField());
        }
    }

    /**
     * 解析单桶聚合的数据
     *
     * @param agg          通用AGG模型
     * @param aggregations ES 返回的 Aggregation
     * @return 数量或带有子聚合的 MAP
     */
    default Object parseSingleBucketAgg(GeneralAgg agg, SingleBucketAggregation aggregations) {
        Aggregations subAggregations = aggregations.getAggregations();
        if (CollectionUtils.isEmpty(subAggregations.asList())) {
            return aggregations.getDocCount();
        }
        Map<String, Object> resMap = Maps.newHashMapWithExpectedSize(subAggregations.asList().size());
        Map<String, Object> subAggMap = EsAggHelper.parseAgg(agg.getSubAggs(), subAggregations);
        resMap.put("count", aggregations.getDocCount());
        resMap.putAll(subAggMap);
        return resMap;
    }

    /**
     * 解析多桶聚合的数据
     *
     * @param agg          通用AGG模型
     * @param aggregations ES 返回的 Aggregation
     * @return MAP，包含子聚合和其本身的数量
     */
    default Map<String, Object> parseMultiBucketAgg(GeneralAgg agg, MultiBucketsAggregation aggregations) {
        List<? extends MultiBucketsAggregation.Bucket> buckets = aggregations.getBuckets();
        Map<String, Object> resMap = Maps.newLinkedHashMapWithExpectedSize(buckets.size());
        buckets.forEach(bucket -> {
            Map<String, Object> subAggMap = EsAggHelper.parseAgg(agg.getSubAggs(), bucket.getAggregations());
            if (subAggMap.isEmpty()) {
                resMap.put(bucket.getKeyAsString(), bucket.getDocCount());
            } else {
                subAggMap.put("count", bucket.getDocCount());
                resMap.put(bucket.getKeyAsString(), subAggMap);
            }
        });
        return resMap;
    }
}

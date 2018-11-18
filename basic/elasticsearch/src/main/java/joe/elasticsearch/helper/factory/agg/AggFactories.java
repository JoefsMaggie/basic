package joe.elasticsearch.helper.factory.agg;

import joe.core.exception.CommonRuntimeException;
import joe.elasticsearch.model.aggs.GeneralAgg;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper.factory.agg
 * @Description : AGG Factory 用于生产 AggregationBuilder
 * @date : 2018年09月30日 14:37
 */
@Component
public class AggFactories {

    private static List<AggFactory> aggFactories;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Map<String, AggFactory> aggFactoryMap = applicationContext.getBeansOfType(AggFactory.class);
        aggFactories = aggFactoryMap.values().parallelStream().collect(Collectors.toList());
    }

    public static AggregationBuilder create(GeneralAgg agg) {
        return findAggFactory(agg)
                .map(aggFactory -> aggFactory.create(agg))
                .orElseThrow(() -> new CommonRuntimeException("500", "创建 AggregationBuilder 异常"));
    }

    public static Object parseAgg(GeneralAgg agg, Aggregations aggregations) {
        return findAggFactory(agg)
                .map(aggFactory -> aggFactory.parseAgg(agg, aggregations))
                .orElseThrow(() -> new CommonRuntimeException("500", "解析 Aggregation 异常"));
    }

    private static Optional<AggFactory> findAggFactory(GeneralAgg agg) {
        return aggFactories.parallelStream()
                           .filter(aggFactory -> aggFactory.getClass().getAnnotation(AggFactoryType.class).value().equals(agg.getType()))
                           .findFirst();
    }
}

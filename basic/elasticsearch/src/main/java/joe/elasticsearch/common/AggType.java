package joe.elasticsearch.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  通用聚合类型
 * Date : 2018年09月25日 09:56
 */
public enum AggType {
    COUNT,
    MAX,
    MIN,
    AVG,
    SUM,
    STATS,
    DISTINCT,
    TERMS,
    HISTOGRAM,
    DATE_HISTOGRAM,
    RANGE,
    DATE_RANGE,
    MISSING,
    FILTER,
    TOP_HITS;

    @JsonCreator
    public static AggType getAggType(String aggType) {
        for (AggType exitsAggType : values()) {
            if (StringUtils.equalsIgnoreCase(exitsAggType.toString(), aggType)) {
                return exitsAggType;
            }
        }
        return null;
    }
}
package joe.elasticsearch.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  数据度量指标
 * Date : 2018年09月07日 10:22
 */
public enum Metric {
    /**
     * 字段存在
     */
    EXISTS,
    /**
     * 字段不存在
     */
    NOT_EXISTS,
    /* ************************************************************
     *                       时间类、数值类                       *
     **************************************************************/
    /**
     * =，可用于字符串
     */
    EQ,
    /**
     * !=，可用于字符串
     */
    NE,
    /**
     * >
     */
    GT,
    /**
     * >=
     */
    GTE,
    /**
     * <
     */
    LT,
    /**
     * <=
     */
    LTE,
    /**
     * (a, b)
     */
    GT_LT,
    /**
     * [a, b)
     */
    GTE_LT,
    /**
     * (a, b]
     */
    GT_LTE,
    /**
     * [a, b]
     */
    GTE_LTE,
    /**
     * > a || < b
     */
    LT_GT,
    /**
     * >= a || < b
     */
    LTE_GT,
    /**
     * > a || <= b
     */
    LT_GTE,
    /**
     * >= a || <= b
     */
    LTE_GTE,

    /* ************************************************************
     *                            字符串                          *
     **************************************************************/
    /**
     * 分词
     */
    PARTICIPLE,
    /**
     * 模糊匹配，*str
     */
    MATCH_FRONT,
    /**
     * 模糊匹配，str*
     */
    MATCH_END,
    /**
     * 模糊匹配，str*str
     */
    MATCH_MID,
    /**
     * 模糊匹配，*str*
     */
    MATCH_FRONT_END,
    /**
     * 模糊匹配，*str*str*
     */
    MATCH_FRONT_MID_END;

    @JsonCreator
    public static Metric getMetric(String metric) {
        for (Metric existsMetric : values()) {
            if (StringUtils.equalsIgnoreCase(existsMetric.toString(), metric)) {
                return existsMetric;
            }
        }
//        throw new CommonRuntimeException("metric 参数错误");
        return null;
    }
}

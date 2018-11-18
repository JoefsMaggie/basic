package joe.tags.modal.utils;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.collect.Lists;
import joe.tags.common.CusTagField;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 人群聚合生成类
 * Date : 2018年09月17日 16:39
 */
@Component
public class CrowdAggHelper {

    private static final String USE_TIME_STAMP_KEY = "useTimeStamp";
    private static boolean USE_TIME_STAMP = BooleanUtils.toBoolean(System.getProperty("useTimeStamp"));

    @Value("${" + USE_TIME_STAMP_KEY + ":false}")
    public void useTimeStamp(boolean useTimeStamp) {
        CrowdAggHelper.USE_TIME_STAMP = useTimeStamp;
    }

    @ApolloConfigChangeListener
    public void listen(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged(USE_TIME_STAMP_KEY)) {
            final ConfigChange useTimeStamp = changeEvent.getChange(USE_TIME_STAMP_KEY);
            CrowdAggHelper.USE_TIME_STAMP = BooleanUtils.toBoolean(useTimeStamp.getNewValue());
        }
    }

    public enum CrowdData {
        ALL, BASIC, FUNNEL, GENDER, ORDER, CODE, JF, COUPON, PHONE
    }

    public static Pair<List<String>, List<AggregationBuilder>> createAggs(CrowdData crowdData) {
        List<String> fields = Collections.emptyList();
        switch (crowdData) {
            case ALL:
                fields = ALL_FIELDS;
                break;
            case BASIC:
                fields = BASIC_FIELDS;
                break;
            case FUNNEL:
                fields = FUNNEL_FIELDS;
                break;
            case GENDER:
                fields = GENDER_FIELDS;
                break;
            case ORDER:
                fields = ORDER_FIELDS;
                break;
            case CODE:
                fields = CODE_FIELDS;
                break;
            case JF:
                fields = JF_FIELDS;
                break;
            case COUPON:
                fields = COUPON_FIELDS;
                break;
            case PHONE:
                fields = PHONE_FIELDS;
                break;
        }
        List<AggregationBuilder> aggs = Lists.newArrayListWithExpectedSize(fields.size());
        fields.forEach(field -> aggs.add(createAgg(field)));
        return Pair.of(fields, aggs);
    }

    private final static List<String> ALL_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD, CusTagField.GZ_STATUS, CusTagField.IS_MEMBER, "activeUsers", CusTagField.GENDER,
            "bought", CusTagField.WSC_COUNT, CusTagField.JF_PRODUCT_COUNT, CusTagField.PT_COUNT, CusTagField.AWARD_ORDER_COUNT,
            "scan", "smartCode", /*CusTagField.CODE_XM_STAT_KEYWORD, CusTagField.CODE_FWM_STAT_KEYWORD,*/ CusTagField.CODE_SYM_STAT_KEYWORD,
            CusTagField.CODE_DGM_STAT_KEYWORD, CusTagField.CODE_WXM_STAT_KEYWORD, CusTagField.CODE_WSM_STAT_KEYWORD,
            CusTagField.JF_BALANCE, CusTagField.VOLUME_30DAYS_COUNT, CusTagField.VOLUME_30DAYS_HAS_USED, CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT,
            CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT,
            CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT
    );
    private final static List<String> BASIC_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD
    );
    private final static List<String> FUNNEL_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD, CusTagField.GZ_STATUS, CusTagField.IS_MEMBER, "activeUsers"
    );
    private final static List<String> GENDER_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD, CusTagField.GENDER
    );
    private final static List<String> ORDER_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD,
            "bought", CusTagField.WSC_COUNT, CusTagField.JF_PRODUCT_COUNT, CusTagField.PT_COUNT, CusTagField.AWARD_ORDER_COUNT
    );
    private final static List<String> CODE_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD, "scan", "smartCode",
            /*CusTagField.CODE_XM_STAT_KEYWORD, CusTagField.CODE_FWM_STAT_KEYWORD,*/ CusTagField.CODE_SYM_STAT_KEYWORD,
            CusTagField.CODE_DGM_STAT_KEYWORD, CusTagField.CODE_WXM_STAT_KEYWORD, CusTagField.CODE_WSM_STAT_KEYWORD
    );
    private final static List<String> JF_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD, CusTagField.JF_BALANCE
    );
    private final static List<String> COUPON_FIELDS = Lists.newArrayList(
            CusTagField.ROW_KEY_KEYWORD,
            CusTagField.VOLUME_30DAYS_COUNT, CusTagField.VOLUME_30DAYS_HAS_USED, CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT,
            CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT,
            CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT, CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT
    );
    private final static List<String> PHONE_FIELDS = Lists.newArrayList(
            CusTagField.IS_BIND_MOBILE
    );

    private static AggregationBuilder createAgg(String field) {
        switch (field) {
            case CusTagField.ROW_KEY_KEYWORD:
                // 总人数
                return AggregationBuilders.count(CusTagField.ROW_KEY_KEYWORD).field(CusTagField.ROW_KEY_KEYWORD);
            case CusTagField.GZ_STATUS:
                // 聚合关注公总号状态，从中获取关注公众号人数
                return AggregationBuilders.terms(CusTagField.GZ_STATUS).field(CusTagField.GZ_STATUS);
            case CusTagField.IS_MEMBER:
                // 聚合是否会员，从中获取会员数量
                return AggregationBuilders.filter(CusTagField.IS_MEMBER, QueryBuilders.termQuery(CusTagField.IS_MEMBER, 1));
            case "activeUsers":
                // 近30天无活跃用户
                // 减31是因为 es 统计的是昨天的数据
                if (USE_TIME_STAMP) {
                    long timeStampBefore30 = LocalDate.now().minusDays(31).atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                    return AggregationBuilders.filter("activeUsers",
                                                      QueryBuilders.boolQuery()
                                                                   .should(QueryBuilders.rangeQuery(CusTagField.LAST_SCAN_TIME_DCRM)
                                                                                        .gte(timeStampBefore30))
                                                                   .should(QueryBuilders.rangeQuery(CusTagField.LAST_ORDER_TIME_DCRM)
                                                                                        .gte(timeStampBefore30))
                                                                   .should(QueryBuilders.rangeQuery(CusTagField.JF_LAST_TIME)
                                                                                        .gte(timeStampBefore30)));
                } else {
                    String dayBefore30 = LocalDate.now().minusDays(31).toString();
                    return AggregationBuilders.filter("activeUsers",
                                                      QueryBuilders.boolQuery()
                                                                   .should(QueryBuilders.rangeQuery(CusTagField.LAST_SCAN_TIME_DCRM_KEYWORD)
                                                                                        .gte(dayBefore30))
                                                                   .should(QueryBuilders.rangeQuery(CusTagField.LAST_ORDER_TIME_DCRM_KEYWORD)
                                                                                        .gte(dayBefore30))
                                                                   .should(QueryBuilders.rangeQuery(CusTagField.JF_LAST_TIME_KEYWORD)
                                                                                        .gte(dayBefore30)));
                }
            case CusTagField.GENDER:
                // 聚合性别
                return AggregationBuilders.terms(CusTagField.GENDER).field(CusTagField.GENDER);
            case "bought":
                // 有订单的用户数
                return AggregationBuilders.filter("bought", QueryBuilders.existsQuery(CusTagField.BUYING_FIRST_BUYING_TIME_KEYWORD));
            case CusTagField.WSC_COUNT:
                // 微商城订单数量
                return AggregationBuilders.sum(CusTagField.WSC_COUNT).field(CusTagField.WSC_COUNT);
            case CusTagField.JF_PRODUCT_COUNT:
                // 积分商城订单数量
                return AggregationBuilders.sum(CusTagField.JF_PRODUCT_COUNT).field(CusTagField.JF_PRODUCT_COUNT);
            case CusTagField.PT_COUNT:
                // 拼团订单数量
                return AggregationBuilders.sum(CusTagField.PT_COUNT).field(CusTagField.PT_COUNT);
            case CusTagField.AWARD_ORDER_COUNT:
                // 送礼订单数量
                return AggregationBuilders.sum(CusTagField.AWARD_ORDER_COUNT).field(CusTagField.AWARD_ORDER_COUNT);
            case "scan":
                // 扫码用户数
                /*return AggregationBuilders.filter("scan", QueryBuilders.boolQuery()
                                                                       .should(QueryBuilders.existsQuery(CusTagField.CODE_XM_STAT))
                                                                       .should(QueryBuilders.existsQuery(CusTagField.CODE_FWM_STAT))
                                                                       .should(QueryBuilders.existsQuery(CusTagField.CODE_SYM_STAT))
                                                                       .should(QueryBuilders.existsQuery(CusTagField.CODE_DGM_STAT))
                                                                       .should(QueryBuilders.existsQuery(CusTagField.CODE_WXM_STAT))
                                                                       .should(QueryBuilders.existsQuery(CusTagField.CODE_WSM_STAT)));*/
                return AggregationBuilders.filter("scan", QueryBuilders.existsQuery(CusTagField.LAST_SCAN_TIME_DCRM_KEYWORD));
            case "smartCode":
                // 智能营销码数量
                return AggregationBuilders.filter("smartCode", QueryBuilders.boolQuery()
                                                                            .should(QueryBuilders.existsQuery(CusTagField.CODE_XM_STAT_KEYWORD))
                                                                            .should(QueryBuilders.existsQuery(CusTagField.CODE_FWM_STAT_KEYWORD)));
            case CusTagField.CODE_XM_STAT_KEYWORD:
                // 箱码数量
                return AggregationBuilders.count(CusTagField.CODE_XM_STAT_KEYWORD).field(CusTagField.CODE_XM_STAT_KEYWORD);
            case CusTagField.CODE_FWM_STAT_KEYWORD:
                // 防伪码数量
                return AggregationBuilders.count(CusTagField.CODE_FWM_STAT_KEYWORD).field(CusTagField.CODE_FWM_STAT_KEYWORD);
            case CusTagField.CODE_SYM_STAT_KEYWORD:
                // 万物溯源码
                return AggregationBuilders.count(CusTagField.CODE_SYM_STAT_KEYWORD).field(CusTagField.CODE_SYM_STAT_KEYWORD);
            case CusTagField.CODE_DGM_STAT_KEYWORD:
                // 超级导购码数量
                return AggregationBuilders.count(CusTagField.CODE_DGM_STAT_KEYWORD).field(CusTagField.CODE_DGM_STAT_KEYWORD);
            case CusTagField.CODE_WXM_STAT_KEYWORD:
                // 微信二维码数量
                return AggregationBuilders.count(CusTagField.CODE_WXM_STAT_KEYWORD).field(CusTagField.CODE_WXM_STAT_KEYWORD);
            case CusTagField.CODE_WSM_STAT_KEYWORD:
                // 智慧微商码数量
                return AggregationBuilders.count(CusTagField.CODE_WSM_STAT_KEYWORD).field(CusTagField.CODE_WSM_STAT_KEYWORD);
            case CusTagField.JF_BALANCE:
                // 积分分布
//                return AggregationBuilders.histogram(CusTagField.JF_BALANCE).field(CusTagField.JF_BALANCE).interval(100).offset(1);
                return AggregationBuilders.range(CusTagField.JF_BALANCE)
                                          .field(CusTagField.JF_BALANCE)
                                          .addRange("1-100", 1, 100)
                                          .addRange("101-500", 101, 500)
                                          .addRange("501-1000", 501, 1000)
                                          .addRange("1001-2000", 1001, 2000)
                                          .addRange("2001-3000", 2001, 3000)
                                          .addRange("3001-4000", 3001, 4000)
                                          .addRange("4001-5000", 4001, 5000)
                                          .addUnboundedFrom("5000以上", 5001);
            case CusTagField.VOLUME_30DAYS_COUNT:
                // 最近30天领取购物券的用户数, 需要大于零
                return AggregationBuilders.filter(CusTagField.VOLUME_30DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.VOLUME_30DAYS_COUNT).gt(0));
            case CusTagField.VOLUME_30DAYS_HAS_USED:
                // 最近30天领取购物券且有使用过的用户数
                return AggregationBuilders.filter(CusTagField.VOLUME_30DAYS_HAS_USED, QueryBuilders.termQuery(CusTagField.VOLUME_30DAYS_HAS_USED, 1));
            case CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT:
                // 近7天每天领券用户数量
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT).gt(0));
            case CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT:
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT).gt(0));
            case CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT:
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT).gt(0));
            case CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT:
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT).gt(0));
            case CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT:
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT).gt(0));
            case CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT:
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT).gt(0));
            case CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT:
                return AggregationBuilders.filter(CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT, QueryBuilders.rangeQuery(CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT).gt(0));
            case CusTagField.IS_BIND_MOBILE:
                return AggregationBuilders.terms(CusTagField.IS_BIND_MOBILE).field(CusTagField.IS_BIND_MOBILE);
            default:
                return null;
        }
    }
}

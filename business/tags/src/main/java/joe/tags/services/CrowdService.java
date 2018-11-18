package joe.tags.services;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joe.tags.common.CusTagField;
import joe.tags.modal.bean.request.CrowdQueryModel;
import joe.tags.modal.bean.request.CrowdQueryReq;
import joe.tag.modal.bean.response.*;
import joe.tags.modal.query.CrowdQuery;
import joe.tags.modal.utils.CrowdAggHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : Joe
 * @version V1.0
 * @Project: basic
 * @Package joe.tag.services
 * @note: 人群服务层
 * @date Date : 2018年09月03日 15:48
 */
@Service
public class CrowdService {
    private final Logger LOGGER = LoggerFactory.getLogger(CrowdService.class);

    private final CrowdQuery crowdQuery;
//    private final HbaseQuery hbaseQuery;

    @Autowired
    public CrowdService(CrowdQuery crowdQuery) {
        this.crowdQuery = crowdQuery;
//        this.hbaseQuery = hbaseQuery;
    }

    /**
     * 人群详情所有数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailAll(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.ALL);
    }

    /**
     * 人群详情基础数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailBasic(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.BASIC);
    }

    /**
     * 人群详情漏斗数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailFunnel(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.FUNNEL);
    }

    /**
     * 人群详情性别数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailGender(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.GENDER);
    }

    /**
     * 人群详情订单数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailOrder(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.ORDER);
    }

    /**
     * 人群详情扫码数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailCode(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.CODE);
    }

    /**
     * 人群详情积分数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailJf(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.JF);
    }

    /**
     * 人群详情优惠券数据
     *
     * @param crowdQueryModels 查询模型
     * @return 人群详情
     */
    public CrowdDetail crowdDetailCoupon(List<CrowdQueryModel> crowdQueryModels) {
        return createCrowdDetail(crowdQueryModels, CrowdAggHelper.CrowdData.COUPON);
    }

    /**
     * 生成人群详情
     *
     * @param crowdQueryModels 查询模型
     * @param crowdData        查询类型
     * @return 人群详情
     */
    private CrowdDetail createCrowdDetail(List<CrowdQueryModel> crowdQueryModels, CrowdAggHelper.CrowdData crowdData) {
        Pair<List<String>, List<AggregationBuilder>> fieldAggsPair = CrowdAggHelper.createAggs(crowdData);
        SearchResponse response = crowdQuery.crowdDetail(crowdQueryModels, fieldAggsPair.getRight());
        return createCrowdDetail(fieldAggsPair.getLeft(), response)
                .setDate(reportDate(Collections.emptyList()));
    }

    /**
     * 人群详情所有数据
     *
     * @param queryRelation 查询关系
     * @return 人群详情
     */
    public CrowdDetail crowdDetailAll(CrowdQueryReq queryRelation) {
        return createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.ALL);
    }

    /**
     * 人群详情基础数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情基础数据
     */
    public CrowdBasic crowdDetailBasic(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.BASIC);
        return new CrowdBasic().setPersonCount(crowdDetail.getTotalUsers())
                               .setReportDate(crowdDetail.getDate());
    }

    /**
     * 人群详情基础数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情基础数据模型
     */
    public CrowdFunnel crowdDetailFunnel(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.FUNNEL);
        return new CrowdFunnel().setTotalCount(crowdDetail.getTotalUsers())
                                .setMemberCount(crowdDetail.getMemberUsers())
                                .setSubscribeCount(crowdDetail.getConcernUsers())
                                .setNotSubscribeCount(crowdDetail.getNotConcernUsers())
                                .setNotActiveCount(crowdDetail.getLast30DaysInactiveUsers());
    }

    /**
     * 人群详情性别数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情性别数据模型
     */
    public CrowdGender crowdDetailGender(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.GENDER);
        return new CrowdGender().setMale(genderCount(crowdDetail, Gender.GenderType.MALE))
                                .setFemale(genderCount(crowdDetail, Gender.GenderType.FEMALE))
                                .setUnknown(genderCount(crowdDetail, Gender.GenderType.UNKNOWN));
    }

    private long genderCount(CrowdDetail crowdDetail, Gender.GenderType genderType) {
        return crowdDetail.getGenders()
                          .stream()
                          .filter(gender -> gender.getGender() == genderType)
                          .findFirst()
                          .map(BasicNumModel::getCount)
                          .orElse(0L);
    }

    /**
     * 人群详情订单数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情订单数据模型
     */
    public CrowdOrder crowdDetailOrder(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.ORDER);
        return new CrowdOrder().setBuyPersonCount(crowdDetail.getBoughtUsers())
                               .setNotBuyPersonCount(crowdDetail.getNotBoughtUsers())
                               .setWsOrdersCount(orderCount(crowdDetail, Order.OrderType.MICRO_MALL))
                               .setGroupOrdersCount(orderCount(crowdDetail, Order.OrderType.SPELL_GROUP))
                               .setIntegralOrdersCount(orderCount(crowdDetail, Order.OrderType.INTEGRAL_MALL))
                               .setAwardOrdersCount(orderCount(crowdDetail, Order.OrderType.PRIZE));
    }

    private long orderCount(CrowdDetail crowdDetail, Order.OrderType orderType) {
        return crowdDetail.getOrders()
                          .stream()
                          .filter(order -> order.getOrderType() == orderType)
                          .findFirst()
                          .map(BasicNumModel::getCount)
                          .orElse(0L);
    }

    /**
     * 人群详情扫码数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情扫码数据模型
     */
    public CrowdCode crowdDetailCode(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.CODE);
        return new CrowdCode().setScanPersonCount(crowdDetail.getScanCodeUsers())
                              .setNotScanPersonCount(crowdDetail.getNotScanCodeUsers())
                              .setFwmPersonCount(codeCount(crowdDetail, Code.CodeType.SMART_MARKETING))
                              .setWsmPersonCount(codeCount(crowdDetail, Code.CodeType.WISDOM_MICRO_MALL))
                              .setDgmPersonCount(codeCount(crowdDetail, Code.CodeType.SUPER_SHOPPERS))
                              .setSymPersonCount(codeCount(crowdDetail, Code.CodeType.TRACE_SOURCE))
                              .setFwm4PersonCount(codeCount(crowdDetail, Code.CodeType.WECHAT));
    }

    private long codeCount(CrowdDetail crowdDetail, Code.CodeType codeType) {
        return crowdDetail.getCodes()
                          .stream()
                          .filter(code -> code.getCodeType() == codeType)
                          .findFirst()
                          .map(BasicNumModel::getCount)
                          .orElse(0L);
    }

    /**
     * 人群详情积分数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情积分数据模型
     */
    public CrowdIntegral crowdDetailJf(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.JF);
        return new CrowdIntegral().setDistributions(crowdDetail.getIntegralDistributions());
    }

    /**
     * 人群详情优惠券数据
     *
     * @param queryRelation 查询模型
     * @return 人群详情优惠券数据模型
     */
    public CrowdCoupon crowdDetailCoupon(@Valid CrowdQueryReq queryRelation) {
        CrowdDetail crowdDetail = createCrowdDetail(queryRelation, CrowdAggHelper.CrowdData.COUPON);
        return new CrowdCoupon().setCouponPersonCount(crowdDetail.getCouponUsers())
                                .setBuyPersonCount(crowdDetail.getCouponOrderUsers())
                                .setNotBuyPersonCount(crowdDetail.getCouponNoOrderUsers())
                                .setBuyPersonRate(Double.valueOf(crowdDetail.getShoppingRate().substring(0, crowdDetail.getShoppingRate().length() - 1)))
                                .setCouponDatePersonCount(crowdDetail.getCouponTrend());
    }

    /**
     * 生成人群详情
     *
     * @param crowdQueryReq 查询关系
     * @param crowdData     查询类型
     * @return 人群详情
     */
    private CrowdDetail createCrowdDetail(CrowdQueryReq crowdQueryReq, CrowdAggHelper.CrowdData crowdData) {
        Pair<List<String>, List<AggregationBuilder>> fieldAggsPair = CrowdAggHelper.createAggs(crowdData);
        SearchResponse response = crowdQuery.crowdDetail(crowdQueryReq, fieldAggsPair.getRight());
        return createCrowdDetail(fieldAggsPair.getLeft(), response)
                .setDate(reportDate(crowdQueryReq.getIndices()));
    }

    /**
     * 查询报告时间
     *
     * @param indices 查询的索引列表
     * @return 时间字符串
     */
    private String reportDate(List<String> indices) {
        return crowdQuery.reportDate(indices);
//        return hbaseQuery.queryDate();
    }

    /**
     * 解析聚合的数据
     *
     * @param fields   聚合的字段列表
     * @param response es 返回对象
     * @return 人群详情
     */
    private CrowdDetail createCrowdDetail(List<String> fields, SearchResponse response) {
        // 总人数
        ValueCount valueCount = response.getAggregations().get(CusTagField.ROW_KEY_KEYWORD);
        long users = valueCount.getValue();
        CrowdDetail crowdDetail = new CrowdDetail().setTotalUsers(users);
        for (String field : fields) {
            switch (field) {
                case CusTagField.GZ_STATUS: // 聚合关注公总号状态，从中获取关注公众号人数
                case CusTagField.IS_MEMBER: // 聚合是否会员，从中获取会员数量
                    break;
                case "activeUsers": // 近30天无活跃用户
                    Terms gzTerms = response.getAggregations().get(CusTagField.GZ_STATUS);
                    Filter memberFilter = response.getAggregations().get(CusTagField.IS_MEMBER);
                    Filter activeUsersFilter = response.getAggregations().get("activeUsers");
                    crowdDetail.setConcernUsers(termsDocCountByKey(gzTerms, "1"))
                               .setNotConcernUsers(termsDocCountByKey(gzTerms, "0"))
                               .setMemberUsers(memberFilter.getDocCount())
                               .setLast30DaysInactiveUsers(users - activeUsersFilter.getDocCount());
                    break;
                case CusTagField.GENDER: // 聚合性别
                    Terms genderTerms = response.getAggregations().get(CusTagField.GENDER);
                    List<Gender> genders = genderTerms.getBuckets()
                                                      .stream()
                                                      .map(bucket -> Gender.builder(bucket.getKeyAsString(), bucket.getDocCount()))
                                                      .collect(Collectors.toList());
                    crowdDetail.setGenders(genders);
                    break;
                case "bought": // 有订单的用户数
                    Filter boughtFilter = response.getAggregations().get("bought");
                    Sum wscCountSum = response.getAggregations().get(CusTagField.WSC_COUNT);
                    Sum jfProductCountSum = response.getAggregations().get(CusTagField.JF_PRODUCT_COUNT);
                    Sum ptCountSum = response.getAggregations().get(CusTagField.PT_COUNT);
                    Sum awardOrderCountSum = response.getAggregations().get(CusTagField.AWARD_ORDER_COUNT);
                    crowdDetail.setBoughtUsers(boughtFilter.getDocCount())
                               .setNotBoughtUsers(users - boughtFilter.getDocCount())
                               .setOrders(Lists.newArrayList(
                                       Order.builder(CusTagField.WSC_COUNT, (long) wscCountSum.getValue()),
                                       Order.builder(CusTagField.JF_PRODUCT_COUNT, (long) jfProductCountSum.getValue()),
                                       Order.builder(CusTagField.PT_COUNT, (long) ptCountSum.getValue()),
                                       Order.builder(CusTagField.AWARD_ORDER_COUNT, (long) awardOrderCountSum.getValue())
                               ));
                    break;
                case CusTagField.WSC_COUNT: // 微商城订单数量
                case CusTagField.JF_PRODUCT_COUNT: // 积分商城订单数量
                case CusTagField.PT_COUNT: // 拼团订单数量
                case CusTagField.AWARD_ORDER_COUNT: // 送礼订单数量
                    break;
                case "scan": // 扫码用户数
                    Filter scanFilter = response.getAggregations().get("scan");
                    /*ValueCount codeXmStatCount = response.getAggregations().get(CusTagField.CODE_XM_STAT_KEYWORD);
                    ValueCount codeFwmStatCount = response.getAggregations().get(CusTagField.CODE_FWM_STAT_KEYWORD);*/
                    Filter smartCodeFilter = response.getAggregations().get("smartCode");
                    ValueCount codeSymStatCount = response.getAggregations().get(CusTagField.CODE_SYM_STAT_KEYWORD);
                    ValueCount codeDgmStatCount = response.getAggregations().get(CusTagField.CODE_DGM_STAT_KEYWORD);
                    ValueCount codeWxmStatCount = response.getAggregations().get(CusTagField.CODE_WXM_STAT_KEYWORD);
                    ValueCount codeWsmStatCount = response.getAggregations().get(CusTagField.CODE_WSM_STAT_KEYWORD);
                    crowdDetail
                            .setScanCodeUsers(scanFilter.getDocCount())
                            .setNotScanCodeUsers(users - scanFilter.getDocCount())
                            .setCodes(Lists.newArrayList(
                                    /*Code.builder(CusTagField.CODE_XM_STAT, codeXmStatCount.getValue()),
                                    Code.builder(CusTagField.CODE_FWM_STAT, codeFwmStatCount.getValue()),*/
                                    Code.builder(CusTagField.CODE_FWM_STAT, smartCodeFilter.getDocCount()),
                                    Code.builder(CusTagField.CODE_SYM_STAT, codeSymStatCount.getValue()),
                                    Code.builder(CusTagField.CODE_DGM_STAT, codeDgmStatCount.getValue()),
                                    Code.builder(CusTagField.CODE_WXM_STAT, codeWxmStatCount.getValue()),
                                    Code.builder(CusTagField.CODE_WSM_STAT, codeWsmStatCount.getValue()))
                                           .stream()
                                           .collect(Collectors.groupingBy(Code::getCodeType,
                                                                          Collectors.reducing((c1, c2) -> c1.setCount(c1.getCount() + c2.getCount()))))
                                           .values()
                                           .stream()
                                           .map(v -> v.orElseGet(Code::new))
                                           .collect(Collectors.toList()));
                    break;
                case CusTagField.CODE_XM_STAT_KEYWORD:  // 箱码数量
                case CusTagField.CODE_FWM_STAT_KEYWORD: // 防伪码数量
                case CusTagField.CODE_SYM_STAT_KEYWORD: // 万物溯源码
                case CusTagField.CODE_DGM_STAT_KEYWORD: // 超级导购码数量
                case CusTagField.CODE_WXM_STAT_KEYWORD: // 微信二维码数量
                case CusTagField.CODE_WSM_STAT_KEYWORD: // 智慧微商码数量
                    break;
                case CusTagField.JF_BALANCE:
                    // 积分分布
//                    Histogram jfBalanceHistogram = response.getAggregations().get(CusTagField.JF_BALANCE);
//                    crowdDetail.setIntegralDistributions(dealWithJfHistogram(jfBalanceHistogram));
                    Range jfBalanceRange = response.getAggregations().get(CusTagField.JF_BALANCE);
                    crowdDetail.setIntegralDistributions(dealWithJfRange(jfBalanceRange));
                    break;
                case CusTagField.VOLUME_30DAYS_COUNT: // 最近30天领取购物券的用户数, 需要大于零
                    LocalDate now = LocalDate.now();
                    Filter volume30DaysFilter = response.getAggregations().get(CusTagField.VOLUME_30DAYS_COUNT);
                    Filter volume30DaysUsedFilter = response.getAggregations().get(CusTagField.VOLUME_30DAYS_HAS_USED);
                    Filter volumeLast1DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT);
                    Filter volumeLast2DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT);
                    Filter volumeLast3DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT);
                    Filter volumeLast4DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT);
                    Filter volumeLast5DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT);
                    Filter volumeLast6DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT);
                    Filter volumeLast7DaysFilter = response.getAggregations().get(CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT);
                    long volume30DaysUsedCount = volume30DaysUsedFilter.getDocCount();
                    crowdDetail.setCouponUsers(volume30DaysFilter.getDocCount())
                               .setCouponOrderUsers(volume30DaysUsedCount)
                               .setCouponNoOrderUsers(volume30DaysFilter.getDocCount() - volume30DaysUsedCount)
                               .setShoppingRate(BasicNumModel.DECIMAL_FORMAT.format(volume30DaysFilter.getDocCount() == 0 ? 0 : volume30DaysUsedCount / (double) volume30DaysFilter.getDocCount()))
                               .setCouponTrend(Lists.newArrayList(
                                       DateCount.builder(now.minusDays(1).toString(), volumeLast1DaysFilter.getDocCount()),
                                       DateCount.builder(now.minusDays(2).toString(), volumeLast2DaysFilter.getDocCount()),
                                       DateCount.builder(now.minusDays(3).toString(), volumeLast3DaysFilter.getDocCount()),
                                       DateCount.builder(now.minusDays(4).toString(), volumeLast4DaysFilter.getDocCount()),
                                       DateCount.builder(now.minusDays(5).toString(), volumeLast5DaysFilter.getDocCount()),
                                       DateCount.builder(now.minusDays(6).toString(), volumeLast6DaysFilter.getDocCount()),
                                       DateCount.builder(now.minusDays(7).toString(), volumeLast7DaysFilter.getDocCount())));
                case CusTagField.VOLUME_30DAYS_HAS_USED: // 最近30天领取购物券且有使用过的用户数
                case CusTagField.ASSET_VOLUME_LAST1DAYS_COUNT: // 近7天每天领券用户数量
                case CusTagField.ASSET_VOLUME_LAST2DAYS_COUNT:
                case CusTagField.ASSET_VOLUME_LAST3DAYS_COUNT:
                case CusTagField.ASSET_VOLUME_LAST4DAYS_COUNT:
                case CusTagField.ASSET_VOLUME_LAST5DAYS_COUNT:
                case CusTagField.ASSET_VOLUME_LAST6DAYS_COUNT:
                case CusTagField.ASSET_VOLUME_LAST7DAYS_COUNT:
                default:
                    break;
            }
        }
        return crowdDetail;
    }

    /**
     * 根据key获取terms中的文档数
     *
     * @param terms terms
     * @param key   key
     * @return 数量
     */
    private long termsDocCountByKey(Terms terms, String key) {
        Terms.Bucket bucket = null;
        try {
            bucket = terms.getBucketByKey(key);
        } catch (Exception e) {
            LOGGER.error("error: \nkey = {}\nterms = {}", key, terms);
        }
        return Objects.isNull(bucket) ? 0L : bucket.getDocCount();
    }

    /**
     * 处理积分直方图数据
     *
     * @param jfBalanceHistogram 积分直方图聚合
     * @return 以时间为 key ，数量为 value 的 map
     */
    private List<IntegralCount> dealWithJfHistogram(Histogram jfBalanceHistogram) {
        Map<String, Long> map = Maps.newLinkedHashMap();
        map.put("1-100", 0L);
        map.put("101-500", 0L);
        map.put("501-1000", 0L);
        map.put("1001-2000", 0L);
        map.put("2001-3000", 0L);
        map.put("3001-4000", 0L);
        map.put("4001-5000", 0L);
        map.put("5000以上", 0L);
        for (Histogram.Bucket bucket : jfBalanceHistogram.getBuckets()) {
            Double key = Double.valueOf(bucket.getKeyAsString());
            if (key >= 1 && key < 101) {
                map.put("1-100", map.get("1-100") + bucket.getDocCount());
            } else if (key >= 101 && key < 501) {
                map.put("101-500", map.get("101-500") + bucket.getDocCount());
            } else if (key >= 501 && key < 1001) {
                map.put("501-1000", map.get("501-1000") + bucket.getDocCount());
            } else if (key >= 1001 && key < 2001) {
                map.put("1001-2000", map.get("1001-2000") + bucket.getDocCount());
            } else if (key >= 2001 && key < 3001) {
                map.put("2001-3000", map.get("2001-3000") + bucket.getDocCount());
            } else if (key >= 3001 && key < 4001) {
                map.put("3001-4000", map.get("3001-4000") + bucket.getDocCount());
            } else if (key >= 4001 && key < 5001) {
                map.put("4001-5000", map.get("4001-5000") + bucket.getDocCount());
            } else if (key >= 5001) {
                map.put("5000以上", map.get("5000以上") + bucket.getDocCount());
            }
        }
        return map.entrySet()
                  .stream()
                  .map(entry -> IntegralCount.builder(entry.getKey(), entry.getValue()))
                  .collect(Collectors.toList());
    }

    private List<IntegralCount> dealWithJfRange(Range jfBalanceRange) {
        Map<String, Long> map = Maps.newLinkedHashMap();
        map.put("1-100", 0L);
        map.put("101-500", 0L);
        map.put("501-1000", 0L);
        map.put("1001-2000", 0L);
        map.put("2001-3000", 0L);
        map.put("3001-4000", 0L);
        map.put("4001-5000", 0L);
        map.put("5000以上", 0L);
        for (Range.Bucket bucket : jfBalanceRange.getBuckets()) {
            map.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return map.entrySet()
                  .stream()
                  .map(entry -> IntegralCount.builder(entry.getKey(), entry.getValue()))
                  .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now());
        String rate = "9.99%";
        System.out.println(rate.substring(0, rate.length() - 1));
    }
}

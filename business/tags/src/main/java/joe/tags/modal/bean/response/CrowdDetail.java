package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import joe.core.utils.Jackson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.tag.modal.bean.response
 * @note: 人群分析详情dto
 * @date Date : 2018年09月10日 15:27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "人群详情模型")
public class CrowdDetail {
    /**
     * 日期
     */
    @ApiModelProperty("数据产生日期")
    private String date;
    /**
     * 总用户数
     */
    @ApiModelProperty("总用户数")
    private Long totalUsers;
    /**
     * 关注公众号用户数
     */
    @ApiModelProperty("关注公众号用户数")
    private Long concernUsers;
    /**
     * 未关注公众号用户数
     */
    @ApiModelProperty("未关注公众号用户数")
    private Long notConcernUsers;
    /**
     * 会员数
     */
    @ApiModelProperty("会员数")
    private Long memberUsers;
    /**
     * 近30天无活跃用户
     */
    @ApiModelProperty("近30天无活跃用户")
    private Long last30DaysInactiveUsers;
    /**
     * 性别属性
     */
    @ApiModelProperty("性别属性")
    private List<Gender> genders;
    /**
     * 购买过用户数
     */
    @ApiModelProperty("购买过用户数")
    private Long boughtUsers;
    /**
     * 未购买过用户数
     */
    @ApiModelProperty("未购买过用户数")
    private Long notBoughtUsers;
    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private List<Order> orders;
    /**
     * 扫码用户
     */
    @ApiModelProperty("扫码用户")
    private Long scanCodeUsers;
    /**
     * 未扫码用户
     */
    @ApiModelProperty("未扫码用户")
    private Long notScanCodeUsers;
    /**
     * 扫码类型
     */
    @ApiModelProperty("扫码类型")
    private List<Code> codes;
    /**
     * 积分分布
     */
    @ApiModelProperty("积分分布")
    private List<IntegralCount> integralDistributions;
    /**
     * 领券用户数
     */
    @ApiModelProperty("领券用户数")
    private Long couponUsers;
    /**
     * 领券下单用户数
     */
    @ApiModelProperty("领券下单用户数")
    private Long couponOrderUsers;
    /**
     * 领券未下单用户数
     */
    @ApiModelProperty("领券未下单用户数")
    private Long couponNoOrderUsers;
    /**
     * 购物转化率
     */
    @ApiModelProperty("购物转化率")
    private String shoppingRate;
    /**
     * 领券用户变化
     */
    @ApiModelProperty("领券用户变化")
    private List<DateCount> couponTrend;

    @Override
    public String toString() {
        return "CrowdDetail" + toJson();
    }

    public String toJson() {
        return Jackson.toJson(this);
    }

    public String getDate() {
        return date;
    }

    public CrowdDetail setDate(String date) {
        this.date = date;
        return this;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public CrowdDetail setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }

    public Long getConcernUsers() {
        return concernUsers;
    }

    public CrowdDetail setConcernUsers(Long concernUsers) {
        this.concernUsers = concernUsers;
        return this;
    }

    public Long getNotConcernUsers() {
        return notConcernUsers;
    }

    public CrowdDetail setNotConcernUsers(Long notConcernUsers) {
        this.notConcernUsers = notConcernUsers;
        return this;
    }

    public Long getMemberUsers() {
        return memberUsers;
    }

    public CrowdDetail setMemberUsers(Long memberUsers) {
        this.memberUsers = memberUsers;
        return this;
    }

    public Long getLast30DaysInactiveUsers() {
        return last30DaysInactiveUsers;
    }

    public CrowdDetail setLast30DaysInactiveUsers(Long last30DaysInactiveUsers) {
        this.last30DaysInactiveUsers = last30DaysInactiveUsers;
        return this;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public CrowdDetail setGenders(List<Gender> genders) {
        this.genders = genders;
        return this;
    }

    public Long getBoughtUsers() {
        return boughtUsers;
    }

    public CrowdDetail setBoughtUsers(Long boughtUsers) {
        this.boughtUsers = boughtUsers;
        return this;
    }

    public Long getNotBoughtUsers() {
        return notBoughtUsers;
    }

    public CrowdDetail setNotBoughtUsers(Long notBoughtUsers) {
        this.notBoughtUsers = notBoughtUsers;
        return this;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public CrowdDetail setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public Long getScanCodeUsers() {
        return scanCodeUsers;
    }

    public CrowdDetail setScanCodeUsers(Long scanCodeUsers) {
        this.scanCodeUsers = scanCodeUsers;
        return this;
    }

    public Long getNotScanCodeUsers() {
        return notScanCodeUsers;
    }

    public CrowdDetail setNotScanCodeUsers(Long notScanCodeUsers) {
        this.notScanCodeUsers = notScanCodeUsers;
        return this;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public CrowdDetail setCodes(List<Code> codes) {
        this.codes = codes;
        return this;
    }

    public List<IntegralCount> getIntegralDistributions() {
        return integralDistributions;
    }

    public CrowdDetail setIntegralDistributions(List<IntegralCount> integralDistributions) {
        this.integralDistributions = integralDistributions;
        return this;
    }

    public Long getCouponUsers() {
        return couponUsers;
    }

    public CrowdDetail setCouponUsers(Long couponUsers) {
        this.couponUsers = couponUsers;
        return this;
    }

    public Long getCouponOrderUsers() {
        return couponOrderUsers;
    }

    public CrowdDetail setCouponOrderUsers(Long couponOrderUsers) {
        this.couponOrderUsers = couponOrderUsers;
        return this;
    }

    public Long getCouponNoOrderUsers() {
        return couponNoOrderUsers;
    }

    public CrowdDetail setCouponNoOrderUsers(Long couponNoOrderUsers) {
        this.couponNoOrderUsers = couponNoOrderUsers;
        return this;
    }

    public String getShoppingRate() {
        return shoppingRate;
    }

    public CrowdDetail setShoppingRate(String shoppingRate) {
        this.shoppingRate = shoppingRate;
        return this;
    }

    public List<DateCount> getCouponTrend() {
        return couponTrend;
    }

    public CrowdDetail setCouponTrend(List<DateCount> couponTrend) {
        this.couponTrend = couponTrend;
        return this;
    }
}

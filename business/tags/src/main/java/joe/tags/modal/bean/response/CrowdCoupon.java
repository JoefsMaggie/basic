package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 人群详情优惠券使用模型
 * Date Date : 2018年09月19日 19:42
 */
public class CrowdCoupon {
    @JsonProperty("coupon_person_count")
    private long couponPersonCount;
    @JsonProperty("not_buy_person_count")
    private long notBuyPersonCount;
    @JsonProperty("buy_person_count")
    private long buyPersonCount;
    @JsonProperty("buy_person_rate")
    private double buyPersonRate;
    @JsonProperty("coupon_date_person_count")
    private List<DateCount> couponDatePersonCount;

    public long getCouponPersonCount() {
        return couponPersonCount;
    }

    public CrowdCoupon setCouponPersonCount(long couponPersonCount) {
        this.couponPersonCount = couponPersonCount;
        return this;
    }

    public long getNotBuyPersonCount() {
        return notBuyPersonCount;
    }

    public CrowdCoupon setNotBuyPersonCount(long notBuyPersonCount) {
        this.notBuyPersonCount = notBuyPersonCount;
        return this;
    }

    public long getBuyPersonCount() {
        return buyPersonCount;
    }

    public CrowdCoupon setBuyPersonCount(long buyPersonCount) {
        this.buyPersonCount = buyPersonCount;
        return this;
    }

    public double getBuyPersonRate() {
        return buyPersonRate;
    }

    public CrowdCoupon setBuyPersonRate(double buyPersonRate) {
        this.buyPersonRate = buyPersonRate;
        return this;
    }

    public List<DateCount> getCouponDatePersonCount() {
        return couponDatePersonCount;
    }

    public CrowdCoupon setCouponDatePersonCount(List<DateCount> couponDatePersonCount) {
        this.couponDatePersonCount = couponDatePersonCount;
        return this;
    }
}

package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.response
 * @note: 人群详情订单数据模型
 * @date Date : 2018年09月19日 19:40
 */
public class CrowdOrder {
    @JsonProperty("buy_person_count")
    private long buyPersonCount;
    @JsonProperty("not_buy_person_count")
    private long notBuyPersonCount;
    @JsonProperty("ws_orders_count")
    private long wsOrdersCount;
    @JsonProperty("integral_orders_count")
    private long integralOrdersCount;
    @JsonProperty("group_orders_count")
    private long groupOrdersCount;
    @JsonProperty("award_orders_count")
    private long awardOrdersCount;

    public long getBuyPersonCount() {
        return buyPersonCount;
    }

    public CrowdOrder setBuyPersonCount(long buyPersonCount) {
        this.buyPersonCount = buyPersonCount;
        return this;
    }

    public long getNotBuyPersonCount() {
        return notBuyPersonCount;
    }

    public CrowdOrder setNotBuyPersonCount(long notBuyPersonCount) {
        this.notBuyPersonCount = notBuyPersonCount;
        return this;
    }

    public long getWsOrdersCount() {
        return wsOrdersCount;
    }

    public CrowdOrder setWsOrdersCount(long wsOrdersCount) {
        this.wsOrdersCount = wsOrdersCount;
        return this;
    }

    public long getIntegralOrdersCount() {
        return integralOrdersCount;
    }

    public CrowdOrder setIntegralOrdersCount(long integralOrdersCount) {
        this.integralOrdersCount = integralOrdersCount;
        return this;
    }

    public long getGroupOrdersCount() {
        return groupOrdersCount;
    }

    public CrowdOrder setGroupOrdersCount(long groupOrdersCount) {
        this.groupOrdersCount = groupOrdersCount;
        return this;
    }

    public long getAwardOrdersCount() {
        return awardOrdersCount;
    }

    public CrowdOrder setAwardOrdersCount(long awardOrdersCount) {
        this.awardOrdersCount = awardOrdersCount;
        return this;
    }
}

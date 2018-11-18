package joe.tags.modal.bean.response;

import joe.tags.common.CusTagField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.tag.modal.bean.response
 * @note: 人群分析详情dto
 * @date Date : 2018年09月10日 15:27
 */
@ApiModel(description = "订单", parent = BasicNumModel.class)
public class Order extends BasicNumModel<Order> {
    public enum OrderType {
        /**
         * 微商城订单
         */
        MICRO_MALL,
        /**
         * 积分商城订单
         */
        INTEGRAL_MALL,
        /**
         * 拼团订单
         */
        SPELL_GROUP,
        /**
         * 奖品订单
         */
        PRIZE
    }

    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private OrderType orderType;

    public static Order builder(String orderType, long count) {
        switch (orderType) {
            case CusTagField.WSC_COUNT:
                return new Order().setOrderType(OrderType.MICRO_MALL).setCount(count);
            case CusTagField.JF_PRODUCT_COUNT:
                return new Order().setOrderType(OrderType.INTEGRAL_MALL).setCount(count);
            case CusTagField.PT_COUNT:
                return new Order().setOrderType(OrderType.SPELL_GROUP).setCount(count);
            case CusTagField.AWARD_ORDER_COUNT:
                return new Order().setOrderType(OrderType.PRIZE).setCount(count);
            default:
                return null;
        }
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Order setOrderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }
}
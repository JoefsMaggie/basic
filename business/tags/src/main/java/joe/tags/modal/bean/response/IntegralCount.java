package joe.tags.modal.bean.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.response
 * @note: 积分分布
 * @date Date : 2018年09月19日 15:06
 */
@ApiModel(parent = BasicNumModel.class)
public class IntegralCount extends BasicNumModel<IntegralCount> {

    @ApiModelProperty("积分分布")
    private String integral;

    public static IntegralCount builder(String integral, Long count) {
        return new IntegralCount(integral, count);
    }

    public IntegralCount() {}

    public IntegralCount(String integral, Long count) {
        this.integral = integral;
        this.count = count;
    }

    public String getIntegral() {
        return integral;
    }

    public IntegralCount setIntegral(String integral) {
        this.integral = integral;
        return this;
    }
}

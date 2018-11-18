package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.DecimalFormat;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 基础数量模型
 * Date Date : 2018年09月10日 16:12
 */
@ApiModel()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicNumModel<T extends BasicNumModel> {
    public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00%");
    /**
     * 数量
     */
    @ApiModelProperty("数量")
    protected Long count;
    /**
     * 比率
     */
    @ApiModelProperty("比率")
    protected String rate;

    public Long getCount() {
        return count;
    }

    public T setCount(Long count) {
        this.count = count;
        return (T) this;
    }

    public String getRate() {
        return rate;
    }

    public T setRate(String rate) {
        this.rate = rate;
        return (T) this;
    }
}

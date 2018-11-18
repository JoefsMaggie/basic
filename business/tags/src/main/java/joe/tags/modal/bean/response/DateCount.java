package joe.tags.modal.bean.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 时间 key value
 * Date Date : 2018年09月19日 14:56
 */
@ApiModel(parent = BasicNumModel.class)
public class DateCount extends BasicNumModel<DateCount> {
    @ApiModelProperty("日期")
    private String date;

    public static DateCount builder(String date, Long count) {
        return new DateCount(date, count);
    }

    public DateCount() {}

    public DateCount(String date, Long count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public DateCount setDate(String date) {
        this.date = date;
        return this;
    }
}
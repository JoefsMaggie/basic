package joe.tags.modal.bean.request;

import joe.core.validation.custom.BlankIfOtherExists;
import joe.elasticsearch.common.ESFieldFormat;
import joe.elasticsearch.common.Metric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 人群查询模型
 * Date Date : 2018年09月06日 16:24
 */
@ApiModel(description = "人群查询模型")
public class CrowdQueryModel {
    /**
     * 查询字段
     */
    @NotBlank
    @ApiModelProperty(value = "查询字段", required = true, dataType = "string", example = "asset:jf_balance")
    private String field;
    /**
     * 数据类型
     */
    @NotNull
    @ApiModelProperty(value = "字段的数据格式", required = true, dataType = "enum", example = "VALUE")
    private ESFieldFormat format;
    /**
     * 值列表
     */
    @Valid
    @NotEmpty
    @ApiModelProperty(value = "查询的数据指标数组", required = true, dataType = "Relation")
    private List<DataMetric> dataMetrics;

    @ApiModel(description = "数据指标，values和left / right组必须存其一，即values存在时left / right可不传，反之亦然")
    public static class DataMetric {
        /**
         * 逻辑
         */
        @NotNull
        @ApiModelProperty(value = "数据指标类型", required = true, dataType = "enum", example = "GT_LT")
        private Metric metric;
        /**
         * 单值
         */
        @BlankIfOtherExists(value = {"left", "right"}, clazz = DataMetric.class)
        @ApiModelProperty(value = "数据数组，metric为单值类型（即非区间类型）时使用，数组形式是为了匹配多值",
                dataType = "array", example = "[\"a\",\"b\"]")
        private List<String> values;
        /**
         * 区间：左值
         */
        @BlankIfOtherExists(value = {"values", "date"}, logic = BlankIfOtherExists.Logic.OR, clazz = DataMetric.class)
        @ApiModelProperty(value = "当metric是区间类型时，如：大于a & 小于b，此时left对应a",
                dataType = "string", example = "3")
        private String left;
        /**
         * 区间：右值
         */
        @BlankIfOtherExists(value = {"values", "date"}, logic = BlankIfOtherExists.Logic.OR, clazz = DataMetric.class)
        @ApiModelProperty(value = "当metric是区间类型时，如：大于a & 小于b，此时right对应b",
                dataType = "string", example = "3120")
        private String right;

        public Metric getMetric() {
            return metric;
        }

        public DataMetric setMetric(Metric metric) {
            this.metric = metric;
            return this;
        }

        public List<String> getValues() {
            return values;
        }

        public DataMetric setValues(List<String> values) {
            this.values = values;
            return this;
        }

        public String getLeft() {
            return left;
        }

        public DataMetric setLeft(String left) {
            this.left = left;
            return this;
        }

        public String getRight() {
            return right;
        }

        public DataMetric setRight(String right) {
            this.right = right;
            return this;
        }

    }

    public String getField() {
        return field;
    }

    public CrowdQueryModel setField(String field) {
        this.field = field;
        return this;
    }

    public ESFieldFormat getFormat() {
        return format;
    }

    public CrowdQueryModel setFormat(ESFieldFormat format) {
        this.format = format;
        return this;
    }

    public List<DataMetric> getDataMetrics() {
        return dataMetrics;
    }

    public CrowdQueryModel setDataMetrics(List<DataMetric> dataMetrics) {
        this.dataMetrics = dataMetrics;
        return this;
    }
}

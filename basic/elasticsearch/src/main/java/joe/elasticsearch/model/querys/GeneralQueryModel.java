package joe.elasticsearch.model.querys;

import com.fasterxml.jackson.annotation.JsonAlias;
import joe.elasticsearch.common.ESFieldFormat;
import joe.elasticsearch.common.Metric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  通用查询模型
 * Date Date : 2018年09月18日 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "通用elasticsearch查询模型")
public class GeneralQueryModel {
    /**
     * 查询字段
     */
    @NotBlank
    @ApiModelProperty(value = "查询字段", required = true, dataType = "string", example = "asset:jf_balance")
    private String field;
    /**
     * 数据类型
     */
    @ApiModelProperty(value = "字段的数据格式", required = true, dataType = "enum", example = "VALUE")
    private ESFieldFormat format;
    /**
     * 值列表
     */
    @Valid
    @NotEmpty
    @ApiModelProperty(value = "查询的数据指标数组", required = true, dataType = "Relation")
    private List<Relation> relations;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "ES 查询指标")
    public static class Relation {
        /**
         * 逻辑
         */
        @NotNull
        @JsonAlias("relation")
        @ApiModelProperty(value = "数据指标类型", required = true, dataType = "enum", example = "GT_LT")
        private Metric metric;
        /**
         * 数据数组
         */
        @NotEmpty
        @ApiModelProperty(value = "数据数组", dataType = "array", example = "[\"a\",\"b\"]")
        private List<String> values;
    }

}

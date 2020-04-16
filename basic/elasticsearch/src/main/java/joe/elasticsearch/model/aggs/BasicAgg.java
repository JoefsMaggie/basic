package joe.elasticsearch.model.aggs;

import com.fasterxml.jackson.annotation.JsonAlias;
import joe.elasticsearch.common.AggType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  基础聚合模型
 * Date : 2018年09月25日 09:56
 */
@Data
@ApiModel
public class BasicAgg<T extends BasicAgg> {
    /**
     * 返回字段名，terms、histogram、range中需要使用
     */
    @NotBlank
    @JsonAlias("return_name")
    @ApiModelProperty("聚合的返回名称，如果不传，则默认为[field.type]")
    private String returnName;
    /**
     * AGG 名称
     */
    @ApiModelProperty(hidden = true)
    private String aggName;
    /**
     * 字段名
     */
    @ApiModelProperty(value = "聚合的字段名")
    private String field;
    /**
     * 脚本
     */
    @ApiModelProperty(value = "聚合需要执行的脚本")
    private String script;
    /**
     * 聚合类型
     */
    @NotNull
    @ApiModelProperty(value = "聚合类型", dataType = "enum")
    private AggType type;

}

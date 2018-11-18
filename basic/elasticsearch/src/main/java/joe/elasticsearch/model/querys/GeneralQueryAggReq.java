package joe.elasticsearch.model.querys;

import joe.elasticsearch.model.aggs.GeneralAgg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 通用查询聚合模型
 * Date Date : 2018年09月27日 16:55
 */
@Data
@ApiModel()
public class GeneralQueryAggReq {
    /**
     * 查询的索引列表
     */
    @NotEmpty
    @ApiModelProperty(dataType = "array", required = true, value = "查询的索引列表")
    private List<String> indices;
    /**
     * 查询条件
     */
    @Valid
    @ApiModelProperty(dataType = "QueryRelation", value = "查询条件")
    private QueryRelation conditions;
    /**
     * 聚合条件
     */
    @Valid
    @ApiModelProperty(dataType = "GeneralAgg", value = "聚合条件")
    private List<GeneralAgg> aggs;

}

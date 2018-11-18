package joe.tags.modal.bean.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import joe.elasticsearch.model.querys.QueryRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.request
 * @note: 根据字段做分页的请求模型
 * @date Date : 2018年09月20日 09:55
 */
@Getter
@Setter
@ToString
@ApiModel
public class FieldPagerReq {
    @Valid
    @JsonAlias("conditions")
    @ApiModelProperty("查询模型")
    private QueryRelation queryRelation;
    @ApiModelProperty("是否去重")
    private boolean distinct;
    @NotEmpty
    @ApiModelProperty("查询的字段列表")
    private String[] fields;
    @ApiModelProperty("分页时，每页的数量")
    private int pageSize;
    @ApiModelProperty("分页时，查询页码，为 0 表示不分页")
    private int pageIndex;
    private String orderBy;
    private String[] indices;

}

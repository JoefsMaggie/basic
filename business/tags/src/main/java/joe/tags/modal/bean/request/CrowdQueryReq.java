package joe.tags.modal.bean.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import joe.elasticsearch.model.querys.QueryRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.request
 * @note: 人群详情查询模型
 * @date Date : 2018年09月19日 16:38
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class CrowdQueryReq extends BasicReq<CrowdQueryReq> {
    @Valid
    @JsonAlias("conditions")
    @ApiModelProperty("查询模型")
    private QueryRelation queryRelation;
    /**
     * 索引列表
     */
    private List<String> indices;

}

package joe.elasticsearch.model.querys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.request
 * @note: ES 通用查询关系
 * @date Date : 2018年09月18日 13:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "ES 通用查询关系")
public class QueryRelation {
    /**
     * 数据 AND 查询
     */
//    @BlankIfOtherExists(value = {"or", "not", "metrics"}, clazz = QueryRelation.class)
    @Valid
    private List<QueryRelation> and;
    /**
     * 数据 OR 查询
     */
//    @BlankIfOtherExists(value = {"and", "not", "metrics"}, clazz = QueryRelation.class)
    @Valid
    private List<QueryRelation> or;
    /**
     * 数据 NOT 查询
     */
//    @BlankIfOtherExists(value = {"or", "and", "metrics"}, clazz = QueryRelation.class)
    @Valid
    private List<QueryRelation> not;
    /**
     * 查询数据
     */
//    @BlankIfOtherExists(value = {"or", "not", "and"}, clazz = QueryRelation.class)
    @Valid
    private List<GeneralQueryModel> metrics;
}

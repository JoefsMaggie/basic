package joe.elasticsearch.model.aggs;

import com.fasterxml.jackson.annotation.JsonAlias;
import joe.elasticsearch.model.querys.QueryRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  通用聚合模型
 * Date Date : 2018年09月27日 15:52
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(parent = BasicAgg.class)
public class GeneralAgg extends BasicAgg<GeneralAgg> {
    /**
     * 子聚合
     */
    @Valid
    @ApiModelProperty(value = "子聚合")
    private List<GeneralAgg> subAggs;
    /**
     * 区间或直方图需要的值
     */
    @ApiModelProperty(value = "区间或直方图需要的值")
    private List<String> values;
    /**
     * subAggs 聚合或 topHits 大小
     */
    @ApiModelProperty(value = "subAggs 聚合或 topHits 大小")
    private int size = 10;
    /**
     * 直方图指定区间
     */
    @JsonAlias("extended_bounds")
    @ApiModelProperty(value = "直方图指定区间")
    private List<String> extendedBounds;
    /**
     * 直方图返回所需的最小文档数
     */
    @JsonAlias("min_doc_count")
    @ApiModelProperty(value = "直方图返回所需的最小文档数")
    private int minDocCount;
    /**
     * 直方图偏移量
     */
    @ApiModelProperty(value = "直方图偏移量")
    private int offset;
    /**
     * filter 聚合用的查询关系
     */
    @ApiModelProperty(value = "filter 聚合用的查询关系")
    private QueryRelation condition;

}

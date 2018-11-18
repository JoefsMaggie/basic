package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.response
 * @note: 根据字段做分页查询的数据返回模型
 * @date Date : 2018年09月20日 10:00
 */
@ApiModel
public class FieldPagerRes {
    @ApiModelProperty("总数")
    private long count;
    @JsonProperty("current_page")
    @ApiModelProperty("当前页码")
    private int currentPage;
    @ApiModelProperty("数据")
    private List<Map<String, Object>> list;

    public long getCount() {
        return count;
    }

    public FieldPagerRes setCount(long count) {
        this.count = count;
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public FieldPagerRes setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public FieldPagerRes setList(List<Map<String, Object>> list) {
        this.list = list;
        return this;
    }
}

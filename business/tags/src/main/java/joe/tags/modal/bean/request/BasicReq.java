package joe.tags.modal.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 基础查询类，暂未解决swagger必须使用requestBody的问题，所有的接口入参都必须继承此类，或者自行加入key字段供校验
 * Date Date : 2018年09月20日 09:55
 */
@ApiModel
public class BasicReq<T extends BasicReq> {
    @NotBlank
    @ApiModelProperty("安全认证 key")
    private String key;

    public String getKey() {
        return key;
    }

    public T setKey(String key) {
        this.key = key;
        return (T) this;
    }
}

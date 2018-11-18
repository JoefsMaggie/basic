package joe.test.model.bean.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : user
 * Date: 2018/10/29
 */
@Data
@Builder
public class User {
    private String id;
    @NotBlank
    private String name;
    private String skinColor;
}

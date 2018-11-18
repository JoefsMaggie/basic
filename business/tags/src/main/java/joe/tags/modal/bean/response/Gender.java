package joe.tags.modal.bean.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 性别
 * Date Date : 2018年09月10日 15:27
 */
@ApiModel(description = "性别", parent = BasicNumModel.class)
public class Gender extends BasicNumModel<Gender> {
    public enum GenderType {
        MALE, FEMALE, UNKNOWN
    }

    /**
     * 性别
     */
    @ApiModelProperty("性别类型")
    private GenderType gender;


    public static Gender builder(String genderType, long count) {
        switch (genderType) {
            case "0":
                return new Gender().setGender(GenderType.UNKNOWN).setCount(count);
            case "1":
                return new Gender().setGender(GenderType.MALE).setCount(count);
            case "2":
                return new Gender().setGender(GenderType.FEMALE).setCount(count);
            default:
                return null;
        }
    }
    public GenderType getGender() {
        return gender;
    }

    public Gender setGender(GenderType gender) {
        this.gender = gender;
        return this;
    }
}
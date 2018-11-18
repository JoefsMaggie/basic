package joe.tags.modal.bean.response;

import joe.tags.common.CusTagField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 二维码类型
 * Date Date : 2018年09月10日 16:18
 */
@ApiModel(description = "二维码", parent = BasicNumModel.class)
public class Code extends BasicNumModel<Code> {
    public enum CodeType {
        /**
         * 智能营销码
         */
        SMART_MARKETING,
        /**
         * 万物溯源码
         */
        TRACE_SOURCE,
        /**
         * 超级导购码
         */
        SUPER_SHOPPERS,
        /**
         * 智慧微商码
         */
        WISDOM_MICRO_MALL,
        /**
         * 微信二维码
         */
        WECHAT
    }

    /**
     * 二维码类型
     */
    @ApiModelProperty("二维码类型")
    private CodeType codeType;

    public static Code builder(String codeType, long count) {
        switch (codeType) {
            case CusTagField.CODE_FWM_STAT:
            case CusTagField.CODE_XM_STAT:
                return new Code().setCodeType(CodeType.SMART_MARKETING).setCount(count);
            case CusTagField.CODE_SYM_STAT:
                return new Code().setCodeType(CodeType.TRACE_SOURCE).setCount(count);
            case CusTagField.CODE_DGM_STAT:
                return new Code().setCodeType(CodeType.SUPER_SHOPPERS).setCount(count);
            case CusTagField.CODE_WSM_STAT:
                return new Code().setCodeType(CodeType.WISDOM_MICRO_MALL).setCount(count);
            case CusTagField.CODE_WXM_STAT:
                return new Code().setCodeType(CodeType.WECHAT).setCount(count);
            default:
                return null;
        }
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public Code setCodeType(CodeType codeType) {
        this.codeType = codeType;
        return this;
    }
}

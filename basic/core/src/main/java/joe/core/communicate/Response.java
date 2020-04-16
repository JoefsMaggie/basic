package joe.core.communicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 返回数据模型
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月04日 15:53
 */
@Data
@ApiModel(description = "返回数据模型")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 4572910611616655491L;

    public static final String ERROR_CODE = "-1";
    public static final String SUCCESS_CODE = "0";

    /**
     * 返回头信息
     */
    @JsonIgnore
    private ResponseHead head;
    @JsonProperty("return_code")
    @ApiModelProperty("http返回码")
    private String returnCode = SUCCESS_CODE;
    @JsonProperty("return_msg")
    @ApiModelProperty("返回信息")
    private String returnMsg;
    /**
     * 返回数据报
     */
    @JsonProperty("return_data")
    @ApiModelProperty("返回数据报")
    private T body;
    /**
     * 错误信息格式化参数
     */
    @JsonIgnore
    private Object[] errorParams;

    public Response(T data) {
        this(null, data);
    }

    public Response(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public Response(String message, T data) {
        this(SUCCESS_CODE, message, data);
    }

    public Response(String errorCode, String message, T data) {
        this.head = new ResponseHead(errorCode, message);
        this.returnCode = errorCode;
        this.returnMsg = message;
        this.body = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return head.isSuccess();
    }

    @JsonIgnore
    public boolean isError() {
        return head.isError();
    }

    /**
     * 根据数据创建接口调用成功的响应对象
     *
     * @param data 数据
     * @return Response<T> 响应对象
     */
    public static <T> Response<T> successData(T data) {
        return new Response<>("OK", data);
    }

    /**
     * 根据消息创建接口调用成功的响应对象
     *
     * @param message 成功的消息
     * @return Response<T> 响应对象
     */
    public static <T> Response<T> successMessage(String message) {
        return new Response<>(SUCCESS_CODE, message);
    }

    /**
     * 根据消息及数据创建接口调用成功的响应对象
     *
     * @param message 成功的消息
     * @param data    数据
     * @return Response<T> 响应对象
     */
    public static <T> Response<T> success(String message, T data) {
        return new Response<>(message, data);
    }

    /**
     * 根据错误代码及错误信息、错误信息格式化参数创建接口调用失败的响应对象
     *
     * @param errorCode   错误代码
     * @param errorParams 错误信息格式化参数
     * @return Response<T> 响应对象
     */
    public static <T> Response<T> error(String errorCode, String... errorParams) {
        String errorMsg = "未知错误，请联系管理员...";
        if (Objects.nonNull(errorParams) && errorParams.length > 0) {
            errorMsg = errorParams[0];
        }
        Response<T> response = new Response<>(errorCode, errorMsg);
        response.setErrorParams(errorParams);
        return response;
    }

    /**
     * 根据错误代码及错误信息、错误信息格式化参数创建接口调用失败的响应对象
     *
     * @return Response<T> 响应对象
     */
    public static Response<String> redirect(String url) {
        return new Response<>(ResponseHead.REDIRECT_CODE, null, url);
    }

    public static Response<StringResponse> create(String key, String value) {
        return create(SUCCESS_CODE, key, value);
    }

    public static Response<IntegerResponse> create(String key, Integer value) {
        return create(SUCCESS_CODE, key, value);
    }

    public static Response<LongResponse> create(String key, Long value) {
        return create(SUCCESS_CODE, key, value);
    }

    public static Response<BooleanResponse> create(String key, Boolean value) {
        return create(SUCCESS_CODE, key, value);
    }

    public static Response<StringResponse> create(String msg, String key, String value) {
        return create(msg, key, value, StringResponse.class);
    }

    public static Response<IntegerResponse> create(String msg, String key, Integer value) {
        return create(msg, key, value, IntegerResponse.class);
    }

    public static Response<LongResponse> create(String msg, String key, Long value) {
        return create(msg, key, value, LongResponse.class);
    }

    public static Response<BooleanResponse> create(String msg, String key, Boolean value) {
        return create(msg, key, value, BooleanResponse.class);
    }

    private static <T> Response<T> create(String msg, String key, Object value, Class<T> clazz) {
        try {
            OneKeyResponse oneKeyResponse = (OneKeyResponse) clazz.newInstance();
            oneKeyResponse.singleton(key, value);
            return Response.success(msg, (T) oneKeyResponse);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("实例化失败", e);
        }
    }
}

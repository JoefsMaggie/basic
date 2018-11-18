package joe.tags.controllers.v1;

import joe.core.communicate.Response;
import joe.core.resolver.annotation.Var;
import joe.tags.common.URI;
import joe.tags.modal.bean.request.CrowdQueryModel;
import joe.tags.modal.bean.response.CrowdDetail;
import joe.tags.services.CrowdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : Joe
 * @version V1.0
 * @Project: basic
 * @Package joe.tag.controllers.v1
 * @note: 人群控制层
 * @date Date : 2018年09月03日 11:57
 */
@RestController
@ApiIgnore
@Api(tags = "人群分析相关接口")
public class V1CrowdController {

    private static final Logger logger = LoggerFactory.getLogger(V1CrowdController.class);
    @Autowired
    private CrowdService crowdService;

    @PostMapping(URI.V1.CROWD_DETAIL_ALL)
    @ApiOperation(value = "人群详情-全量数据", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailAll(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailAll 接口");
        return Response.successData(crowdService.crowdDetailAll(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_BASIC)
    @ApiOperation(value = "人群详情-基本信息", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailBasic(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailBasic 接口");
        return Response.successData(crowdService.crowdDetailBasic(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_FUNNEL)
    @ApiOperation(value = "用户转化漏斗", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailFunnel(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailFunnel 接口");
        return Response.successData(crowdService.crowdDetailFunnel(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_GENDER)
    @ApiOperation(value = "基础属性", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailGender(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailGender 接口");
        return Response.successData(crowdService.crowdDetailGender(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_ORDER)
    @ApiOperation(value = "人群转化-订单", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailOrder(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailOrder 接口");
        return Response.successData(crowdService.crowdDetailOrder(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_CODE)
    @ApiOperation(value = "人群转化-扫码", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailCode(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailCode 接口");
        return Response.successData(crowdService.crowdDetailCode(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_JF)
    @ApiOperation(value = "人群互动-积分", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailJf(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailJf 接口");
        return Response.successData(crowdService.crowdDetailJf(queryModels));
    }

    @PostMapping(URI.V1.CROWD_DETAIL_COUPON)
    @ApiOperation(value = "人群详情，人群互动-卡券", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "queryModels", value = "查询模型，直接传数组，不用带 queryModels 参数",
            required = true, allowMultiple = true, dataType = "CrowdQueryModel")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailCoupon(@Var @Valid @RequestBody List<CrowdQueryModel> queryModels) {
        logger.info("调用 crowdDetailCoupon 接口");
        return Response.successData(crowdService.crowdDetailCoupon(queryModels));
    }

}

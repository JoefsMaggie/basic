package joe.tags.controllers.v2;

import joe.core.communicate.Response;
import joe.core.resolver.annotation.Var;
import joe.tags.common.URI;
import joe.tags.modal.bean.request.CrowdQueryReq;
import joe.tag.modal.bean.response.*;
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

import javax.validation.Valid;

/**
 * @author : Joe
 * @version V1.0

 1
 * @note: 人群控制层
 * Date Date : 2018年09月03日 11:57
 */
@RestController
@Api(tags = {"人群分析相关接口"})
public class V2CrowdController {

    private static final Logger logger = LoggerFactory.getLogger(V2CrowdController.class);
    private final CrowdService crowdService;

    @Autowired
    public V2CrowdController(CrowdService crowdService) {
        this.crowdService = crowdService;
    }

    @PostMapping(URI.V2.CROWD_DETAIL_ALL)
    @ApiOperation(value = "人群详情-全量数据", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdDetail> crowdDetailAll(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailAll 接口");
        return Response.successData(crowdService.crowdDetailAll(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_BASIC)
    @ApiOperation(value = "人群详情-基本信息", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdBasic> crowdDetailBasic(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailBasic 接口");
        return Response.successData(crowdService.crowdDetailBasic(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_FUNNEL)
    @ApiOperation(value = "用户转化漏斗", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdFunnel> crowdDetailFunnel(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailFunnel 接口");
        return Response.successData(crowdService.crowdDetailFunnel(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_GENDER)
    @ApiOperation(value = "基础属性", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdGender> crowdDetailGender(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailGender 接口");
        return Response.successData(crowdService.crowdDetailGender(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_ORDER)
    @ApiOperation(value = "人群转化-订单", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdOrder> crowdDetailOrder(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailOrder 接口");
        return Response.successData(crowdService.crowdDetailOrder(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_CODE)
    @ApiOperation(value = "人群转化-扫码", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdCode> crowdDetailCode(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailCode 接口");
        return Response.successData(crowdService.crowdDetailCode(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_JF)
    @ApiOperation(value = "人群互动-积分", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdIntegral> crowdDetailJf(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailJf 接口");
        return Response.successData(crowdService.crowdDetailJf(crowdQueryReq));
    }

    @PostMapping(URI.V2.CROWD_DETAIL_COUPON)
    @ApiOperation(value = "人群详情，人群互动-卡券", notes = "根据前端传递的查询参数过滤人群", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "crowdQueryReq", value = "查询关系", required = true, dataType = "CrowdQueryReq")
    @ApiResponse(code = 200, message = "OK", response = CrowdDetail.class)
    public Response<CrowdCoupon> crowdDetailCoupon(@Var @Valid @RequestBody CrowdQueryReq crowdQueryReq) {
        logger.info("调用 crowdDetailCoupon 接口");
        return Response.successData(crowdService.crowdDetailCoupon(crowdQueryReq));
    }

}

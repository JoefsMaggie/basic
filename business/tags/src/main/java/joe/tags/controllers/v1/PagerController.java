package joe.tags.controllers.v1;

import joe.core.communicate.Response;
import joe.core.resolver.annotation.Var;
import joe.tags.common.URI;
import joe.tags.modal.bean.request.FieldPagerReq;
import joe.tags.modal.bean.request.SpecialReq;
import joe.tags.modal.bean.response.FieldPagerRes;
import joe.tags.services.PagerService;
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
 * @author : Joe joe_fs@sina.com
 * @version V1.0

 1
 * @note: ES 分页查询控制层
 * Date Date : 2018年09月20日 12:01
 */
@RestController
@Api(tags = {"ES 分页查询"})
public class PagerController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PagerController.class);

    private final PagerService pagerService;

    @Autowired
    public PagerController(PagerService pagerService) {this.pagerService = pagerService;}

    @PostMapping(URI.V2.PAGER_QUERY)
    @ApiOperation(value = "ES 分页查询接口", notes = "根据前端传递的查询参数进行过滤", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "fieldPagerReq", value = "查询关系", required = true, dataType = "FieldPagerReq")
    @ApiResponse(code = 200, message = "OK", response = FieldPagerRes.class)
    public Response<FieldPagerRes> pagerQuery(@Var @Valid @RequestBody FieldPagerReq fieldPagerReq) {
        LOGGER.info("调用分页接口： \n{}", fieldPagerReq);
        return Response.successData(pagerService.fieldValues(fieldPagerReq));
    }

    @PostMapping(URI.V2.PAGER_QUERY_SPECIAL)
    public Response<FieldPagerRes> special(@Var @Valid @RequestBody FieldPagerReq fieldPagerReq, @Var @Valid @RequestBody SpecialReq specialReq) {
        LOGGER.info("调用特殊处理分页接口： \n{}，\n{}", fieldPagerReq, specialReq);
        specialReq.setUseSpecial(true);
        return Response.successData(pagerService.specialFieldValues(fieldPagerReq, specialReq));
    }

}

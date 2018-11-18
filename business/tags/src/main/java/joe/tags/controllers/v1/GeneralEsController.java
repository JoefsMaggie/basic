package joe.tags.controllers.v1;

import joe.core.communicate.Response;
import joe.core.resolver.annotation.Var;
import joe.elasticsearch.model.EsRes;
import joe.elasticsearch.model.querys.GeneralQueryAggReq;
import joe.tags.common.URI;
import joe.tags.services.GeneralEsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0

 1
 * @note: ES 通用查询聚合控制层
 * Date Date : 2018年09月27日 17:01
 */
@Slf4j
@RestController
@Api(tags = {"ES 通用查询聚合"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeneralEsController {

    private final GeneralEsService generalEsService;

    @PostMapping(URI.V2.GENERAL_QUERY_AGG)
    @ApiOperation(value = "ES 通用查询聚合接口", notes = "通用接口，完全由参数决定需要的查询与聚合形式", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "generalQueryAggReq", value = "查询、聚合关系", required = true, dataType = "GeneralQueryAggReq")
    @ApiResponse(code = 200, message = "OK", response = EsRes.class)
    public Response<Map<String, Object>> gen(@Var @Valid @RequestBody GeneralQueryAggReq generalQueryAggReq) {
        log.info("调用 ES 通用接口： \n{}", generalQueryAggReq);
        return Response.successData(generalEsService.handler(generalQueryAggReq));
    }
}

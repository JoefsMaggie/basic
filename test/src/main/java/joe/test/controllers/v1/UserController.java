package joe.test.controllers.v1;

import joe.core.communicate.Response;
import joe.core.communicate.StringResponse;
import joe.core.resolver.annotation.Var;
import joe.test.model.bean.request.User;
import joe.test.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : user controller
 * Date: 2018/10/29
 */
@RestController
public class UserController {
    private Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/query")
    public Response<User> query(@Var String id) {
        return Response.successData(userService.query(id));
    }

    @PostMapping("/users/query")
    public Response<List<User>> queryUsers(@Var @Valid List<String> ids) {
        return Response.successData(userService.queryUsers(ids));
    }

    @PostMapping("/user/add")
    public Response<StringResponse> add(@Var @Valid User user) {
        userService.add(user);
        return Response.successMessage("ok");
    }

    @PostMapping("/user/update")
    public Response<StringResponse> update(@Var @Valid User user) {
        LOG.info("controller update");
        userService.update(user);
        return Response.successMessage("ok");
    }

    @PostMapping("/user/delete")
    public Response<StringResponse> delete(@Var String id) {
        userService.delete(id);
        return Response.successMessage("ok");
    }
}

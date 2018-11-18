package joe.test.services;

import joe.test.model.bean.request.User;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * user service
 * Date:
 */
public interface IUserService {
    User query(String id);

    void add(User user);

    void update(User user);

    void delete(String id);

    List<User> queryUsers(List<String> ids);
}

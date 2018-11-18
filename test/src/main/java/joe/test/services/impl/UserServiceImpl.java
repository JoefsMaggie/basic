package joe.test.services.impl;

import joe.test.model.bean.request.User;
import joe.test.model.mapper.UserMapper;
import joe.test.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * user service impl
 * Date: 2018/10/29
 */
@Service
public class UserServiceImpl implements IUserService {
    private Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User query(String id) {
        LOG.info(userMapper.queryById(id).toString());
        User user = userMapper.query(User.builder().id(id).build());
        LOG.info(user.toString());
        return user;
    }

    @Override
    public List<User> queryUsers(List<String> ids) {
        return userMapper.queryUsers(ids);
    }

    @Override
    public void add(User user) {
        userMapper.add(user);
    }

    @Override
    public void update(User user) {
        LOG.info("service update");
        userMapper.update(user);
    }

    @Override
    public void delete(String id) {
        userMapper.delete(id);
    }

}

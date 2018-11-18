package joe.test.model.mapper;

import joe.database.mybatis.language.SimpleInsertLangDriver;
import joe.database.mybatis.language.SimpleSelectInLangDriver;
import joe.database.mybatis.language.SimpleSelectLangDriver;
import joe.database.mybatis.language.SimpleUpdateLangDriver;
import joe.test.model.bean.request.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : user mapper
 * Date: 2018/10/29
 */
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    User queryById(String id);

    @Lang(SimpleSelectLangDriver.class)
    @Select("select * from user (#{user})")
    User query(User user);

    @Lang(SimpleInsertLangDriver.class)
    @Insert("insert into user (#{user})")
    void add(User user);

    @Lang(SimpleUpdateLangDriver.class)
    @Update("update user (#{user}) where id = #{id}")
    void update(User user);

    void delete(String id);

    @Lang(SimpleSelectInLangDriver.class)
    @Select("select * from user where id in (#{ids})")
    List<User> queryUsers(List<String> ids);
}

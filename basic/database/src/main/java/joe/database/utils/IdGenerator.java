package joe.database.utils;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * ID 生成器
 * Date: 2018/10/31
 */
public interface IdGenerator {

    default String newIdStr() {
        return String.valueOf(newId());
    }

    long newId();
}

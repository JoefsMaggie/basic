package joe.database.utils;

/**
 * ID 生成器
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/31
 */
public interface IdGenerator {

    default String newIdStr() {
        return String.valueOf(newId());
    }

    long newId();
}

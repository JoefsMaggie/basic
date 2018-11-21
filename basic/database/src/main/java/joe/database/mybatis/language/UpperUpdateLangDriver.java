package joe.database.mybatis.language;

/**
 * 首字母改成大写
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/11/20
 */
public class UpperUpdateLangDriver extends AbstractUpdateLangDriver {
    @Override
    public String fieldProc(String fieldName) {
        return lowerCamelToUpperCamel(fieldName);
    }
}

package joe.database.mybatis.language;

/**
 * 改成首字母大写
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/11/20
 */
public class UpperInsertLangDriver extends AbstractInsertLangDriver {
    @Override
    public String fieldProc(String fieldName) {
        return lowerCamelToUpperCamel(fieldName);
    }
}

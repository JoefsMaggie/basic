package joe.database.mybatis.language;

import com.google.common.base.CaseFormat;

import java.util.regex.Pattern;

/**
 * mybatis 增强类接口
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/30
 */
public interface ILanguageDriver {

    Pattern pattern = Pattern.compile("\\(#\\{(\\w+)}\\)");

    String fieldProc(String fieldName);

    default String lowerCamelToUnderscore(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    default String lowerCamelToUpperCamel(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
    }
}

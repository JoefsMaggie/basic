package joe.database.mybatis.language;

import com.google.common.base.CaseFormat;

import java.util.regex.Pattern;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : mybatis 增强类接口
 * Date: 2018/10/30
 */
public interface ILanguageDriver {

     Pattern pattern = Pattern.compile("\\(#\\{(\\w+)}\\)");

     default String camelToUnderscore(String fieldName) {
          return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
     }
}

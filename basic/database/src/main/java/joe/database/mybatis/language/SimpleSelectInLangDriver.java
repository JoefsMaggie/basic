package joe.database.mybatis.language;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.regex.Matcher;

/**
 * 增强 mybatis select in sql 的注解
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/29
 */
public class SimpleSelectInLangDriver extends XMLLanguageDriver implements ILanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        String collection = "list";
        if (parameterType.isArray()) {
            collection = "array";
        }
        if (matcher.find()) {
            String replacement = "(<foreach collection=\"" + collection + "\" item=\"_item\" separator=\",\">#{_item}</foreach>)";
            script = matcher.replaceAll(replacement);
        }
        script = "<script>" + script + "</script>";
        return super.createSqlSource(configuration, script, parameterType);
    }

    @Override
    public String fieldProc(String fieldName) {
        return lowerCamelToUnderscore(fieldName);
    }
}

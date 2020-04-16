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
public abstract class AbstractSelectInLangDriver extends XMLLanguageDriver implements ILanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            String replacement = "(<foreach collection=\"$1\" item=\"_item\" separator=\",\">#{_item}</foreach>)";
            script = matcher.replaceAll(replacement);
        }
        script = "<script>" + script + "</script>";
        return super.createSqlSource(configuration, script, parameterType);
    }

}

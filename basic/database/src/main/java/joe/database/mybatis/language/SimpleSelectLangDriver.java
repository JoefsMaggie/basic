package joe.database.mybatis.language;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * 增强 mybatis select sql 的注解
 * Date: 2018/10/30
 */
public class SimpleSelectLangDriver extends XMLLanguageDriver implements ILanguageDriver {
    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<where>");
            for (Field field : parameterType.getDeclaredFields()) {
                String tmp = "<if test=\"_field != null\">AND _column = #{_field}</if>";
                sb.append(tmp.replaceAll("_field", field.getName())
                             .replaceAll("_column", camelToUnderscore(field.getName())));
            }
            sb.append("</where>");
            script = matcher.replaceAll(sb.toString());
            script = "<script>" + script + "</script>";
        }
        return super.createSqlSource(configuration, script, parameterType);
    }
}

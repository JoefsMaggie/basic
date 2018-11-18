package joe.database.mybatis.language;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : 增强 mybatis insert sql 的注解
 * Date: 2018/10/30
 */
public class SimpleInsertLangDriver extends XMLLanguageDriver implements ILanguageDriver {
    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            StringBuilder fieldSb = new StringBuilder();
            StringBuilder columnSb = new StringBuilder();
            columnSb.append("(");
            for (Field field : parameterType.getDeclaredFields()) {
                fieldSb.append("#{").append(field.getName()).append("},");
                columnSb.append(camelToUnderscore(field.getName())).append(",");
            }
            fieldSb.deleteCharAt(fieldSb.lastIndexOf(","));
            columnSb.deleteCharAt(columnSb.lastIndexOf(","));
            columnSb.append(") values (").append(fieldSb.toString()).append(")");
            script = matcher.replaceAll(columnSb.toString());
            script = "<script>" + script + "</script>";
        }
        return super.createSqlSource(configuration, script, parameterType);
    }
}

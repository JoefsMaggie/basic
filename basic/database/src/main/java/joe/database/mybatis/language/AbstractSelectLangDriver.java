package joe.database.mybatis.language;

import joe.database.mybatis.language.annotation.Ignore;
import joe.database.mybatis.language.annotation.IgnoreSqlType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * 增强 mybatis select sql 的注解
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/30
 */
public abstract class AbstractSelectLangDriver extends XMLLanguageDriver implements ILanguageDriver {

    private static final String FIELD_IF = "<if test='_field != null'> \r\n" + 
        "AND \r\n" + 
        "<choose> \r\n" +
        "<when test='_field.contains(\",\")> \r\n" +
        "<foreach collection='_field.split(\",\")' item='_item' open='_column IN (' separator=',' close=')'>#{_item}</forecah> \r\n" +
        "</when> \r\n" +
        "<otherwise> \r\n" +
        "_column = #{_field} \r\n" +
        "</otherwise> \r\n" +
        "</choose> \r\n" +
        "</if>";

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<where>");
            for (Field field : parameterType.getDeclaredFields()) {
                var ignore = field.getAnnotation(Ignore.class);
                if (Objects.nonNull(ignore)
                        && (Arrays.stream(ignore.value())
                                  .anyMatch(sqlType -> sqlType.equals(IgnoreSqlType.SELECT) || sqlType.equals(IgnoreSqlType.ALL)))) {
                    continue;
                }
                sb.append(FIELD_IF.replaceAll("_field", field.getName())
                                  .replaceAll("_column", fieldProc(field.getName())));
            }
            sb.append("</where>");
            script = matcher.replaceAll(sb.toString());
            script = "<script>" + script + "</script>";
        }
        return super.createSqlSource(configuration, script, parameterType);
    }

}

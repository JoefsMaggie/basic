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
 * 增强 mybatis insert sql 的注解
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/30
 */
public abstract class AbstractInsertLangDriver extends XMLLanguageDriver implements ILanguageDriver {

    private static final String FIELD_IF = "<if test=\"_field != null\">,_column = #{_field}</if>";

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            StringBuilder fieldSb = new StringBuilder();
            StringBuilder columnSb = new StringBuilder();
            columnSb.append("(");
            for (Field field : parameterType.getDeclaredFields()) {
                var ignore = field.getAnnotation(Ignore.class);
                if (Objects.nonNull(ignore)
                        && Arrays.stream(ignore.value())
                        .noneMatch(sqlType -> sqlType.equals(IgnoreSqlType.INSERT) || sqlType.equals(IgnoreSqlType.ALL))) {
                    continue;
                }
                fieldSb.append("#{").append(field.getName()).append("},");
                columnSb.append(fieldProc(field.getName())).append(",");
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

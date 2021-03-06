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
 * 增强 mybatis update sql 的注解
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/30
 */
public abstract class AbstractUpdateLangDriver extends XMLLanguageDriver implements ILanguageDriver {

    private static final String FIELD_IF = "<if test=\"_field != null\">_column = #{_field}, </if>";

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<set>");
            for (Field field : parameterType.getDeclaredFields()) {
                var ignore = field.getAnnotation(Ignore.class);
                if (Objects.nonNull(ignore)
                        && (Arrays.asList(ignore.value()).contains(IgnoreSqlType.UPDATE)
                        || Arrays.asList(ignore.value()).contains(IgnoreSqlType.ALL))) {
                    continue;
                }
                sb.append(FIELD_IF.replaceAll("_field", field.getName())
                        .replaceAll("_column", fieldProc(field.getName())));
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("</set>");
            script = matcher.replaceAll(sb.toString());
            script = "<script>" + script + "</script>";
        }
        return super.createSqlSource(configuration, script, parameterType);
    }
}

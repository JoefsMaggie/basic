package joe.test.demo;

import java.lang.annotation.*;

public class JdkDemo {

    private static final String STR = """
            a
            b
            c
            """;

    public static void main(String[] args) {
        String sql = """
                select *
                from $table
                where field = 1
                """;
        System.out.println(sql);
        Record record = new Record("t", 1);
        System.out.println(record);
        System.out.println(record.ia());

        System.out.println(instantOf(record.sa()));
        System.out.println(instantOf("sb"));
        System.out.println(instantOf("sc"));
        System.out.println(instantOf(record.ia()));
        System.out.println(instantOf(0L));
        System.out.println(instantOf(0.0D));
    }

    private static String instantOf(Object obj) {
        if (obj instanceof String str) {
            return switchFeature(str);
        }
        return switch (obj) {
            case Integer i -> "Integer " + i;
            case Long l -> "Long " + l;
            case Double d -> "Double " + d;
            default -> "unknown type";
        };
    }

    private static String switchFeature(String s) {
        return switch (s) {
            case "sb" -> "b";
            case "sc", "sd" -> {
                System.out.println("unknown string");
                yield "unknown";
            }
            default -> "default";
        };
    }
}

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Test {
    String value();
}

@Test("""
        c
        d
        """)
class TestBlockString {

}

record Record(String sa, int ia) {
}

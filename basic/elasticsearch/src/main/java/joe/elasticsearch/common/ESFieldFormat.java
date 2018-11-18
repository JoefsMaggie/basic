package joe.elasticsearch.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

public enum ESFieldFormat {
        DATE, VALUE, ENUM, BOOLEAN, STRING;

        @JsonCreator
        public static ESFieldFormat getFormat(String format) {
            for (ESFieldFormat existsESFieldFormat : values()) {
                if (StringUtils.equalsIgnoreCase(existsESFieldFormat.toString(), format)) {
                    return existsESFieldFormat;
                }
            }
            return null;
        }
    }
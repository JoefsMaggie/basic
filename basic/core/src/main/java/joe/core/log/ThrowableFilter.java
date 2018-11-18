package joe.core.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.google.common.collect.Sets;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * @author : Joe
 * @version V1.0
 * @Project: basic
 * @Package joe.core.log
 * @note: 错误日志过滤器
 * @date Date : 2018年09月03日 11:57
 */
public class ThrowableFilter extends TurboFilter {

    private String marker;
    private Marker markerToAccept;

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable throwable) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        Throwable t = throwable;
        if (t == null && (params != null && params.length > 0)) {
            Object exception = params[params.length - 1];
            if (exception instanceof Throwable) {
                t = (Throwable) exception;
            }
        }
        if (Objects.nonNull(t)) {
            StackTraceElement[] stackTrace = t.getStackTrace();
            Set<StackTraceElement> traceLines = Sets.newLinkedHashSetWithExpectedSize(10);
            for (StackTraceElement stack : stackTrace) {
                String line = stack.toString();
                if (line.startsWith("joe.")) {
                    traceLines.add(stack);
                } else if (line.startsWith("\tat joe.")) {
                    traceLines.add(stack);
                } else if (line.startsWith("Caused")) {
                    traceLines.add(stack);
                } else if (line.startsWith("\t")) {
                    traceLines.add(stack);
                }
            }
            t.setStackTrace(traceLines.toArray(new StackTraceElement[0]));
        }
        if (markerToAccept.equals(marker)) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.NEUTRAL;
        }
    }

    @Override
    public void start() {
        if (marker != null && marker.trim().length() > 0) {
            markerToAccept = MarkerFactory.getMarker(marker);
            super.start();
        }
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public static void main(String[] args) {
        try {
            throw new NullPointerException("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

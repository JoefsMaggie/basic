package joe.core.communicate;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Collections;
import java.util.Map;

/**
 * 单值返回类型
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月04日 17:02
 */
public class OneKeyResponse<V> {
    private Map<String, Object> oneKeyMap = Collections.emptyMap();

    public void singleton(String key, V value) {
        oneKeyMap = Collections.singletonMap(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return oneKeyMap;
    }
}

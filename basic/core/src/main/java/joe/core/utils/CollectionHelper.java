package joe.core.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionHelper {
    public <V, T> Map<String, V> listMap2Map(List<Map<String, T>> list, String key, String value) {
        return list.parallelStream()
                .map(m -> Collections.singletonMap(m.get(key).toString(), (V) m.get(value)))
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public <V, T> Map<String, V> listMap2Map(List<Map<String, T>> list, String key, Function<Map<String, T>, V> valFun) {
        return list.parallelStream()
                .map(m -> Collections.singletonMap(m.get(key).toString(), valFun.apply(m)))
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public <K, V, T> Map<K, V> listMap2Map(List<Map<String, T>> list, Function<Map<String, T>, K> keyFun, Function<Map<String, T>, V> valFun) {
        return list.parallelStream()
                .map(m -> Collections.singletonMap(keyFun.apply(m), valFun.apply(m)))
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

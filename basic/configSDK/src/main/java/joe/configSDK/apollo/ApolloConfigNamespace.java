package joe.configSDK.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.config.apollo
 * @Description : 根据命名空间获取配置信息
 * @date : 2018年10月11日 17:26
 */
public class ApolloConfigNamespace {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApolloConfigNamespace.class);

    public static final String DEFAULT_NAMESPACE = "application";
    private static final String DEFAULT_ARRAY_DELIMITER = ",";
    private static final String[] DEFAULT_ARRAY_VALUE = null;
    private static ApolloEnv env;
    /**
     * 配置，value：left -> 新值；right -> 旧值
     */
    private Map<String, Pair<String, String>> configMap;
    private String DEFAULT_VALUE = null;

    public static ApolloConfigNamespace builder() {
        return new ApolloConfigNamespace();
    }

    public static ApolloConfigNamespace builder(List<String> namespaces) {
        return new ApolloConfigNamespace(namespaces);
    }

    public static ApolloConfigNamespace builder(String... namespaces) {
        return new ApolloConfigNamespace(namespaces);
    }

    public static ApolloConfigNamespace buildIncludeDefault(List<String> namespaces) {
        if (!namespaces.contains(DEFAULT_NAMESPACE)) {
            namespaces.add(DEFAULT_NAMESPACE);
        }
        return new ApolloConfigNamespace(namespaces);
    }

    public static ApolloConfigNamespace buildIncludeDefault(String... namespaces) {
        if (ArrayUtils.contains(namespaces, DEFAULT_NAMESPACE)) {
            return new ApolloConfigNamespace(namespaces);
        }
        int length = namespaces.length;
        String[] newNamespaces = new String[length + 1];
        newNamespaces[0] = DEFAULT_NAMESPACE;
        System.arraycopy(namespaces, 0, newNamespaces, 1, length);
        return new ApolloConfigNamespace(newNamespaces);
    }

    public ApolloConfigNamespace() {
        this(DEFAULT_NAMESPACE);
    }

    public ApolloConfigNamespace(String namespace) {
        this(Lists.newArrayList(namespace));
    }

    public ApolloConfigNamespace(String... namespaces) {
        this(Arrays.asList(namespaces));
    }

    public ApolloConfigNamespace(List<String> namespaces) {
        this.checkEnv();
        namespaces.forEach(namespace -> {
            Config config = ConfigService.getConfig(namespace);
            Set<String> propertyNames = config.getPropertyNames();
            if (Objects.isNull(configMap)) {
                configMap = new ConcurrentHashMap<>(propertyNames.size());
            }
            propertyNames.forEach(propertyName -> {
                String property = config.getProperty(propertyName, DEFAULT_VALUE);
                configMap.put(propertyName, Pair.of(property, null));
            });
            // 设置监听
            listener(config);
        });
    }

    /**
     * 校验环境
     */
    private void checkEnv() {
        if (Objects.nonNull(ApolloConfigNamespace.env)) {
            LOGGER.info("env: {}", ApolloConfigNamespace.env);
            return;
        }
        String env = System.getProperty("env");
        if (StringUtils.isBlank(env) || StringUtils.equalsIgnoreCase(env, ApolloEnv.LOCAL.toString())) {
            System.setProperty("env", ApolloEnv.DEV.toString());
            ApolloConfigNamespace.env = ApolloEnv.DEV;
        } else {
            switch (ApolloEnv.getInst(env)) {
                case FAT:
                    ApolloConfigNamespace.env = ApolloEnv.FAT;
                    break;
                case UAT:
                    ApolloConfigNamespace.env = ApolloEnv.UAT;
                    break;
                case PRO:
                    ApolloConfigNamespace.env = ApolloEnv.PRO;
                    break;
                default:
                    ApolloConfigNamespace.env = ApolloEnv.DEV;
            }
        }
        LOGGER.info("env: {}", ApolloConfigNamespace.env);
    }

    /**
     * 设置 apollo 配置监听器
     */
    private void listener(Config config) {
        config.addChangeListener(changeEvent -> {
            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                LOGGER.info("property {} in namespace {} is {} by value {}, the old value is {}",
                            key, changeEvent.getNamespace(), change.getChangeType(), change.getNewValue(), change.getOldValue());
                configMap.put(change.getPropertyName(), Pair.of(change.getNewValue(), change.getOldValue()));
            }
        });
    }

    public int configInteger(String key) {
        return configInteger(key, 0);
    }

    public int configInteger(String key, int defaultValue) {
        String config = config(key);
        return NumberUtils.toInt(config, defaultValue);
    }

    public long configLong(String key) {
        return configLong(key, 0);
    }

    public long configLong(String key, long defaultValue) {
        String config = config(key);
        return NumberUtils.toLong(config, defaultValue);
    }

    public double configDouble(String key) {
        return configDouble(key, 0.0);
    }

    public double configDouble(String key, double defaultValue) {
        String config = config(key);
        return NumberUtils.toDouble(config, defaultValue);
    }

    public boolean configBoolean(String key) {
        String config = config(key);
        return BooleanUtils.toBoolean(config);
    }

    public String[] configArray(String key) {
        return configArray(key, DEFAULT_ARRAY_DELIMITER);
    }

    public String[] configArray(String key, String delimiter) {
        return configArray(key, delimiter, DEFAULT_ARRAY_VALUE);
    }

    public String[] configArray(String key, String... defaultValue) {
        return configArray(key, DEFAULT_ARRAY_DELIMITER, defaultValue);
    }

    public String[] configArray(String key, String delimiter, String... defaultValue) {
        String config = config(key);
        if (StringUtils.equals(config, DEFAULT_VALUE)) {
            return defaultValue;
        }
        return config.split(delimiter);
    }

    public List<String> configList(String key) {
        return configList(key, DEFAULT_ARRAY_DELIMITER);
    }

    public List<String> configList(String key, String... defaultValue) {
        return configList(key, DEFAULT_ARRAY_DELIMITER, defaultValue);
    }

    public List<String> configList(String key, String delimiter) {
        return configList(key, delimiter, DEFAULT_ARRAY_VALUE);
    }

    public List<String> configList(String key, String delimiter, String... defaultValue) {
        return Arrays.stream(configArray(key, delimiter, defaultValue)).collect(Collectors.toList());
    }

    public String config(String key) {
        return config(key, DEFAULT_VALUE);
    }

    public String config(String key, String defaultValue) {
        Pair<String, String> propertyPair = configMap.get(key);
        if (Objects.isNull(propertyPair)) {
            return defaultValue;
        }
        String property = propertyPair.getLeft();
        if (StringUtils.equals(property, DEFAULT_VALUE)) {
            return defaultValue;
        }
        return property;
    }

    public Map<String, Pair<String, String>> getConfigMap() {
        return configMap;
    }

    public ApolloConfigNamespace setConfigMap(Map<String, Pair<String, String>> configMap) {
        this.configMap = configMap;
        return this;
    }
}

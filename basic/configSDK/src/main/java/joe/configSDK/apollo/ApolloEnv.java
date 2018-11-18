package joe.configSDK.apollo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * Apollo 环境
 * Date : 2018年10月12日 11:32
 */
public enum ApolloEnv {
    LOCAL, DEV, FAT, UAT, PRO;

    public static ApolloEnv getInst(String env) {
        for (ApolloEnv apolloEnv : values()) {
            if (StringUtils.equalsIgnoreCase(apolloEnv.toString(), env)) {
                return apolloEnv;
            }
        }
        throw new IllegalArgumentException("no such apollo env with name " + env);
    }
}

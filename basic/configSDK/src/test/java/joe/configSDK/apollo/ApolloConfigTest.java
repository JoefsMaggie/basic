package joe.configSDK.apollo;

import org.junit.Before;
import org.junit.Test;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * 测试
 * Date : 2018年10月10日 15:10
 */
public class ApolloConfigTest {
    @Before
    public void systemConfig() {
        System.setProperty("apollo.cluster", "test-cluster");
    }

    @Test
    public void config() {
        ApolloConfigNamespace apolloConfigNamespace = ApolloConfigNamespace.builder();
        print(apolloConfigNamespace);
    }

    @Test
    public void publicConfig() {
        ApolloConfigNamespace apolloConfigNamespace = ApolloConfigNamespace.builder("Business2.test2-public");
        print(apolloConfigNamespace);
    }

    @Test
    public void associated() {
        ApolloConfigNamespace apolloConfigNamespace = ApolloConfigNamespace.builder("Business2.test-public");
        print(apolloConfigNamespace);
    }

    @Test
    public void multiNamespaces() {
        ApolloConfigNamespace apolloConfigNamespace = ApolloConfigNamespace.buildIncludeDefault("Business2.test-public", "Business2.test2-public");
        print(apolloConfigNamespace);
    }

    private void print(ApolloConfigNamespace apolloConfigNamespace) {
        System.out.println(apolloConfigNamespace.config("test"));
        System.out.println(apolloConfigNamespace.config("keyOne"));
        System.out.println(apolloConfigNamespace.configInteger("keyTwo"));
        System.out.println(apolloConfigNamespace.configBoolean("keyThree"));
        System.out.println(apolloConfigNamespace.config("keyFour", "defaultValue"));
        System.out.println(apolloConfigNamespace.getConfigMap());
        loop();
    }

    private void loop() {
        while (true) ;
    }
}

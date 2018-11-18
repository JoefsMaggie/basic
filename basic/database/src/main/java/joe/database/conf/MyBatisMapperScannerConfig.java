package joe.database.conf;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : 配置 mybatis 的扫描路径
 * Date: 2018/10/29
 */
//@Component
@AutoConfigureAfter(MybatisConfig.class)
public class MyBatisMapperScannerConfig {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 获取之前注入的beanName为sqlSessionFactory的对象
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        // 指定 mapper 配置文件的路径
        mapperScannerConfigurer.setBasePackage("joe.*.*.mapper");
        return mapperScannerConfigurer;
    }
}

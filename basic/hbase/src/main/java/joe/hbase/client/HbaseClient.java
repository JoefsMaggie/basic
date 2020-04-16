package joe.hbase.client;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * hbase
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月20日 9:20
 */
@Configuration
public class HbaseClient {

    @Autowired
    private HBaseProperties properties;

    @Bean
    public HbaseTemplate hbaseTemplate() {
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        hbaseTemplate.setConfiguration(conf());
        hbaseTemplate.setAutoFlush(true);
        return hbaseTemplate;
    }

    private org.apache.hadoop.conf.Configuration conf() {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        properties.getConfig().forEach(conf::set);
        return conf;
    }
}
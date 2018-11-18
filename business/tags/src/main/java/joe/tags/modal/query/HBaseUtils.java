package joe.tags.modal.query;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * HBase工具类
 * Author JiaPeng_lv
 */
@Component
public class HBaseUtils {
    private static Connection connection;
    private static Configuration configuration;
    private static HBaseUtils hBaseUtils;
    private static Properties properties;

    /**
     * 创建连接池并初始化环境配置
     */
    public void init() {
        properties = System.getProperties();
        //实例化HBase配置类
        if (configuration == null) {
            configuration = HBaseConfiguration.create();
        }
        try {
            //加载本地hadoop二进制包
//            properties.setProperty("hadoop.home.dir", "D:\hadoop-common-2.6.0-bin-master");
            //zookeeper集群的URL配置信息
            configuration.set("hbase.zookeeper.quorum", "node0.Joe1.com,node1.Joe1.com,node2.Joe1.com");
            //HBase的Master
//            configuration.set("hbase.master","hba:60000");
            //客户端连接zookeeper端口
            configuration.set("hbase.zookeeper.property.clientPort", "2181");
            //HBase RPC请求超时时间，默认60s(60000)
            configuration.setInt("hbase.rpc.timeout", 20000);
            //客户端重试最大次数，默认35
            configuration.setInt("hbase.client.retries.number", 10);
            //客户端发起一次操作数据请求直至得到响应之间的总超时时间，可能包含多个RPC请求，默认为2min
            configuration.setInt("hbase.client.operation.timeout", 30000);
            //客户端发起一次scan操作的rpc调用至得到响应之间的总超时时间
            configuration.setInt("hbase.client.scanner.timeout.period", 200000);
            //获取hbase连接对象
            if (connection == null || connection.isClosed()) {
                connection = ConnectionFactory.createConnection(configuration);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接池
     */
    public static void close() {
        try {
            if (connection != null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私有无参构造方法
     */
    private HBaseUtils() {}

    /**
     * 唯一实例，线程安全，保证连接池唯一
     *
     * @return
     */
    public static HBaseUtils getInstance() {
        if (hBaseUtils == null) {
            synchronized (HBaseUtils.class) {
                if (hBaseUtils == null) {
                    hBaseUtils = new HBaseUtils();
                    hBaseUtils.init();
                }
            }
        }
        return hBaseUtils;
    }

    /**
     * 获取单条数据
     *
     * @param tablename
     * @param row
     * @return
     * @throws IOException
     */
    public static Result getRow(String tablename, byte[] row) throws IOException {
        Table table = null;
        Result result = null;
        try {
            table = connection.getTable(TableName.valueOf(tablename));
            Get get = new Get(row);
            result = table.get(get);
        } finally {
            table.close();
        }
        return result;
    }

    /**
     * 查询多行信息
     *
     * @param tablename
     * @param rows
     * @return
     * @throws IOException
     */
    public static Result[] getRows(String tablename, List<byte[]> rows) throws IOException {
        Table table = null;
        List<Get> gets = null;
        Result[] results = null;
        try {
            table = connection.getTable(TableName.valueOf(tablename));
            gets = new ArrayList<Get>();
            for (byte[] row : rows) {
                if (row != null) {
                    gets.add(new Get(row));
                }
            }
            if (gets.size() > 0) {
                results = table.get(gets);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            table.close();
        }
        return results;
    }

    /**
     * 获取整表数据
     *
     * @param tablename
     * @return
     */
    public static ResultScanner get(String tablename) throws IOException {
        Table table = null;
        ResultScanner results = null;
        try {
            table = connection.getTable(TableName.valueOf(tablename));
            Scan scan = new Scan();
            scan.setCaching(1000);
            results = table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            table.close();
        }
        return results;
    }

    /**
     * 单行插入数据
     *
     * @param tablename
     * @param rowkey
     * @param family
     * @param cloumns
     * @throws IOException
     */
    public static void put(String tablename, String rowkey, String family, Map<String, String> cloumns) throws IOException {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tablename));
            Put put = new Put(rowkey.getBytes());
            for (Map.Entry<String, String> entry : cloumns.entrySet()) {
                put.addColumn(family.getBytes(), entry.getKey().getBytes(), entry.getValue().getBytes());
            }
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            table.close();
            close();
        }
    }
}
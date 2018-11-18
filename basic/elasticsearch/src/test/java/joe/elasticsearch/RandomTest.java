package joe.elasticsearch;

import joe.database.utils.SnowflakeFactory;
import joe.database.utils.SnowflakeGenerator;
import lombok.val;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Random 性能测试
 * Date: 2018/10/31
 */
public class RandomTest {

    @Test
    public void address() throws SocketException, UnknownHostException {
        final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    @Test
    public void bit() {
        System.out.println(-1 << 3);
        System.out.println(-2 ^ (-1 << 3));
        System.out.println(~-8);

        System.out.println();

        System.out.println(Integer.toBinaryString(-2));
        System.out.println(Integer.toBinaryString(-8));
        System.out.println(Integer.toBinaryString(-2 ^ -8));
        System.out.println(Integer.toBinaryString(-10));
        System.out.println(Integer.toBinaryString(-10 >> 1));
        System.out.println(Integer.toBinaryString(-10 >> 3));
        System.out.println(Integer.toBinaryString(~-10));
    }

    @Test
    public void generator() {
        SnowflakeGenerator snowflakeFactory = new SnowflakeGenerator(1, 1);
        long start = System.currentTimeMillis();
        Stream.iterate(0, n -> n + 1)
              .limit(100000)
              .parallel()
              .forEach(integer -> System.out.println(snowflakeFactory.newId()));
        System.out.println(System.currentTimeMillis() - start);
        long id = snowflakeFactory.newId();
        System.out.println(id);
        System.out.println(SnowflakeFactory.getTSDate(id));
        System.out.println(SnowflakeFactory.getWorkerId(id));
        System.out.println(SnowflakeFactory.getTimestamps(id));
    }

    @Test
    public void random() {
        System.out.println(Math.random());
        System.out.println(ThreadLocalRandom.current().nextLong());
        System.out.println(UUID.randomUUID().toString());
        long loop = 1000000;
        long tmp = 0;
        for (int i = 0; i < loop; i++) {
            long start = System.currentTimeMillis();
            Math.random();
            tmp += System.currentTimeMillis() - start;
        }
        System.out.println(tmp);
        tmp = 0;
        for (int i = 0; i < loop; i++) {
            long start = System.currentTimeMillis();
            ThreadLocalRandom.current().nextLong();
            tmp += System.currentTimeMillis() - start;
        }
        System.out.println(tmp);
        tmp = 0;
        for (int i = 0; i < loop; i++) {
            long start = System.currentTimeMillis();
            UUID.randomUUID().toString();
            tmp += System.currentTimeMillis() - start;
        }
        System.out.println(tmp);

        val random = new Random();
        for (int i = 0; i < loop; i++) {
            System.out.println(random.nextInt(10));
        }
    }
}

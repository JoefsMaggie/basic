package joe.database.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * snowflake 算法生成 ID
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/31
 * <p>
 * Twitter_Snowflake <br>
 * Snowflake的结构如下(每部分用-分开): <br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0 <br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截) <br>
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间， <br>
 * 由我们程序来指定的（如下下面程序SnowflakeFactory类的START_TIMESTAMP属性）。 <br>
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69 <br>
 * 10位的数据机器位，可以部署在1024个节点，包括3位dataCenterId和7位workerId <br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * Snowflake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，<br>
 * 并且效率较高，经测试，Snowflake每秒能够产生26万ID左右。
 */
public class SnowflakeFactory {

    // ============================== Fields ================================
    /**
     * 开始时间截 (2018-10-01)
     */
    private static final long START_TIMESTAMP = 1538323200000L;
    /**
     * 机器id所占的位数
     */
    private static final long WORKER_ID_BITS = 7L;
    /**
     * 数据标识id所占的位数
     */
    private static final long DATA_CENTER_ID_BITS = 3L;
    /**
     * 支持的最大机器 ID，7的结果是127 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    /**
     * 支持的最大数据标识 ID，3的结果是7
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    /**
     * 序列在 ID 中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;
    /**
     * 机器 ID 向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据标识 ID 向左移17位(12+5)
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 工作机器ID(0~7)
     */
    private long workerId;
    /**
     * 数据中心ID(0~127)
     */
    private long dataCenterId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成 ID 的时间截
     */
    private long lastTimestamp = -1L;

    private Random random = new Random();

    // ============================== Constructors ================================

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowflakeFactory(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            //sequence = 0L;
            // 设置随机的初始值，避免尾数分布不均
            sequence = random.nextInt(10);
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT) // 时间戳移位
                | (dataCenterId << DATA_CENTER_ID_SHIFT) // 数据中心 ID 移位
                | (workerId << WORKER_ID_SHIFT) // 工作节点 ID 移位
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取workerId
     *
     * @param snowflakeId snowflake算法生成的Id
     * @return workerId
     */
    public static int getWorkerId(long snowflakeId) {
        // '0'*45 + '1'*7+ '0'*10
        long mask = 0x1fc00;
        return (int) ((snowflakeId & mask) >> SEQUENCE_BITS);
    }

    /**
     * 获取时间戳
     *
     * @param snowflakeId snowflake算法生成的Id
     * @return id里面的时间戳
     */
    public static long getTimestamps(long snowflakeId) {
        // '0'*1 + '1'*41+ '0'*22
        long mask = 0x7fffffffffc00000L;
        return ((snowflakeId & mask) >> TIMESTAMP_LEFT_SHIFT) + START_TIMESTAMP;
    }

    public static Date getTSDate(long snowflakeId) {
        return new Date(getTimestamps(snowflakeId));
    }

    //==============================Test=============================================

    /**
     * 测试
     *
     * @throws ParseException exc
     */
    public static void main(String[] args) throws ParseException {
        SnowflakeFactory idWorker = new SnowflakeFactory(100, 3);

        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            long id = idWorker.nextId();
            // System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }

        t = System.currentTimeMillis() - t;
        System.out.println(t);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.parse("2017-01-01").getTime());
    }

}
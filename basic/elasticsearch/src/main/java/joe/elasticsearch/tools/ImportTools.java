package joe.elasticsearch.tools;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joe.database.utils.IdGenerator;
import joe.database.utils.SnowflakeGenerator;
import joe.elasticsearch.client.conf.EsClient;
import joe.elasticsearch.client.conf.EsConfig;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : ES 数据导入工具
 * Date: 2018/10/31
 */
public class ImportTools {

    private static final String CLUSTER_NAME = "search";
    private static final String CLUSTER_NODES = "192.168.5.207:9300";
    private static final String DEFAULT_INDEX = "shop";
    private static final String DEFAULT_TYPE = "cloth";
    private static final int SINGLE_FILE_DATA_COUNT = 1000;
    private static String index = DEFAULT_INDEX;
    private static String type = DEFAULT_TYPE;
    private static String indexJson;
    private static final List<Long> longs = Lists.newArrayList(
            1234567890L, 9876543210L, 8765432109L, 7654321098L, 6543210987L,
            5432109876L, 4321098765L, 3210987654L, 2109876543L, 1098765432L
    );
    private static final List<String> strings = Lists.newArrayList(
            "1234567890L", "9876543210L", "8765432109L", "7654321098L", "6543210987L",
            "5432109876L", "4321098765L", "3210987654L", "2109876543L", "1098765432L"
    );
    private static final int randomSize = longs.size();
    private Random random = new Random();
    private IdGenerator idGenerator;
    private TransportClient client;
    private static final String FILE_SUFFIX = ".json";
    private static String memberLogin;
    private static List<String> memberLoginList = Lists.newArrayList(
            "1234567890L", "9876543210L", "8765432109L", "7654321098L", "6543210987L",
            "5432109876L", "4321098765L", "3210987654L", "2109876543L", "1098765432L"
    );
    private static int memberLoginSize = memberLoginList.size();
    private static int concurrent = 4;
    private static int fieldCount = 500;
    private static int totalDataCount = 1000_0000;
    private static final int threadNum = 10;
    private static int loop = 500;
    private static int dataCount = totalDataCount / concurrent / loop;
    private static final String PATH = System.getProperty("path");
    private static final Set<PosixFilePermission> FILE_PERMISSIONS = PosixFilePermissions.fromString("rwxrwxrw-");
    private static final FileAttribute<Set<PosixFilePermission>> FILE_ATTRIBUTE = PosixFilePermissions.asFileAttribute(FILE_PERMISSIONS);
    private static String ip;

    static {
        try {
            if (StringUtils.isBlank(System.getProperty("ip"))) {
                ip = InetAddress.getLocalHost().getHostAddress();
            } else {
                ip = System.getProperty("ip");
            }
            System.out.println(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("starting...");
        init();
        System.out.println("connecting es ...");
        List<Pair<ImportTools, Integer>> tools = Lists.newArrayListWithExpectedSize(ImportTools.concurrent);
        for (int i = 1; i <= ImportTools.concurrent; i++) {
            tools.add(Pair.of(new ImportTools(1, i), i));
        }
        // 计算循环次数，即每个线程生成的文件个数
        loop = totalDataCount / concurrent / SINGLE_FILE_DATA_COUNT;
        tools.parallelStream().forEach(importToolsPair -> {
            try {
                importToolsPair.getKey().createJson(importToolsPair);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("exit");
    }

    private static void init() {
        System.out.println("init...");
        if (StringUtils.isBlank(PATH)) {
            System.out.println("path 参数不能为空，请指定保存路径");
        }
        final String index = System.getProperty("index");
        if (StringUtils.isNotBlank(index)) {
            ImportTools.index = index;
        }
        final String type = System.getProperty("type");
        if (StringUtils.isNotBlank(type)) {
            ImportTools.type = type;
        }
        final HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("_index", ImportTools.index);
        map.put("_type", ImportTools.type);
        indexJson = new JSONObject(Collections.singletonMap("index", map)).toString();
        final String concurrent = System.getProperty("concurrent");
        if (StringUtils.isNotBlank(concurrent)) {
            ImportTools.concurrent = Integer.valueOf(concurrent);
        }
        final String dataCount = System.getProperty("dataCount");
        if (StringUtils.isNotBlank(dataCount)) {
            ImportTools.totalDataCount = Integer.valueOf(dataCount);
        }
        final String fieldCount = System.getProperty("fieldCount");
        if (StringUtils.isNotBlank(fieldCount)) {
            ImportTools.fieldCount = Integer.valueOf(fieldCount);
        }
        final String memberLogin = System.getProperty("memberLogin");
        if (StringUtils.isNotBlank(memberLogin)) {
            ImportTools.memberLogin = memberLogin;
        }
    }

    private void createJson(Pair<ImportTools, Integer> importToolsPair) throws IOException {
        final String shellFilePath = PATH + "/import" + importToolsPair.getValue() + ".sh";
        final Path shellFile = Paths.get(shellFilePath);
        if (Files.exists(shellFile)) {
            Files.delete(shellFile);
        }
        Files.createFile(shellFile, FILE_ATTRIBUTE);
        final BufferedWriter shellBufWriter = Files.newBufferedWriter(shellFile);
        shellBufWriter.write("#!/bin/bash \n\n");
        for (int k = 0; k < loop; k++) {
            final String dataCurPath = "data/data-" + importToolsPair.getValue() + "-" + k + FILE_SUFFIX;
            final String filePath = PATH + "/" + dataCurPath;
            String curl = "curl -H 'Content-Type: application/x-ndjson' " +
                    "-XPOST 'http://" + ip + ":9200/" + index + "/" + type + "/_bulk' " +
                    "--data-binary @" + dataCurPath + " > /dev/null \n";
            shellBufWriter.write(curl);
            shellBufWriter.flush();
            final Path dataFile = Paths.get(filePath);
            if (Files.exists(dataFile)) {
                Files.delete(dataFile);
            }
            System.out.println(String.format("create path %s", dataFile));
            Files.createFile(dataFile);
            BufferedWriter dataBufWriter = Files.newBufferedWriter(dataFile);
            JSONObject jsonObject = new JSONObject();
            System.out.println("creating data " + LocalDateTime.now().toString());
            for (int i = 0; i < SINGLE_FILE_DATA_COUNT; i++) {
                // 生成数据
                for (int j = 0; j < ImportTools.fieldCount; ) {
                    int idx = random.nextInt(randomSize);
                    jsonObject.put("field" + ++j, longs.get(idx));
                    jsonObject.put("field" + ++j, strings.get(idx));
                    jsonObject.put("field" + ++j, longs.get(idx));
                    jsonObject.put("field" + ++j, strings.get(idx));
                    jsonObject.put("field" + ++j, longs.get(idx));
                    jsonObject.put("field" + ++j, strings.get(idx));
                    jsonObject.put("field" + ++j, longs.get(idx));
                    jsonObject.put("field" + ++j, strings.get(idx));
                    jsonObject.put("field" + ++j, longs.get(idx));
                    jsonObject.put("field" + ++j, strings.get(idx));
                }
                if (StringUtils.isNotBlank(memberLogin)) {
                    jsonObject.put("memberLogin", memberLogin);
                } else {
                    jsonObject.put("memberLogin", memberLoginList.get(random.nextInt(memberLoginSize)));
                }
                // 写数据
                dataBufWriter.write(indexJson + "\n");
                dataBufWriter.write(jsonObject.toString() + "\n");
                jsonObject.clear();
                if (i % 100 == 0) {
                    // 刷数据
                    dataBufWriter.flush();
                }
            }
            dataBufWriter.close();
            System.out.println("created  data " + LocalDateTime.now().toString());
        }
        shellBufWriter.close();
    }

    private void concurrentCreateData() {
        // 新建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        // 确认可并发操作的数量
        Semaphore semaphore = new Semaphore(threadNum);
        final BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int i = 0; i < dataCount; i++) {
            // 获得操作权限，当并发达到threadNum，则要等待有人释放线程。
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.execute(() -> {
                // 操作
                insert(bulkRequest);
                // 释放线程
                semaphore.release();
            });
        }
        // 关闭线程池
        executorService.shutdown();
    }

    private void bulkData(ImportTools importTools) {
        for (int j = 0; j < loop; j++) {
            final BulkRequestBuilder bulkRequest = client.prepareBulk();
            final val start = System.currentTimeMillis();
            for (int i = 0; i < dataCount; i++) {
                importTools.insert(bulkRequest);
            }
            final val end = System.currentTimeMillis();
            System.out.println("use time 1 : " + (end - start));
            System.out.println("data count : " + bulkRequest.request().requests().size());
            bulkRequest.get();
            System.out.println("use time 2 : " + (System.currentTimeMillis() - start));
        }
    }

    private ImportTools() {
        this(DEFAULT_TYPE, 1, 1);
    }

    private ImportTools(int dcId, int workerId) {
        this(DEFAULT_TYPE, dcId, workerId);
    }

    private ImportTools(String type, int dcId, int workerId) {
        this(DEFAULT_INDEX, type, dcId, workerId);
    }

    private ImportTools(String index, String type, int dcId, int workerId) {
        EsConfig esConfig = new EsConfig();
        esConfig.setClusterName(CLUSTER_NAME).setClusterNodes(CLUSTER_NODES);
        EsClient esClient = new EsClient(esConfig);
        client = esClient.client();
        this.index = index;
        this.type = type;
        this.idGenerator = new SnowflakeGenerator(workerId, dcId);
    }

    private void insert(BulkRequestBuilder bulkRequest) {
        // 插入
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < ImportTools.fieldCount; i++) {
            int idx = random.nextInt(randomSize);
            jsonObject.put("field" + i, longs.get(idx));
            jsonObject.put("field" + ++i, strings.get(idx));
        }
        bulkRequest.add(client.prepareIndex(index, type, idGenerator.newIdStr()).setSource(jsonObject.toString(), XContentType.JSON));
    }

    public void update() {
        // 更新
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "test1");
        jsonObject.put("text", "test2");
        JSONObject infoJson = new JSONObject();
        infoJson.put("name", "questions");
        jsonObject.put("questions_answer", infoJson);
        final BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareUpdate(index, type, "1").setDoc(jsonObject.toString(), XContentType.JSON));

        BulkResponse response = bulkRequest.get();
    }

    public void delete() {
        // 删除
        final BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareDelete(index, "tiku", "1"));
    }
}

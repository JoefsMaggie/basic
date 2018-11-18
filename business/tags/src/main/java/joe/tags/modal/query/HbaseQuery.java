package joe.tags.modal.query;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.query
 * @note: hbase 查询
 * @date Date : 2018年09月20日 16:53
 */
@Component
public class HbaseQuery {
    private final HbaseTemplate hbaseTemplate;
    @Value("${hbase.tableName.updateTime}")
    private String updateTimeTable;

    @Autowired(required = false)
    public HbaseQuery(HbaseTemplate hbaseTemplate) {this.hbaseTemplate = hbaseTemplate;}

    public String queryDate() {
        return hbaseTemplate.get(updateTimeTable, "CustomerTags_LastUpdateTime", "cf1", (result, rowNum) -> {
            List<Cell> ceList = result.listCells();
            String res = "";
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    res = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }
            return res;
        });
    }
}

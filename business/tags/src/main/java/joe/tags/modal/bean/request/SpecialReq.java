package joe.tags.modal.bean.request;

import com.google.common.collect.Maps;
import joe.elasticsearch.common.Metric;
import joe.tags.common.CusTagField;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : 特殊处理
 * @date : 2018年10月23日 15:43
 */
@Data
public class SpecialReq {
    private boolean useSpecial;
    private Integer pageindex;
    private Integer pagesize;
    private String orderby;
    private String openid = "";
    private String memberlogin;
    private Integer gzstatus;
    private List<Integer> sexs;
    private Integer minpoints;
    private Integer maxpoints;
    private Integer minpinmoney;
    private Integer maxpinmoney;
    private List<Map<String, Integer>> growthvaluelist;
    private Long gzstarttime;
    private Long gzendtime;
    private Integer gztimeday;
    private Integer isbindmoblie;
    private String mobileEmpty;
    private String mobileNotEmpty;
    private String mobileExists;
    private Integer mingrowthvalue;
    private Integer maxgrowthvalue;
    private List<Integer> gzstatuslist;
    private Integer isMember;
    private Integer sourcelevel1;
    private Integer sourcelevel2;
    private List<Integer> userlabels;
    private List<Integer> identities;
    private Map<String, Object> special;
    public static Map<String, Pair<Object, Metric>> specialField;

    static {
        specialField = Maps.newHashMapWithExpectedSize(16);
        specialField.put("pageindex", null);
        specialField.put("pagesize", null);
        specialField.put("orderby", null);
        specialField.put("openid", Pair.of(CusTagField.OPEN_ID_KEYWORD, Metric.NE));
        specialField.put("memberlogin", Pair.of(CusTagField.MEMBER_LOGIN_KEYWORD, Metric.EQ));
        specialField.put("gzstatus", Pair.of(CusTagField.GZ_STATUS, Metric.EQ));
        specialField.put("sexs", Pair.of(CusTagField.GENDER, Metric.EQ));
        specialField.put("minpoints", Pair.of(CusTagField.JF_BALANCE, Metric.GTE));
        specialField.put("maxpoints", Pair.of(CusTagField.JF_BALANCE, Metric.LTE));
        specialField.put("minpinmoney", Pair.of(CusTagField.HB_BALANCE, Metric.GTE));
        specialField.put("maxpinmoney", Pair.of(CusTagField.HB_BALANCE, Metric.LTE));

        specialField.put("mingrowthvalue", Pair.of(CusTagField.GROWTH_VALUE, Metric.GTE));
        specialField.put("maxgrowthvalue", Pair.of(CusTagField.GROWTH_VALUE, Metric.LTE));
        HashMap<Object, Object> subMap = Maps.newHashMapWithExpectedSize(2);
        subMap.put("mingrowthvalue", Pair.of(CusTagField.GROWTH_VALUE, null));
        subMap.put("maxgrowthvalue", Pair.of(CusTagField.GROWTH_VALUE, null));
        specialField.put("growthvaluelist", Pair.of(subMap, Metric.GTE_LTE));
        // 成长值列表不为空，加此条件
        specialField.put("isMember", Pair.of(CusTagField.IS_MEMBER, Metric.EQ));

        specialField.put("gzstarttime", Pair.of(CusTagField.NEWEST_GZ_TIME, Metric.GTE));
        specialField.put("gzendtime", Pair.of(CusTagField.NEWEST_GZ_TIME, Metric.LTE));
        specialField.put("gztimeday", Pair.of(CusTagField.NEWEST_GZ_TIME, Metric.EQ));
        specialField.put("isbindmoblie", Pair.of(CusTagField.IS_BIND_MOBILE, Metric.EQ));
        specialField.put("mobileEmpty", Pair.of(CusTagField.MOBILE_KEYWORD, Metric.EQ));
        specialField.put("mobileNotEmpty", Pair.of(CusTagField.MOBILE_KEYWORD, Metric.NE));
        specialField.put("mobileExists", Pair.of(CusTagField.MOBILE_KEYWORD, Metric.EXISTS));
        specialField.put("gzstatuslist", Pair.of(CusTagField.GZ_STATUS, Metric.EQ));
        specialField.put("sourcelevel1", Pair.of(CusTagField.SOURCE_LEVEL_1, Metric.EQ));
        specialField.put("sourcelevel2", Pair.of(CusTagField.SOURCE_LEVEL_2, Metric.EQ));
        specialField.put("userlabels", Pair.of(CusTagField.USER_LABEL, Metric.EQ));
        specialField.put("identities", Pair.of(CusTagField.IDENTITY, Metric.EQ));
    }
}

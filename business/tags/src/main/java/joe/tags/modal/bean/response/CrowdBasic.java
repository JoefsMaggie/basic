package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.response
 * @note: 人群详情基础数据模型
 * @date Date : 2018年09月19日 19:39
 */
public class CrowdBasic {
    @JsonProperty("person_count")
    private long personCount;
    @JsonProperty("report_date")
    private String reportDate;

    public long getPersonCount() {
        return personCount;
    }

    public CrowdBasic setPersonCount(long personCount) {
        this.personCount = personCount;
        return this;
    }

    public String getReportDate() {
        return reportDate;
    }

    public CrowdBasic setReportDate(String reportDate) {
        this.reportDate = reportDate;
        return this;
    }
}

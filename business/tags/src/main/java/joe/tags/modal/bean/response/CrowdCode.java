package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.response
 * @note: 人群详情扫码数据模型
 * @date Date : 2018年09月19日 19:40
 */
public class CrowdCode {
    @JsonProperty("scan_person_count")
    private long scanPersonCount;
    @JsonProperty("not_scan_person_count")
    private long notScanPersonCount;
    @JsonProperty("fwm_person_count")
    private long fwmPersonCount;
    @JsonProperty("dgm_person_count")
    private long dgmPersonCount;
    @JsonProperty("fwm4_person_count")
    private long fwm4PersonCount;
    @JsonProperty("wsm_person_count")
    private long wsmPersonCount;
    @JsonProperty("sym_person_count")
    private long symPersonCount;

    public long getScanPersonCount() {
        return scanPersonCount;
    }

    public CrowdCode setScanPersonCount(long scanPersonCount) {
        this.scanPersonCount = scanPersonCount;
        return this;
    }

    public long getNotScanPersonCount() {
        return notScanPersonCount;
    }

    public CrowdCode setNotScanPersonCount(long notScanPersonCount) {
        this.notScanPersonCount = notScanPersonCount;
        return this;
    }

    public long getFwmPersonCount() {
        return fwmPersonCount;
    }

    public CrowdCode setFwmPersonCount(long fwmPersonCount) {
        this.fwmPersonCount = fwmPersonCount;
        return this;
    }

    public long getDgmPersonCount() {
        return dgmPersonCount;
    }

    public CrowdCode setDgmPersonCount(long dgmPersonCount) {
        this.dgmPersonCount = dgmPersonCount;
        return this;
    }

    public long getFwm4PersonCount() {
        return fwm4PersonCount;
    }

    public CrowdCode setFwm4PersonCount(long fwm4PersonCount) {
        this.fwm4PersonCount = fwm4PersonCount;
        return this;
    }

    public long getWsmPersonCount() {
        return wsmPersonCount;
    }

    public CrowdCode setWsmPersonCount(long wsmPersonCount) {
        this.wsmPersonCount = wsmPersonCount;
        return this;
    }

    public long getSymPersonCount() {
        return symPersonCount;
    }

    public CrowdCode setSymPersonCount(long symPersonCount) {
        this.symPersonCount = symPersonCount;
        return this;
    }
}

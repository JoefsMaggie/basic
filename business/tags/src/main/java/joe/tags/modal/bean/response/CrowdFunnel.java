package joe.tags.modal.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 人群详情漏斗模型
 * Date Date : 2018年09月19日 19:39
 */
public class CrowdFunnel {
    @JsonProperty("total_count")
    private long totalCount;
    @JsonProperty("subscribe_count")
    private long subscribeCount;
    @JsonProperty("member_count")
    private long memberCount;
    @JsonProperty("not_subscribe_count")
    private long notSubscribeCount;
    @JsonProperty("not_active_count")
    private long notActiveCount;

    public long getTotalCount() {
        return totalCount;
    }

    public CrowdFunnel setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public long getSubscribeCount() {
        return subscribeCount;
    }

    public CrowdFunnel setSubscribeCount(long subscribeCount) {
        this.subscribeCount = subscribeCount;
        return this;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public CrowdFunnel setMemberCount(long memberCount) {
        this.memberCount = memberCount;
        return this;
    }

    public long getNotSubscribeCount() {
        return notSubscribeCount;
    }

    public CrowdFunnel setNotSubscribeCount(long notSubscribeCount) {
        this.notSubscribeCount = notSubscribeCount;
        return this;
    }

    public long getNotActiveCount() {
        return notActiveCount;
    }

    public CrowdFunnel setNotActiveCount(long notActiveCount) {
        this.notActiveCount = notActiveCount;
        return this;
    }
}

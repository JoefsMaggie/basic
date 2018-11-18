package joe.tags.modal.bean.response;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.modal.bean.response
 * @note: 人群详情性别饼图模型
 * @date Date : 2018年09月19日 19:40
 */
public class CrowdGender {
    private long male;
    private long female;
    private long unknown;

    public long getMale() {
        return male;
    }

    public CrowdGender setMale(long male) {
        this.male = male;
        return this;
    }

    public long getFemale() {
        return female;
    }

    public CrowdGender setFemale(long female) {
        this.female = female;
        return this;
    }

    public long getUnknown() {
        return unknown;
    }

    public CrowdGender setUnknown(long unknown) {
        this.unknown = unknown;
        return this;
    }
}

package joe.tags.modal.bean.response;

import java.util.List;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 人群详情积分直方图模型
 * Date Date : 2018年09月19日 19:41
 */
public class CrowdIntegral {
    private List<IntegralCount> distributions;

    public List<IntegralCount> getDistributions() {
        return distributions;
    }

    public CrowdIntegral setDistributions(List<IntegralCount> distributions) {
        this.distributions = distributions;
        return this;
    }
}

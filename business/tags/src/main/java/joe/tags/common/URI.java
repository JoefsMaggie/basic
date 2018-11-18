package joe.tags.common;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: URI
 * Date Date : 2018年09月05日 11:22
 */
public interface URI {
    interface V1 {
        String CROWD_DETAIL_ALL = "/v1/dcrmTags/crowdDetail/all";
        String CROWD_DETAIL_BASIC = "/v1/dcrmTags/crowdDetail/basic";
        String CROWD_DETAIL_FUNNEL = "/v1/dcrmTags/crowdDetail/funnel";
        String CROWD_DETAIL_GENDER = "/v1/dcrmTags/crowdDetail/gender";
        String CROWD_DETAIL_ORDER = "/v1/dcrmTags/crowdDetail/order";
        String CROWD_DETAIL_CODE = "/v1/dcrmTags/crowdDetail/code";
        String CROWD_DETAIL_JF = "/v1/dcrmTags/crowdDetail/jf";
        String CROWD_DETAIL_COUPON = "/v1/dcrmTags/crowdDetail/coupon";
    }

    interface V2 {
        String CROWD_DETAIL_ALL = "/v1/dcrmTags/crowdAll";
        String CROWD_DETAIL_BASIC = "/v1/dcrmTags/crowdBasic";
        String CROWD_DETAIL_FUNNEL = "/v1/dcrmTags/crowdFunnel";
        String CROWD_DETAIL_GENDER = "/v1/dcrmTags/crowdGender";
        String CROWD_DETAIL_ORDER = "/v1/dcrmTags/crowdOrder";
        String CROWD_DETAIL_CODE = "/v1/dcrmTags/crowdCode";
        String CROWD_DETAIL_JF = "/v1/dcrmTags/crowdJf";
        String CROWD_DETAIL_COUPON = "/v1/dcrmTags/crowdCoupon";

        String PAGER_QUERY = "/v1/dcrmTags/pagerByField";
        String PAGER_QUERY_SPECIAL = "/v1/dcrmTags/pagerByFieldSpecial";

        String GENERAL_QUERY_AGG = "/v1/dcrmTags/general";
    }
}

package joe.tags.common;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.tag.common
 * @note: ES 字段
 * @date Date : 2018年09月05日 16:36
 */
public interface CusTagField {
    String ROW_KEY = "rowkey";
    String ROW_KEY_KEYWORD = "rowkey.keyword";

    /* ************************************************************************
     ********************************** basic *********************************
     ************************************************************************ */
    /**
     * 品牌商账号
     */
    String MEMBER_LOGIN = "basic:memberlogin";
    String MEMBER_LOGIN_KEYWORD = "basic:memberlogin.keyword";
    /**
     * 关注状态
     */
    String GZ_STATUS = "basic:gz_status";
    /**
     * 关注时间 实时
     */
    String NEWEST_GZ_TIME = "basic:newest_gz_time";
    /**
     * 是否会员
     */
    String IS_MEMBER = "basic:is_member";
    /**
     * OPEN_ID
     */
    String OPEN_ID = "basic:openid";
    String OPEN_ID_KEYWORD = "basic:openid.keyword";
    /**
     * ID
     */
    String ID = "basic:id";
    /**
     * 来源渠道
     */
    String SOURCE_LEVEL_1 = "basic:source_level1";
    String SOURCE_LEVEL_2 = "basic:source_level2";
    /**
     * 用户标签
     */
    String USER_LABEL = "basic:user_label";
    /**
     * 身份标识
     */
    String IDENTITY = "basic:identity";
    /**
     * 是否绑定手机
     */
    String IS_BIND_MOBILE = "basic:is_bind_mobile";
    /**
     * 手机号码
     */
    String MOBILE = "basic:mobile";
    String MOBILE_KEYWORD = "basic:mobile.keyword";
    /**
     * 性别
     */
    String GENDER = "basic:sex";
    /**
     * 文档最后更新时间
     */
    String LAST_UPDATE_TIME = "basic:last_update_time";
    String LAST_UPDATE_TIME_KEYWORD = "basic:last_update_time.keyword";
    /**
     * 文档最后更新时间，时间戳
     */
    String LATEST_UPDATE_TIME = "basic:latest_update_time";
    /**
     * 成长值
     */
    String GROWTH_VALUE = "basic:growth_value";

    /* ************************************************************************
     ********************************** asset *********************************
     ************************************************************************ */
    /**
     * 最后领取积分时间
     */
    String JF_LAST_TIME = "asset:jf_last_time";
    String JF_LAST_TIME_KEYWORD = "asset:jf_last_time.keyword";
    /**
     * 现有积分
     */
    String JF_BALANCE = "asset:jf_balance";
    /**
     * 红包零钱余额
     */
    String HB_BALANCE = "asset:hb_balance";
    /**
     * 发放购物券
     */
    String ASSET_VOLUME_STAT = "asset:asset_volume_stat";
    /**
     * 未用购物券数
     */
    String ASSET_VOLUME_UNUSED_NUM = "asset:asset_volume_unused_num";
    /**
     * 已用购物券
     */
    String ASSET_VOLUME_USED_STAT = "asset:asset_volume_used_stat";
    /**
     * 失效购物券数
     */
    String ASSET_VOLUME_EXPIRED_NUM = "asset:asset_volume_expired_num";
    /**
     * 30天内领券数
     */
    String VOLUME_30DAYS_COUNT = "asset:asset_volume_30days_count";
    /**
     * 30天内领了券且使用过
     */
    String VOLUME_30DAYS_HAS_USED = "asset:asset_volume_30days_has_used";
    /**
     * 30天内领了券全部没有使用
     */
    String VOLUME_30DAYS_NO_USE = "asset:asset_volume_30days_no_use";
    /**
     * 前7天是否有领券
     */
    String ASSET_VOLUME_LAST7DAYS_COUNT = "asset:asset_volume_last7day_count";
    /**
     * 前6天是否有领券
     */
    String ASSET_VOLUME_LAST6DAYS_COUNT = "asset:asset_volume_last6day_count";
    /**
     * 前5天是否有领券
     */
    String ASSET_VOLUME_LAST5DAYS_COUNT = "asset:asset_volume_last5day_count";
    /**
     * 前4天是否有领券
     */
    String ASSET_VOLUME_LAST4DAYS_COUNT = "asset:asset_volume_last4day_count";
    /**
     * 前3天是否有领券
     */
    String ASSET_VOLUME_LAST3DAYS_COUNT = "asset:asset_volume_last3day_count";
    /**
     * 前2天是否有领券
     */
    String ASSET_VOLUME_LAST2DAYS_COUNT = "asset:asset_volume_last2day_count";
    /**
     * 前1天是否有领券
     */
    String ASSET_VOLUME_LAST1DAYS_COUNT = "asset:asset_volume_last1day_count";

    /* ************************************************************************
     ********************************** scan **********************************
     ************************************************************************ */
    /**
     * DCRM最后扫码时间
     */
    String LAST_SCAN_TIME_DCRM = "scan:last_scan_time_dcrm";
    String LAST_SCAN_TIME_DCRM_KEYWORD = "scan:last_scan_time_dcrm.keyword";
    /**
     * 扫码类别：防伪码
     */
    String CODE_FWM_STAT = "scan:code_fwm_stat";
    String CODE_FWM_STAT_KEYWORD = "scan:code_fwm_stat.keyword";
    String CODE_FWM_STAT_TIMES = "scan:code_fwm_stat.times";
    /**
     * 扫码类别：导购码
     */
    String CODE_DGM_STAT = "scan:code_dgm_stat";
    String CODE_DGM_STAT_KEYWORD = "scan:code_dgm_stat.keyword";
    String CODE_DGM_STAT_TIMES = "scan:code_dgm_stat.times";
    /**
     * 扫码类别：包裹码
     */
    String CODE_BGM_STAT = "scan:code_bgm_stat";
    String CODE_BGM_STAT_KEYWORD = "scan:code_bgm_stat.keyword";
    String CODE_BGM_STAT_TIMES = "scan:code_bgm_stat.times";
    /**
     * 扫码类别：微商码
     */
    String CODE_WSM_STAT = "scan:code_wsm_stat";
    String CODE_WSM_STAT_KEYWORD = "scan:code_wsm_stat.keyword";
    String CODE_WSM_STAT_TIMES = "scan:code_wsm_stat.times";
    /**
     * 扫码类别：溯源码
     */
    String CODE_SYM_STAT = "scan:code_sym_stat";
    String CODE_SYM_STAT_KEYWORD = "scan:code_sym_stat.keyword";
    String CODE_SYM_STAT_TIMES = "scan:code_sym_stat.times";
    /**
     * 扫码类别：名片码
     */
    String CODE_MPM_STAT = "scan:code_mpm_stat";
    String CODE_MPM_STAT_KEYWORD = "scan:code_mpm_stat.keyword";
    String CODE_MPM_STAT_TIMES = "scan:code_mpm_stat.times";
    /**
     * 扫码类别：Q 微码
     */
    String CODE_QWM_STAT = "scan:code_qwm_stat";
    String CODE_QWM_STAT_KEYWORD = "scan:code_qwm_stat.keyword";
    String CODE_QWM_STAT_TIMES = "scan:code_qwm_stat.times";
    /**
     * 扫码类别：产品码
     */
    String CODE_CPM_STAT = "scan:code_cpm_stat";
    String CODE_CPM_STAT_KEYWORD = "scan:code_cpm_stat.keyword";
    String CODE_CPM_STAT_TIMES = "scan:code_cpm_stat.times";
    /**
     * 扫码类别：微信二维码
     */
    String CODE_WXM_STAT = "scan:code_wxm_stat";
    String CODE_WXM_STAT_KEYWORD = "scan:code_wxm_stat.keyword";
    String CODE_WXM_STAT_TIMES = "scan:code_wxm_stat.times";
    /**
     * 扫码类别：箱码
     */
    String CODE_XM_STAT = "scan:code_xm_stat";
    String CODE_XM_STAT_KEYWORD = "scan:code_xm_stat.keyword";
    String CODE_XM_STAT_TIMES = "scan:code_xm_stat.times";

    /* ************************************************************************
     ********************************* buying *********************************
     ************************************************************************ */
    /**
     * 最后下单时间
     */
    String LAST_ORDER_TIME_DCRM = "buying:last_order_time_dcrm";
    String LAST_ORDER_TIME_DCRM_KEYWORD = "buying:last_order_time_dcrm.keyword";
    /**
     * 首次下单时间
     */
    String BUYING_FIRST_BUYING_TIME = "buying:first_buying_time";
    String BUYING_FIRST_BUYING_TIME_KEYWORD = "buying:first_buying_time.keyword";
    /**
     *
     */
    String BUYING_DAY_FROM_LAST_ORDER = "buying:day_from_last_order";
    /**
     * 微商城参与次数
     */
    String WSC_COUNT = "buying:wsc_count";
    /**
     * 积分商城礼品兑换次数
     */
    String JF_PRODUCT_COUNT = "buying:jf_product_count";
    /**
     * 促销活动参与次数
     */
    String CX_ACTIVITY_COUNT_STAT = "buying:cx_activity_count_stat";
    /**
     * 拼团购活动参与次数
     */
    String PT_COUNT = "buying:pt_count";
    /**
     * 我要送礼活动参与次数
     */
    String SL_COUNT = "buying:sl_count";
    /**
     * 中奖订单参与次数
     */
    String AWARD_ORDER_COUNT = "buying:award_order_count";
}

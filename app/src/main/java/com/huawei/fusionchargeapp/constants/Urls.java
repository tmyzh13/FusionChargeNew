package com.huawei.fusionchargeapp.constants;

/**
 * 接口地址
 * Created by john on 2017/11/7.
 */

public class Urls {


    //        public static final String ROOT="http://114.115.169.46:8088/charger/api/v1/";//菊
//    public static final String ROOT="http://10.40.143.67:8088/charger/api/v1/";//李凯
//    public static final String ROOT="http://10.40.143.130:8088/charger/api/v1/";//胡明明
//    public static final String ROOT="https://10.40.143.130:8088/charger/api/v1/";//菊1
//    public static final String ROOT = "http://114.115.141.57:8080/charger/api/v1/";
    public static final String ROOT = "http://114.115.169.46:8088/charger/api/v1/";  //开发环境
    //    public static final String ROOT = "http://114.115.141.57:8080/charger/api/v1/";      //测试环境
//    public static final String ROOT = "http://114.115.144.154:8088/charger/api/v1/";//新测试环境
//    public static final String ROOT="http://139.159.134.139:8088/charger/api/v1/";//5-7出包
//    public static final String ROOT = "https://10.40.143.11:8088/charger/api/v1/";   //大灰灰
//    public static final String ROOT = "http://10.40.143.49:8088/charger/api/v1/";
//    public static final String ROOT = "http://10.40.143.72:8088/charger/api/v1/";
//    public static final String ROOT = "http://10.40.143.72:8088/charger/api/v1/";
//    public static final String ROOT = "http://10.186.254.65:8088/charger/api/v1/";//张楠
//    public static final String ROOT = "http://mcloud-sit.huawei.com/mcloud/umag/FreeProxyForText/safecampuse_charging/charger/api/v1/";//华为
//    public static final String ROOT="https://fusioncharge.rnd.huawei.com:8088/charger/api/v1/";
//    public static final String ROOT="http://mcloud-sit.huawei.com/mcloud/umag/FreeProxyForText/safecampuse_charging/charger/api/v1/";
     //打包一并修改地址
    public static final String IMAGE_URL="http://114.115.169.46:8088/charger/img/";

   
    
    //登录
    public static final String LOGIN = "user/login";
    //注册
    public static final String REGISTER = "user/register";
    //忘记密码
    public static final String RESET_PWD = "user/restpwd";
    //修改密码
    public static final String MODIFY_PWD = "user/modifypwd";
    //获取验证码
    public static final String GET_CODE = "user/sendHuaweiCaptcha";

    public static final String GET_USER_INFO = "user/getUserInfo";

    public static final String MODIFY_USER_INFO = "user/modifyUserInfo";
    //预约
    public static final String APPOINT_CHARGE = "charging/reserve";
    //首页地图和列表
    public static final String MAP_DATA = "appMap/search";
    //首页标记简介
    public static final String HOME_MAP_INFO = "appMap/infoNew";
    //查询充电桩费率
    public static final String QUERY_FEE = "appMap/queryFee";

    //查询用户是否有未支付的订单
    public static final String GET_USER_NOT_PAY = "appChargeOrder/queryAppChargeOrderNoPay";
    //查询用户充电状态
    public static final String GET_USER_CHARGER_STATUE = "appChargeOrder/queryAppChargeOrderCharging";
    //获取充电详情
    public static final String GET_CHARGE_STATUE = "charging/queryChargingState";
    //获取用户预约记录
    public static final String GET_USER_APPOINTMENT = "charging/getReserveById";
    //获取充电检查状态（（0失败1成功2检测中））
    public static final String GET_CHECK_STATUE = "charging/queryChargingState";
    //获取支付页面详情
    public static final String GET_PAY_DETAIL = "appChargeOrder/queryAppChargeOrderDetail";
    //取消预约
    public static final String CANCEL_APPOINTMENT = "charging/cancelReserve";
    //余额支付
    public static final String BALANCE_PAY = "orderPay/charge";
    //结束充电
    public static final String STOP_CHARGER = "charging/stop";
    //开始充电
    public static final String START_CHARGER = "charging/start";

    //扫码充电
    public static final String SCAN_CHARGE = "charging/scan";
    //发布评论
    public static final String COMMENT = "appChargingPileEvaluate/addAppChargingPileEvaluate";
    public static final String COMMENT_SORT = "appChargingPileEvaluate/queryAppEvaluateTypeByStation";
    public static final String COMMENT_INFO = "appChargingPileEvaluate/queryAppChargingPileEvaluateByStation";
    public static final String COMMENT_INFO_TYPE = "appChargingPileEvaluate/queryAppEvaluateType";

    //充电站信息
    public static final String CHARGE_PILE_DETAIL = "appMap/info";

    //我的订单
    public static final String MY_ORDER = "user/queryAllChargeOrder";
    //校验验证码
    public static final String CHECK_CODE = "user/checkCaptcha";

    //所有预约信息
    public static final String ALL_APPOINTMENT_INFO = "charging/getAllReserveByAppUserId";

    //上传头像
    public static final String MODIFY_IMG = "user/modifyimg";


    //上传用户信息
    public static final String REPORT_LOCATION = "user/uploadUserRegion";

    //用户提意见
    public static final String PUBLISH_SUGGESTION = "appUserRecord/addFeedbackRecord";

    //充值消费记录
    public static final String BALANCE_RECORD = "appUserRecord/getUserBalanceOrderRecord";

    //能够开取发票的消费记录
    public static final String INVOICE_RECHARGE_LIST = "invoice/chargerOrderList";

    //园区查询站
    public static final String GET_ZONE_STATION = "appZoneGis/queryZoneGis/";

    //iAdmin登录
    public static final String IADMIN_LOGIN = "user/iadminLogin";

    //华为用户验证码
    public static final String HUAWEI_CAPTCHA = "user/sendHuaweiCaptcha";

    //华为验证码注册
    public static final String HUAWE_REGISTER = "user/register ";

    //华为获取手机号码
    public static final String HUAWEI_GET_INFO = "/ProxyForText/idataws/PersonServlet";


    //app上可用套餐
    public static final String APP_TAO_CAN = "appBusinessPackage/queryUsefulBusinessPackage";
    //购买套餐
    public static final String APP_TAO_CAN_PAY = "orderPay/buyPackage";

    //用户注销
    public static final String USER_LOGOUT = "user/userLogOut";
    //我的套餐
    public static final String MY_TAO_CAN = "appBusinessPackage/queryMyBusinessPackage/";
    //申请开票
    public static final String APPLY_INVOICE = "orderPay/saveInvoiceOrderAndPay";

    //获取用户历史开票记录
    public static final String INVOICE_HISTORY = "invoice/getInvoinceList";

    //每个历史开票详情
    public static String INVOICE_HISTORY_ITEM = "invoice/getInvoiceDetail/";

    //开票记录中相关的消费记录
    public static final String INVOICE_HISTORY_ITEM_CONSUME = "invoice/getInvoiceBoundOrder/";
    //获取我的账户余额
    public static final String GET_MY_BALANCE = "balance";
    //获取用户积分
    public static final String GET_USER_SCORE = "user/getUserScore";
    public static final String UN_PAY_INVOICE = "invoice/unPayInvoice/";

    public static final String REPAY_INVOICE = "orderPay/rePayInvoiceOrder";
    //充值余额
    public static final String CHARGERING_BALANCE="orderPay/recharge";
}

package com.huawei.fusionchargeapp.constants;

/**
 * 一些用于记录的关键字
 * Created by john on 2017/11/7.
 */

public class Constant {


    //默认筛选距离 这里测试设置500
    public static final double DEFAULT_DISTANCE=300;
//    condition.x1=73;
//    condition.x2=135;
//    condition.y1=10;
//    condition.y2=50;
    public static final double X1=73;
    public static final double X2=135;
    public static final double Y1=10;
    public static final double Y2=50;
    //记住密码
    public static final String REMEMBER="remember";

    //是否登录过了
    public static final String LOGIN_STATUE="login_staue";

    //充电时间
    public static final String CHARGING_TIME="charge_time";
    //充电总长
    public static final String CHARGING_TOTAL="charge_total";
    //我的位置
    public static final String MY_LOCATION="my_location";
    //导航刷新自己的位子
    public static final String REFRESH_LOCATION="refresh_location";
    //预约计时
    public static final String TIME_APPOINTMENT="time_appointment";
    //预约时长
    public static final String APPOINTMENT_DURING="appointment_during";
    //首页刷新
    public static final String HOME_STATUE_REFRESH="HOME_STATUE_REFRESH";
    //预约超时
    public static final String APPOINTMENT_TIME_OUT="appointment_time_out";
    //刷新预约时间
    public static final String REFRESH_APPOINTMENT_TIME="refresh_appointment_time";
    //刷新地图和列表数据
    public static final String REFRESH_MAP_OR_LIST_DATA="refresh_map_or_list_data";
    //刷新首页的状态
    public static final String REFRESH_HOME_STATUE="refresh_home_statue";
    //注销账号取消预约中的信息
    public static final String LOGIN_OUT_SET_APPOINT_VIEW_GONE = "login_out_set_appoint_view_gone";

    //更改头像后刷新头像信息
    public static final String REFRESH_MAIN_HEAD_PHOTO = "refresh_main_head_photo";
    public static final String REFRESH_APPLY_ORDER_LIST_ACTIVITY = "refresh_apply_order_list_activity";
    public static final String PAY_SUCCESS_FINISH="pay_success_finish";

    public static final String START_OR_STOP_LOCATION="start_or_stop_location";
}

package com.huawei.fusionchargeapp.utils;

import android.content.Context;

import com.corelibs.utils.ToastMgr;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.HomeOrderBean;
import com.huawei.fusionchargeapp.views.LoginActivity;

/**
 * 公共逻辑判断
 * Created by issuser on 2018/4/27.
 */

public class ActionControl {

    private static ActionControl instant;
    //有未支付订单
    private boolean hasNoPayOrder;
    private HomeOrderBean homeOrderBean;
    //有预约
    private boolean hasAppointment;
    private HomeAppointmentBean homeAppointmentBean;
    //有充电中订单
    private boolean hasCharging;
    private HomeChargeOrderBean homeChargeOrderBean;

    private Context context;

    private ActionControl(Context context) {
        this.context = context;
    }

    public static ActionControl getInstance(Context context) {
        if (instant == null) {
            instant = new ActionControl(context);
        }

        return instant;
    }

    /**
     * 在公共逻辑判断后可操作
     *
     * @return
     */
    public boolean canAction() {
        if (UserHelper.getSavedUser() == null) {
            //未登录
            context.startActivity(LoginActivity.getLauncher(context));
            return false;
        }

        if (hasNoPayOrder) {
            //未支付订单
            ToastMgr.show(context.getString(R.string.has_no_pay_order));
//            context.startActivity(PayActivity.getLauncher(context,homeOrderBean.orderRecordNum));
            return false;
        }

        if (hasAppointment) {
            ToastMgr.show(context.getString(R.string.has_appointment));
            return false;
        }

        if (hasCharging) {
            ToastMgr.show(context.getString(R.string.has_charging_order));
            return false;
        }

        return true;
    }

    public void setHasNoPayOrder(boolean b, HomeOrderBean bean) {
        hasNoPayOrder = b;
        homeOrderBean = bean;
    }

    public void setHasAppointment(boolean b, HomeAppointmentBean bean) {
        hasAppointment = b;
        homeAppointmentBean = bean;
    }

    public void setHasCharging(boolean b, HomeChargeOrderBean bean) {
        hasCharging = b;
        homeChargeOrderBean = bean;
    }
}

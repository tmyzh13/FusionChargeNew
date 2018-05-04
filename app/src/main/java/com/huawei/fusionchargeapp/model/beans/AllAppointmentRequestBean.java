package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by admin on 2018/5/4.
 */

public class AllAppointmentRequestBean {
    public int page;
    public int rp;
    public AppointmentCondition condition = new AppointmentCondition();
    public long getAppUserId() {
        return condition.appUserId;
    }

    public void setAppUserId(long appUserId) {
        condition.appUserId = appUserId;
    }

    class AppointmentCondition{
        public long appUserId;
    }
}

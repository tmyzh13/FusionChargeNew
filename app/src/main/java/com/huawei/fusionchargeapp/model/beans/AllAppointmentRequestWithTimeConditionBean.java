package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by admin on 2018/5/4.
 */

public class AllAppointmentRequestWithTimeConditionBean {
    public int page;
    public int rp;
    public AllAppointmentRequestWithTimeConditionBean.AppointmentTimeCondition condition = new AllAppointmentRequestWithTimeConditionBean.AppointmentTimeCondition();

    public long getAppUserId() {
        return condition.appUserId;
    }

    public void setAppUserId(long appUserId) {
        condition.appUserId = appUserId;
    }

    public String getStartTime() {
        return condition.startTime;
    }

    public void setStartTime(String startTime) {
        condition.startTime = startTime;
    }

    public String getEndTime() {
        return condition.endTime;
    }

    public void setEndTime(String endTime) {
        condition.endTime = endTime;
    }

    class AppointmentTimeCondition{
        public long appUserId;
        public String startTime;
        public String endTime;
    }
}

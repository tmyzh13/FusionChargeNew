package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/18.
 */

public class RequestLogoutBean {

    private String phone;

    public String getAppUserId() {
        return phone;
    }

    public void setAppUserId(String appUserId) {
        this.phone = appUserId;
    }

    @Override
    public String toString() {
        return "RequestLogoutBean{" +
                "appUserId='" + phone + '\'' +
                '}';
    }
}

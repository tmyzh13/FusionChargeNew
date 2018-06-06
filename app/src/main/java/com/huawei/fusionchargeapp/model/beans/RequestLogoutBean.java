package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/18.
 */

public class RequestLogoutBean {

    private String appUserId;

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    @Override
    public String toString() {
        return "RequestLogoutBean{" +
                "appUserId='" + appUserId + '\'' +
                '}';
    }
}

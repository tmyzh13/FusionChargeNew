package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/14.
 */

public class RequestIadminLoginBean {

    private String jobNumber;

    private String phone;

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "RequestIadminLoginBean{" +
                "jobNumber='" + jobNumber + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

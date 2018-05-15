package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/15.
 */

public class RequestIAdminRegister {

    private String phone;

    private String jobNumber;

    private String captcha; //验证码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "RequestIAdminRegister{" +
                "phone='" + phone + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", captcha='" + captcha + '\'' +
                '}';
    }
}

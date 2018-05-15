package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/15.
 */

public class RequestIAdminVerCode {

    private String type;

    private String phone;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "RequestIAdminVerCode{" +
                "type='" + type + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

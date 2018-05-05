package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/5.
 */

public class UploadImageBean {

    private String imgurl;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "UploadImageBean{" +
                "imgurl='" + imgurl + '\'' +
                '}';
    }
}

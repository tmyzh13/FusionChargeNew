package com.huawei.fusionchargeapp.model.beans;

import java.io.Serializable;

/**
 * Created by issuser on 2018/4/23.
 *
 * modify by  zhangwei  on 2018/5/3  更改数据结构
 */

public class CommentsBean implements Serializable{

    public String userName;
    public String createTime;
    public String evaluateContent;
    public String photoUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    @Override
    public String toString() {
        return "CommentsBean{" +
                "userName='" + userName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", evaluateContent='" + evaluateContent + '\'' +
                ", photoUrl" +photoUrl+
                '}';
    }
}

package com.huawei.fusionchargeapp.model.beans;

import java.util.List;

/**
 * Created by issuser on 2018/5/18.
 */

public class RequestApplyInvoiceBean {
//    "orderRecordNums":["1525660529389004","1525592208401011"],  //批量选择的准备开票的充电订单
//            "appUserId":"16703",  //APP用户ID
//            "type": "企业发票",   //抬头类型
//            "title": "抬头",     //发票抬头
//            "code":"7611500",    //税号
//            "content": "17366206080",  //发票内容
//            "amount": "123",    //发票金额
//            "kpRemark": "17366206080", //更多信息背注
//            "name": "123",   //收件人名称
//            "phone":"7611500", //联系电话
//            "recAddr": "龙眠大道", //详细地址
//            "email": "123@qq.com"  //电子邮箱
    public List<String> orderRecordNums;
    public long appUserId;
    public String type;
    public String title;
    public String code;
    public String content;
    public double amount;
    public String kpRemark;
    public String name;
    public String phone;
    public String recAddr;
    public String email;
}

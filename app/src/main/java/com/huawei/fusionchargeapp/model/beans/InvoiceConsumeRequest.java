package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/5/18.
 */

public class InvoiceConsumeRequest {
    public int page,rp;
    public Condition condition = new Condition();

    public void setCondition(int id){
        condition.id = id;
    }

    public class Condition{
        public int id;
    }
}

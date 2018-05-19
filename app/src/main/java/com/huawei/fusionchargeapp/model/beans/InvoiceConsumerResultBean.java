package com.huawei.fusionchargeapp.model.beans;

import com.corelibs.subscriber.ResponseHandler;

import java.util.List;

/**
 * Created by issuser on 2018/5/19.
 */

public class InvoiceConsumerResultBean implements ResponseHandler.IBaseData {
    public int code,page,rp,total;
    public List<InvoiceConsumeBean> rawRecords;

    @Override
    public boolean isSuccess() {
        return code == 0;
    }

    @Override
    public int status() {
        return code;
    }

    @Override
    public Object data() {
        return rawRecords;
    }

    @Override
    public String msg() {
        return null;
    }
}

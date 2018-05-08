package com.huawei.fusionchargeapp.model.beans;

import com.corelibs.subscriber.ResponseHandler;

import java.util.List;

/**
 * Created by admin on 2018/5/8.
 */

public class BalanceResultBean implements ResponseHandler.IBaseData {
    public int code,page,rp,total;
    public List<RechargeAndConsumeBean> rawRecords;
    @Override
    public boolean isSuccess() {
        return (code == 0);
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

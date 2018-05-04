package com.huawei.fusionchargeapp.model.beans;

import com.corelibs.subscriber.ResponseHandler;

import java.util.List;

/**
 * Created by admin on 2018/5/4.
 */

public class MyAllAppointment implements ResponseHandler.IBaseData{
    public int code;
    public int page;
    public List<AllAppointmentResultBean> rawRecords;
    public int rp;
    public int total;

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

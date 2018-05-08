package com.huawei.fusionchargeapp.model.beans;

import com.corelibs.subscriber.ResponseHandler;

/**
 * Created by admin on 2018/5/8.
 */

public class ResultOnlyWithCodeBean implements ResponseHandler.IBaseData {
    public int code;
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
        return null;
    }

    @Override
    public String msg() {
        return null;
    }
}

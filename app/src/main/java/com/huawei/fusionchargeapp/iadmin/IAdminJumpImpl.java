package com.huawei.fusionchargeapp.iadmin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.hae.mcloud.rt.mbus.access.BaseServiceProvider;

/**
 * Created by zhangwei on 2018/5/8.
 */

public class IAdminJumpImpl extends BaseServiceProvider implements IAdminJump {

    private Context mContext;

    public IAdminJumpImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public String toFusionCharge(String name) {
        Log.e("zw","toFusionCharge : start   " + name);

        try{
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);

        } catch (Exception e) {
            Log.e("zw", "toFusionCharge error : " + e.getMessage());
        }
        Log.e("zw","toFusionCharge : end   " + name);
        return "success";
    }
}

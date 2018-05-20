package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;

/**
 * Created by issuser on 2018/4/25.
 */

public interface GuaildView extends BaseView{
    void cancelAppointmentSuccess();
    void getZoneInfo(long id,int isGisOpen);
    void noZoneInfo();
}

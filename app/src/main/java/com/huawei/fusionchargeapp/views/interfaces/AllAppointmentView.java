package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentResultBean;

import java.util.List;

/**
 * Created by admin on 2018/5/4.
 */

public interface AllAppointmentView extends BaseView {
    void getAppointmentInfo(List<AllAppointmentResultBean> bean);
    void getInfoFail(boolean state);//true 有时间限制查询， false 无限制条件查询
}

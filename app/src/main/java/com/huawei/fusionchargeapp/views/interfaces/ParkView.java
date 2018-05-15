package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.ZoneStationBean;

import java.util.List;

/**
 * Created by john on 2018/5/15.
 */

public interface ParkView extends BaseView{

    void renderDatas(List<ZoneStationBean> list);
    void getMarkInfo(long id,MapInfoBean bean);
    void showPileFeeInfo(PileFeeBean bean);
}

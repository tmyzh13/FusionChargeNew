package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;

import java.util.List;

/**
 * Created by issuser on 2018/4/23.
 */

public interface HomeListView extends BaseView {

    void rendData(List<MapDataBean> list);
}

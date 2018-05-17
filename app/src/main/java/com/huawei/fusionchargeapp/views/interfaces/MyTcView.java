package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;

import java.util.List;

/**
 * Created by issuser on 2018/5/17.
 */

public interface MyTcView extends BaseView {

    void renderTaocanDatas(List<TaocanBean> list);
}

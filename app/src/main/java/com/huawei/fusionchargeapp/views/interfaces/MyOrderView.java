package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BasePaginationView;
import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.RawRecordBean;

import java.util.List;

/**
 * Created by zhangwei on 2018/5/2.
 */

public interface MyOrderView extends BasePaginationView {

    public void onLoadingSuccess(List<RawRecordBean> newBeans);

    public void onLoadingFail(String msg);

    public void onRefreshSuccess(List<RawRecordBean> newBeans);

    public void onRefreshFail(String msg);

    public void noData();
}

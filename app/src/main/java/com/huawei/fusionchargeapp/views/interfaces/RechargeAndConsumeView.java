package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BasePaginationView;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeBean;

import java.util.List;

/**
 * Created by admin on 2018/5/8.
 */

public interface RechargeAndConsumeView extends BasePaginationView {
    void getBalanceDetailFail();
    void getBalanceDetail(List<RechargeAndConsumeBean> list);
}

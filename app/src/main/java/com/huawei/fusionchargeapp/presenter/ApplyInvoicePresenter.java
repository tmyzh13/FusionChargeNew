package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ApplyInvoiceApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestApplyInvoiceBean;
import com.huawei.fusionchargeapp.views.interfaces.ApplyInvoiceView;
import com.huawei.hae.mcloud.bundle.base.login.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by issuser on 2018/5/18.
 */

public class ApplyInvoicePresenter  extends BasePresenter<ApplyInvoiceView> {

    private ApplyInvoiceApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(ApplyInvoiceApi.class);
    }

    @Override
    public void onStart() {

    }

    public void applyInvoice(ArrayList<String> orderNums, String type, String title, String code, String content, double amount,
                             String kpRemark, String name, String phone, String recAddr, String email){
        RequestApplyInvoiceBean bean =new RequestApplyInvoiceBean();
        bean.orderRecordNums=orderNums;
        bean.appUserId= UserHelper.getSavedUser().appUserId;
        bean.type=type;
        bean.title=title;
        bean.code=code;
        bean.content=content;
        bean.amount=amount;
        bean.kpRemark=kpRemark;
        bean.name=name;
        bean.phone=phone;
        bean.recAddr=recAddr;
        bean.email=email;
        view.showLoading();
        api.applyInvoice(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer(this.<BaseData>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        view.applySuccess();
                    }
                });
    }
}

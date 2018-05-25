package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ApplyInvoiceApi;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceResultBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RepayInvoiceBean;
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

    public void applyInvoice(List<String> orderNums, int payType,double postage, String type, String title, String code, String content, double amount,
                             String kpRemark, String name, String phone, String recAddr, String email){
        RequestApplyInvoiceBean bean =new RequestApplyInvoiceBean();
        bean.orderRecordNums=orderNums;
        bean.chargeOrderTotalFee = amount;
        bean.payType = payType;
        bean.postage = postage;
//        bean.invoiceInfo.appUserId= UserHelper.getSavedUser().appUserId;
        bean.invoiceInfo.type=type;
        bean.invoiceInfo.title=title;
        bean.invoiceInfo.code=code;
        bean.invoiceInfo.content=content;
        bean.invoiceInfo.amount=amount;
        bean.invoiceInfo.kpRemark=kpRemark;
        bean.invoiceInfo.name=name;
        bean.invoiceInfo.phone=phone;
        bean.invoiceInfo.recAddr=recAddr;
        bean.invoiceInfo.email=email;
        view.showLoading();
        api.applyInvoice(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer(this.<BaseData<ApplyInvoiceResultBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<ApplyInvoiceResultBean>>(view) {
                    @Override
                    public void success(BaseData<ApplyInvoiceResultBean> baseData) {
                        view.applySuccess(baseData.data);
                    }
                });
    }

    public void getUnpayInvocie(String orderNums){
        String url = Urls.UN_PAY_INVOICE + orderNums;
        view.showLoading();
        api.getUnpayInvocie(UserHelper.getSavedUser().token,url)
                .compose(new ResponseTransformer(this.<BaseData<RequestApplyInvoiceBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<RequestApplyInvoiceBean>>(view) {
                    @Override
                    public void success(BaseData<RequestApplyInvoiceBean> baseData) {
                        view.getUnpayInvoiceSucess(baseData.data);
                    }
                });
    }

    public void repayInvoice(RepayInvoiceBean bean){
        view.showLoading();
        api.repayInvoice(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer(this.<BaseData<ApplyInvoiceResultBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<ApplyInvoiceResultBean>>(view) {
                    @Override
                    public void success(BaseData<ApplyInvoiceResultBean> baseData) {
                        view.repayInvoiceSucess(baseData.data);
                    }
                });
    }
}

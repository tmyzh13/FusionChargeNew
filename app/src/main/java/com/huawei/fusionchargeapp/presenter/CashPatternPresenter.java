package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.ToastMgr;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.CashPatternApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayBalance;
import com.huawei.fusionchargeapp.views.interfaces.CashPatternView;

/**
 * Created by issuser on 2018/5/30.
 */

public class CashPatternPresenter extends BasePresenter<CashPatternView> {

    private CashPatternApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(CashPatternApi.class);
    }

    @Override
    public void onStart() {

    }

    public void payBalance(double amount,int payType){
        RequestPayBalance bean=new RequestPayBalance();
        bean.rechargeAmount=amount;
        bean.payType=payType;
        view.showLoading();
        api.payBalance(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<PayResultBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<PayResultBean>>(view) {
                    @Override
                    public void success(BaseData<PayResultBean> payResultBeanBaseData) {
                        if(payResultBeanBaseData.data!=null){
                            view.paySuccess(payResultBeanBaseData.data);
                        }else{
                            ToastMgr.show(getString(R.string.pay_error));
                        }
                    }
                });
    }

}

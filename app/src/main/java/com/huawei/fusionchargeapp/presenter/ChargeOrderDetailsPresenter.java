package com.huawei.fusionchargeapp.presenter;

import android.text.TextUtils;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.OrderRequestInfo;
import com.huawei.fusionchargeapp.views.ChagerStatueActivity;
import com.huawei.fusionchargeapp.views.ChargeOrderDetailsActivity;
import com.huawei.fusionchargeapp.views.interfaces.ChargeOrderDetailView;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by zhangwei on 2018/5/2.
 */

public class ChargeOrderDetailsPresenter extends BasePresenter<ChargeOrderDetailView>{

    private ScanApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(ScanApi.class);
    }

    @Override
    public void onStart() {

    }

    public void startCharge(OrderRequestInfo info){
        api.getOrderDetail(UserHelper.getSavedUser().token,info)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {

                        view.onSuccess();

                        /*int code = -1;
                        try {
                            code = Integer.parseInt(baseData.data.toString());
                        } catch (Exception e) {
                            hideLoading();
                            showToast(getString(R.string.server_wrong));
                            return;
                        }

                        if(code == 4) {
                            HomeChargeOrderBean homeChargeOrderBean = new HomeChargeOrderBean();
                            homeChargeOrderBean.virtualId = scanChargeInfo.getVirtualId();
                            homeChargeOrderBean.chargeGunNum = chooseGun.getGunCode();
                            startActivity(ChagerStatueActivity.getLauncher(ChargeOrderDetailsActivity.this,homeChargeOrderBean));
                        } else if( code == 5) {
                            showToast(getString(R.string.request_refuse));
                        } else {
                            showToast(getString(R.string.server_wrong));
                        }*/

                    }
                });
    }
}

package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.MyTaoCanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.views.interfaces.MyTaocanView;

/**
 * Created by issuser on 2018/5/18.
 */

public class MyTaocanPresenter extends BasePresenter<MyTaocanView> {

    private MyTaoCanApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(MyTaoCanApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getMyTaoCan(){
        view.showLoading();
        api.getMyTaocan(UserHelper.getSavedUser().token, Urls.MY_TAO_CAN+UserHelper.getSavedUser().appUserId)
                .compose(new ResponseTransformer(this.<BaseData>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {

                    }
                });
    }
}

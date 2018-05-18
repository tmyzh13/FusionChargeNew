package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.MyAcountApi;
import com.huawei.fusionchargeapp.model.beans.BalanceBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.views.interfaces.MyAcountView;

/**
 * Created by issuser on 2018/5/18.
 */

public class MyAcountPresenter extends BasePresenter<MyAcountView> {

    MyAcountApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(MyAcountApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getMyBalance(){
        view.showLoading();
        api.getMyBalance(UserHelper.getSavedUser().token)
                .compose(new ResponseTransformer<>(this.<BaseData<BalanceBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<BalanceBean>>(view) {
                    @Override
                    public void success(BaseData<BalanceBean> baseData) {
                        if(baseData.data!=null){
                            view.renderBalance(baseData.data);
                        }
                    }
                });
    }
}

package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.MyTcApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;
import com.huawei.fusionchargeapp.views.interfaces.MyTcView;
import com.huawei.hae.mcloud.bundle.base.login.model.User;

import java.util.List;

/**
 * Created by issuser on 2018/5/17.
 */

public class MyTcPresenter extends BasePresenter<MyTcView> {

    MyTcApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(MyTcApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getTaocan(){
        view.showLoading();
        api.getAppTaocan(UserHelper.getSavedUser().token)
                .compose(new ResponseTransformer(this.<BaseData<List<TaocanBean>>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<List<TaocanBean>>>(view) {
                    @Override
                    public void success(BaseData<List<TaocanBean>> baseData) {
                        if(baseData!=null&&baseData.data!=null){
                            view.renderTaocanDatas(baseData.data);
                        }

                    }
                });
    }
}

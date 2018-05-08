package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.SuggestionApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.SuggestionBean;
import com.huawei.fusionchargeapp.views.interfaces.SuggestionView;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by admin on 2018/5/8.
 */

public class SuggestionPresenter extends BasePresenter<SuggestionView> {
    SuggestionApi api;

    @Override
    public void onStart() {
    }

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(SuggestionApi.class);
    }

    public void commit(SuggestionBean bean){
        api.commit(UserHelper.getSavedUser().token, bean)
        .compose(new ResponseTransformer<>(this.<BaseData<Void>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<Void>>(view) {
                    @Override
                    public void success(BaseData<Void> baseData) {
                        view.CommitSucess();
                    }
                });
    }

}

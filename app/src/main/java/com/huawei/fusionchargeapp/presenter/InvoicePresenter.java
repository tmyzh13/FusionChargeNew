package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.InvoiceApi;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequestBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeResultBean;
import com.huawei.fusionchargeapp.views.interfaces.InvoiceView;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by admin on 2018/5/15.
 */

public class InvoicePresenter extends BasePresenter<InvoiceView> {

    private static final int PAGE_NUM = 10;

    InvoiceApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(InvoiceApi.class);
    }

    @Override
    public void onStart() {
    }

    public void getInvoiceConsume(int page){
        InvoiceConsumeRequestBean bean = new InvoiceConsumeRequestBean();
        bean.page = page;
        bean.rp = PAGE_NUM;
        bean.setCondition(UserHelper.getSavedUser().appUserId);
//        bean.setCondition(16703L);//调试使用

        api.getInvoiceConsume(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<InvoiceConsumeResultBean>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<InvoiceConsumeResultBean>(view) {
                    @Override
                    public void success(InvoiceConsumeResultBean baseData) {
                        if (baseData.total < PAGE_NUM) {
                            view.onAllPageLoaded();
                        }
                        view.getInvoiceConsume(baseData.rawRecords);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getInvoiceConsumeFailed();
                    }
                });
    }
}

package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.InvoiceApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequest;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequestBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryItemBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryResultBean;
import com.huawei.fusionchargeapp.views.interfaces.InvoiceHistoryView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

/**
 * Created by issuser on 2018/5/18.
 */

public class InvoiceHistoryPresenter extends BasePresenter<InvoiceHistoryView> {
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

        api.getInvoiceHistory(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<InvoiceHistoryResultBean>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<InvoiceHistoryResultBean>(view) {
                    @Override
                    public void success(InvoiceHistoryResultBean baseData) {
                        if (baseData.total < PAGE_NUM) {
                            view.onAllPageLoaded();
                        }
                        view.getInvoiceHistory(baseData.rawRecords);
                    }
                    @Override
                    public void onError(Throwable e) {
                        view.getInvoiceConsumeFailed();
                    }
                });
    }


    public void getInvoiceHistoryItem(int id){
        String url = Urls.INVOICE_HISTORY_ITEM + id;
        api.getInvoiceHistoryItem(UserHelper.getSavedUser().token,url)
                .compose(new ResponseTransformer<>(this.<BaseData<InvoiceHistoryItemBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<InvoiceHistoryItemBean>>(view) {
                    @Override
                    public void success(BaseData<InvoiceHistoryItemBean> baseData) {
                        view.getInvoiceHistoryItem((InvoiceHistoryItemBean)baseData.data());
                    }
                });
    }

    public void getInvoiceHistoryConsume(int id,int page){
        InvoiceConsumeRequest bean = new InvoiceConsumeRequest();
        bean.page = page;
        bean.rp = PAGE_NUM;
        bean.setCondition(id);
        api.getInvoiceHistoryConsume(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<List<InvoiceConsumeBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<InvoiceConsumeBean>>>(view) {
                    @Override
                    public void success(BaseData<List<InvoiceConsumeBean>> baseData) {
                        view.getInvoiceHistoryConsume((List<InvoiceConsumeBean>)baseData.data());
                    }
                });
    }
}

package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.pagination.presenter.PagePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.AllAppointmentApi;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentRequestBean;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentRequestWithTimeConditionBean;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentResultBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyAllAppointment;
import com.huawei.fusionchargeapp.views.interfaces.AllAppointmentView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

import rx.Subscription;

/**
 * Created by admin on 2018/5/4.
 */

public class AllAppointmentPresenter extends PagePresenter<AllAppointmentView> {

    AllAppointmentApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(AllAppointmentApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getAppointmentInfoWithTimeCondition(int page,int rp,String startTime,String endTme){
        AllAppointmentRequestWithTimeConditionBean bean = new AllAppointmentRequestWithTimeConditionBean();
        bean.page = page;
        bean.rp=rp;
        bean.setAppUserId(UserHelper.getSavedUser().appUserId);
        bean.setStartTime(startTime);
        bean.setEndTime(endTme);
        api.getAppointmentInfoWithTimeCondition(UserHelper.getSavedUser().token, bean)
                .compose(new ResponseTransformer<>(this.<MyAllAppointment>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<MyAllAppointment>(view) {
                    @Override
                    public void success(MyAllAppointment baseData) {
                        Log.e("liutao",""+baseData.data());
                        view.getAppointmentInfo(baseData.rawRecords);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("liutao","error---");
                        view.getInfoFail(true);
                        super.onError(e);
                    }
                });
    }

    public void getAppointmentInfo(int page,int rp){
        AllAppointmentRequestBean bean = new AllAppointmentRequestBean();
        bean.page = page;
        bean.rp=rp;
        bean.setAppUserId(UserHelper.getSavedUser().appUserId);
        api.getAppointmentInfo(UserHelper.getSavedUser().token, bean)
                .compose(new ResponseTransformer<>(this.<MyAllAppointment>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<MyAllAppointment>(view) {
                    @Override
                    public void success(MyAllAppointment baseData) {
                        view.getAppointmentInfo(baseData.rawRecords);
                    }
                    @Override
                    public void onError(Throwable e) {
                        view.getInfoFail(false);
                        super.onError(e);
                    }
                });
    }


}

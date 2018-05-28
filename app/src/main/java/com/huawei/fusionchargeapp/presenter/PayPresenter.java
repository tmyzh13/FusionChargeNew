package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.PayApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyTaocanBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayDetailBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayTCBean;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;
import com.huawei.fusionchargeapp.views.interfaces.PayView;

import java.util.List;

/**
 * Created by issuser on 2018/4/25.
 */

public class PayPresenter extends BasePresenter<PayView> {

    private PayApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(PayApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getPayDetailInfo(String orderNum){
        RequestPayDetailBean bean=new RequestPayDetailBean();
        bean.orderRecordNum=orderNum;
        view.showLoading();
        api.getPayDetail(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<PayInfoBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<PayInfoBean>>(view) {
                    @Override
                    public void success(BaseData<PayInfoBean> baseData) {
                        view.renderData(baseData.data);
                    }
                });
    }

    public void payAction(String orderNum,double total,int payType){
        RequestPayBean bean=new RequestPayBean();
        bean.orderRecordNum=orderNum;
        bean.payType=payType;
        bean.totalFee=total;
        view.showLoading();
        api.balancePay(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<PayResultBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<PayResultBean>>(view) {
                    @Override
                    public void success(BaseData<PayResultBean> baseData) {
                        view.paySuccess(baseData.data);
                    }

                    @Override
                    public boolean operationError(BaseData baseData, int status, String message) {
                        if(status==214){
                            //余额不足
                            view.payBalanceNotEnough();
                        }else{
                            view.payFail();
                        }
                        return super.operationError(baseData,status,message);
                    }

                });
    }

    public void payAction(String orderNum,double total,int payType,long id){
        RequestPayTCBean bean=new RequestPayTCBean();
        bean.orderRecordNum=orderNum;
        bean.payType=payType;
        bean.totalFee=total;

        bean.packageId = id;
        view.showLoading();
        api.balancePay(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<PayResultBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<PayResultBean>>(view) {
                    @Override
                    public void success(BaseData<PayResultBean> baseData) {
                        view.paySuccess(baseData.data);
                    }

                    @Override
                    public boolean operationError(BaseData baseData, int status, String message) {
                        if(status==214){
                            //余额不足
                            view.payBalanceNotEnough();
                        }else{
                            view.payFail();
                        }
                        return super.operationError(baseData,status,message);
                    }

                });
    }

    public void getMyTaoCan(){
        view.showLoading();
        api.getMyTaocan(UserHelper.getSavedUser().token, Urls.MY_TAO_CAN+UserHelper.getSavedUser().appUserId)
                .compose(new ResponseTransformer(this.<BaseData<MyTaocanBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<MyTaocanBean>>(view) {
                    @Override
                    public void success(BaseData<MyTaocanBean> baseData) {
                        view.renderMyTaoCan(baseData.data);
                    }
                });
    }
}

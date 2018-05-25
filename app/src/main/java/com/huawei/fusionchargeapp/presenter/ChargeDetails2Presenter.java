package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.GunList;
import com.huawei.fusionchargeapp.model.beans.PileList;
import com.huawei.fusionchargeapp.model.beans.RequestChargePileDetailBean;
import com.huawei.fusionchargeapp.model.beans.RequestChargeQueryFeeBean;
import com.huawei.fusionchargeapp.views.ChargeDetails2Activity;
import com.huawei.fusionchargeapp.views.interfaces.ChargeDetails2View;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwei on 2018/5/24.
 */

public class ChargeDetails2Presenter extends BasePresenter<ChargeDetails2View>{

    private ScanApi api;
    private int feeIndex = 0;
    private List<ChargeDetailFeeBean> feeList = new ArrayList<>();

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(ScanApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getChargeStationDetail(String id, String type){
        RequestChargePileDetailBean bean = new RequestChargePileDetailBean();

        bean.setId(id);
        bean.setType(type);

        if(bean.getType().equals(ChargeDetails2Activity.STATION)){
            api.getChargeStationDetail(UserHelper.getSavedUser().token,bean)
                    .compose(new ResponseTransformer<>(this.<BaseData<ChargeStationDetailBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                    .subscribe(new ResponseSubscriber<BaseData<ChargeStationDetailBean>>(view) {
                        @Override
                        public void success(BaseData<ChargeStationDetailBean> baseData) {
                            Log.e("zw"," success1 : " + baseData.data.toString());
                            ChargeStationDetailBean chargeStationDetailBean = baseData.data;
                            if(chargeStationDetailBean.getPileList() != null ) {
                                getFeeData(chargeStationDetailBean);
                            } else {
                                view.onGetChargeStationDetailSuccess(chargeStationDetailBean,feeList);
                            }
                        }

                        @Override
                        public boolean operationError(BaseData<ChargeStationDetailBean> baseData, int status, String message) {
                            Log.e("zw"," error : " + baseData.toString());
                            view.onGetChargeStationDetailFail(getString(R.string.no_data));
                            return super.operationError(baseData, status, message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.onGetChargeStationDetailFail(getString(R.string.wrong_request));
                        }
                    });
        } else if(bean.getType() .equals( ChargeDetails2Activity.PILE)) {
            api.getChargePileDetail(UserHelper.getSavedUser().token,bean)
                    .compose(new ResponseTransformer<>(this.<BaseData<PileList>>bindUntilEvent(ActivityEvent.DESTROY)))
                    .subscribe(new ResponseSubscriber<BaseData<PileList>>(view) {
                        @Override
                        public void success(BaseData<PileList> baseData) {
                            PileList pileList = baseData.data;
                            ChargeStationDetailBean chargeStationDetailBean = new ChargeStationDetailBean();
                            chargeStationDetailBean.setAddress(pileList.getAddress());
                            chargeStationDetailBean.setName(pileList.getName());
                            chargeStationDetailBean.setAverageScore(pileList.getAverageScore());
                            chargeStationDetailBean.setLatitude(Double.parseDouble(pileList.getLatitude()));
                            chargeStationDetailBean.setLongitude(Double.parseDouble(pileList.getLongitude()));
                            chargeStationDetailBean.setPhotoUrl(pileList.getPhotoUrl());
                            chargeStationDetailBean.setId(pileList.getId());
                            chargeStationDetailBean.setType(ChargeDetails2Activity.PILE);
                            List<GunList> gunLists=pileList.getGunList();
                            if(gunLists!=null){
                                for(int i=0;i<gunLists.size();i++){
                                    gunLists.get(i).setChargingPileId(pileList.getId());
                                }
                            }
                            List<PileList> list = new ArrayList<>();
                            list.add(pileList);
                            chargeStationDetailBean.setPileList(list);
                            getFeeData(chargeStationDetailBean);
                        }

                        @Override
                        public boolean operationError(BaseData<PileList> baseData, int status, String message) {
                            Log.e("zw"," error : " + baseData.toString());
                            view.onGetChargeStationDetailFail(getString(R.string.no_data));
                            return super.operationError(baseData, status, message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.onGetChargeStationDetailFail(getString(R.string.wrong_request));
                        }
                    });
        }
    }

    private void getFeeData(final ChargeStationDetailBean chargeStationDetailBean){
        if(feeIndex < chargeStationDetailBean.getPileList().size()) {
            RequestChargeQueryFeeBean bean = new RequestChargeQueryFeeBean();
            bean.setChargePileId(chargeStationDetailBean.getPileList().get(feeIndex).getId() + "");
            api.getQueryFee(UserHelper.getSavedUser().token,bean)
                    .compose(new ResponseTransformer<>(this.<BaseData<ChargeDetailFeeBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                    .subscribe(new ResponseSubscriber<BaseData<ChargeDetailFeeBean>>(view) {
                        @Override
                        public void success(BaseData<ChargeDetailFeeBean> chargeDetailFeeBean) {
                            feeList.add(chargeDetailFeeBean.data);
                            feeIndex++;
                            if(feeIndex < chargeStationDetailBean.getPileList().size()) {
                                getFeeData(chargeStationDetailBean);
                            } else {
                                view.onGetChargeStationDetailSuccess(chargeStationDetailBean,feeList);
                            }
                        }

                        @Override
                        public boolean operationError(BaseData<ChargeDetailFeeBean> chargeDetailFeeBeanBaseData, int status, String message) {
                            feeList.add(new ChargeDetailFeeBean());
                            feeIndex++;
                            if(feeIndex < chargeStationDetailBean.getPileList().size()) {
                                getFeeData(chargeStationDetailBean);
                            } else {
                                view.onGetChargeStationDetailSuccess(chargeStationDetailBean,feeList);
                            }
                            return super.operationError(chargeDetailFeeBeanBaseData, status, message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            feeList.add(new ChargeDetailFeeBean());
                            feeIndex++;
                            if(feeIndex < chargeStationDetailBean.getPileList().size()) {
                                getFeeData(chargeStationDetailBean);
                            } else {
                                view.onGetChargeStationDetailSuccess(chargeStationDetailBean,feeList);
                            }
                        }
                    });
        }
    }
}

package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.ChargePileTypeAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeListBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.MyLocationBean;
import com.huawei.fusionchargeapp.model.beans.PileList;
import com.huawei.fusionchargeapp.model.beans.RequestChargePileDetailBean;
import com.huawei.fusionchargeapp.model.beans.RequestChargeQueryFeeBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.weights.MyViewPager;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.trello.rxlifecycle.ActivityEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ChargeDetailsActivity extends BaseActivity {

    private static final String TAG = ChargeDetailsActivity.class.getSimpleName();
    private Context context = ChargeDetailsActivity.this;

    private final String STATION = "station";
    private final String PILE = "pile";

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.charge_pile_name_tv)
    TextView chargePileNameTv;
    @Bind(R.id.score_tv)
    TextView scoreTv;
    @Bind(R.id.charge_pile_address_tv)
    TextView chargePileAddressTv;
    @Bind(R.id.distance_tv)
    TextView distanceTv;
    @Bind(R.id.picture_tv)
    TextView pictureTv;
    @Bind(R.id.position_tv)
    TextView positionTv;
    @Bind(R.id.comments_tv)
    TextView commentsTv;
    @Bind(R.id.my_view_pager)
    MyViewPager myViewPager;
    ChargePileTypeAdapter mAdapter;
    private ScanApi api;
    private double userLatitude;
    private double userLongitude;
    private String id;
    private String type;

    private ChargeStationDetailBean chargeStationDetailBean;
    private int feeId = 0;
    private List<ChargeDetailFeeBean> feeList = new ArrayList<>();

    public static Intent getLauncher(Context context,String id,String type) {
        Intent intent = new Intent(context, ChargeDetailsActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("type",type);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        api = ApiFactory.getFactory().create(ScanApi.class);

        navBar.setColorRes(R.color.app_blue);
        navBar.setNavTitle(context.getString(R.string.charging_pile_detail));

//        latitude = getIntent().getDoubleExtra("latitude",0);
//        longitude = getIntent().getDoubleExtra("longitude",0);
        MyLocationBean bean = PreferencesHelper.getData(MyLocationBean.class);
        if(bean != null) {
            userLatitude = bean.latitude;
            userLongitude = bean.longtitude;
        }
        id = getIntent().getStringExtra("id");
        type=getIntent().getStringExtra("type");
        getData();
    }

    private void getData(){
        showLoading();
        final RequestChargePileDetailBean bean = new RequestChargePileDetailBean();

        bean.setId(id);
        bean.setType(type);

        if(bean.getType() .equals( STATION)){
             api.getChargeStationDetail(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<ChargeStationDetailBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<ChargeStationDetailBean>>() {
                    @Override
                    public void success(BaseData<ChargeStationDetailBean> baseData) {
                        Log.e("zw",TAG + " success1 : " + baseData.data.toString());
                        chargeStationDetailBean = baseData.data;
                        getFeeData();
                    }

                    @Override
                    public boolean operationError(BaseData<ChargeStationDetailBean> baseData, int status, String message) {
                        Log.e("zw",TAG + " error : " + baseData.toString());
                        hideLoading();
                        showToast(getString(R.string.no_data));
                        finish();
                        return super.operationError(baseData, status, message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideLoading();
                        showToast(getString(R.string.wrong_request));
                        finish();
                    }
                });
        } else if(bean.getType() .equals( PILE)) {
            api.getChargePileDetail(UserHelper.getSavedUser().token,bean)
                    .compose(new ResponseTransformer<>(this.<BaseData<PileList>>bindUntilEvent(ActivityEvent.DESTROY)))
                    .subscribe(new ResponseSubscriber<BaseData<PileList>>() {
                        @Override
                        public void success(BaseData<PileList> baseData) {
                            PileList pileList = baseData.data;
                            chargeStationDetailBean = new ChargeStationDetailBean();
                            chargeStationDetailBean.setAddress(pileList.getAddress());
                            chargeStationDetailBean.setName(pileList.getName());
                            chargeStationDetailBean.setAverageScore(pileList.getAverageScore());
                            chargeStationDetailBean.setLatitude(Double.parseDouble(pileList.getLatitude()));
                            chargeStationDetailBean.setLongitude(Double.parseDouble(pileList.getLongitude()));
                            chargeStationDetailBean.setPhotoUrl(pileList.getPhotoUrl());
                            List<PileList> list = new ArrayList<>();
                            list.add(pileList);
                            chargeStationDetailBean.setPileList(list);
                            getFeeData();
                        }

                        @Override
                        public boolean operationError(BaseData<PileList> baseData, int status, String message) {
                            Log.e("zw",TAG + " error : " + baseData.toString());
                            hideLoading();
                            showToast(getString(R.string.no_data));
                            finish();
                            return super.operationError(baseData, status, message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            hideLoading();
                            showToast(getString(R.string.wrong_request));
                            finish();
                        }
                    });
        }


    }

    private void getFeeData(){
        if(feeId < chargeStationDetailBean.getPileList().size()) {
            RequestChargeQueryFeeBean bean = new RequestChargeQueryFeeBean();
            bean.setChargePileId(chargeStationDetailBean.getPileList().get(feeId).getId() + "");
            api.getQueryFee(UserHelper.getSavedUser().token,bean)
                    .compose(new ResponseTransformer<>(this.<BaseData<ChargeDetailFeeBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                    .subscribe(new ResponseSubscriber<BaseData<ChargeDetailFeeBean>>() {
                        @Override
                        public void success(BaseData<ChargeDetailFeeBean> chargeDetailFeeBean) {
                            feeList.add(chargeDetailFeeBean.data);
                            feeId++;
                            if(feeId < chargeStationDetailBean.getPileList().size()) {
                                getFeeData();
                            } else {
                                initView();
                                hideLoading();
                            }
                        }

                        @Override
                        public boolean operationError(BaseData<ChargeDetailFeeBean> chargeDetailFeeBeanBaseData, int status, String message) {
                            feeList.add(new ChargeDetailFeeBean());
                            feeId++;
                            if(feeId < chargeStationDetailBean.getPileList().size()) {

                                getFeeData();
                            } else {
                                initView();
                                hideLoading();
                            }
                            return super.operationError(chargeDetailFeeBeanBaseData, status, message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            feeList.add(new ChargeDetailFeeBean());
                            feeId++;
                            if(feeId < chargeStationDetailBean.getPileList().size()) {
                                getFeeData();
                            } else {
                                initView();
                                hideLoading();
                            }
                        }
                    });
        }

    }

    private void initView(){
        if(chargeStationDetailBean == null ) {
            showToast(getString(R.string.no_data));
            finish();
        } else {
            chargePileNameTv.setText(chargeStationDetailBean.getName());
            chargePileAddressTv.setText(chargeStationDetailBean.getAddress());
            if ("-1.0".equals(chargeStationDetailBean.getAverageScore())){
                scoreTv.setText("--");
            }else {
                scoreTv.setText(chargeStationDetailBean.getAverageScore() + "");
            }
//        计算距离
            LatLng positionLatlng = new LatLng(userLatitude,userLongitude);
            LatLng userLatlng = new LatLng(chargeStationDetailBean.getLatitude(),chargeStationDetailBean.getLongitude());
            float distance = AMapUtils.calculateLineDistance(positionLatlng,userLatlng);
            if(distance > 1000) {
                DecimalFormat df = new DecimalFormat("#.0");
                String km = df.format(distance/1000);
                distanceTv.setText(km + "KM");
            } else {
                distanceTv.setText(distance + "M");
            }

            mAdapter = new ChargePileTypeAdapter(getSupportFragmentManager(),chargeStationDetailBean,feeList);
            myViewPager.setAdapter(mAdapter);

            chosePicture();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.picture_tv)
    public void choicePicture() {
        chosePicture();
    }

    @OnClick(R.id.position_tv)
    public void choicePosition() {
        chosePosition();
    }

    @OnClick(R.id.comments_tv)
    public void choiceComments() {
        choseComments();
    }

    private void chosePicture() {
        pictureTv.setTextColor(getResources().getColor(R.color.app_blue));
        positionTv.setTextColor(getResources().getColor(R.color.text_main));
        commentsTv.setTextColor(getResources().getColor(R.color.text_main));
        myViewPager.setCurrentItem(0);
    }

    private void chosePosition() {
        positionTv.setTextColor(getResources().getColor(R.color.app_blue));
        pictureTv.setTextColor(getResources().getColor(R.color.text_main));
        commentsTv.setTextColor(getResources().getColor(R.color.text_main));
        myViewPager.setCurrentItem(1);
    }

    private void choseComments() {
        commentsTv.setTextColor(getResources().getColor(R.color.app_blue));
        positionTv.setTextColor(getResources().getColor(R.color.text_main));
        pictureTv.setTextColor(getResources().getColor(R.color.text_main));
        myViewPager.setCurrentItem(2);
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }
}

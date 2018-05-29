package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.corelibs.base.BaseActivity;
import com.corelibs.utils.PreferencesHelper;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.ChargePileTypeAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.MyLocationBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.ChargeDetails2Presenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.ChargeDetails2View;
import com.huawei.fusionchargeapp.weights.MyViewPager;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargeDetails2Activity extends BaseActivity<ChargeDetails2View, ChargeDetails2Presenter> implements ChargeDetails2View {

    public static final String STATION = "station";
    public static final String PILE = "pile";

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.charge_pile_name_tv)
    TextView chargePileNameTv;
    @Bind(R.id.is_open_iv)
    ImageView isOpenIv;
    @Bind(R.id.score_tv)
    TextView scoreTv;
    @Bind(R.id.score_unit)
    TextView scoreUnit;
    @Bind(R.id.charge_pile_address_tv)
    TextView chargePileAddressTv;
    @Bind(R.id.iv_guaild)
    ImageView ivGuaild;
    @Bind(R.id.distance_tv)
    TextView distanceTv;
    @Bind(R.id.ll_guild)
    LinearLayout llGuild;
    @Bind(R.id.picture_tv)
    TextView pictureTv;
    @Bind(R.id.position_tv)
    TextView positionTv;
    @Bind(R.id.comments_tv)
    TextView commentsTv;
    @Bind(R.id.my_view_pager)
    MyViewPager myViewPager;

    private String id;
    private String type;

    private ChargeStationDetailBean detailBean;

    public static Intent getLauncher(Context context, String id, String type) {
        Intent intent = new Intent(context, ChargeDetails2Activity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        } else {
            navBar.setColor(getResources().getColor(R.color.app_blue));
        }


        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        if (type.equals(STATION)) {
            navBar.setNavTitle(getString(R.string.charging_pile_detail));
        } else if (type.equals(PILE)) {
            navBar.setNavTitle(getString(R.string.charging_pile_detail_));
        }

        showLoading();
        presenter.getChargeStationDetail(id, type);
    }

    @Override
    protected ChargeDetails2Presenter createPresenter() {
        return new ChargeDetails2Presenter();
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(this));
    }


    @Override
    public void onGetChargeStationDetailSuccess(ChargeStationDetailBean chargeStationDetailBean,List<ChargeDetailFeeBean> feeList) {
        initView(chargeStationDetailBean,feeList);
        hideLoading();
    }

    @Override
    public void onGetChargeStationDetailFail(String message) {
        hideLoading();
        showToast(message);
        finish();
    }

    private void initView(ChargeStationDetailBean chargeStationDetailBean,List<ChargeDetailFeeBean> feeList){
        this.detailBean = chargeStationDetailBean;
        if(chargeStationDetailBean == null ) {
            showToast(getString(R.string.no_data));
            finish();
        } else {
            chargePileNameTv.setText(chargeStationDetailBean.getName());
            chargePileAddressTv.setText(chargeStationDetailBean.getAddress());
            if ("-1.0".equals(chargeStationDetailBean.getAverageScore())) {
                scoreTv.setText("");
                scoreUnit.setText(R.string.no_score);
            } else {
                scoreTv.setText(chargeStationDetailBean.getAverageScore() + "");
                scoreUnit.setText(R.string.score);
            }
//        计算距离
            MyLocationBean bean = PreferencesHelper.getData(MyLocationBean.class);
            double distance = Tools.GetDistance(bean.latitude, bean.longtitude, chargeStationDetailBean.getLatitude(), chargeStationDetailBean.getLongitude());
            distanceTv.setText(distance + "KM");
            /*if (distance > 1000) {
                DecimalFormat df = new DecimalFormat("#.0");
                String km = df.format(distance / 1000);
                distanceTv.setText(km + "KM");
            } else {
                distanceTv.setText(distance + "M");
            }*/

            ChargePileTypeAdapter mAdapter = new ChargePileTypeAdapter(getSupportFragmentManager(), chargeStationDetailBean, feeList);
            myViewPager.setAdapter(mAdapter);

            chosePosition();
        }
    }

    @OnClick({R.id.picture_tv, R.id.position_tv, R.id.comments_tv,R.id.iv_guaild})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.picture_tv:
                chosePicture();
                break;
            case R.id.position_tv:
                chosePosition();
                break;
            case R.id.comments_tv:
                choseComments();
                break;
            case R.id.iv_guaild:
                startActivity(GuildActivity.getLauncher(this, detailBean.getLatitude(),
                        detailBean.getLongitude(), null, false,
                        detailBean.getId(),detailBean.getType()));
                break;
        }
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
}

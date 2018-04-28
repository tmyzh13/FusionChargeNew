package com.huawei.fusionchargeapp.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;


public class PictureFragment extends BaseFragment {


    private static final String IMG_URL = "http://10.40.143.17:8088/charger/";
    public ChargeStationDetailBean data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String[] imgUrls = data.getPhotoUrl().split(",");

            ImageView iv_bg = getParentView().findViewById(R.id.charge_pile_iv);

        Log.e("zw","url : " + IMG_URL + imgUrls[0]);
        Glide.with(this).load(IMG_URL + imgUrls[0]).placeholder(R.mipmap.home_bg).into(iv_bg);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void goLogin() {

    }
}

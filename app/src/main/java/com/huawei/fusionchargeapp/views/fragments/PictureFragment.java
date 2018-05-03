package com.huawei.fusionchargeapp.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;


/**
 *   problem : 图片无法显示  OPEN
 */

public class PictureFragment extends BaseFragment {

    //TODO  后期需要替换URL
    private static final String IMG_URL = "http://10.40.143.10:8088/charger/";

    public ChargeStationDetailBean data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String[] imgUrls = data.getPhotoUrl().split(",");

        ImageView iv_bg = getParentView().findViewById(R.id.charge_pile_iv);
        ImageView iv_bg2 = getParentView().findViewById(R.id.charge_pile_iv2);
        ImageView iv_bg3 = getParentView().findViewById(R.id.charge_pile_iv3);
        ImageView iv_bg4 = getParentView().findViewById(R.id.charge_pile_iv4);

        Log.e("zw","url : " + IMG_URL + imgUrls[0]);
//        String u = "http://10.40.143.10:8088/charger/pileImg\\e9996613e3034f208e5a7779da37d1a2.jpg";
        for (int i = 0; i < imgUrls.length; i++) {
            String url = IMG_URL + imgUrls[i];
            url = url.replaceAll("\\\\","/");
            Glide.with(this).load(url).placeholder(R.mipmap.home_bg).into(iv_bg);
            if(i == 1) {
                iv_bg2.setVisibility(View.VISIBLE);
            }
            if(i == 2) {
                iv_bg3.setVisibility(View.VISIBLE);
            }
            if(i == 4) {
                iv_bg4.setVisibility(View.VISIBLE);
            }
        }



    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void goLogin() {

    }
}

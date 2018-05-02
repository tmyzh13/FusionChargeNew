package com.huawei.fusionchargeapp.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

            final ImageView iv_bg = getParentView().findViewById(R.id.charge_pile_iv);

        Log.e("zw","url : " + IMG_URL + imgUrls[0]);
        String u = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525253590748&di=bd8d5556edae1cbe73ab2d466c09c560&imgtype=0&src=http%3A%2F%2Fpic35.nipic.com%2F20131121%2F2531170_145358633000_2.jpg";
//        Glide.with(this).load(IMG_URL + imgUrls[0]).placeholder(R.mipmap.home_bg).into(iv_bg);

        SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    Log.e("zw","glide : " +  (resource == null));
                    iv_bg.setImageDrawable(resource);
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                Log.e("zw","glide :onLoadStarted " );
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Log.e("zw","glide : onLoadFailed : " + e.getMessage());
            }
        };

       /* Glide.with(this).load(u).override(320,320).into(target);*/
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

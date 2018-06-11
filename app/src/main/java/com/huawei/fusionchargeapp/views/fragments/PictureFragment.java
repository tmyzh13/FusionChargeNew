package com.huawei.fusionchargeapp.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.utils.Tools;

import butterknife.Bind;


/**
 *   problem : 图片无法显示  OPEN
 */

public class PictureFragment extends BaseFragment {

    @Bind(R.id.charge_pile_iv)
    ImageView iv_bg;
    @Bind(R.id.charge_pile_iv2)
    ImageView iv_bg2;
    @Bind(R.id.charge_pile_iv3)
    ImageView iv_bg3;
    @Bind(R.id.charge_pile_iv4)
    ImageView iv_bg4;
    @Bind(R.id.has_no_picture)
    TextView noPicture;

    //TODO  后期需要替换URL
    private static final String IMG_URL = Urls.IMAGE_URL;

    public ChargeStationDetailBean data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Tools.isNull(data.getPhotoUrl())) {
            showNoPitctureView();
            return;
        }
        String[] imgUrls = data.getPhotoUrl().split(",");


//        Log.e("zw","url : " + IMG_URL + ","+imgUrls[0]+",photoUrl="+data.getPhotoUrl()+"v");
//        String u = "http://10.40.143.10:8088/charger/pileImg\\e9996613e3034f208e5a7779da37d1a2.jpg";
        for (int i = 0; i < imgUrls.length; i++) {
            String url = IMG_URL + imgUrls[i];
            url = url.replaceAll("\\\\","/");
            Log.e("liutao",url);
            if(i==0){
                Glide.with(this).load(url).placeholder(R.mipmap.home_bg).into(iv_bg);
            }
            if(i == 1) {
                iv_bg2.setVisibility(View.VISIBLE);
                Glide.with(this).load(url).placeholder(R.mipmap.home_bg).into(iv_bg2);
            }
            if(i == 2) {
                iv_bg3.setVisibility(View.VISIBLE);
                Glide.with(this).load(url).placeholder(R.mipmap.home_bg).into(iv_bg3);
            }
            if(i == 3) {
                iv_bg4.setVisibility(View.VISIBLE);
                Glide.with(this).load(url).placeholder(R.mipmap.home_bg).into(iv_bg4);
            }
        }



    }
    private void showNoPitctureView(){
        noPicture.setVisibility(View.VISIBLE);
        iv_bg.setVisibility(View.GONE);
        iv_bg2.setVisibility(View.GONE);
        iv_bg3.setVisibility(View.GONE);
        iv_bg4.setVisibility(View.GONE);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void goLogin() {

    }
}

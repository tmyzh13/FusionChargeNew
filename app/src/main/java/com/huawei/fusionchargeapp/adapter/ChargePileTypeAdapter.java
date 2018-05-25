package com.huawei.fusionchargeapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.views.fragments.CommentsFragment;
import com.huawei.fusionchargeapp.views.fragments.PictureFragment;
import com.huawei.fusionchargeapp.views.fragments.Position2Fragment;
import com.huawei.fusionchargeapp.views.fragments.PositionFragment;

import java.util.List;

/**
 * Created by issuser on 2018/4/12.
 */

public class ChargePileTypeAdapter extends FragmentPagerAdapter {

    private PictureFragment pictureFragment;
    private Position2Fragment positionFragment;
    private CommentsFragment commentsFragment;

    private ChargeStationDetailBean data;

    private List<ChargeDetailFeeBean> feeList;

    public ChargePileTypeAdapter(FragmentManager fm, ChargeStationDetailBean bean, List<ChargeDetailFeeBean> feeList) {
        super(fm);
        this.data = bean;
        this.feeList = feeList;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            if (pictureFragment == null) {
                pictureFragment = new PictureFragment();
                pictureFragment.data = data;
            }
            return pictureFragment;
        } else if (position == 1) {
            if (positionFragment == null) {
                positionFragment = new Position2Fragment();
                positionFragment.data = data;
                positionFragment.feeList = feeList;
            }
            return positionFragment;
        } else if (position == 2) {
            if (commentsFragment == null) {
                commentsFragment = new CommentsFragment();
                commentsFragment.data = data;
            }
            return commentsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

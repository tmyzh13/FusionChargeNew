package com.huawei.fusionchargeapp.model.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhangwei on 2018/5/24.
 */

public class ChargeMultipleBean implements MultiItemEntity {

    public static final int CHARGE_DETAIL_HEAD = 0x00001;
    public static final int CHARGE_DETAIL_BODY = 0x00002;

    private PileList mPileList;
    private ChargeDetailFeeBean mFeeBean;
    private GunList mGunList;

    private int mType;

    public ChargeMultipleBean(int type) {
        mType = type;
    }
    @Override
    public int getItemType() {
        return mType;
    }

    public PileList getmPileList() {
        return mPileList;
    }

    public void setmPileList(PileList mPileList) {
        this.mPileList = mPileList;
    }

    public ChargeDetailFeeBean getmFeeBean() {
        return mFeeBean;
    }

    public void setmFeeBean(ChargeDetailFeeBean mFeeBean) {
        this.mFeeBean = mFeeBean;
    }

    public GunList getmGunList() {
        return mGunList;
    }

    public void setmGunList(GunList mGunList) {
        this.mGunList = mGunList;
    }
}

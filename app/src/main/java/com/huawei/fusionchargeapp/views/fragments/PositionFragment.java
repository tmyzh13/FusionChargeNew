package com.huawei.fusionchargeapp.views.fragments;


import android.os.Bundle;
import android.widget.ListView;

import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.ChargePileAdapter;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;

import butterknife.Bind;


public class PositionFragment extends BaseFragment {
    private static final String TAG = PositionFragment.class.getSimpleName();

    public ChargeStationDetailBean data;

    @Bind(R.id.charge_pile_Lv)
    ListView chargePileLv;
    private ChargePileAdapter chargePileAdapter;
//    private List<ChargePileBean.ElectricGunBean> gunList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_position;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        chargePileAdapter = new ChargePileAdapter(getActivity(), data);
        chargePileLv.setAdapter(chargePileAdapter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void goLogin() {

    }
}

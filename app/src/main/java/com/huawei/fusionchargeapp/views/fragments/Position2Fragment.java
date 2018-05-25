package com.huawei.fusionchargeapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.PreferencesHelper;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.ChargePile2Adapter;
import com.huawei.fusionchargeapp.model.beans.AppointTimeOutBean;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeMultipleBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.GunList;
import com.huawei.fusionchargeapp.model.beans.PileList;
import com.huawei.fusionchargeapp.views.AppointmentChargeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangwei on 2018/5/24.
 */

public class Position2Fragment extends BaseFragment {

    public ChargeStationDetailBean data;
    public List<ChargeDetailFeeBean> feeList;

    @Bind(R.id.rv)
    RecyclerView rv;
    private List<ChargeMultipleBean> multipleBeans;

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_position2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Log.e("zw","position2 fragment :" + data.toString() + " ///  " + data.getAddress());
        multipleBeans = new ArrayList<>();

        List<PileList> pileLists = data.getPileList();
        if(data != null) {
            for (int i = 0; i < pileLists.size(); i++) {
                ChargeMultipleBean headBean = new ChargeMultipleBean(ChargeMultipleBean.CHARGE_DETAIL_HEAD);
                headBean.setmPileList(pileLists.get(i));
                headBean.setmFeeBean(feeList.get(i));
                multipleBeans.add(headBean);

                List<GunList> gunLists = pileLists.get(i).getGunList();
                for (int j = 0; j < gunLists.size(); j++) {
                    ChargeMultipleBean bodyBean = new ChargeMultipleBean(ChargeMultipleBean.CHARGE_DETAIL_BODY);
                    bodyBean.setmGunList(gunLists.get(j));
                    multipleBeans.add(bodyBean);
                }
            }
        }

        ChargePile2Adapter adapter = new ChargePile2Adapter(getActivity(), multipleBeans);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.gun_appointment_tv) {
                    GunList gunList = multipleBeans.get(position).getmGunList();
                    PileList pileList = null;

                    for (int i = position; i >= 0; i--) {
                        if(multipleBeans.get(i).getmPileList() != null) {
                            pileList = multipleBeans.get(i).getmPileList();
                            break;
                        }
                    }
                    Intent intent = new Intent(getActivity(),AppointmentChargeActivity.class);
                    intent.putExtra("gunCode",gunList.getGunCode());
                    intent.putExtra("chargingPileId",gunList.getChargingPileId());
                    intent.putExtra("chargingPileName",pileList.getName());
                    intent.putExtra("latitude",data.getLatitude());
                    intent.putExtra("longitude",data.getLongitude());
                    intent.putExtra("address",data.getAddress());
                    intent.putExtra("runCode",pileList.getRunCode());

                    AppointTimeOutBean bean = new AppointTimeOutBean();
                    bean.setGunCode(gunList.getGunCode());
                    bean.setAddress(data.getAddress());
                    bean.setChargingPileName(pileList.getName());
                    bean.setChargingPileId(gunList.getChargingPileId());
                    bean.setLatitude(data.getLatitude());
                    bean.setLongitude(data.getLongitude());
                    bean.setRunCode(pileList.getRunCode());
                    PreferencesHelper.saveData(bean);
                    Log.e("zw","xinban : " + bean.toString());
                    getActivity().startActivity(intent);
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.shape_custom_divider));
        rv.addItemDecoration(decoration);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}

package com.huawei.fusionchargeapp.views.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.corelibs.base.BaseFragment;
import com.corelibs.subscriber.RxBusSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.rxbus.RxBus;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.layout.PtrLollipopLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapters.HomeListAdpter;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.model.beans.MyLocationBean;
import com.huawei.fusionchargeapp.presenter.HomeListPresenter;
import com.huawei.fusionchargeapp.utils.ChoiceManager;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.ChargeDetailsActivity;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.HomeListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by issuser on 2018/4/19.
 *
 */

public class HomeListFragment extends BaseFragment<HomeListView, HomeListPresenter> implements HomeListView, PtrLollipopLayout.RefreshCallback, PtrAutoLoadMoreLayout.RefreshLoadCallback {

    @Bind(R.id.lv_piles)
    AutoLoadMoreListView lv_piles;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout<AutoLoadMoreListView> ptrLayout;

    public HomeListAdpter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_home;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        adapter = new HomeListAdpter(getContext());
        lv_piles.setAdapter(adapter);
        lv_piles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取详情要token 所以判断
                if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
                    startActivity(LoginActivity.getLauncher(getContext()));
                } else {
                    startActivity(ChargeDetailsActivity.getLauncher(getActivity(), list_datas.get(position).id + "", list_datas.get(position).type));
                }
            }
        });
        ptrLayout.disableLoading();
        presenter.setOtherLoading(false);
        presenter.getDatas();
        ptrLayout.setRefreshLoadCallback(this);
        RxBus.getDefault().toObservable(Object.class, Constant.REFRESH_MAP_OR_LIST_DATA)
                .compose(this.<Object>bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {

                    @Override
                    public void receive(Object data) {
                        presenter.getDatas();
                    }
                });
    }

    @Override
    protected HomeListPresenter createPresenter() {
        return new HomeListPresenter();
    }

    @Override
    public void showLoading() {
        ptrLayout.setRefreshing();
    }

    @Override
    public void hideLoading() {
        ptrLayout.complete();
    }

    @Override
    public void goLogin() {

    }

    private List<MapDataBean> list_datas;

    @Override
    public void rendData(List<MapDataBean> list) {
        //添加一遍距离 并且做一边筛选
        MyLocationBean bean = PreferencesHelper.getData(MyLocationBean.class);
        List<MapDataBean> temp = new ArrayList<>();
        if (bean != null) {
            for (int i = 0; i < list.size(); i++) {
                double distance = Tools.GetDistance(bean.latitude, bean.longtitude, list.get(i).latitude, list.get(i).longitude);
                Log.e("yzh","---distance--"+distance);
                list.get(i).distance = distance;
                //如果大于distance范围过滤
                if (distance <= ChoiceManager.getInstance().getDistance()) {
                    temp.add(list.get(i));
                }
            }
            list_datas = temp;
            adapter.replaceAll(temp);
        } else {
            list_datas = list;
            adapter.replaceAll(list);
        }
    }

    @Override
    public void onRefreshing(PtrFrameLayout frame) {
        if (!frame.isAutoRefresh()) {
            presenter.setOtherLoading(true);
            presenter.getDatas();
        }

    }

    @Override
    public void onLoading(PtrFrameLayout frame) {

    }
}

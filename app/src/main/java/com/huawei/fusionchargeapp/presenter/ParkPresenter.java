package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.base.BaseView;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ParkApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.RequesHomeMapInfo;
import com.huawei.fusionchargeapp.model.beans.RequestFeeBean;
import com.huawei.fusionchargeapp.model.beans.RequestZoneStationBean;
import com.huawei.fusionchargeapp.model.beans.ZoneDataBean;
import com.huawei.fusionchargeapp.model.beans.ZonePileBean;
import com.huawei.fusionchargeapp.model.beans.ZoneStationBean;
import com.huawei.fusionchargeapp.views.interfaces.ParkView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2018/5/15.
 */

public class ParkPresenter extends BasePresenter<ParkView> {

    private ParkApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(ParkApi.class);
    }

    @Override
    public void onStart() {


    }

    public void getZoneDatas(long zoneID) {
        RequestZoneStationBean bean = new RequestZoneStationBean();
        bean.zoneId = zoneID;
        api.getZoneStations(UserHelper.getSavedUser().token, Urls.GET_ZONE_STATION + zoneID)
                .compose(new ResponseTransformer<>(this.<BaseData<ZoneDataBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<ZoneDataBean>>(view) {
                    @Override
                    public void success(BaseData<ZoneDataBean> baseData) {
                        List<ZoneStationBean> list = new ArrayList<>();
                        if (baseData.data != null) {
                            if (baseData.data.site != null) {
                                for (int i = 0; i < baseData.data.site.size(); i++) {
                                        ZoneStationBean zoneStationBean=baseData.data.site.get(i);
                                        zoneStationBean.type="station";
                                        list.add(zoneStationBean);
                                }
                            }
                            if(baseData.data.pile!=null){
                                for (int i = 0; i < baseData.data.pile.size(); i++) {
                                    ZonePileBean zonePileBean=baseData.data.pile.get(i);
                                    ZoneStationBean bean=new ZoneStationBean();
                                    bean.id=zonePileBean.pileId;
                                    bean.latitude=zonePileBean.latitude;
                                    bean.longitude=zonePileBean.longitude;
                                    bean.type="pile";
                                    list.add(bean);
                                }
                            }
                            view.renderDatas(list);
                        }

                    }
                });
    }

    //        "type":"pile"//pile桩、station站
    public void getInfo(final long id, String type) {
        RequesHomeMapInfo bean = new RequesHomeMapInfo();
        bean.id = id;
        bean.type = type;
        view.showLoading();
        api.getHomeMapInfo(bean)
                .compose(new ResponseTransformer<>(this.<BaseData<MapInfoBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<MapInfoBean>>(view) {
                    @Override
                    public void success(BaseData<MapInfoBean> baseData) {
                        Log.e("zw", "hahaha : " + baseData.data.toString());
                        view.getMarkInfo(id, baseData.data);
                    }
                });

    }

    /**
     * 获取费率信息
     */
    public void getFeeInfo(long id) {
        RequestFeeBean bean = new RequestFeeBean();
        bean.chargePileId = id;
        view.showLoading();
        api.getFeeData(bean)
                .compose(new ResponseTransformer<>(this.<BaseData<PileFeeBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<PileFeeBean>>(view) {
                    @Override
                    public void success(BaseData<PileFeeBean> baseData) {
//                        view.getMarkInfo(baseData.data);
                        view.showPileFeeInfo(baseData.data);
                    }
                });
    }
}

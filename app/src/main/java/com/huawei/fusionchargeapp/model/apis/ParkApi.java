package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.RequesHomeMapInfo;
import com.huawei.fusionchargeapp.model.beans.RequestFeeBean;
import com.huawei.fusionchargeapp.model.beans.RequestZoneStationBean;
import com.huawei.fusionchargeapp.model.beans.ZoneDataBean;
import com.huawei.fusionchargeapp.model.beans.ZoneStationBean;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by john on 2018/5/15.
 */

public interface ParkApi {
//    @POST(Urls.GET_ZONE_STATION)
    @POST
    Observable<BaseData<ZoneDataBean>> getZoneStations(@Header("AccessToken") String token, @Url String url);
    @POST(Urls.HOME_MAP_INFO)
    Observable<BaseData<MapInfoBean>> getHomeMapInfo(@Body RequesHomeMapInfo info);
    @POST(Urls.QUERY_FEE)
    Observable<BaseData<PileFeeBean>> getFeeData(@Body RequestFeeBean bean);
}

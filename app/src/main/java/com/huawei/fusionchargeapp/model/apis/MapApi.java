package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.Condition;
import com.huawei.fusionchargeapp.model.beans.Condition0;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.HomeOrderBean;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.ReportUserLocation;
import com.huawei.fusionchargeapp.model.beans.RequesHomeMapInfo;
import com.huawei.fusionchargeapp.model.beans.RequestChargeStateBean;
import com.huawei.fusionchargeapp.model.beans.RequestFeeBean;
import com.huawei.fusionchargeapp.model.beans.RequestHomeAppointment;
import com.huawei.fusionchargeapp.model.beans.SearchCondition;


import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/4/23.
 */

public interface MapApi {

    @POST(Urls.MAP_DATA)
    Observable<BaseData<List<MapDataBean>>> getMapDatas(@Body Condition bean);

    @POST(Urls.MAP_DATA)
    Observable<BaseData<List<MapDataBean>>> getMapDatas0(@Body Condition0 bean);

    @POST(Urls.MAP_DATA)
    Observable<BaseData<List<MapDataBean>>> getMapDatas0(@Body SearchCondition bean);

    @POST(Urls.HOME_MAP_INFO)
    Observable<BaseData<MapInfoBean>> getHomeMapInfo(@Body RequesHomeMapInfo info);

    @POST(Urls.QUERY_FEE)
    Observable<BaseData<PileFeeBean>> getFeeData(@Body RequestFeeBean bean);

    @POST(Urls.GET_USER_NOT_PAY)
    Observable<BaseData<HomeOrderBean>> getUserOrderStatue(@Header("AccessToken") String token);

    @POST(Urls.GET_USER_CHARGER_STATUE)
    Observable<BaseData<HomeChargeOrderBean>> getUserChargerStatue(@Header("AccessToken") String token);

    @POST(Urls.GET_CHECK_STATUE)
    Observable<BaseData> getCheckStatue(@Header("AccessToken") String token, @Body RequestChargeStateBean bean);


    @POST(Urls.GET_USER_APPOINTMENT)
    Observable<BaseData<HomeAppointmentBean>> getUserAppointmentRecord(@Header("AccessToken") String token,@Body RequestHomeAppointment bean);

    @POST(Urls.REPORT_LOCATION)
    Observable<BaseData> reportUserLocation(@Body ReportUserLocation bean);

}

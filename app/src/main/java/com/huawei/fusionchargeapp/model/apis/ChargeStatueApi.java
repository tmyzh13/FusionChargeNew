package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.ChargerStatueBean;
import com.huawei.fusionchargeapp.model.beans.RequestEndChargerBean;
import com.huawei.fusionchargeapp.model.beans.RequestStartChargerBean;
import com.huawei.fusionchargeapp.model.beans.RequstChargeStatueBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/4/24.
 */

public interface ChargeStatueApi {

    @POST(Urls.GET_CHARGE_STATUE)
    Observable<BaseData<ChargerStatueBean>> getChargeStatue(@Header("AccessToken") String token,@Body RequstChargeStatueBean bean);

    @POST(Urls.START_CHARGER)
    Observable<BaseData> startCharge(@Body RequestStartChargerBean bean);

    @POST(Urls.STOP_CHARGER)
    Observable<BaseData<String>> stopCharge(@Header("AccessToken") String token,@Body RequestEndChargerBean bean);
}

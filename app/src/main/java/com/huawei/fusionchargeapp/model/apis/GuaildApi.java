package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.PileList;
import com.huawei.fusionchargeapp.model.beans.RequestCancelAppointment;
import com.huawei.fusionchargeapp.model.beans.RequestChargePileDetailBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/4/25.
 */

public interface GuaildApi {
    @POST(Urls.CANCEL_APPOINTMENT)
    Observable<BaseData> cancelAppointment(@Header("AccessToken") String token,@Body RequestCancelAppointment bean);

    //充电站详情_STATION
    @POST(Urls.CHARGE_PILE_DETAIL)
    Observable<BaseData<ChargeStationDetailBean>> getChargeStationDetail( @Body RequestChargePileDetailBean bean);

    //充电站详情_PILE
    @POST(Urls.CHARGE_PILE_DETAIL)
    Observable<BaseData<PileList>> getChargePileDetail( @Body RequestChargePileDetailBean bean);
}

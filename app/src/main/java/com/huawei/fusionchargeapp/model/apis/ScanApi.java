package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.MyOrderData;
import com.huawei.fusionchargeapp.model.beans.OrderRequestInfo;
import com.huawei.fusionchargeapp.model.beans.PileList;
import com.huawei.fusionchargeapp.model.beans.RequestChargePileDetailBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderBean;
import com.huawei.fusionchargeapp.model.beans.RequestScanBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhangwei on 2018/4/26.
 */

public interface ScanApi {

    //充电桩信息
    @POST(Urls.SCAN_CHARGE)
    Observable<BaseData<ScanChargeInfo>> getScanChargeInfo(@Header("AccessToken") String token, @Body RequestScanBean bean);

    //开始充电
    @POST(Urls.START_CHARGER)
    Observable<BaseData> getOrderDetail(@Header("AccessToken") String token, @Body OrderRequestInfo bean);

    //充电站详情_STATION
    @POST(Urls.CHARGE_PILE_DETAIL)
    Observable<BaseData<ChargeStationDetailBean>> getChargeStationDetail(@Header("AccessToken") String token, @Body RequestChargePileDetailBean bean);

    //充电站详情_PILE
    @POST(Urls.CHARGE_PILE_DETAIL)
    Observable<BaseData<PileList>> getChargePileDetail(@Header("AccessToken") String token, @Body RequestChargePileDetailBean bean);


    //我的订单
    @POST(Urls.MY_ORDER)
    Observable<MyOrderData> getMyOrder(@Header("AccessToken") String token, @Body RequestMyOrderBean bean);


}

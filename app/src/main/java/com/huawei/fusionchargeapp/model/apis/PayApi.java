package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyTaocanBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayDetailBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayTCBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayTaocanBean;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;


import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by issuser on 2018/4/25.
 */

public interface PayApi {

    @POST(Urls.GET_PAY_DETAIL)
    Observable<BaseData<PayInfoBean>> getPayDetail(@Header("AccessToken") String token, @Body RequestPayDetailBean bean);

    @POST(Urls.BALANCE_PAY)
    Observable<BaseData<PayResultBean>> balancePay(@Header("AccessToken") String token, @Body RequestPayBean bean);

    @POST(Urls.BALANCE_PAY)
    Observable<BaseData<PayResultBean>> balancePay(@Header("AccessToken") String token, @Body RequestPayTCBean bean);
    @POST
    Observable<BaseData<MyTaocanBean>> getMyTaocan(@Header("AccessToken") String token, @Url String url);
}

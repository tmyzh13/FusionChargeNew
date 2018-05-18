package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BalanceBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by john on 2018/5/18.
 */

public interface MyAcountApi {
    @GET(Urls.GET_MY_BALANCE)
    Observable<BaseData<BalanceBean>> getMyBalance(@Header("AccessToken") String token);
}

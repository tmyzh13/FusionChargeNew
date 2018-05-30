package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayBalance;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/5/30.
 */

public interface CashPatternApi {

    @POST(Urls.CHARGERING_BALANCE)
    Observable<BaseData<PayResultBean>>  payBalance(@Header("AccessToken") String token, @Body RequestPayBalance bean);
}

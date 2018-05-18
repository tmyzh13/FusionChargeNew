package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestPayTaocanBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/5/17.
 */

public interface TaocanPayapi {
    @POST(Urls.APP_TAO_CAN_PAY)
    Observable<BaseData> payTaocan(@Header("AccessToken") String token, @Body RequestPayTaocanBean bean);
}

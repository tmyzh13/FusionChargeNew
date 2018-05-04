package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.AppointRequestBean;
import com.huawei.fusionchargeapp.model.beans.AppointResponseBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;

import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/4/23.
 */

public interface AppointApi {
    @POST(Urls.APPOINT_CHARGE)
    Observable<BaseData<AppointResponseBean>> appoint(@Header("AccessToken") String token, @Body AppointRequestBean bean);
}

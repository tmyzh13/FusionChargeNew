package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestCancelAppointment;

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
}

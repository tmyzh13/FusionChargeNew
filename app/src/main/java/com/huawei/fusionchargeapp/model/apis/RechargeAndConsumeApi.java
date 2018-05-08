package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BalanceResultBean;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeRequestBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2018/5/8.
 */

public interface RechargeAndConsumeApi {
    @POST(Urls.BALANCE_RECORD)
    Observable<BalanceResultBean> getAppointmentInfo(@Header("AccessToken") String token, @Body RechargeAndConsumeRequestBean bean);
}

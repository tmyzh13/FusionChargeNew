package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentRequestBean;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentResultBean;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentRequestWithTimeConditionBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyAllAppointment;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2018/5/4.
 */

public interface AllAppointmentApi {
    @POST(Urls.ALL_APPOINTMENT_INFO)
    Observable<MyAllAppointment> getAppointmentInfo(@Header("AccessToken") String token, @Body AllAppointmentRequestBean bean);

    @POST(Urls.ALL_APPOINTMENT_INFO)
    Observable<MyAllAppointment> getAppointmentInfoWithTimeCondition(@Header("AccessToken") String token, @Body AllAppointmentRequestWithTimeConditionBean bean);
}
package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceResultBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RepayInvoiceBean;
import com.huawei.fusionchargeapp.model.beans.RequestApplyInvoiceBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by issuser on 2018/5/18.
 */

public interface ApplyInvoiceApi {

    @POST(Urls.APPLY_INVOICE)
    Observable<BaseData<ApplyInvoiceResultBean>> applyInvoice(@Header("AccessToken") String token, @Body RequestApplyInvoiceBean bean);

    @GET
    Observable<BaseData<RequestApplyInvoiceBean>> getUnpayInvocie(@Header("AccessToken") String token, @Url String url);

    @POST(Urls.REPAY_INVOICE)
    Observable<BaseData<ApplyInvoiceResultBean>> repayInvoice(@Header("AccessToken") String token, @Body RepayInvoiceBean bean);

}

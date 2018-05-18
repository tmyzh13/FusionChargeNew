package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequestBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeResultBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryResultBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by admin on 2018/5/15.
 */

public interface InvoiceApi {

    @POST(Urls.INVOICE_RECHARGE_LIST)
    Observable<InvoiceConsumeResultBean> getInvoiceConsume(@Header("AccessToken") String token,@Body InvoiceConsumeRequestBean bean);

    @POST(Urls.INVOICE_HISTORY)
    Observable<InvoiceHistoryResultBean> getInvoiceHistory(@Header("AccessToken") String token, @Body InvoiceConsumeRequestBean bean);

    @POST
    Observable<InvoiceHistoryResultBean> getInvoiceHistory(@Header("AccessToken") String token, @Url String url);
}

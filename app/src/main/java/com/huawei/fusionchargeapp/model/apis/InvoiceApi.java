package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequestBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeResultBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2018/5/15.
 */

public interface InvoiceApi {

    @POST(Urls.INVOICE_RECHARGE_LIST)
    Observable<InvoiceConsumeResultBean> getInvoiceConsume(@Body InvoiceConsumeRequestBean bean);
}

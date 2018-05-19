package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequest;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeRequestBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeResultBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumerResultBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryItemBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryResultBean;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @GET
    Observable<BaseData<InvoiceHistoryItemBean>> getInvoiceHistoryItem(@Header("AccessToken") String token, @Url String url);

    @POST(Urls.INVOICE_HISTORY_ITEM_CONSUME)
    Observable<InvoiceConsumerResultBean> getInvoiceHistoryConsume(@Header("AccessToken") String token, @Body InvoiceConsumeRequest bean);
}

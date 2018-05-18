package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestApplyInvoiceBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/5/18.
 */

public interface ApplyInvoiceApi {

    @POST(Urls.APPLY_INVOICE)
    Observable<BaseData> applyInvoice(@Body RequestApplyInvoiceBean bean);
}

package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyTaocanBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by issuser on 2018/5/18.
 */

public interface MyTaoCanApi {
    @POST
    Observable<BaseData<MyTaocanBean>> getMyTaocan(@Header("AccessToken") String token, @Url String url);
}

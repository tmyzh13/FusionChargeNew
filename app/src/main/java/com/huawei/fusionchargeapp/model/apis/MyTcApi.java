package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;

import java.util.List;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by issuser on 2018/5/17.
 */

public interface MyTcApi {

    @POST(Urls.APP_TAO_CAN)
    Observable<BaseData<List<TaocanBean>>> getAppTaocan(@Header("AccessToken") String token);
}

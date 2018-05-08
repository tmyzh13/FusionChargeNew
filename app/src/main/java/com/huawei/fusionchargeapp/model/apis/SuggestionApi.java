package com.huawei.fusionchargeapp.model.apis;

import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.SuggestionBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2018/5/8.
 */

public interface SuggestionApi {
    @POST(Urls.PUBLISH_SUGGESTION)
    Observable<BaseData<Void>> commit(@Header("AccessToken") String token, @Body SuggestionBean bean);
}

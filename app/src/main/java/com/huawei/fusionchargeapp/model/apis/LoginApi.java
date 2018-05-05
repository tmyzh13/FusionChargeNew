package com.huawei.fusionchargeapp.model.apis;

import com.corelibs.api.RequestBodyCreator;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.CheckCodeBean;
import com.huawei.fusionchargeapp.model.beans.LoginRequestBean;
import com.huawei.fusionchargeapp.model.beans.ModifyPwdRequestBean;
import com.huawei.fusionchargeapp.model.beans.ModifyUserInfoRequestBean;
import com.huawei.fusionchargeapp.model.beans.ResponseMessageBean;
import com.huawei.fusionchargeapp.model.beans.RestPwdRequestBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.model.beans.UserInfoBean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by issuser on 2018/4/19.
 */

public interface LoginApi {
    //login
    @POST(Urls.LOGIN)
    Observable<BaseData<UserBean>> login(@Body LoginRequestBean bean);

    @POST(Urls.REGISTER)
    Observable<BaseData> register(@Body LoginRequestBean bean);

    @POST(Urls.GET_CODE)
    Observable<BaseData> getCode(@Body LoginRequestBean bean);

    //重置密码、忘记密码
    @POST(Urls.RESET_PWD)
    Observable<BaseData> restPwd(@Body RestPwdRequestBean bean);

    //修改密码
    @POST(Urls.RESET_PWD)
    Observable<BaseData<ResponseMessageBean>> modifyPwd(@Header("AccessToken")String accessToken,@Body ModifyPwdRequestBean bean);

    @POST(Urls.CHECK_CODE)
    Observable<BaseData> checkCode(@Body CheckCodeBean bean);

    //获取用户信息
    @GET(Urls.GET_USER_INFO)
    Observable<BaseData<UserInfoBean>> getUserInfo(@Header("AccessToken")String accessToken);

    //修改用户信息
    @POST(Urls.MODIFY_USER_INFO)
    Observable<BaseData<ResponseMessageBean>> modifyUserInfo(@Header("AccessToken")String accessToken,@Body ModifyUserInfoRequestBean bean);

    //上传用户头像
    @POST(Urls.MODIFY_IMG)
    @Multipart
    //表示请求发送multipart数据，需要配合使用@Part
    Observable<BaseData> upload(@Header("AccessToken")String accessToken,@Part("file"+ RequestBodyCreator.MULTIPART_JEPG) RequestBody file);

}

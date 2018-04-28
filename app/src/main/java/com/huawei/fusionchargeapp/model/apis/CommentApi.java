package com.huawei.fusionchargeapp.model.apis;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import com.huawei.fusionchargeapp.constants.Urls;
import rx.Observable;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.CommentSortBean;
import com.huawei.fusionchargeapp.model.beans.CommentsBean;
import com.huawei.fusionchargeapp.model.beans.NullPostBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PublishCommentsBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayDetailBean;
import com.huawei.fusionchargeapp.model.beans.StationBean;

import java.util.List;

/**
 * Created by issuser on 2018/4/25.
 */

public interface CommentApi {
    //publish comment
    @POST(Urls.COMMENT)
    Observable<BaseData> publish(@Header("AccessToken") String token,@Body PublishCommentsBean bean);

    @POST(Urls.COMMENT_SORT)
    Observable<BaseData<List<CommentSortBean>>> getCommentSortAndTimes(@Header("AccessToken") String token, @Body StationBean bean);

    @POST(Urls.COMMENT_INFO)
    Observable<BaseData<List<CommentsBean>>> queryCommentInfo(@Header("AccessToken") String token,@Body StationBean bean);

    @POST(Urls.COMMENT_INFO_TYPE)
    Observable<BaseData<List<CommentSortBean>>> queryCommentSortType(@Header("AccessToken") String token,@Body NullPostBean bean);

    @POST(Urls.GET_PAY_DETAIL)
    Observable<BaseData<PayInfoBean>> getPayDetail(@Header("AccessToken") String token, @Body RequestPayDetailBean bean);
}

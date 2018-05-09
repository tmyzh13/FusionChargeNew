package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.CommentApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.CommentSortBean;
import com.huawei.fusionchargeapp.model.beans.CommentsBean;
import com.huawei.fusionchargeapp.model.beans.NullPostBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PublishCommentsBean;
import com.huawei.fusionchargeapp.model.beans.RequestPayDetailBean;
import com.huawei.fusionchargeapp.model.beans.StationBean;
import com.huawei.fusionchargeapp.views.interfaces.CommentView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

/**
 * Created by issuser on 2018/4/25.
 */

public class CommentPresenter extends BasePresenter<CommentView> {
    private CommentApi api;
    @Override
    public void onStart() {
    }

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api =  ApiFactory.getFactory().create(CommentApi.class);
    }

    private String token = UserHelper.getSavedUser().token;
    public void publish(PublishCommentsBean bean) {
        api.publish(token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        Log.e("publish","---success");
                        view.commentPublished();
                    }
                });
    }

    public void getCommentSortAndTimes(int id) {
        StationBean bean = new StationBean();
        bean.setStationId(id);
        api.getCommentSortAndTimes(token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<List<CommentSortBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<CommentSortBean>>>(view) {
                    @Override
                    public void success(BaseData<List<CommentSortBean>> baseData) {
                        Log.e("zw","getCommentSortAndTimes---success : " + baseData.toString());
                        view.getCommentSortAndTimes(baseData.data);
                    }
                });
    }
    public void queryCommentInfo(int id) {
        StationBean bean = new StationBean();
        bean.setStationId(id);
        api.queryCommentInfo(token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<List<CommentsBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<CommentsBean>>>(view) {
                    @Override
                    public void success(BaseData<List<CommentsBean>> baseData) {
                        Log.e("zw","queryCommentInfo---success : " + baseData.toString());
                        if(baseData.data!=null){
                            view.queryCommentInfo(baseData.data);
                        }
                    }

                });
    }
    public void queryCommentSortType() {
        api.queryCommentSortType(token, new NullPostBean())
                .compose(new ResponseTransformer<>(this.<BaseData<List<CommentSortBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<CommentSortBean>>>(view) {
                    @Override
                    public void success(BaseData<List<CommentSortBean>> baseData) {
                        Log.e("queryCommentSortType","---success");
                        view.queryCommentSortType(baseData.data);
                    }
                });
    }

    public void getPayDetailInfo(String orderNum){
        RequestPayDetailBean bean=new RequestPayDetailBean();
        bean.orderRecordNum=orderNum;
        view.showLoading();
        api.getPayDetail(token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<PayInfoBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<PayInfoBean>>(view) {
                    @Override
                    public void success(BaseData<PayInfoBean> baseData) {
                        view.renderData(baseData.data);
                    }
                });
    }
}


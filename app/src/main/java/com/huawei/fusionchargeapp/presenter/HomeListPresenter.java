package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.apis.MapApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.Condition;
import com.huawei.fusionchargeapp.model.beans.Condition0;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.utils.ChoiceManager;
import com.huawei.fusionchargeapp.views.interfaces.HomeListView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

/**
 * Created by issuser on 2018/4/23.
 */

public class HomeListPresenter extends BasePresenter<HomeListView> {

    MapApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(MapApi.class);
    }

    @Override
    public void onStart() {

    }
    private  boolean otherLoading=false;

    public void setOtherLoading(boolean otherLoading){
        this.otherLoading=otherLoading;
    }

    public void getDatas(){
        if(otherLoading){
        }else{
            view.showLoading();
        }
        doGetDatas(newCondition());

    }

    public void getDataType0(){
        if(otherLoading){
        }else{
            view.showLoading();
        }
        Condition0 condition=new Condition0();
        condition.workStatus=ChoiceManager.getInstance().getStatue();
        condition.x1=100;
        condition.x2=200;
        condition.y1=30;
        condition.y2=40;
        condition.selectType=3;
        api.getMapDatas0(condition)
                .compose(new ResponseTransformer<>(this.<BaseData<List<MapDataBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<MapDataBean>>>(view) {
                    @Override
                    public void success(BaseData<List<MapDataBean>> baseData) {
                        view.rendData(baseData.data);
                    }
                });
    }

    public void getDatas(String stationName){
        Condition0 condition = new Condition0();
        condition.workStatus=ChoiceManager.getInstance().getStatue();
        condition.x1=100;
        condition.x2=200;
        condition.y1=30;
        condition.y2=40;
        condition.selectType=3;
        condition.stationName =stationName;
        api.getMapDatas0(condition)
                .compose(new ResponseTransformer<>(this.<BaseData<List<MapDataBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<MapDataBean>>>(view) {
                    @Override
                    public void success(BaseData<List<MapDataBean>> baseData) {
                        view.rendData(baseData.data);
                    }
                });
    }

    private Condition newCondition(){
        Condition condition=new Condition();
        if(ChoiceManager.getInstance().getType()==0){
            condition.pileType=3;
        }else{
            condition.pileType= ChoiceManager.getInstance().getType();
        }

            condition.workStatus=ChoiceManager.getInstance().getStatue();
        condition.x1=100;
        condition.x2=200;
        condition.y1=30;
        condition.y2=40;
        condition.selectType=3;
        return condition;
    }
    private void doGetDatas(Condition condition) {
        api.getMapDatas(condition)
                .compose(new ResponseTransformer<>(this.<BaseData<List<MapDataBean>>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<List<MapDataBean>>>(view) {
                    @Override
                    public void success(BaseData<List<MapDataBean>> baseData) {
                        view.rendData(baseData.data);
                    }
                });
    }
}

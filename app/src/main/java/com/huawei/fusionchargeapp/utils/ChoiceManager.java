package com.huawei.fusionchargeapp.utils;

import com.huawei.fusionchargeapp.constants.Constant;

/**
 * Created by issuser on 2018/4/20.
 */

public class ChoiceManager {

    private static ChoiceManager instance;

    // 方式空 1交流2直流3交直流一体
    private int type=0;
    // 状态空 //“充电方式1快充2慢充3快慢充”  1空闲2繁忙 3故障
    private int statue=0;
    //距离 默认距离是100 这里测试改成500
    //默认为false;true为打开左侧栏
    private boolean drawerStatus = false;
    private double distance=Constant.DEFAULT_DISTANCE;

    public static final int USER_INFO = 123;
    public static final int NO_INFO = -1;

    private int fromActivity = -1;

    private ChoiceManager(){

    }

    public static ChoiceManager getInstance(){
        if(instance==null){
            instance=new ChoiceManager();
        }
        return instance;
    }

    public void resetChoice(){
        type=0;
        statue=0;
        distance= Constant.DEFAULT_DISTANCE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isDrawerStatus() {
        return drawerStatus;
    }

    public void setDrawerStatus(boolean drawerStatus) {
        this.drawerStatus = drawerStatus;
    }

    public void setFromActivity(int fromActivity) {
        this.fromActivity = fromActivity;
    }

    public int getFromActivity(){
        return this.fromActivity;
    }
}

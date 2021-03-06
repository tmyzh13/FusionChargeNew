package com.huawei.fusionchargeapp.model.beans;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by issuser on 2018/4/23.
 */

public class MapDataBean implements Comparable<MapDataBean> {

    public String address;
    public long id;
    public double latitude;
    public double longitude;
    public int alterNum;
    public int directNum;
    public String title;
    //station pile
    public String type;
    public double distance;
    public int pileNum;
    public int gunNum;
    public int freeNum;
    public int busyNum;

    @Override
    public String toString() {
        return "MapDataBean{" +
                "address='" + address + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", alterNum=" + alterNum +
                ", directNum=" + directNum +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", distance=" + distance +
                ", pileNum=" + pileNum +
                ", gunNum=" + gunNum +
                ", freeNum=" + freeNum +
                ", busyNum=" + busyNum +
                '}';
    }

    @Override
    public int compareTo(@NonNull MapDataBean o) {
       if(this.distance>o.distance){
           return 1;
       }else{
           return  -1;
       }
    }
}

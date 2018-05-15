package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by john on 2018/5/15.
 */

public class ZoneStationBean {

    public long id;
    public double latitude;
    public double longitude;
    public String type;

    @Override
    public String toString() {
        return "ZoneStationBean{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type='" + type + '\'' +
                '}';
    }
}

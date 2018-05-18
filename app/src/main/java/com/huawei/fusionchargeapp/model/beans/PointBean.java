package com.huawei.fusionchargeapp.model.beans;

import java.util.List;

/**
 * Created by issuser on 2018/5/18.
 */

public class PointBean {

    public List<List<Double>> outData;
    public List<List<List<Double>>> parkData;
    public List<List<Double>> inData;

    @Override
    public String toString() {
        return "PointBean{" +
                "outData=" + outData +
                ", parkData=" + parkData +
                ", inData=" + inData +
                '}';
    }
}

package com.huawei.fusionchargeapp.model.beans;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/4.
 */

public class AllAppointmentResultBean implements Serializable {
    public String chargingAddress;
    public long chargingPileId;
    public String chargingPileName;
    public String gunCode;
    public String reserveBeginTime;
    public int reserveId;
    public String runCode;//电桩编号
    public int state;
}

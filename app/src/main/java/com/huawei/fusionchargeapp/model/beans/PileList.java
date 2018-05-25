package com.huawei.fusionchargeapp.model.beans;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by zhangwei on 2018/4/27.
 */

public class PileList{

    private String address;
    private String alterNum;
    private String averageScore;
    private String directNum;
    private String freeNum;
    private List<GunList> gunList;
    private String gunNum;
    private int id;
    private String integrationNum;
    private String latitude;
    private String longitude;
    private String maxCurrent;
    private String maxPower;
    private String maxVoltage;
    private String name;
    private String objType;
    private String openStatus;
    private String photoUrl;
    private String pileList;
    private String pileNum;
    private String runCode;
    //runStatus==>status  1在线 2离线
    private int status;
    private String type;
    private String virtualId;
    private String workStatus;

    private long zoneId;
    //0 否1 是
    private int isGis;

    public void setZoneId(long zongId){
        this.zoneId=zongId;
    }

    public long getZoneId(){
        return zoneId;
    }

    public void setIsGis(int gis){
        this.isGis=gis;
    }

    public int getGis(){
        return isGis;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setAlterNum(String alterNum) {
        this.alterNum = alterNum;
    }
    public String getAlterNum() {
        return alterNum;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }
    public String getAverageScore() {
        return averageScore;
    }

    public void setDirectNum(String directNum) {
        this.directNum = directNum;
    }
    public String getDirectNum() {
        return directNum;
    }

    public void setFreeNum(String freeNum) {
        this.freeNum = freeNum;
    }
    public String getFreeNum() {
        return freeNum;
    }

    public void setGunList(List<GunList> gunList) {
        this.gunList = gunList;
    }
    public List<GunList> getGunList() {
        return gunList;
    }

    public void setGunNum(String gunNum) {
        this.gunNum = gunNum;
    }
    public String getGunNum() {
        return gunNum;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setIntegrationNum(String integrationNum) {
        this.integrationNum = integrationNum;
    }
    public String getIntegrationNum() {
        return integrationNum;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setMaxCurrent(String maxCurrent) {
        this.maxCurrent = maxCurrent;
    }
    public String getMaxCurrent() {
        return maxCurrent;
    }

    public void setMaxPower(String maxPower) {
        this.maxPower = maxPower;
    }
    public String getMaxPower() {
        return maxPower;
    }

    public void setMaxVoltage(String maxVoltage) {
        this.maxVoltage = maxVoltage;
    }
    public String getMaxVoltage() {
        return maxVoltage;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }
    public String getObjType() {
        return objType;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }
    public String getOpenStatus() {
        return openStatus;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPileList(String pileList) {
        this.pileList = pileList;
    }
    public String getPileList() {
        return pileList;
    }

    public void setPileNum(String pileNum) {
        this.pileNum = pileNum;
    }
    public String getPileNum() {
        return pileNum;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }
    public String getRunCode() {
        return runCode;
    }

    public void setRunStatus(int runStatus) {
        this.status = runStatus;
    }
    public int getRunStatus() {
        return status;
    }

    public void setType(String type) {
        this.objType = type;
    }
    public String getType() {
        return objType;
    }

    public void setVirtualId(String virtualId) {
        this.virtualId = virtualId;
    }
    public String getVirtualId() {
        return virtualId;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }
    public String getWorkStatus() {
        return workStatus;
    }

    @Override
    public String toString() {
        return "PileList{" +
                "address='" + address + '\'' +
                ", alterNum='" + alterNum + '\'' +
                ", averageScore='" + averageScore + '\'' +
                ", directNum='" + directNum + '\'' +
                ", freeNum='" + freeNum + '\'' +
                ", gunList=" + gunList +
                ", gunNum='" + gunNum + '\'' +
                ", id=" + id +
                ", integrationNum='" + integrationNum + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", maxCurrent='" + maxCurrent + '\'' +
                ", maxPower='" + maxPower + '\'' +
                ", maxVoltage='" + maxVoltage + '\'' +
                ", name='" + name + '\'' +
                ", objType='" + objType + '\'' +
                ", openStatus='" + openStatus + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", pileList='" + pileList + '\'' +
                ", pileNum='" + pileNum + '\'' +
                ", runCode='" + runCode + '\'' +
                ", runStatus=" + status +
                ", type='" + type + '\'' +
                ", virtualId='" + virtualId + '\'' +
                ", workStatus='" + workStatus + '\'' +
                '}';
    }
}
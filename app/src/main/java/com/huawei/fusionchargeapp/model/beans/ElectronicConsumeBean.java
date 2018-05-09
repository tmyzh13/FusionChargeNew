package com.huawei.fusionchargeapp.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2018/5/9.
 */

public class ElectronicConsumeBean implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    public ElectronicConsumeBean(int type, String time, String adress, String money) {
        this.type = type;
        this.time = time;
        this.adress = adress;
        this.money = money;
    }
    public ElectronicConsumeBean(Parcel parcel) {
        this.type = parcel.readInt();
        this.time = parcel.readString();
        this.adress = parcel.readString();
        this.money = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(time);
        parcel.writeString(adress);
        parcel.writeString(money);
    }
    public static final Parcelable.Creator<ElectronicConsumeBean> CREATOR = new Parcelable.Creator<ElectronicConsumeBean>(){
        @Override
        public ElectronicConsumeBean createFromParcel(Parcel parcel) {
            return new ElectronicConsumeBean(parcel);
        }

        @Override
        public ElectronicConsumeBean[] newArray(int i) {
            return new ElectronicConsumeBean[i];
        }
    };

    public int type;
    public String time,adress,money;
}

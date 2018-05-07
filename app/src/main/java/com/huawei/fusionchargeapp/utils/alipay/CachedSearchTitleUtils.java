package com.huawei.fusionchargeapp.utils.alipay;


import com.corelibs.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by issuser on 2018/4/27.
 */

public class CachedSearchTitleUtils {
    private static List<CachedData> historyData = new ArrayList<>();
    //private static int lastTimeHistory = 0;
    private static final int MAX_NUM_HITORY = 5;
    //private static boolean isCachedChange = false;

    public static void saveHistoryData() {
        HistoryDataBean bean = new HistoryDataBean();
        bean.historyData = historyData;
        PreferencesHelper.saveData(bean);
    }

    public static class HistoryDataBean {
        List<CachedData> historyData;
    }

    public static void initHistoryData(){
        HistoryDataBean bean = PreferencesHelper.getData(HistoryDataBean.class);
        if (null == bean) {
            return;
        }
        historyData = bean.historyData;
        //isCachedChange = false;
    }
    public static void addHistoryData(CachedData data) {
        if (historyData.size() == 0){
            historyData.add(data);
            return;
        }
        int pos = -1;
        for (int i=0;i<historyData.size();i++) {
            if (historyData.get(i).equals(data)) {
                pos = i;
            }
        }
        if (pos != -1){
            addDataToFirst(data,pos);
            return;
        }
        if (historyData.size() >= MAX_NUM_HITORY) {
           addDataToFirst(data,MAX_NUM_HITORY-1);
        } else {
            historyData.add(data);
            addDataToFirst(data,historyData.size()-1);
        }
    }

    private static void addDataToFirst(CachedData data,int pos){
        for (int i= pos;i>0;i--) {
            historyData.set(i,historyData.get(i-1));
        }
        historyData.set(0,data);
    }

    public static void resetHistoryData(){
        historyData = new ArrayList<>();
        //lastTimeHistory = 0;
    }
    public static List<String> getHistoryData(){
        List<String> list = new ArrayList<>();
        for (int i=0;i< historyData.size();i++){
            list.add(historyData.get(i).station);
        }
        return list;
    }
    public static CachedData getCachedData(int postion){
        return historyData.get(postion);
    }

    public static class CachedData{
        public String station ,type;
        public String id;
        public CachedData(String station, String type, String id) {
            this.station = station;
            this.type = type;
            this.id = id;
        }
        public boolean equals(CachedData data){
            if (data == null){
                return false;
            }
            if (id.equals(data.id)) {
                return true;
            } else {
                return false;
            }
        }
    }
}

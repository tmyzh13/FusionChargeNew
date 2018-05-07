package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeListBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.GunList;
import com.huawei.fusionchargeapp.model.beans.PileList;

import java.util.List;

/**
 * 充电桩 列表adapter
 * Created by issuser on 2018/4/19.
 */

public class ChargePileAdapter extends BaseAdapter {
    private Context context;
    private List<PileList> datas;
    private LayoutInflater mInflater;
    private List<GunList> gunList;
    private List<ChargeDetailFeeBean> feeList;

    private ChargeStationDetailBean bean;

    public ChargePileAdapter(Context context, ChargeStationDetailBean bean, List<ChargeDetailFeeBean> feeList) {
        this.context = context;
        this.bean = bean;
        this.datas = bean.getPileList();
        this.feeList = feeList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 :datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PileList chargePileBean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_charge_pile_list, null);
            holder.chargeFeeTv = convertView.findViewById(R.id.charging_fee);
            holder.serviceFeeTv = convertView.findViewById(R.id.service_fee);
            holder.pileStatusIv = convertView.findViewById(R.id.pile_status_iv);
            holder.pileStatusTv = convertView.findViewById(R.id.pile_status_tv);
            holder.pileNumTv = convertView.findViewById(R.id.pile_num_tv);
            holder.maxPowerTv = convertView.findViewById(R.id.max_power_tv);
            holder.maxElectronicTv = convertView.findViewById(R.id.max_electronic_tv);
            holder.maxVoltageTv = convertView.findViewById(R.id.max_voltage_tv);
            holder.electricGunLv = convertView.findViewById(R.id.electric_gun_lv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (chargePileBean.getRunStatus() == 1) {
            holder.pileStatusIv.setImageResource(R.mipmap.charge_on);
        } else {
            holder.pileStatusIv.setImageResource(R.mipmap.charge_off);
        }

        //获取充电价格和服务费价格
        holder.serviceFeeTv.setText(feeList.get(position).getServiceFee() + context.getString(R.string.yuan_du));
        List<ChargeDetailFeeListBean> feeListBean = feeList.get(position).getFeeList();
        if( feeListBean != null) {
            double max = 0;
            double min = 0;
            for (int i = 0; i < feeListBean.size(); i++) {
                double temp = feeListBean.get(i).getMultiFee();
                if(temp < min) {
                    min = temp;
                }
                if(temp > max) {
                    max = temp;
                }
            }
            holder.chargeFeeTv.setText(min + context.getString(R.string.yuan_du) + "~" + max + context.getString(R.string.yuan_du));
        }

        holder.pileNumTv.setText(chargePileBean.getRunCode());
        holder.maxPowerTv.setText(chargePileBean.getMaxPower() + "kw");
        holder.maxElectronicTv.setText(chargePileBean.getMaxCurrent() + "A");
        holder.maxVoltageTv.setText(chargePileBean.getMaxVoltage() + "V"); //电压
        holder.pileStatusTv.setText(chargePileBean.getRunStatus() == 1 ?
                context.getString(R.string.statue_offline) : context.getString(R.string.statue_online));

        gunList = chargePileBean.getGunList();

        ElectricGunAdapter adapter = new ElectricGunAdapter(context, gunList,chargePileBean,bean.getAddress(),bean.getLatitude(),bean.getLongitude());
        holder.electricGunLv.setAdapter(adapter);
//        adapter.setData(gunList);
        setListViewHeight(holder.electricGunLv);
        return convertView;
    }

    public final class ViewHolder {
        public ImageView pileStatusIv;
        public TextView pileStatusTv;

        public TextView pileNumTv;
        public TextView maxPowerTv;
        public TextView maxElectronicTv;
        public TextView maxVoltageTv;

        public TextView chargeFeeTv;
        public TextView serviceFeeTv;

        ListView electricGunLv;
    }

    /**
     * 设置内部Listview的高度
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

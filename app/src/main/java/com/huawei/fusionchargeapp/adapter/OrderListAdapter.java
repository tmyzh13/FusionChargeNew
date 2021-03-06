package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.OrderBean;
import com.huawei.fusionchargeapp.model.beans.RawRecordBean;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.PayActivity;

import java.util.List;

/**
 * Created by issuser on 2018/4/27.
 */

public class OrderListAdapter extends BaseAdapter {
    private List<RawRecordBean> datas;
    private LayoutInflater inflater;
    private Context context;

    public OrderListAdapter(Context context, List<RawRecordBean> list) {
        this.datas = list;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<RawRecordBean> data){
        this.datas = data;
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
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_order_lv_layout,null);
            holder.oder_code_tv = convertView.findViewById(R.id.oder_code_tv);
            holder.oder_time_tv = convertView.findViewById(R.id.oder_time_tv);
            holder.charge_pile_code = convertView.findViewById(R.id.charge_pile_code);
            holder.charge_pile_address = convertView.findViewById(R.id.charge_pile_address);
            holder.oder_status_tv = convertView.findViewById(R.id.oder_status_tv);
            holder.pay_status = convertView.findViewById(R.id.pay_status);
            holder.oder_charge_fee_tv = convertView.findViewById(R.id.oder_charge_fee_tv);
            holder.oder_service_fee_tv = convertView.findViewById(R.id.oder_service_fee_tv);
            holder.oder_total_fee_tv = convertView.findViewById(R.id.oder_total_fee_tv);
            holder.charge_gun_code = convertView.findViewById(R.id.charge_gun_code);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        final RawRecordBean bean = datas.get(position);
        holder.oder_code_tv.setText(bean.getOrderNum());
        if (Tools.isNull(bean.getChargeEndTime())) {
            holder.oder_time_tv.setText("");
        } else if(bean.getChargeEndTime().length() >2){
            holder.oder_time_tv.setText(bean.getChargeEndTime().substring(0,bean.getChargeEndTime().length()-2));
        } else {
            holder.oder_time_tv.setText(bean.getChargeEndTime());
        }
        holder.charge_pile_code.setText(bean.getRunCode());
        holder.charge_gun_code.setText(bean.getGunCode());
        holder.charge_pile_address.setText(bean.getAddress());
        //0 未支付 1 已支付
        if(bean.getPayStatus() == 0) {
//            '当前状态(0:充电中，1：充电结束，2：充电启动失败，3：充电停止失败)' 在未支付的状态下判断
            if(bean.getStatus()==0){
                holder.pay_status.setText(R.string.order_charging);
            }
            //6-15修改 未支付订单下只判断
//            else if(bean.getStatus()==1){
//                holder.pay_status.setText(R.string.order_charge_end);
//            }else if(bean.getStatus()==2){
//                holder.pay_status.setText(R.string.order_charge_start_failed);
//            }else if(bean.getStatus()==3){
//                holder.pay_status.setText(R.string.order_charge_stop_failed);
//            }
            else{
                holder.pay_status.setText(R.string.no_pay);
            }
            holder.pay_status.setTextColor(Color.RED);
        } else if(bean.getPayStatus() == 1) {
            holder.pay_status.setText(R.string.has_pay);
            holder.pay_status.setTextColor(Color.GREEN);
        }
        holder.pay_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.getPayStatus() == 0) {
                    //未完成订单处理
                    //&&bean.getStatus()!=1&&bean.getStatus()!=2&&bean.getStatus()!=3修改
                    if(bean.getStatus()!=0){
                        context.startActivity(PayActivity.getLauncher(context,bean.getOrderNum(),"0"));
                    }
                }
            }
        });
        holder.oder_status_tv.setText(bean.getChargTime()+"");
        holder.oder_charge_fee_tv.setText(context.getString(R.string.much_money_double,bean.getEneryCharge()));
        holder.oder_service_fee_tv.setText(context.getString(R.string.much_money_double,bean.getServiceCharge()));
        holder.oder_total_fee_tv.setText(context.getString(R.string.much_money_double,bean.getServiceCharge() + bean.getEneryCharge()));

        return convertView;
    }

    public class ViewHolder{
        TextView oder_code_tv;
        TextView oder_time_tv;
        TextView charge_pile_code;
        TextView charge_pile_address;
        TextView oder_status_tv;
        TextView pay_status;
        TextView oder_charge_fee_tv;
        TextView oder_service_fee_tv;
        TextView oder_total_fee_tv;
        TextView charge_gun_code;
    }
}

package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentResultBean;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/3.
 */

public class AppointmentListAdapter extends BaseAdapter {
    private List<AllAppointmentResultBean> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private static final String[] STATE_TEXT= {"进行中","已完成","已取消","已超时"};
    private static final int[] STATE_COLOR = {Color.YELLOW,Color.GREEN,Color.BLUE,Color.RED};

    public AppointmentListAdapter(Context context,List<AllAppointmentResultBean> bean) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        data = bean;
    }

    public void setData(List<AllAppointmentResultBean> bean) {
        data = bean;
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return null == data ? null : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_my_appoint_detail,null);
            holder.runCode= (TextView) view.findViewById(R.id.station);
            holder.time = (TextView) view.findViewById(R.id.appoint_time);
            holder.piple = (TextView) view.findViewById(R.id.pile);
            holder.adress = (TextView) view.findViewById(R.id.adress);
            holder.state = (TextView) view.findViewById(R.id.state);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AllAppointmentResultBean bean = data.get(i);
        holder.adress.setText(String.format(context.getString(R.string.run_adress),bean.chargingAddress));
        holder.piple.setText(String.format(context.getString(R.string.pile_info),bean.gunCode));
        holder.runCode.setText(context.getString(R.string.charging_pile_no)+bean.runCode);
        holder.time.setText(bean.reserveBeginTime);
        holder.state.setText(STATE_TEXT[bean.state-1]);
        holder.state.setTextColor(STATE_COLOR[bean.state-1]);
        holder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.state == 4) {
                    //超时条目处理
                }
            }
        });
        return view;
    }



    public class ViewHolder {
        TextView runCode,time,piple,adress,state;
    }
}

package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/11.
 */

public class CashPatternAdapter extends BaseAdapter {

    private List<String> sort_name = new ArrayList<>();
    private List<String> sort_description  = new ArrayList<>();
    private List<Integer> sort_img = new ArrayList<>();
    private List<Boolean> check_state = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public CashPatternAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);


        //假数据
        sort_name.add(context.getString(R.string.zhi_fu_bao));
        sort_name.add(context.getString(R.string.wei_xin));
        sort_description.add(context.getString(R.string.advice_cash_style_selecet,context.getString(R.string.zhi_fu_bao_account)));
        sort_description.add(context.getString(R.string.advice_cash_style_selecet,context.getString(R.string.wei_xin_wallet)));
        sort_img.add(R.drawable.list_ic_zhifubao);
        sort_img.add(R.drawable.list_ic_weixin);
        for (int i= 0; i< sort_img.size();i++) {
            check_state.add(i==0);
        }
    }

    public void changeCheckedState(int pos){
        for (int i= 0; i< sort_img.size();i++) {
            check_state.set(i,i==pos);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sort_img == null ? 0 : sort_img.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_cash_pattern,null);
            holder = new ViewHolder();
            holder.select = (CheckBox) view.findViewById(R.id.cash_check);
            holder.sort_img = (ImageView) view.findViewById(R.id.cash_img);
            holder.sort_name = (TextView) view.findViewById(R.id.cash_name);
            holder.sort_description = (TextView) view.findViewById(R.id.cash_description);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.select.setOnCheckedChangeListener(null);
        holder.select.setChecked(check_state.get(i));
        holder.select.setOnCheckedChangeListener(new ChechedChangelisten(i));
        holder.sort_img.setImageResource(sort_img.get(i));
        holder.sort_name.setText(sort_name.get(i));
        holder.sort_description.setText(sort_description.get(i));

        return view;
    }

    public class ChechedChangelisten implements CompoundButton.OnCheckedChangeListener {
        private int positon;

        public ChechedChangelisten(int positon) {
            this.positon = positon;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                changeCheckedState(positon);
            }
        }
    }


    public class ViewHolder{
        public TextView sort_name,sort_description;
        public ImageView sort_img;
        public CheckBox select;
    }
}
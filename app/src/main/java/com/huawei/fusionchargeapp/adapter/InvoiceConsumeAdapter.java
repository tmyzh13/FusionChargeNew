package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ElectronicConsumeBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceConsumeAdapter extends BaseAdapter {
    private List<ElectronicConsumeBean> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public InvoiceConsumeAdapter(Context context, List<ElectronicConsumeBean> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<ElectronicConsumeBean> list){
        data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_invoice_comsume_detail,null);
            holder = new ViewHolder();
            holder.sort = (TextView) view.findViewById(R.id.consume_sort);
            holder.time = (TextView) view.findViewById(R.id.consume_time);
            holder.adress = (TextView) view.findViewById(R.id.consume_adress);
            holder.money = (TextView) view.findViewById(R.id.consume_money);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.time.setText(data.get(i).time);
        holder.adress.setText(data.get(i).adress);
        holder.sort.setText(data.get(i).type);
        holder.money.setText(getSpanString(data.get(i).money));
        return view;
    }

    private SpannableString getSpanString(String str){
        SpannableString sp = new SpannableString(str);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0,str.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.text_extra)),0,str.length()-1,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sp;
    }

    public class ViewHolder{
        TextView sort,time,adress,money;
    }
}

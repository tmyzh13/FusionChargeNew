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
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceHistoryAdapter extends BaseAdapter {
    private List<InvoiceHistoryBean> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public InvoiceHistoryAdapter(Context context, List<InvoiceHistoryBean> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<InvoiceHistoryBean> list){
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
            view = inflater.inflate(R.layout.item_invoice_hitory,null);
            holder = new ViewHolder();
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.status = (TextView) view.findViewById(R.id.invoice_status);
            holder.money = (TextView) view.findViewById(R.id.invoice_money);
            holder.sort = (TextView) view.findViewById(R.id.invoice_sort);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.time.setText(data.get(i).time);
        holder.status.setText(data.get(i).status);
        holder.sort.setText(data.get(i).sort);
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
        TextView time,money,status,sort;
    }
}
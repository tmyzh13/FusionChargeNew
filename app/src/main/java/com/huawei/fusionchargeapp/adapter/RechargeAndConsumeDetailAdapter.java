package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by admin on 2018/5/7.
 */

public class RechargeAndConsumeDetailAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> groupList = new ArrayList<>();
    private List<List<RechargeAndConsumeBean>> itemLst = new ArrayList<>();

    private static final int[] sort_img = {R.drawable.list_ic_zhifubao,R.drawable.list_ic_weixin};
    private static final String[] sort_text =  {"支付宝","微信"};
    private static final String[] DEL_OR_ADD = {"-","+"};
    private static final String[] BALANCE = {"消费","充值"};

    public RechargeAndConsumeDetailAdapter(Context context, List<String> groupList, List<List<RechargeAndConsumeBean>> itemLst) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.groupList = groupList;
        this.itemLst = itemLst;
    }

    public void setDatas(List<String> groupList, List<List<RechargeAndConsumeBean>> itemLst){
        this.groupList = groupList;
        this.itemLst = itemLst;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<RechargeAndConsumeBean> list = itemLst.get(i);
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return itemLst.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        for (int j = 0 ; j < i;j++){
            i1 += itemLst.get(i).size();
        }
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.group_recharge_and_consume,null);
            holder = new GroupViewHolder();
            holder.sort = (TextView) view.findViewById(R.id.tv_month);
            holder.divider = view.findViewById(R.id.divider);
            view.setTag(holder);
        } else {
            holder =  (GroupViewHolder) view.getTag();
        }
        holder.sort.setText(groupList.get(i));
        holder.divider.setVisibility(i==0 ? View.GONE : View.VISIBLE);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.item_recharge_and_consume,null);
            holder = new ItemViewHolder();
            holder.img_sort = (ImageView) view.findViewById(R.id.recharge_sort_image);
            holder.recharge_sort = (TextView) view.findViewById(R.id.recharge_sort_name);
            holder.tv_week = (TextView) view.findViewById(R.id.tv_week);
            holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_money_subscription = (TextView) view.findViewById(R.id.tv_money_subscription);
            view.setTag(holder);
        } else {
            holder = (ItemViewHolder) view.getTag();
        }
        RechargeAndConsumeBean bean = itemLst.get(i).get(i1);
        if (bean.type == 1 || bean.type == 2){
            holder.recharge_sort.setVisibility(View.VISIBLE);
            holder.img_sort.setVisibility(View.VISIBLE);
            holder.recharge_sort.setText(sort_text[bean.type-1]);
            holder.img_sort.setImageResource(sort_img[bean.type-1]);
        } else {
            holder.recharge_sort.setVisibility(View.GONE);
            holder.img_sort.setVisibility(View.GONE);
        }

        holder.tv_week.setText(bean.weekDay);
        holder.tv_time.setText(bean.createTime.substring(bean.createTime.length()-8,bean.createTime.length()-3));
        holder.tv_money.setText(DEL_OR_ADD[bean.isAdd]+bean.occurCost);
        holder.tv_money_subscription.setText(BALANCE[bean.isAdd]+bean.occurCost);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public class ItemViewHolder{
        public TextView tv_week,tv_time,tv_money,tv_money_subscription,recharge_sort;
        public ImageView img_sort;
    }

    public class GroupViewHolder{
        public TextView sort;
        public View divider;
    }
}

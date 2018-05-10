package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceBean;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by admin on 2018/5/7.
 */

public class ApplyInvoiceAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> groupList = new ArrayList<>();
    private List<List<ApplyInvoiceBean>> itemLst = new ArrayList<>();
    private List<List<Boolean>> stateList = new ArrayList<>();
    private OnItemClickListener listener;


    public ApplyInvoiceAdapter(Context context, List<String> groupList, List<List<ApplyInvoiceBean>> itemLst, OnItemClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.groupList = groupList;
        this.itemLst = itemLst;
        this.listener = listener;
        resetStateList(false);
    }

    private void resetStateList(boolean state){
        for(int i=0;i<itemLst.size();i++){
            stateList.add(new ArrayList<Boolean>());
            for(int j=0;j<itemLst.get(i).size();j++){
                stateList.get(i).add(state);
            }
        }
    }

    private void changeStateList(int group,int item,boolean state){
        stateList.get(group).set(item,state);
    }

    public void setDatas(List<String> groupList, List<List<ApplyInvoiceBean>> itemLst){
        this.groupList = groupList;
        this.itemLst = itemLst;
        resetStateList(false);
        notifyDataSetChanged();
    }

    public void setDatas(List<List<Boolean>> stateList){
        this.stateList = stateList;
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<ApplyInvoiceBean> list = itemLst.get(i);
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
        if (view == null) {
            view = inflater.inflate(R.layout.group_apply_invoice,null);
        }
        ((TextView) view).setText(groupList.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final ItemViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.item_apply_invoice,null);
            holder = new ItemViewHolder();
            holder.creatTime = (TextView) view.findViewById(R.id.charge_time);
            holder.select = (CheckBox) view.findViewById(R.id.cb_check);
            holder.pileNum = (TextView) view.findViewById(R.id.pile_num);
            holder.pileAdress = (TextView) view.findViewById(R.id.pile_adress);
            holder.money = (TextView) view.findViewById(R.id.money);
            holder.view = view.findViewById(R.id.divider);
            view.setTag(holder);
        } else {
            holder = (ItemViewHolder) view.getTag();
        }

        holder.view.setVisibility(i1==0 ? View.GONE :View.VISIBLE);
        holder.pileAdress.setText(itemLst.get(i).get(i1).pileAdress);
        holder.pileNum.setText(itemLst.get(i).get(i1).pileNum+"");
        holder.creatTime.setText(itemLst.get(i).get(i1).chargeTime);
        holder.money.setText(itemLst.get(i).get(i1).money);
        if (stateList.size()>i && stateList.get(i).size() >i1){
            holder.select.setChecked(stateList.get(i).get(i1));
        }
        holder.select.setOnCheckedChangeListener(new OnViewClickListener(i,i1,listener));
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public class ItemViewHolder{
        public TextView creatTime,pileNum,pileAdress,money;
        public CheckBox select;
        public View view;
    }

    public class OnViewClickListener implements CompoundButton.OnCheckedChangeListener{
        private int group,item;
        private OnItemClickListener listener;

        public OnViewClickListener(int group, int item,OnItemClickListener listener) {
            this.group = group;
            this.item = item;
            this.listener = listener;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (listener != null){
                listener.afterClick(group,item,b);
            }
            changeStateList(group,item,b);
        }
    }

    public void setAllCheckBoxChecked(boolean status){
        for(int i=0;i<stateList.size();i++){
            for(int j=0;j<stateList.get(i).size();j++){
                changeStateList(i,j,status);
            }
        }
        notifyDataSetChanged();
    }


    public interface OnItemClickListener{
        void afterClick(int indexOfGroup,int indexOfItem,boolean add);
    }
}
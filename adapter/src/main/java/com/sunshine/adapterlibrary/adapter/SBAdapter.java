package com.sunshine.adapterlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.sunshine.adapterlibrary.interfaces.BAdapter;
import com.sunshine.adapterlibrary.interfaces.Converter;
import com.sunshine.adapterlibrary.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView/GridView通用的Adapter
 * 没有position回参
 */
public class SBAdapter<T> extends BaseAdapter implements BAdapter<BaseAdapter> {
    private Context mContext;
    private List<? super T> list;
    protected LayoutInflater mInflater;
    private int mItemLayoutId;
    Converter<? super T> converter;

    public SBAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this(context, null);
    }

    public SBAdapter(Context context, List list) {
        this(context,list,new LinearLayout(context).getId());
    }

    public SBAdapter(Context context, int itemLayoutId) {
        this(context,null,itemLayoutId);
    }

    public SBAdapter(Context context, List list, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;

    }


    public SBAdapter<T> list(List list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        return this;
    }

    public SBAdapter<T> layout(int itemLayoutId) {
        this.mItemLayoutId = itemLayoutId;
        return this;
    }

    public SBAdapter<T> bindViewData(Converter<? super T> converter) {
        this.converter = converter;
        return this;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public List getList() {
        // TODO Auto-generated method stub
        return this.list;
    }

    @Override
    public void appendList(List list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        // TODO Auto-generated method stub
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public BaseAdapter getAdapter() {
        return this;
    }

    @Override
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void addList(List list2) {
        if (list2 == null) {
            list2 = new ArrayList<>();
        }
        // TODO Auto-generated method stub
        this.list.addAll(list2);
        notifyDataSetChanged();
    }

    @Override
    public BAdapter<BaseAdapter> addHeaderView(View headerView) {
        return this;
    }

    @Override
    public BAdapter<BaseAdapter> addFooterView(View footerView) {
        return this;
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return (T) this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        converter.convert(viewHolder, getItem(position));
        return viewHolder.getItemView();

    }


    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}

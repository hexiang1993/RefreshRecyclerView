package com.sunshine.adapterlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sunshine.adapterlibrary.viewholder.RViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView通用的Adapter
 * 用于自定义
 */
public abstract class RBaseAdapter<T> extends Adapter<RViewHolder> {
    private Context mContext;
    private List<T> list;
    protected LayoutInflater mInflater;
    private int mItemLayoutId;

    public RBaseAdapter(Context context) {
        this(context, null);
    }

    public RBaseAdapter(Context context, List list) {
        this(context, list, 0);
    }

    public RBaseAdapter(Context context, int itemLayoutId) {
        this(context, null, itemLayoutId);
    }

    public RBaseAdapter(Context context, List<T> list, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        this.list = list;

    }


    public void setitemLayoutId(int itemLayoutId) {
        this.mItemLayoutId = itemLayoutId;
    }

    public List<T> getList() {
        return this.list;
    }

    public void appendList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<T> list2) {
        this.list.addAll(list2);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    boolean hasHeader = false;
    boolean hasFooter = false;
    View headerView;
    View footerView;

    public void setHeaderView(View headerView) {
        hasHeader = true;
        this.headerView = headerView;
    }

    public void setFooterView(View footerView) {
        hasFooter = true;
        this.footerView = footerView;
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getFooterView() {
        return footerView;
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        if (hasHeader && position == 0) {
            return;
        } else if (hasFooter && position == (list.size() + (hasHeader ? 1 : 0))) {
            return;
        } else
            convert(holder, list.get(position));
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        if (hasHeader && position == 0) {
            return new RViewHolder(headerView);
        } else if (hasFooter && position == (list.size() + (hasHeader ? 1 : 0))) {
            return new RViewHolder(footerView);
        } else
            return RViewHolder.get(mContext, parent, mItemLayoutId, position);

    }

    //这里定义抽象方法，我们在匿名内部类实现的时候实现此方法来调用控件
    public abstract void convert(RViewHolder holder, T item);
}

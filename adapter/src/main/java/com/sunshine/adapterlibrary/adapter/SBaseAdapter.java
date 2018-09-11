package com.sunshine.adapterlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.sunshine.adapterlibrary.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的ListView/GridView的Adapter
 * 用于自定义
 */
public abstract class SBaseAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> list;
    protected LayoutInflater mInflater;
    private int mItemLayoutId;

    public SBaseAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this(context, null);
    }

    public SBaseAdapter(Context context, List list) {
        this(context, list, new LinearLayout(context).getId());
    }

    public SBaseAdapter(Context context, int itemLayoutId) {
        this(context, null, itemLayoutId);
    }

    public SBaseAdapter(Context context, List<T> list, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
    }


    public void setitemLayoutId(int itemLayoutId) {
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public List<T> getList() {
        return this.list;
    }

    public void appendList(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<T> list2) {
        if (list2 == null) {
            list2 = new ArrayList<>();
        }
        this.list.addAll(list2);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, (T) getItem(position));
        return viewHolder.getItemView();

    }

    public abstract void convert(ViewHolder holder, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}

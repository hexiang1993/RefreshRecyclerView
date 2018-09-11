package com.sunshine.adapterlibrary.interfaces;

import android.view.View;

import java.util.List;

/**
 * Created by xhe
 */
public interface BAdapter<T> {
    public List getList();

    public void appendList(List list);

    public T getAdapter();

    //刷新数据
    public void notifyDataChanged();

    public void addList(List list);

    public BAdapter<T> addHeaderView(View headerView);

    public BAdapter<T> addFooterView(View footerView);

}

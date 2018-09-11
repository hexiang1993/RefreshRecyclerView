package com.sunshine.adapterlibrary.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunshine.adapterlibrary.interfaces.Holder;

public class RViewHolder extends RecyclerView.ViewHolder implements Holder {

    private Context mContext;
    private View mConvertView;
    private SparseArray<View> mViews;

    public RViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        this.mViews = new SparseArray<View>();
    }

    public static RViewHolder get(Context context, ViewGroup parent, int layoutId, int position) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RViewHolder(view);
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @Override
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符�?
     *
     * @param viewId
     * @param text
     * @return
     */
    @Override
    public Holder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为View设置点击事件
     *
     * @param viewId
     * @param listener
     * @return
     */
    @Override
    public Holder setClick(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    @Override
    public Holder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    @Override
    public Holder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    @Override
    public View getItemView() {
        return mConvertView;
    }
}

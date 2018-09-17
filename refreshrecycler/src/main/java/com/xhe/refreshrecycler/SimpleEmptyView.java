package com.xhe.refreshrecycler;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhe.refreshrecycler.utils.DensityUtils;

/**
 * 普通的空布局
 * @author hexiang
 * @time 2018/9/3
 */

public class SimpleEmptyView extends RelativeLayout {

    private ImageView ivIcon;
    private TextView tvContent;
    private Context context;

    public SimpleEmptyView(Context context) {
        this(context, null);
    }

    public SimpleEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        ivIcon = new ImageView(context);
        tvContent = new TextView(context);
        tvContent.setGravity(Gravity.CENTER);
        tvContent.setTextSize(16);
        tvContent.setTextColor(0xff666666);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        linearLayout.addView(ivIcon, new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(DensityUtils.dp2px(context, 40), DensityUtils.dp2px(context, 15), DensityUtils.dp2px(context, 40), 0);
        linearLayout.addView(tvContent, textParams);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(linearLayout, params);
        tvContent.setText("暂无数据暂无数据暂无数据暂无数据暂无数据暂无数据暂无数据暂无数据");
        ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.img_no_message));
    }


    /**
     * 设置空布局的图标
     *
     * @param resId
     * @return
     */
    public SimpleEmptyView setLabelIcon(@DrawableRes int resId) {
        ivIcon.setImageResource(resId);
        return this;
    }

    /**
     * 设置图标大小
     * 默认wrap_content
     *
     * @param w
     * @param h
     * @return
     */
    public SimpleEmptyView setIconSize(int w, int h) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
        if (lp==null){
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        lp.width = DensityUtils.dp2px(context,w);
        lp.height = DensityUtils.dp2px(context,h);
        ivIcon.setLayoutParams(lp);
        return this;
    }

    /**
     * 设置空布局的文字
     *
     * @param str
     * @return
     */
    public SimpleEmptyView setLabelText(String str) {
        tvContent.setText(str == null ? "" : str);
        return this;
    }

    /**
     * 设置文字颜色值
     *
     * @param colorValue
     * @return
     */
    public SimpleEmptyView setTextColor(int colorValue) {
        tvContent.setTextColor(colorValue);
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param size
     * @return
     */
    public SimpleEmptyView setTextSize(int size) {
        tvContent.setTextSize(size);
        return this;
    }
}

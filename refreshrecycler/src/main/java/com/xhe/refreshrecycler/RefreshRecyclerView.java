package com.xhe.refreshrecycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sunshine.adapterlibrary.interfaces.BAdapter;
import com.xhe.refreshrecycler.interfaces.LoadmoreListener;
import com.xhe.refreshrecycler.interfaces.RefreshListener;
import com.xhe.refreshrecycler.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexiang
 * @time 2018/8/31
 */

public class RefreshRecyclerView extends RelativeLayout {
    //是否可以刷新
    private boolean enabledRefresh = true;
    //是否可以加载更多
    private boolean enabledLoadmore = false;
    //在数据为空的时候，是否展示空数据布局
    private boolean enabledShowEmpty = false;
    //在加载失败的时候是否展示失败布局
    private boolean enabledShowFailure = false;

    //是否正在加载更多
    private boolean isLoadmore = false;

    //没有数据展示的布局
    private RelativeLayout layoutEmpty;
    //数据获取失败展示的布局
    private RelativeLayout layoutFailure;
    //每页展示的数量
    private int pageCount = 10;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private RefreshListener refreshListener;
    private LoadmoreListener loadmoreListener;

    private LayoutParams LP_RL = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.base_refresh_layout_xhe);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.refresh_layout);
        enabledRefresh = typedArray.getBoolean(R.styleable.refresh_layout_enabled_refresh, true);
        enabledLoadmore = typedArray.getBoolean(R.styleable.refresh_layout_enabled_loadmore, false);
        enabledShowEmpty = typedArray.getBoolean(R.styleable.refresh_layout_enabled_showempty, false);
        enabledShowFailure = typedArray.getBoolean(R.styleable.refresh_layout_enabled_showfailure, false);
        enabledLoadmore = typedArray.getBoolean(R.styleable.refresh_layout_enabled_loadmore, false);
        pageCount = typedArray.getInteger(R.styleable.refresh_layout_page_count, 10);

        int columnNum = typedArray.getInteger(R.styleable.refresh_layout_column_num, 1);
        int layoutType = typedArray.getInteger(R.styleable.refresh_layout_rv_layout_manager, 0);

        setEnabledShowEmpty(enabledShowFailure);
        setEnabledShowFailure(enabledShowFailure);
        setLayoutManager(getDefaultLayoutManager(layoutType, columnNum));
        setLayoutFailure(typedArray.getResourceId(R.styleable.refresh_layout_layout_failure, R.layout.layout_bad_network111));
        setLayoutEmpty(typedArray.getResourceId(R.styleable.refresh_layout_layout_empty, R.layout.layout_empty_view111));

    }

    private void initView() {
        refreshLayout = new SmartRefreshLayout(context);
        addView(refreshLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recyclerView = new RecyclerView(context);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        refreshLayout.addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        layoutEmpty = new RelativeLayout(context);
        addView(layoutEmpty, LP_RL);
        layoutEmpty.setVisibility(GONE);

        layoutFailure = new RelativeLayout(context);
        addView(layoutFailure, LP_RL);
        layoutFailure.setVisibility(GONE);

    }

    private RecyclerView.LayoutManager getDefaultLayoutManager(int layoutManagerType, int coulmn) {
        switch (layoutManagerType) {
            case 1:
                return new GridLayoutManager(context, coulmn);
            case 2:
                return new StaggeredGridLayoutManager(coulmn, StaggeredGridLayoutManager.VERTICAL);
            default:
                return new LinearLayoutManager(context);
        }
    }


    /**
     * 设置布局管理器
     *
     * @param layoutManager
     * @return
     */
    public RefreshRecyclerView setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        if (adapter != null) {
            recyclerView.setAdapter((RecyclerView.Adapter) adapter.getAdapter());
        }
        return this;
    }

    /**
     * 设置RecyclerView的适配器
     *
     * @param adapter
     * @return
     */
    public RefreshRecyclerView setAdapter(BAdapter adapter) {
        this.adapter = adapter;
        this.recyclerView.setAdapter((RecyclerView.Adapter) adapter.getAdapter());
        return this;
    }

    /**
     * 设置每页显示的数量，默认10
     *
     * @param pageCount
     * @return
     */
    public RefreshRecyclerView setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    /**
     * 设置下拉刷新的头部view
     * 可设置默认头部：SmartRefreshLayout.setDefaultRefreshHeaderCreater
     * @param refreshHeader
     * @return
     */
    public RefreshRecyclerView setRefreshHeader(RefreshHeader refreshHeader){
        refreshLayout.setRefreshHeader(refreshHeader);
        return this;
    }

    /**
     * 设置加载更多的底部view
     * 可设置默认底部：SmartRefreshLayout.setDefaultRefreshFooterCreater
     * @param refreshFooter
     * @return
     */
    public RefreshRecyclerView setRefreshHeader(RefreshFooter refreshFooter){
        refreshLayout.setRefreshFooter(refreshFooter);
        return this;
    }


    /**
     * 是否可以下拉刷新
     *
     * @param enabledRefresh
     */
    public RefreshRecyclerView setEnabledRefresh(boolean enabledRefresh) {
        this.enabledRefresh = enabledRefresh;
        refreshLayout.setEnableRefresh(enabledRefresh);
        refreshLayout.setEnableHeaderTranslationContent(enabledRefresh);
        return this;
    }

    /**
     * 下拉刷新监听事件
     *
     * @param listener
     * @return
     */
    public RefreshRecyclerView setOnRefreshListener(@NonNull final RefreshListener listener) {
        refreshListener = listener;
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (layoutEmpty != null) {
                    layoutEmpty.setVisibility(GONE);
                }

                if (layoutFailure != null) {
                    layoutFailure.setVisibility(GONE);
                }

                listener.onRefrsh();
            }
        });

        return this;
    }


    /**
     * 是否可以上拉加载更多
     *
     * @param enabledLoadmore
     */
    public RefreshRecyclerView setEnabledLoadmore(boolean enabledLoadmore) {
        this.enabledLoadmore = enabledLoadmore;
        refreshLayout.setEnableLoadmore(enabledLoadmore);
        refreshLayout.setEnableFooterTranslationContent(enabledLoadmore);
        return this;
    }

    /**
     * 上拉加载监听事件
     *
     * @param listener
     * @return
     */
    public RefreshRecyclerView setOnLoadmoreListener(@NonNull final LoadmoreListener listener) {
        loadmoreListener = listener;
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                listener.loadMore();
            }
        });
        return this;
    }

    /**
     * 是否需要展示空数据布局
     *
     * @param showEmpty
     * @return
     */
    public RefreshRecyclerView setEnabledShowEmpty(boolean showEmpty) {
        enabledShowEmpty = showEmpty;
//        if (showEmpty) {
//            layoutEmpty = new RelativeLayout(context);
//            addView(layoutEmpty, LP_RL);
//            layoutEmpty.setVisibility(GONE);
//        } else {
//            if (layoutEmpty != null) {
//                removeView(layoutEmpty);
//            }
//        }
        return this;
    }


    /**
     * 空布局View
     *
     * @param view
     */
    public RefreshRecyclerView setLayoutEmpty(@NonNull View view) {
//        if (!enabledShowEmpty) {
//            return this;
//        }
        this.layoutEmpty.removeAllViews();
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        this.layoutEmpty.addView(view, lp);

        return this;
    }

    /**
     * 空布局layoutId
     *
     * @param viewId
     * @return
     */
    @SuppressLint("InflateParams")
    public RefreshRecyclerView setLayoutEmpty(@LayoutRes int viewId) {
        if (viewId != 0) {
            setLayoutEmpty(LayoutInflater.from(context).inflate(viewId, null));
        }

        return this;
    }


    /**
     * 是否需要展示失败布局
     *
     * @param showFailure
     * @return
     */
    public RefreshRecyclerView setEnabledShowFailure(boolean showFailure) {
        enabledShowFailure = showFailure;
//        if (showFailure) {
//            layoutFailure = new RelativeLayout(context);
//            addView(layoutFailure, LP_RL);
//            layoutFailure.setVisibility(GONE);
//        } else {
//            if (layoutFailure != null) {
//                removeView(layoutFailure);
//            }
//        }
        return this;
    }

    /**
     * 失败布局View
     *
     * @param view
     */
    public RefreshRecyclerView setLayoutFailure(@NonNull View view) {
//        if (!enabledShowFailure) {
//            return this;
//        }

        this.layoutFailure.removeAllViews();
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        View reload = view.findViewById(ResourceUtil.getId(context, "reload"));
        if (reload != null) {
            reload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutFailure.setVisibility(GONE);
                    doRefresh();
                }
            });
        }

        this.layoutFailure.addView(view, lp);
        return this;
    }

    /**
     * 失败布局layoutId
     *
     * @param viewId
     * @return
     */
    @SuppressLint("InflateParams")
    public RefreshRecyclerView setLayoutFailure(@LayoutRes int viewId) {
        if (viewId != 0) {
            setLayoutFailure(LayoutInflater.from(context).inflate(viewId, null));
        }
        return this;
    }


    /**
     * 获取列表数据
     *
     * @return
     */
    public List getList() {
        return this.adapter.getList();
    }

    /**
     * 获取列表适配器
     *
     * @return
     */
    public BAdapter getAdapter() {
        return this.adapter;
    }

    /**
     * 获取列表控件
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }


    /**
     * 手动调刷新
     */
    public void doRefresh() {
        refreshLayout.autoRefresh();
//        if (refreshListener != null) {
//            refreshListener.onRefrsh();
//        }
    }

    /**
     * 刷新完成后设置数据
     *
     * @param list
     * @param success 是否刷新成功
     */
    public void finishRefresh(List list, boolean success) {

        if (!success) {
            refreshLayout.finishRefresh(0,false);
            recyclerView.setVisibility(GONE);
            if (enabledShowFailure && layoutFailure != null) {
                layoutFailure.setVisibility(VISIBLE);
            }
            return;
        }
        if (layoutFailure != null) {
            layoutFailure.setVisibility(GONE);
        }
        if (null == list) {
            list = new ArrayList();
        }
        adapter.appendList(list);
        refreshLayout.finishRefresh(0,true);
        if (list.size() == 0) {
            recyclerView.setVisibility(GONE);
            if (enabledShowEmpty && layoutEmpty != null) {
                layoutEmpty.setVisibility(VISIBLE);
            }
        } else {
            recyclerView.setVisibility(VISIBLE);
            if (layoutEmpty != null) {
                layoutEmpty.setVisibility(GONE);
            }
            if (list.size() >= pageCount) {
                refreshLayout.setLoadmoreFinished(false);
            } else {
                refreshLayout.setLoadmoreFinished(true);
            }
        }
    }

    /**
     * 加载更多后设置数据
     *
     * @param list
     * @param success 是否加载成功
     */
    public void finishLoadmore(List list, boolean success) {
        if (!success) {
            refreshLayout.finishLoadmore(0,false);
            refreshLayout.setLoadmoreFinished(false);
            return;
        }
        if (null == list) {
            list = new ArrayList();
        }
        adapter.addList(list);
        refreshLayout.finishLoadmore(0,true);
        if (list.size() == 0) {
            refreshLayout.setLoadmoreFinished(false);
        } else {
            if (list.size() >= pageCount) {
                refreshLayout.setLoadmoreFinished(false);
            } else {
                refreshLayout.setLoadmoreFinished(true);
            }
        }
    }


}

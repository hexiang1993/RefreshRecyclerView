# RefreshRecyclerView

## 布局
```
<com.xhe.refreshrecycler.RefreshRecyclerView
        android:id="@+id/refresh"
        style="@style/base_refresh_layout_xhe"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

```
<style name="base_refresh_layout_xhe">
        <!-- Customize your theme here. -->
        <item name="enabled_refresh">false</item>
        <item name="enabled_loadmore">true</item>
        <item name="layout_failure">@layout/layout_bad_network111</item>
        <item name="layout_empty">@layout/layout_empty_view111</item>
        <item name="page_count">10</item><!--每页显示的数量-->
        <item name="column_num">1</item><!--列数-->
        <item name="rv_layout_manager">LinearLayoutManager</item>
    </style>
```


## 使用
```
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view111, null);
        TextView tvMsg = emptyView.findViewById(R.id.tv_msg);
        tvMsg.setText("重新设置空布局");
        refreshRv
                .setAdapter(new RBAdapter<String>(MainActivity.this)
                        .layout(R.layout.item_list)
                        .bindViewData(new Converter<String>() {
                            @Override
                            public void convert(Holder holder, String data) {
                                TextView textView = holder.getView(R.id.text);
                                textView.setText(data);

//                              holder.setText(R.id.text,data);
                            }
                        }))
                .setPageCount(10) //每页显示的数据个数
                .setLayoutEmpty(new SimpleEmptyView(this).setIconSize(100, 100).setTextSize(14)) //空数据布局
//                .setLayoutEmpty(emptyView)
//                .setLayoutFailure(R.layout.layout_bad_network111) //加载失败布局
                .setEnabledLoadmore(true) //是否可以上拉加载更多
                .setEnabledRefresh(true) //是否可以下拉刷新
                .setOnRefreshListener(new RefreshListener() { //下拉刷新监听事件
                    @Override
                    public void onRefrsh() {
                        count = 0;
                        load(true);
                    }
                })
                .setOnLoadmoreListener(new LoadmoreListener() { //上拉加载更多监听事件
                    @Override
                    public void loadMore() {
                        load(false);
                    }
                })
                .doRefresh(); //主动刷新


         //作用：结束刷新，并设置数据
         //参数一：list如果是空的就会展示空数据布局
         //参数二：刷新成功或者失败，false则会展示失败的布局
         refreshRv.finishRefresh(list, true);

         //作用：结束加载更多，并设置数据
         //根据list的size来控制能不能继续上拉加载更多
         refreshRv.finishLoadmore(list, true);

```


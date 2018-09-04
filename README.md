# RefreshRecyclerView

可以上拉加载，下拉刷新
空数据展示的布局
网络加载失败的布局

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
refreshRv
                .setAdapter(new RBAdapter<String>(this)
                        .layout(R.layout.item_list)
                        .bindViewData(new Converter<String>() {
                            @Override
                            public void convert(Holder holder, String item) {
                                holder.setText(R.id.text, item);
                            }
                        }))
                .setPageCount(10)
                .setLayoutEmpty(new SimpleEmptyView(this).setIconSize(100,100).setTextSize(14))
//                .setLayoutEmpty(emptyView)
//                .setLayoutFailure(R.layout.layout_bad_network111)
                .setEnabledLoadmore(true)
                .setEnabledRefresh(true)
                .setOnRefreshListener(new RefreshListener() {
                    @Override
                    public void onRefrsh() {
                        count = 0;
                        load(true);
                    }
                })
                .setOnLoadmoreListener(new LoadmoreListener() {
                    @Override
                    public void loadMore() {
                        load(false);
                    }
                })
                .doRefresh();
```


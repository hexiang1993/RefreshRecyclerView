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

## 在styles.xml中配置
```
    <style name="base_refresh_layout_xhe">
        <item name="enabled_refresh">false</item>
        <item name="enabled_loadmore">true</item>
        <item name="enabled_showempty">true</item>
        <item name="enabled_showfailure">true</item>
        <item name="layout_failure">@layout/layout_bad_network111</item>
        <item name="layout_empty">@layout/layout_empty_view111</item>
        <item name="page_count">10</item><!--每页显示的数量-->
        <item name="column_num">1</item><!--列数-->
        <item name="rv_layout_manager">LinearLayoutManager</item>
    </style>

```
## 在代码中动态配置

#### 1.设置每页展示数据的个数，默认10条
```
        refreshRv.setPageCount(20);
```

#### 2.设置是否需要在数据为空的时候展示空数据布局，默认不展示false
```
        refreshRv.setEnabledShowEmpty(true);
```

#### 3.设置空数据布局，有默认布局（R.layout.layout_empty_view111），也可根据自己需求设置【注：在配置空布局权限的前提下，配置此项才生效】
```
        //SimpleEmptyView 是库中自定义的view，可以参照此自己定义自己需要的view，也可以直接设置layout布局
        refreshRv.setLayoutEmpty(new SimpleEmptyView(this).setIconSize(100, 100).setTextSize(14));
        refreshRv.setLayoutEmpty(emptyView);
        refreshRv.setLayoutEmpty(R.layout.layout_empty_view111);
```

#### 4.设置是否需要在数据获取失败的时候展示失败布局，默认不展示false
```
        refreshRv.setEnabledShowFailure(true);
```

#### 5.设置失败布局，有默认布局（R.layout.layout_bad_network111），也可根据自己需求设置【注：在配置失败布局权限的前提下，配置此项才生效】
```
        refreshRv.setLayoutFailure(failureView);
        refreshRv.setLayoutFailure(R.layout.layout_bad_network111);
```

#### 6.设置是否可以下拉刷新，默认开启true
```
        refreshRv.setEnabledRefresh(true);
        //下拉刷新监听事件
        refreshRv.setOnRefreshListener(new RefreshListener() {
                                     @Override
                                     public void onRefrsh() {
                                         count = 0;
                                         load(true);
                                     }
                                 })

```


#### 7.设置是否可以上拉加载更多，默认关闭false
```
        refreshRv.setEnabledLoadmore(true);
        //上拉加载更多监听事件
        refreshRv.setOnLoadmoreListener(new LoadmoreListener() {
                                     @Override
                                     public void loadMore() {
                                         load(false);
                                     }
                                 })
```

#### 8.设置适配器，一般的都可以使用万能适配器，如RBAdapter，RPAdapter(有position回参)
```
        refreshRv.setAdapter(new RBAdapter<String>(MainActivity.this)
                        .layout(R.layout.item_list)
                        .bindViewData(new Converter<String>() {
                            @Override
                            public void convert(Holder holder, String data) {
                                TextView textView = holder.getView(R.id.text);
                                textView.setText(data);

//                              holder.setText(R.id.text,data);
                            }
                        }))

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
                .setEnabledShowEmpty(true)
                .setLayoutEmpty(new SimpleEmptyView(this).setIconSize(100, 100).setTextSize(14)) //空数据布局
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


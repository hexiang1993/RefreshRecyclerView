package com.xhe.refreshrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sunshine.adapterlibrary.adapter.RPAdapter;
import com.sunshine.adapterlibrary.adapter.SBAdapter;
import com.sunshine.adapterlibrary.adapter.SPAdapter;
import com.sunshine.adapterlibrary.interfaces.Converter;
import com.sunshine.adapterlibrary.interfaces.Holder;
import com.sunshine.adapterlibrary.interfaces.PConverter;
import com.xhe.refreshrecycler.RefreshRecyclerView;
import com.xhe.refreshrecycler.SimpleEmptyView;
import com.xhe.refreshrecycler.interfaces.LoadmoreListener;
import com.xhe.refreshrecycler.interfaces.RefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshRecyclerView refreshRv;
    private ListView listView;
    private List<String> listData = new ArrayList<>();
    int count = 0;
    private SPAdapter<String> adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        refreshRv = findViewById(R.id.refresh);
        listView = findViewById(R.id.listview);
        adapter = new SPAdapter<>(context);
        listView.setAdapter(adapter
                .layout(R.layout.item_list)
                .list(listData)
                .bindPositionData(new PConverter<String>() {
                    @Override
                    public void convert(Holder holder, String data, int pos) {
                        TextView textView = holder.getView(R.id.text);

                        if (pos > 8 && pos < 18) {

                        } else {
                            textView.setText(data);
                        }
                    }
                }));

        //如果不需要position信息，可以直接使用SBAdapter
        listView.setAdapter(new SBAdapter<String>(context)
                .layout(R.layout.item_list)
                .list(listData)
                .bindViewData(new Converter<String>() {
                    @Override
                    public void convert(Holder holder, String data) {
                        TextView textView = holder.getView(R.id.text);
                        textView.setText(data);

                        ImageView imageView = holder.getView(R.id.imageview);
                        imageView.setImageResource(R.drawable.message_default);
                    }
                }));

        //如果需要position信息，使用SPAdapter，回调中会返回position
        listView.setAdapter(new SPAdapter<String>(context)
                .layout(R.layout.item_list)
                .list(listData)
                .bindPositionData(new PConverter<String>() {
                    @Override
                    public void convert(Holder holder, String data, int pos) {
                        //单纯的给view设置文字图片，可以使用以下简单方式
                        holder.setText(R.id.text, data);
                        holder.setImageResource(R.id.imageview, R.drawable.message_default);
                    }
                }));


        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view111, null);
        TextView tvMsg = emptyView.findViewById(R.id.tv_msg);
        tvMsg.setText("重新设置空布局");

        refreshRv
                .setAdapter(new RPAdapter<String>(context)
                        .layout(R.layout.item_list)
                        .bindPositionData(new PConverter<String>() {
                            @Override
                            public void convert(Holder holder, String data, int pos) {
                                TextView textView = holder.getView(R.id.text);

                                if (pos > 10 && pos < 18) {

                                } else {
                                    textView.setText(data);
                                }
                            }
                        }))
                .setPageCount(10)
                .setEnabledShowEmpty(false)
                .setEnabledShowFailure(false)
                .setLayoutEmpty(R.layout.layout_empty_view111)
                .setLayoutEmpty(new SimpleEmptyView(this).setIconSize(100, 100).setTextSize(14))
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
    }

    private void load(final boolean isrefresh) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    if (isrefresh)
                        Thread.sleep(1000);
                    else
                        Thread.sleep(500);
                } catch (Exception e) {

                }
                Log.e("isrefresh", "isrefresh");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList();
                        if (count == 3) {
                            for (int i = count * 10; i < (count + 1) * 10 - 2; i++) {
                                list.add(" 数据 ---- " + i);
                            }
                        } else {
                            for (int i = count * 10; i < (count + 1) * 10; i++) {
                                list.add(" 数据 ---- " + i);
                            }
                        }


                        if (isrefresh) {
                            adapter.appendList(list);
                            refreshRv.finishRefresh(list, true);
                        } else {
                            if (count == 2) {
                                refreshRv.finishLoadmore(null, false);
                            } else {
                                adapter.addList(list);
                                refreshRv.finishLoadmore(list, true);
                            }
                        }


                        count++;
                    }
                });

            }
        }).start();
    }
}

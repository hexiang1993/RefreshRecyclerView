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
    private int refreshCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        refreshRv = (RefreshRecyclerView) findViewById(R.id.refresh);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new SPAdapter<>(this);
        adapter.notifyDataChanged();
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
                        .list(listData)
                        .bindPositionData(new PConverter<String>() {
                            @Override
                            public void convert(Holder holder, String data, int pos) {
                                TextView textView = holder.getView(R.id.text);
                                ImageView imageView = holder.getView(R.id.imageview);

                                textView.setText(data);
                                imageView.setImageResource(R.drawable.message_default);

                                //单纯的给view设置文字图片，可以使用以下简单方式
                                holder.setText(R.id.text, data);
                                holder.setImageResource(R.id.imageview, R.drawable.message_default);

                                if (pos > 10 && pos < 18) {

                                } else {
                                    textView.setText(data);
                                }
                            }
                        }))
                .setPageCount(15)
                .setEnabledShowEmpty(true)
                .setEnabledShowFailure(true)
//                .setLayoutEmpty(new SimpleEmptyView(this).setIconSize(100, 100).setLabelText("暂无数据").setTextSize(14))
//                .setLayoutEmpty(R.layout.layout_empty_view111)
//                .setLayoutEmpty(emptyView)
                .setLayoutFailure(R.layout.layout_bad_network111)
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
                    if (isrefresh) {
                        refreshCount++;
                        Thread.sleep(1000);
                    } else {
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("isrefresh", "isrefresh");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList();
                        for (int i = count * 15; i < (count + 1) * 15; i++) {
                            list.add(" 数据 ---- " + i);
                        }


                        if (isrefresh) {
                            if (refreshCount == 5) {
                                refreshRv.finishRefresh(null, true);
                            } else if (refreshCount == 3) {
                                refreshRv.finishRefresh(null, false);
                            } else {
                                refreshRv.finishRefresh(list, true);
                            }
                        } else {
                            if (refreshCount == 2) {
                                refreshRv.finishLoadmore(null, false);
                            } else {
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

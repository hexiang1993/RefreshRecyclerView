package com.xhe.refreshrecyclerview;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gengqiquan.adapter.adapter.RBAdapter;
import com.gengqiquan.adapter.interfaces.Converter;
import com.gengqiquan.adapter.interfaces.Holder;
import com.xhe.refreshrecycler.RefreshRecyclerView;
import com.xhe.refreshrecycler.SimpleEmptyView;
import com.xhe.refreshrecycler.interfaces.LoadmoreListener;
import com.xhe.refreshrecycler.interfaces.RefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshRecyclerView refreshRv;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshRv = findViewById(R.id.refresh);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view111,null);
        TextView tvMsg = emptyView.findViewById(R.id.tv_msg);
        tvMsg.setText("重新设置空布局");
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
                            refreshRv.finishRefresh(list, true);
                        } else {
                            if (count==2){
                                refreshRv.finishLoadmore(null,false);
                            }else {
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

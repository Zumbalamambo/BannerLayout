package com.bannersimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.recycler.listener.OnRecyclerBannerClickListener;
import com.recycler.model.RecyclerBannerModel;
import com.recycler.widget.RecyclerBannerLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MainAdapter());

        final RecyclerBannerLayout recyclerBannerLayout = new RecyclerBannerLayout(getApplicationContext());
        recyclerBannerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        recyclerBannerLayout
                .initListResources(initSystemNetWorkModel())
                .initTips(true, true, true)
                .setOnRecyclerBannerClickListener(new OnRecyclerBannerClickListener() {
                    @Override
                    public void onBannerClick(View view, int position, Object model) {
                        if (recyclerBannerLayout.getRecyclerBannerStatus() == -1) {
                            Toast.makeText(view.getContext(), "开始轮播", Toast.LENGTH_SHORT).show();
                            recyclerBannerLayout.restoreRecyclerBanner();
                        } else {
                            recyclerBannerLayout.stopRecyclerBanner();
                            Toast.makeText(view.getContext(), "暂停轮播", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start(true);
        setContentView(recyclerBannerLayout);
    }


    /**
     * Comes with the Model class, the use of network data
     */
    private List<RecyclerBannerModel> initSystemNetWorkModel() {
        List<RecyclerBannerModel> mDatas = new ArrayList<>();
        mDatas.add(new RecyclerBannerModel("http://ww2.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6kxwh0j30dw099ta3.jpg", "At that time just love, this time to break up"));
        mDatas.add(new RecyclerBannerModel("error image test", "Shame it ~"));
        mDatas.add(new RecyclerBannerModel("http://ww1.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6f7f26j30dw0ii76k.jpg", "The legs are not long but thin"));
        mDatas.add(new RecyclerBannerModel("http://ww4.sinaimg.cn/bmiddle/0060lm7Tgw1f94c63dfjxj30dw0hjjtn.jpg", "Late at night"));
        return mDatas;
    }

}

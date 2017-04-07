package com.bannersimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.recycler.listener.OnRecyclerBannerClickListener;
import com.recycler.model.RecyclerBannerModel;
import com.recycler.widget.RecyclerBannerLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerBannerLayout recyclerBannerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MainAdapter());
        setContentView(R.layout.activity_main);

        recyclerBannerLayout = (RecyclerBannerLayout) findViewById(R.id.banner);
//        recyclerBannerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        recyclerBannerLayout
                .setTipsSite(RecyclerBannerLayout.TOP)
                .initListResources(initSystemNetWorkModel())
                .initTips(true, true, true)
                .setPageNumViewSite(RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_BOTTOM_CENTER)
                .initPageNumView()
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
    }

    private List<RecyclerBannerModel> updateData() {
        List<RecyclerBannerModel> mDatas = new ArrayList<>();
        mDatas.add(new RecyclerBannerModel("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491588490192&di=8b910d6222c7c24f4ae03b381ab7ac67&imgtype=0&src=http%3A%2F%2Fmvimg2.meitudata.com%2F55c06894af3767451.jpg", "banner 1"));
        mDatas.add(new RecyclerBannerModel("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491588490192&di=ea09b4082ebf1916cc6586929900d892&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01b2a856f1ef166ac7257d207d8a1a.jpg%40900w_1l_2o_100sh.jpg", "banner 2"));
        mDatas.add(new RecyclerBannerModel("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491588490192&di=1308ea437fb1e2b488c40813938ceee3&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01445b56f1ef176ac7257d207ce87d.jpg%40900w_1l_2o_100sh.jpg", "banner 3"));
        mDatas.add(new RecyclerBannerModel("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491588490192&di=c7c9dfd2fc4b1eeb5a4a874ec9a30d1d&imgtype=0&src=http%3A%2F%2Fmvimg2.meitudata.com%2F55713dd0165c89055.jpg", "banner 4"));
        mDatas.add(new RecyclerBannerModel("http://ww2.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6kxwh0j30dw099ta3.jpg", "At that time just love, this time to break up"));
        mDatas.add(new RecyclerBannerModel("error image test", "Shame it ~"));
        return mDatas;
    }

    private List<RecyclerBannerModel> initSystemNetWorkModel() {
        List<RecyclerBannerModel> mDatas = new ArrayList<>();
        mDatas.add(new RecyclerBannerModel("http://ww2.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6kxwh0j30dw099ta3.jpg", "At that time just love, this time to break up"));
        mDatas.add(new RecyclerBannerModel("error image test", "Shame it ~"));
        mDatas.add(new RecyclerBannerModel("http://ww1.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6f7f26j30dw0ii76k.jpg", "The legs are not long but thin"));
        mDatas.add(new RecyclerBannerModel("http://ww4.sinaimg.cn/bmiddle/0060lm7Tgw1f94c63dfjxj30dw0hjjtn.jpg", "Late at night"));
        return mDatas;
    }

}

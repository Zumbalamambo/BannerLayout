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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MainAdapter());
        setContentView(R.layout.activity_main);

        RecyclerBannerLayout recyclerBannerLayout = (RecyclerBannerLayout) findViewById(R.id.banner);
//        recyclerBannerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        int margin = 16;
        recyclerBannerLayout
//                .setTipsSite(RecyclerBannerLayout.TOP)
//                .isVertical(true)
                .initListResources(initSystemNetWorkModel())
//                .setTitleSetting(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), 22)
//                .setTipsBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
//                .initTipsDotsSelector(R.drawable.banner)
//                .setTipsWidthAndHeight(RecyclerBannerLayout.MATCH_PARENT, 100)
//                .setTitleMargin(40, 20)
//                .setDotsWidthAndHeight(30, 30)
//                .setDotsMargin(30, 30)
//                .setPageNumViewRadius(5)
//                .setPageNumViewTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
//                .setPageNumViewBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
//                .setPageNumViewTextSize(22)
//                .setPageNumViewMark(" & ")
                .setCardMargin(margin, margin, margin, margin)
                .setCardRadius(16)
                .setPageNumViewMargin(margin, 26, margin, 26)
//                .initTips(true, true, true)
                .setPageNumViewSite(RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_BOTTOM_RIGHT)
                .initPageNumView()
                .setOnRecyclerBannerClickListener(new OnRecyclerBannerClickListener<RecyclerBannerModel>() {
                    @Override
                    public void onBannerClick(View view, int position, RecyclerBannerModel model) {
                        Toast.makeText(view.getContext(), "banner click", Toast.LENGTH_SHORT).show();
                    }
                }).start(true, 4000);
    }

    private List<RecyclerBannerModel> initSystemNetWorkModel() {
        List<RecyclerBannerModel> mDatas = new ArrayList<>();
        mDatas.add(new RecyclerBannerModel("http://ww2.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6kxwh0j30dw099ta3.jpg", "At that time just love, this time to break up"));
        mDatas.add(new RecyclerBannerModel("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491588490192&di=c7c9dfd2fc4b1eeb5a4a874ec9a30d1d&imgtype=0&src=http%3A%2F%2Fmvimg2.meitudata.com%2F55713dd0165c89055.jpg", "Shame it ~"));
        mDatas.add(new RecyclerBannerModel("http://ww1.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6f7f26j30dw0ii76k.jpg", "The legs are not long but thin"));
        mDatas.add(new RecyclerBannerModel("http://ww4.sinaimg.cn/bmiddle/0060lm7Tgw1f94c63dfjxj30dw0hjjtn.jpg", "Late at night"));
        return mDatas;
    }

}

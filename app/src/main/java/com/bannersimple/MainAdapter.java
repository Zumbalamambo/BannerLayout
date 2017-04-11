package com.bannersimple;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bannersimple.model.SimpleRecyclerBannerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 2016/11/14
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.BaseViewHolder> {
    private static final int IMAGE_MODEL = 0;
    private static final int SYSTEM_NETWORK_MODEL = 1;
    private static final int IMAGE_LOADER_MANAGER = 2;
    private static final int IS_VERTICAL = 3;

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {

            case IMAGE_MODEL:

                holder.getTitle().setText(getString(holder.getContext(), R.string.image_holder));
                break;
            case SYSTEM_NETWORK_MODEL:

                holder.getTitle().setText(getString(holder.getContext(), R.string.system_network_model));
                break;
            case IS_VERTICAL:

                holder.getTitle().setText(getString(holder.getContext(), R.string.is_vertical));
                break;
            default:

                holder.getTitle().setText(getString(holder.getContext(), R.string.customize_load_Picture_Manager));
                break;
        }
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return new BaseViewHolder(linearLayout);
    }


    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return IMAGE_MODEL;
            case 1:
                return SYSTEM_NETWORK_MODEL;
            case 2:
                return IMAGE_LOADER_MANAGER;
            default:
                return IS_VERTICAL;
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private LinearLayout rootView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            rootView = (LinearLayout) itemView;
            title = new TextView(rootView.getContext());
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorBackground));
            title.setTextSize(14);
            title.setPadding(10, 10, 10, 10);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 450);

            rootView.addView(title);
        }

        public TextView getTitle() {
            return title;
        }


        public Context getContext() {
            return itemView.getContext();
        }
    }

    /**
     * Comes with the Model class, the use of network data
     */
    private List<SimpleRecyclerBannerModel> initModel() {
        List<SimpleRecyclerBannerModel> mDatas = new ArrayList<>();
        mDatas.add(new SimpleRecyclerBannerModel("http://ww2.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6kxwh0j30dw099ta3.jpg", "At that time just love, this time to break up"));
        mDatas.add(new SimpleRecyclerBannerModel("http://ww4.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6qyhzgj30dw07t75g.jpg", "Shame it ~"));
        mDatas.add(new SimpleRecyclerBannerModel("http://ww1.sinaimg.cn/bmiddle/0060lm7Tgw1f94c6f7f26j30dw0ii76k.jpg", "The legs are not long but thin"));
        mDatas.add(new SimpleRecyclerBannerModel("http://ww4.sinaimg.cn/bmiddle/0060lm7Tgw1f94c63dfjxj30dw0hjjtn.jpg", "Late at night"));
        return mDatas;
    }

    private String getString(Context context, int i) {
        return context.getString(i);
    }
}
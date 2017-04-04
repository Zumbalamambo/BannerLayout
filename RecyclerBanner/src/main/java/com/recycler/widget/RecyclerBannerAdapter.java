package com.recycler.widget;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.recycler.listener.OnRecyclerBannerClickListener;
import com.recycler.listener.RecyclerBannerImageLoaderManager;
import com.recycler.model.RecyclerBannerModel;

import java.util.List;

/**
 * by y on 2017/4/4.
 */

class RecyclerBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<? extends RecyclerBannerModel> mDatas = null;
    private RecyclerBannerImageLoaderManager imageLoaderManager = null;
    private OnRecyclerBannerClickListener clickListener = null;
    private int error_image;
    private int place_image;

    void setImageLoaderManager(RecyclerBannerImageLoaderManager imageLoaderManager) {
        this.imageLoaderManager = imageLoaderManager;
    }

    void setClickListener(OnRecyclerBannerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void setErrorImage(int error_image) {
        this.error_image = error_image;
    }

    void setPlaceImage(int place_image) {
        this.place_image = place_image;
    }

    RecyclerBannerAdapter(List<? extends RecyclerBannerModel> imageList) {
        this.mDatas = imageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatImageView imageView = new AppCompatImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new RecyclerView.ViewHolder(imageView) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mDatas == null) {
            return;
        }

        final int pos = position % mDatas.size();

        if (imageLoaderManager == null) {
            Glide
                    .with(holder.itemView.getContext())
                    .load(mDatas.get(pos).getImage())
                    .placeholder(place_image)
                    .error(error_image)
                    .centerCrop()
                    .into((ImageView) holder.itemView);
        } else {
            //noinspection unchecked
            imageLoaderManager.display((ImageView) holder.itemView, mDatas.get(pos));
        }

        if (clickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //noinspection unchecked
                    clickListener.onBannerClick(v, pos, mDatas.get(pos));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : Integer.MAX_VALUE;
    }
}

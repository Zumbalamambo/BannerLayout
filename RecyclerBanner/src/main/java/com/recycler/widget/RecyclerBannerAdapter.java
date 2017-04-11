package com.recycler.widget;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.recycler.R;
import com.recycler.listener.OnRecyclerBannerClickListener;
import com.recycler.listener.RecyclerBannerImageLoaderManager;
import com.recycler.listener.RecyclerBannerModelCallBack;

import java.util.List;

/**
 * by y on 2017/4/4.
 */

class RecyclerBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<? extends RecyclerBannerModelCallBack> mDatas = null;
    private RecyclerBannerImageLoaderManager imageLoaderManager = null;
    private OnRecyclerBannerClickListener clickListener = null;
    private CardViewInterface cardViewInterface = null;
    private int error_image;
    private int place_image;

    void setCardViewInterface(CardViewInterface cardViewInterface) {
        this.cardViewInterface = cardViewInterface;
    }

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

    RecyclerBannerAdapter(List<? extends RecyclerBannerModelCallBack> imageList) {
        this.mDatas = imageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
        CardView cardView = new CardView(parent.getContext());

        AppCompatImageView imageView = new AppCompatImageView(parent.getContext());
        imageView.setId(R.id.banner_image);
        if (cardViewInterface != null) {
            layoutParams.setMargins(
                    cardViewInterface.getCardLeftMargin(),
                    cardViewInterface.getCardTopMargin(),
                    cardViewInterface.getCardRightMargin(),
                    cardViewInterface.getCardBottomMargin());
            cardView.setRadius(cardViewInterface.getCardRadius());
        }
        cardView.setLayoutParams(layoutParams);
        cardView.addView(imageView);
        return new RecyclerView.ViewHolder(cardView) {
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
                    .load(mDatas.get(pos).getRecyclerBannerImageUrl())
                    .placeholder(place_image)
                    .error(error_image)
                    .centerCrop()
                    .into((ImageView) holder.itemView.findViewById(R.id.banner_image));
        } else {
            //noinspection unchecked
            imageLoaderManager.display((ImageView) holder.itemView.findViewById(R.id.banner_image), mDatas.get(pos));
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

    public interface CardViewInterface {

        float getCardRadius();

        int getCardBottomMargin();

        int getCardRightMargin();

        int getCardLeftMargin();

        int getCardTopMargin();
    }
}

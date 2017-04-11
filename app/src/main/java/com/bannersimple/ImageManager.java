package com.bannersimple;

import android.widget.ImageView;

import com.bannersimple.model.SimpleRecyclerBannerModel;
import com.recycler.listener.RecyclerBannerImageLoaderManager;
import com.squareup.picasso.Picasso;

/**
 * by y on 2016/12/2
 */

public class ImageManager implements RecyclerBannerImageLoaderManager<SimpleRecyclerBannerModel> {

    @Override
    public void display(ImageView imageView, SimpleRecyclerBannerModel model) {
        Picasso.with(imageView.getContext())
                .load((String) model.getRecyclerBannerImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
}

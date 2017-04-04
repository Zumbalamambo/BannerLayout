package com.bannersimple;

import android.widget.ImageView;

import com.recycler.listener.RecyclerBannerImageLoaderManager;
import com.bannersimple.bean.BannerBean;
import com.squareup.picasso.Picasso;

/**
 * by y on 2016/12/2
 */

public class ImageManager implements RecyclerBannerImageLoaderManager<BannerBean> {

    @Override
    public void display(ImageView imageView, BannerBean model) {
        Picasso.with(imageView.getContext())
                .load(model.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
}

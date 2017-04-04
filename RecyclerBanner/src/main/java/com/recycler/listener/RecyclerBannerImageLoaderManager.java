package com.recycler.listener;

import android.widget.ImageView;

/**
 * by y on 2016/10/27
 */

public interface RecyclerBannerImageLoaderManager<T> {
    void display(ImageView imageView, T model);
}

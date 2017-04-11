package com.bannersimple.model;

import com.recycler.listener.RecyclerBannerModelCallBack;

/**
 * by y on 2016/10/24
 */

public class SimpleRecyclerBannerModel implements RecyclerBannerModelCallBack {
    private Object image;
    private String title;

    public SimpleRecyclerBannerModel() {
    }

    public SimpleRecyclerBannerModel(Object image) {
        this.image = image;
    }

    public SimpleRecyclerBannerModel(Object image, String title) {
        this.image = image;
        this.title = title;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Object getRecyclerBannerImageUrl() {
        return getImage();
    }

    @Override
    public String getRecyclerBannerTitle() {
        return getTitle();
    }
}

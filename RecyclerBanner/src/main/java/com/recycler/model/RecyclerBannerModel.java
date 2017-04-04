package com.recycler.model;

/**
 * by y on 2016/10/24
 */

public class RecyclerBannerModel {
    private Object image;
    private String title;

    public RecyclerBannerModel() {
    }

    public RecyclerBannerModel(Object image) {
        this.image = image;
    }

    public RecyclerBannerModel(Object image, String title) {
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

}

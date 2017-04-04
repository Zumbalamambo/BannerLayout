package com.bannersimple.bean;

import com.recycler.model.RecyclerBannerModel;

/**
 * by y on 2016/12/2
 */

public class BannerBean extends RecyclerBannerModel {

    private String imageUrl;
    private String thisTitle;

    public BannerBean(String imageUrl, String thisTitle) {
        this.imageUrl = imageUrl;
        this.thisTitle = thisTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThisTitle() {
        return thisTitle;
    }

    public void setThisTitle(String thisTitle) {
        this.thisTitle = thisTitle;
    }
}

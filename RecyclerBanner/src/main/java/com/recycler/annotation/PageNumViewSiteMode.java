package com.recycler.annotation;

import android.support.annotation.IntDef;

import com.recycler.widget.RecyclerBannerLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by y on 2017/1/19.
 */
@IntDef({RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_TOP_LEFT,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_TOP_RIGHT,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_BOTTOM_LEFT,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_BOTTOM_RIGHT,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_CENTER_LEFT,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_CENTER_RIGHT,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_TOP_CENTER,
        RecyclerBannerLayout.PAGE_NUM_VIEW_SITE_BOTTOM_CENTER})
@Retention(RetentionPolicy.SOURCE)
public @interface PageNumViewSiteMode {
}


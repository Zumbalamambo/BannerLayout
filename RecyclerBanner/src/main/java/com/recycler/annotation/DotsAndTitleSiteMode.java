package com.recycler.annotation;

import android.support.annotation.IntDef;

import com.recycler.widget.RecyclerBannerLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by y on 2017/1/19.
 */

@IntDef({RecyclerBannerLayout.CENTER,
        RecyclerBannerLayout.LEFT,
        RecyclerBannerLayout.RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface DotsAndTitleSiteMode {
}


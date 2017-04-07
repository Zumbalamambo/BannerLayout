package com.recycler.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.recycler.R;
import com.recycler.annotation.DotsAndTitleSiteMode;
import com.recycler.annotation.PageNumViewSiteMode;
import com.recycler.annotation.TipsSiteMode;
import com.recycler.exception.BannerException;
import com.recycler.listener.OnRecyclerBannerClickListener;
import com.recycler.listener.OnRecyclerBannerTitleListener;
import com.recycler.listener.RecyclerBannerImageLoaderManager;
import com.recycler.listener.RecyclerSelectItem;
import com.recycler.model.RecyclerBannerModel;
import com.recycler.util.RecyclerBannerHandlerUtils;
import com.recycler.util.RecyclerBannerSelectorUtils;
import com.recycler.widget.RecyclerBannerTipsLayout.DotsInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * by y on 2016/10/24
 */

public class RecyclerBannerLayout extends FrameLayout
        implements
        DotsInterface,
        RecyclerBannerTipsLayout.TitleInterface,
        RecyclerBannerTipsLayout.TipsInterface,
        RecyclerBannerPageView.PageNumViewInterface, RecyclerSelectItem {

    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * pageNumberView site type
     */
    public static final int PAGE_NUM_VIEW_SITE_TOP_LEFT = 0;
    public static final int PAGE_NUM_VIEW_SITE_TOP_RIGHT = 1;
    public static final int PAGE_NUM_VIEW_SITE_BOTTOM_LEFT = 2;
    public static final int PAGE_NUM_VIEW_SITE_BOTTOM_RIGHT = 3;
    public static final int PAGE_NUM_VIEW_SITE_CENTER_LEFT = 4;
    public static final int PAGE_NUM_VIEW_SITE_CENTER_RIGHT = 5;
    public static final int PAGE_NUM_VIEW_SITE_TOP_CENTER = 6;
    public static final int PAGE_NUM_VIEW_SITE_BOTTOM_CENTER = 7;
    /**
     * tipsLayout location marker
     */
    public static final int LEFT = 9;
    public static final int TOP = 10;
    public static final int RIGHT = 11;
    public static final int BOTTOM = 12;
    public static final int CENTER = 13;

    private OnRecyclerBannerClickListener onRecyclerBannerClickListener = null;
    private List<? extends RecyclerBannerModel> imageList = null;
    private RecyclerBannerHandlerUtils recyclerBannerHandlerUtils = null;
    private RecyclerBannerTipsLayout recyclerBannerTipLayout = null;
    private RecyclerBannerImageLoaderManager recyclerImageLoaderManage = null; //Image Load Manager
    private OnRecyclerBannerTitleListener onRecyclerBannerTitleListener = null;
    private RecyclerBannerPageView pageView = null; // viewpager page count textView
    private RecyclerBannerAdapter recyclerAdapter = null;
    private RecyclerView recyclerView = null;

    private boolean isStartRotation;//Whether auto rotation is enabled or not is not enabled by default
    private boolean isTipsBackground;//Whether to display a  dots background
    private int errorImageView;//Glide Load error placeholder
    private int placeImageView;//Placeholder in glide loading
    private boolean isVertical;//Whether the vertical sliding ,The default is not

    private boolean isVisibleDots;//Whether to display the dots default display
    private int dotsWidth;// dots width
    private int dotsHeight;// dots height
    private int dotsSelector; //Dots State Selector
    private long delayTime; //Rotation time
    private int dotsLeftMargin; //The dots are marginLeft
    private int dotsRightMargin;//The dots are marginRight
    private int dotsSite;
    private float enabledRadius;
    private float normalRadius;
    private int enabledColor; //dots enabledColor
    private int normalColor;//dots normalColor

    private boolean isVisibleTitle;//Whether to display the title default is not displayed
    private float titleSize;//font size
    private int titleColor;//font color
    private int titleLeftMargin;//title marginLeft
    private int titleRightMargin;//title marginRight
    private int titleWidth;//title width
    private int titleHeight;// title height
    private int titleSite;

    private int tipsLayoutHeight; //BannerTipsLayout height
    private int tipsLayoutWidth; // BannerTipsLayout width
    private int tipsBackgroundColor; //BannerTipsLayout BackgroundColor
    private int tipsSite;

    private float pageNumViewRadius;
    private int pageNumViewPaddingTop;
    private int pageNumViewPaddingLeft;
    private int pageNumViewPaddingBottom;
    private int pageNumViewPaddingRight;
    private int pageNumViewTopMargin;
    private int pageNumViewRightMargin;
    private int pageNumViewBottomMargin;
    private int pageNumViewLeftMargin;
    private int pageNumViewSite;
    private int pageNumViewTextColor;
    private int pageNumViewBackgroundColor;
    private float pageNumViewTextSize;
    private String pageNumViewMark;


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RecyclerBannerLayout);

        isTipsBackground = typedArray.getBoolean(R.styleable.RecyclerBannerLayout_rvb_is_tips_background, RecyclerBannerDefaults.IS_TIPS_LAYOUT_BACKGROUND);
        tipsBackgroundColor = typedArray.getColor(R.styleable.RecyclerBannerLayout_rvb_tips_background, ContextCompat.getColor(getContext(), RecyclerBannerDefaults.TIPS_LAYOUT_BACKGROUND));
        tipsLayoutWidth = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_tips_width, RecyclerBannerDefaults.TIPS_LAYOUT_WIDTH);
        tipsLayoutHeight = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_tips_height, RecyclerBannerDefaults.TIPS_LAYOUT_HEIGHT);

        isVisibleDots = typedArray.getBoolean(R.styleable.RecyclerBannerLayout_rvb_dots_visible, RecyclerBannerDefaults.IS_VISIBLE_DOTS);
        dotsLeftMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_dots_left_margin, RecyclerBannerDefaults.DOTS_LEFT_MARGIN);
        dotsRightMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_dots_right_margin, RecyclerBannerDefaults.DOTS_RIGHT_MARGIN);
        dotsWidth = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_dots_width, RecyclerBannerDefaults.DOTS_WIDth);
        dotsHeight = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_dots_height, RecyclerBannerDefaults.DOTS_HEIGHT);
        dotsSelector = typedArray.getResourceId(R.styleable.RecyclerBannerLayout_rvb_dots_selector, RecyclerBannerDefaults.DOTS_SELECTOR);
        enabledRadius = typedArray.getFloat(R.styleable.RecyclerBannerLayout_rvb_enabledRadius, RecyclerBannerDefaults.ENABLED_RADIUS);
        enabledColor = typedArray.getColor(R.styleable.RecyclerBannerLayout_rvb_enabledColor, ContextCompat.getColor(getContext(), RecyclerBannerDefaults.ENABLED_COLOR));
        normalRadius = typedArray.getFloat(R.styleable.RecyclerBannerLayout_rvb_normalRadius, RecyclerBannerDefaults.NORMAL_RADIUS);
        normalColor = typedArray.getColor(R.styleable.RecyclerBannerLayout_rvb_normalColor, ContextCompat.getColor(getContext(), RecyclerBannerDefaults.NORMAL_COLOR));

        delayTime = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_delay_time, RecyclerBannerDefaults.DELAY_TIME);
        isStartRotation = typedArray.getBoolean(R.styleable.RecyclerBannerLayout_rvb_start_rotation, RecyclerBannerDefaults.IS_START_ROTATION);
        errorImageView = typedArray.getResourceId(R.styleable.RecyclerBannerLayout_rvb_glide_error_image, RecyclerBannerDefaults.GLIDE_ERROR_IMAGE);
        placeImageView = typedArray.getResourceId(R.styleable.RecyclerBannerLayout_rvb_glide_place_image, RecyclerBannerDefaults.GLIDE_PIACE_IMAGE);
        isVertical = typedArray.getBoolean(R.styleable.RecyclerBannerLayout_rvb_banner_isVertical, RecyclerBannerDefaults.IS_VERTICAL);

        isVisibleTitle = typedArray.getBoolean(R.styleable.RecyclerBannerLayout_rvb_title_visible, RecyclerBannerDefaults.TITLE_VISIBLE);
        titleColor = typedArray.getColor(R.styleable.RecyclerBannerLayout_rvb_title_color, ContextCompat.getColor(getContext(), RecyclerBannerDefaults.TITLE_COLOR));
        titleSize = typedArray.getDimension(R.styleable.RecyclerBannerLayout_rvb_title_size, RecyclerBannerDefaults.TITLE_SIZE);
        titleRightMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_title_right_margin, RecyclerBannerDefaults.TITLE_RIGHT_MARGIN);
        titleLeftMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_title_left_margin, RecyclerBannerDefaults.TITLE_LEFT_MARGIN);
        titleWidth = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_title_width, RecyclerBannerDefaults.TITLE_WIDTH);
        titleHeight = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_title_height, RecyclerBannerDefaults.TITLE_HEIGHT);


        tipsSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_tips_site, BOTTOM);
        dotsSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_dots_site, RIGHT);
        titleSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_title_site, LEFT);
        pageNumViewSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_pageNumView_site, PAGE_NUM_VIEW_SITE_TOP_RIGHT);

        pageNumViewRadius = typedArray.getFloat(R.styleable.RecyclerBannerLayout_rvb_page_num_view_radius, RecyclerBannerDefaults.PAGE_NUM_VIEW_RADIUS);
        pageNumViewPaddingTop = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_paddingTop, RecyclerBannerDefaults.PAGE_NUM_VIEW_PADDING_TOP);
        pageNumViewPaddingLeft = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_paddingLeft, RecyclerBannerDefaults.PAGE_NUM_VIEW_PADDING_LEFT);
        pageNumViewPaddingBottom = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_paddingBottom, RecyclerBannerDefaults.PAGE_NUM_VIEW_PADDING_BOTTOM);
        pageNumViewPaddingRight = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_paddingRight, RecyclerBannerDefaults.PAGE_NUM_VIEW_PADDING_RIGHT);
        pageNumViewTopMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_marginTop, RecyclerBannerDefaults.PAGE_NUM_VIEW_TOP_MARGIN);
        pageNumViewRightMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_marginRight, RecyclerBannerDefaults.PAGE_NUM_VIEW_RIGHT_MARGIN);
        pageNumViewBottomMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_marginBottom, RecyclerBannerDefaults.PAGE_NUM_VIEW_BOTTOM_MARGIN);
        pageNumViewLeftMargin = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_page_num_view_marginLeft, RecyclerBannerDefaults.PAGE_NUM_VIEW_LEFT_MARGIN);
        pageNumViewTextColor = typedArray.getColor(R.styleable.RecyclerBannerLayout_rvb_page_num_view_textColor, ContextCompat.getColor(getContext(), RecyclerBannerDefaults.PAGE_NUL_VIEW_TEXT_COLOR));
        pageNumViewBackgroundColor = typedArray.getColor(R.styleable.RecyclerBannerLayout_rvb_page_num_view_BackgroundColor, ContextCompat.getColor(getContext(), RecyclerBannerDefaults.PAGE_NUM_VIEW_BACKGROUND));
        pageNumViewTextSize = typedArray.getDimension(R.styleable.RecyclerBannerLayout_rvb_page_num_view_textSize, RecyclerBannerDefaults.PAGE_NUM_VIEW_SIZE);
        pageNumViewMark = typedArray.getString(R.styleable.RecyclerBannerLayout_rvb_page_num_view_mark);
        if (isNull(pageNumViewMark)) {
            pageNumViewMark = RecyclerBannerDefaults.PAGE_NUM_VIEW_MARK;
        }
        typedArray.recycle();
    }


    public RecyclerBannerLayout(Context context) {
        super(context);
        init(null);
    }

    public RecyclerBannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RecyclerBannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        restoreRecyclerBanner();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRecyclerBanner();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopRecyclerBanner();
        } else if (visibility == VISIBLE) {
            restoreRecyclerBanner();
        }
    }


    /************************************************************************************************************
     * <p>
     * <p>                          BannerLayout method start
     * <p>
     *************************************************************************************************************/


    public RecyclerBannerLayout initPageNumView() {
        if (!isNull(pageView)) {
            pageView = null;
        }
        pageView = new RecyclerBannerPageView(getContext());
        pageView.setText(1 + pageNumViewMark + getDotsSize());
        addView(pageView, pageView.initPageView(this));
        return this;
    }

    public RecyclerBannerLayout initTips() {
        initTips(isTipsBackground, isVisibleDots, isVisibleTitle);
        return this;
    }


    /**
     * Initialize the dots control, do not initialize this method if you select custom hint bar
     *
     * @param isBackgroundColor Whether to display the background color
     * @param isVisibleDots     Whether to display small dots, the default display
     * @param isVisibleTitle    Whether to display title, the default is not displayed
     */
    public RecyclerBannerLayout initTips(boolean isBackgroundColor,
                                         boolean isVisibleDots,
                                         boolean isVisibleTitle) {
        if (isNull(imageList)) {
            throw new BannerException(getString(R.string.banner_adapterType_null));
        }
        this.isTipsBackground = isBackgroundColor;
        this.isVisibleDots = isVisibleDots;
        this.isVisibleTitle = isVisibleTitle;
        clearRecyclerBannerTipLayout();
        recyclerBannerTipLayout = new RecyclerBannerTipsLayout(getContext());
        recyclerBannerTipLayout.removeAllViews();
        if (isVisibleDots) {
            recyclerBannerTipLayout.setDots(this);
        }
        if (isVisibleTitle) {
            recyclerBannerTipLayout.setTitle(this);
            if (!isNull(onRecyclerBannerTitleListener)) {
                recyclerBannerTipLayout.setTitle(onRecyclerBannerTitleListener.getTitle(0));
            } else {
                recyclerBannerTipLayout.setTitle(imageList.get(0).getTitle());
            }
        }
        addView(recyclerBannerTipLayout, recyclerBannerTipLayout.setBannerTips(this));
        return this;
    }

    /**
     * Initializes a List image resource
     */
    public RecyclerBannerLayout initListResources(@NonNull List<? extends RecyclerBannerModel> imageList) {
        if (isNull(imageList)) {
            throw new BannerException(getString(R.string.list_null));
        }
        this.imageList = imageList;
        initAdapter();
        return this;
    }


    /**
     * Initializes an Array image resource
     */
    public RecyclerBannerLayout initArrayResources(@NonNull Object[] imageArray) {
        if (isNull(imageArray)) {
            throw new BannerException(getString(R.string.array_null));
        }
        List<RecyclerBannerModel> imageArrayList = new ArrayList<>();
        for (Object url : Arrays.asList(imageArray)) {
            imageArrayList.add(new RecyclerBannerModel(url));
        }
        initListResources(imageArrayList);
        return this;
    }

    /**
     * Initializes an Array image resource
     */
    public RecyclerBannerLayout initArrayResources(@NonNull Object[] imageArray, @NonNull String[] imageArrayTitle) {
        if (isNull(imageArray, imageArrayTitle)) {
            throw new BannerException(getString(R.string.array_null_));
        }
        List<Object> url = Arrays.asList(imageArray);
        List<String> title = Arrays.asList(imageArrayTitle);
        if (url.size() != title.size()) {
            throw new BannerException(getString(R.string.array_size_lnconsistent));
        }
        List<RecyclerBannerModel> imageArrayList = new ArrayList<>();
        int size = url.size();
        for (int i = 0; i < size; i++) {
            imageArrayList.add(new RecyclerBannerModel(url.get(i), title.get(i)));
        }
        initListResources(imageArrayList);
        return this;
    }

    /**
     * Initialize the rotation handler
     */
    public RecyclerBannerLayout start(boolean isStartRotation) {
        start(isStartRotation, delayTime);
        return this;
    }

    /**
     * Initialize the rotation handler
     */
    public RecyclerBannerLayout start(boolean isStartRotation, long delayTime) {
        clearHandler();
        this.delayTime = delayTime;
        this.isStartRotation = isStartRotation;
        if (isStartRotation && getDotsSize() > 1) {
            recyclerBannerHandlerUtils = new RecyclerBannerHandlerUtils(this, getScrollToPosition());
            recyclerBannerHandlerUtils.setDelayTime(delayTime);
            restoreRecyclerBanner();
        }
        return this;
    }


    public RecyclerBannerLayout setErrorImageView(@DrawableRes int errorImageView) {
        this.errorImageView = errorImageView;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setErrorImage(errorImageView);
        }
        return this;
    }

    public RecyclerBannerLayout setPlaceImageView(@DrawableRes int placeImageView) {
        this.placeImageView = placeImageView;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setErrorImage(placeImageView);
        }
        return this;
    }


    public List<? extends RecyclerBannerModel> getImageList() {
        return imageList;
    }


    public RecyclerBannerLayout clearHandler() {
        if (!isNull(recyclerBannerHandlerUtils)) {
            recyclerBannerHandlerUtils.setBannerStatus(-1);
            recyclerBannerHandlerUtils.removeCallbacksAndMessages(null);
            recyclerBannerHandlerUtils = null;
        }
        return this;
    }

    public RecyclerBannerLayout clearRecyclerBannerTipLayout() {
        if (!isNull(recyclerBannerTipLayout)) {
            recyclerBannerTipLayout.removeAllViews();
            recyclerBannerTipLayout = null;
        }
        return this;
    }

    public int getRecyclerBannerStatus() {
        if (isNull(recyclerBannerHandlerUtils)) {
            throw new BannerException(getString(R.string.banner_handler_erro));
        }
        return recyclerBannerHandlerUtils.getBannerStatus();
    }

    public void stopRecyclerBanner() {
        if (!isNull(recyclerBannerHandlerUtils)) {
            isStartRotation = false;
            recyclerBannerHandlerUtils.sendEmptyMessage(RecyclerBannerHandlerUtils.MSG_KEEP);
            recyclerBannerHandlerUtils.removeCallbacksAndMessages(null);
            recyclerBannerHandlerUtils.setBannerStatus(-1);
        }
    }

    public void restoreRecyclerBanner() {
        if (!isNull(recyclerBannerHandlerUtils)) {
            stopRecyclerBanner();
            isStartRotation = true;
            recyclerBannerHandlerUtils.sendEmptyMessage(RecyclerBannerHandlerUtils.MSG_BREAK);
        }
    }


    /************************************************************************************************************
     * <p>
     * <p>                          BannerTipsLayout method start
     *                              the call takes effect before the initTips () method
     * <p>
     *************************************************************************************************************/


    public RecyclerBannerLayout setTitleSetting(@ColorInt int titleColor, float titleSize) {
        if (titleSize != -1) {
            this.titleSize = titleSize;
        }
        if (titleColor != -1) {
            this.titleColor = titleColor;
        }
        return this;
    }


    /**
     * setting BannerTipsLayout background
     * The call takes effect before the initTips () method
     */
    public RecyclerBannerLayout setTipsBackgroundColor(@ColorInt int colorId) {
        this.tipsBackgroundColor = colorId;
        return this;
    }


    /**
     * sets the status selector for small dots
     * T
     */
    public RecyclerBannerLayout initTipsDotsSelector(@DrawableRes int dotsSelector) {
        this.dotsSelector = dotsSelector;
        return this;
    }

    /**
     * setting BannerTipsLayoutHeight
     */
    public RecyclerBannerLayout setTipsWidthAndHeight(int width,
                                                      int height) {
        this.tipsLayoutHeight = height;
        this.tipsLayoutWidth = width;
        return this;
    }


    /**
     * set the position of the tips in the layout
     */
    public RecyclerBannerLayout setTipsSite(@TipsSiteMode int tipsSite) {
        this.tipsSite = tipsSite;
        return this;
    }

    /**
     * sets the title marginLeft and marginRight, the default is 10
     */
    public RecyclerBannerLayout setTitleMargin(int leftMargin,
                                               int rightMargin) {
        this.titleLeftMargin = leftMargin;
        this.titleRightMargin = rightMargin;
        return this;
    }


    /**
     * set the position of the title in the layout
     */
    public RecyclerBannerLayout setTitleSite(@DotsAndTitleSiteMode int titleSite) {
        this.titleSite = titleSite;
        return this;
    }

    /**
     * set the dots width and height, the default is 15
     */
    public RecyclerBannerLayout setDotsWidthAndHeight(int width,
                                                      int height) {
        this.dotsWidth = width;
        this.dotsHeight = height;
        return this;
    }

    /**
     * set the position of the dots in the layout
     */
    public RecyclerBannerLayout setDotsSite(@DotsAndTitleSiteMode int dotsSite) {
        this.dotsSite = dotsSite;
        return this;
    }

    /**
     * sets the dots marginLeft and marginRight, the default is 10
     */
    public RecyclerBannerLayout setDotsMargin(int leftMargin,
                                              int rightMargin) {
        this.dotsLeftMargin = leftMargin;
        this.dotsRightMargin = rightMargin;
        return this;
    }

    public RecyclerBannerLayout setNormalRadius(float normalRadius) {
        this.normalRadius = normalRadius;
        return this;
    }

    public RecyclerBannerLayout setNormalColor(@ColorInt int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public RecyclerBannerLayout setEnabledColor(@ColorInt int enabledColor) {
        this.enabledColor = enabledColor;
        return this;
    }

    public RecyclerBannerLayout setEnabledRadius(float enabledRadius) {
        this.enabledRadius = enabledRadius;
        return this;
    }

    /************************************************************************************************************
     * <p>
     * <p>                          BannerPageNumView  setting    start
     * <p>
     *************************************************************************************************************/

    public RecyclerBannerLayout setPageNumViewRadius(float pageNumViewRadius) {
        this.pageNumViewRadius = pageNumViewRadius;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewPadding(int top,
                                                      int bottom,
                                                      int left,
                                                      int right) {
        this.pageNumViewPaddingTop = top;
        this.pageNumViewPaddingBottom = bottom;
        this.pageNumViewPaddingLeft = left;
        this.pageNumViewPaddingRight = right;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewMargin(int top,
                                                     int bottom,
                                                     int left,
                                                     int right) {
        this.pageNumViewTopMargin = top;
        this.pageNumViewBottomMargin = bottom;
        this.pageNumViewLeftMargin = left;
        this.pageNumViewRightMargin = right;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewTextColor(@ColorInt int pageNumViewTextColor) {
        this.pageNumViewTextColor = pageNumViewTextColor;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewBackgroundColor(@ColorInt int pageNumViewBackgroundColor) {
        this.pageNumViewBackgroundColor = pageNumViewBackgroundColor;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewTextSize(float pageNumViewTextSize) {
        this.pageNumViewTextSize = pageNumViewTextSize;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewSite(@PageNumViewSiteMode int pageNumViewSite) {
        this.pageNumViewSite = pageNumViewSite;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewMark(@NonNull String pageNumViewMark) {
        this.pageNumViewMark = pageNumViewMark;
        return this;
    }

    public RecyclerBannerLayout setPageNumViewMark(@StringRes int pageNumViewMark) {
        this.pageNumViewMark = getString(pageNumViewMark);
        return this;
    }

    /************************************************************************************************************
     * <p>
     * <p>                          BannerLayout interface listener      start
     * <p>
     *************************************************************************************************************/


    public RecyclerBannerLayout setOnRecyclerBannerClickListener(@NonNull OnRecyclerBannerClickListener onBannerClickListener) {
        this.onRecyclerBannerClickListener = onBannerClickListener;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setClickListener(onBannerClickListener);
        }
        return this;
    }

    public RecyclerBannerLayout addOnRecyclerBannerTitleListener(@NonNull OnRecyclerBannerTitleListener onBannerTitleListener) {
        this.onRecyclerBannerTitleListener = onBannerTitleListener;
        return this;
    }

    public RecyclerBannerLayout setRecyclerImageLoaderManager(@NonNull RecyclerBannerImageLoaderManager loaderManage) {
        this.recyclerImageLoaderManage = loaderManage;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setImageLoaderManager(loaderManage);
        }
        return this;
    }


    private void initAdapter() {
        recyclerAdapter = new RecyclerBannerAdapter(imageList);
        recyclerAdapter.setErrorImage(errorImageView);
        recyclerAdapter.setPlaceImage(placeImageView);
        recyclerAdapter.setClickListener(onRecyclerBannerClickListener);
        recyclerAdapter.setImageLoaderManager(recyclerImageLoaderManage);


        recyclerView = new RecyclerView(getContext());
        final LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
        if (isVertical) {
            manager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyclerAdapter);
        final int[] preEnablePosition = {0};
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                alterTipsView(manager, preEnablePosition);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (!isNull(recyclerBannerHandlerUtils) && isStartRotation) {
                            recyclerBannerHandlerUtils.sendMessage(Message.obtain(recyclerBannerHandlerUtils,
                                    RecyclerBannerHandlerUtils.MSG_PAGE, manager.findLastCompletelyVisibleItemPosition(), 0));
                        }
                        break;
                    default:
                        if (!isNull(recyclerBannerHandlerUtils)) {
                            recyclerBannerHandlerUtils.sendEmptyMessage(RecyclerBannerHandlerUtils.MSG_KEEP);
                            recyclerBannerHandlerUtils.setBannerStatus(-1);
                        }
                        break;
                }

            }
        });

        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(getScrollToPosition());
        addView(recyclerView);
        start(isStartRotation);
    }


    private void alterTipsView(LinearLayoutManager manager, int[] preEnablePosition) {
        int newPosition = manager.findFirstVisibleItemPosition() % imageList.size();
        if (!isNull(pageView)) {
            pageView.setText(newPosition + 1 + pageNumViewMark + getDotsSize());
        }
        if (!isNull(recyclerBannerTipLayout)) {
            if (isVisibleDots) {
                recyclerBannerTipLayout.changeDotsPosition(preEnablePosition[0], newPosition);
            }
            if (isVisibleTitle) {
                recyclerBannerTipLayout.clearText();
                if (!isNull(onRecyclerBannerTitleListener)) {
                    recyclerBannerTipLayout.setTitle(onRecyclerBannerTitleListener.getTitle(newPosition));
                } else {
                    recyclerBannerTipLayout.setTitle(imageList.get(newPosition).getTitle());
                }
            }
        }
        preEnablePosition[0] = newPosition;
    }

    private int getScrollToPosition() {
        return (Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % getDotsSize());
    }

    private int getDotsSize() {
        if (!isNull(imageList) && imageList.size() > 0) {
            return imageList.size();
        }
        throw new BannerException(getString(R.string.list_null));
    }

    private String getString(int id) {
        return getContext().getString(id);
    }

    private boolean isNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setCurrentItem(int i) {
        if (!isNull(recyclerView)) {
            recyclerView.smoothScrollToPosition(i);
        }
    }

    /************************************************************************************************************
     * <p>
     * <p>                          RecyclerBannerWidget method
     * <p>
     *************************************************************************************************************/


    @Override
    public int dotsCount() {
        return getDotsSize();
    }

    @Override
    public Drawable dotsSelector() {
        return dotsSelector == RecyclerBannerDefaults.DOTS_SELECTOR ?
                RecyclerBannerSelectorUtils.getDrawableSelector(
                        enabledRadius,
                        enabledColor,
                        normalRadius,
                        normalColor)
                :
                ContextCompat.getDrawable(getContext(), dotsSelector);
    }

    @Override
    public int dotsHeight() {
        return dotsHeight;
    }

    @Override
    public int dotsWidth() {
        return dotsWidth;
    }

    @Override
    public int dotsLeftMargin() {
        return dotsLeftMargin;
    }

    @Override
    public int dotsRightMargin() {
        return dotsRightMargin;
    }

    @Override
    public int dotsSite() {
        return dotsSite;
    }

    @Override
    public int titleColor() {
        return titleColor;
    }

    @Override
    public float titleSize() {
        return titleSize;
    }

    @Override
    public int titleLeftMargin() {
        return titleLeftMargin;
    }

    @Override
    public int titleRightMargin() {
        return titleRightMargin;
    }

    @Override
    public int titleWidth() {
        return titleWidth;
    }

    @Override
    public int titleHeight() {
        return titleHeight;
    }

    @Override
    public int titleSite() {
        return titleSite;
    }


    @Override
    public int tipsSite() {
        return tipsSite;
    }

    @Override
    public int tipsWidth() {
        return tipsLayoutWidth;
    }

    @Override
    public int tipsHeight() {
        return tipsLayoutHeight;
    }

    @Override
    public int tipsLayoutBackgroundColor() {
        return tipsBackgroundColor;
    }

    @Override
    public boolean isBackgroundColor() {
        return isTipsBackground;
    }


    @Override
    public int getPageNumViewTopMargin() {
        return pageNumViewTopMargin;
    }

    @Override
    public int getPageNumViewRightMargin() {
        return pageNumViewRightMargin;
    }

    @Override
    public int getPageNumViewBottomMargin() {
        return pageNumViewBottomMargin;
    }

    @Override
    public int getPageNumViewLeftMargin() {
        return pageNumViewLeftMargin;
    }

    @Override
    public int pageNumViewSite() {
        return pageNumViewSite;
    }

    @Override
    public int getPageNumViewTextColor() {
        return pageNumViewTextColor;
    }

    @Override
    public float getPageNumViewTextSize() {
        return pageNumViewTextSize;
    }

    @Override
    public int getPageNumViewPaddingTop() {
        return pageNumViewPaddingTop;
    }

    @Override
    public int getPageNumViewPaddingLeft() {
        return pageNumViewPaddingLeft;
    }

    @Override
    public int getPageNumViewPaddingBottom() {
        return pageNumViewPaddingBottom;
    }

    @Override
    public int getPageNumViewPaddingRight() {
        return pageNumViewPaddingRight;
    }

    @Override
    public float getPageNumViewRadius() {
        return pageNumViewRadius;
    }


    @Override
    public int getPageNumViewBackgroundColor() {
        return pageNumViewBackgroundColor;
    }

}

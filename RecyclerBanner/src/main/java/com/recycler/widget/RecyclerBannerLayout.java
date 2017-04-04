package com.recycler.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.recycler.R;
import com.recycler.annotation.DotsAndTitleSiteMode;
import com.recycler.annotation.PageNumViewSiteMode;
import com.recycler.annotation.TipsSiteMode;
import com.recycler.listener.OnRecyclerBannerClickListener;
import com.recycler.listener.OnRecyclerBannerTitleListener;
import com.recycler.listener.RecyclerBannerImageLoaderManager;
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
        RecyclerBannerPageView.PageNumViewInterface {

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
    public static final int ALIGN_PARENT_LEFT = 9;
    public static final int ALIGN_PARENT_TOP = 10;
    public static final int ALIGN_PARENT_RIGHT = 11;
    public static final int ALIGN_PARENT_BOTTOM = 12;
    public static final int CENTER_IN_PARENT = 13;

    private OnRecyclerBannerClickListener onBannerClickListener = null;
    private List<? extends RecyclerBannerModel> imageList = null;
    private RecyclerBannerHandlerUtils bannerHandlerUtils = null;
    private RecyclerBannerTipsLayout bannerTipLayout = null;
    private RecyclerBannerImageLoaderManager imageLoaderManage = null; //Image Load Manager
    private View tipsView = null; //The custom hint bar must take over viewpager's OnPageChangeListener method
    private OnRecyclerBannerTitleListener onBannerTitleListener = null;
    private RecyclerBannerPageView pageView = null; // viewpager page count textView
    private RecyclerBannerAdapter recyclerAdapter;

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


        tipsSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_tips_site, ALIGN_PARENT_BOTTOM);
        dotsSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_dots_site, ALIGN_PARENT_RIGHT);
        titleSite = typedArray.getInteger(R.styleable.RecyclerBannerLayout_rvb_title_site, ALIGN_PARENT_LEFT);
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


    /************************************************************************************************************
     * <p>
     * <p>                          BannerLayout method start
     * <p>
     *************************************************************************************************************/

    public RecyclerBannerLayout clearBanner() {
        clearHandler();
        clearBannerTipLayout();
        clearImageLoaderManager();
        clearBannerClickListener();
        clearImageList();
        clearPageView();
        clearBannerTitleListener();
        clearTipsView();
        return this;
    }


    public RecyclerBannerLayout initPageNumView() {
        clearPageView();
        pageView = new RecyclerBannerPageView(getContext());
        pageView.setText(1 + pageNumViewMark + getDotsSize());
        addView(pageView, pageView.initPageView(this));
        return this;
    }


    /**
     * Initialize the dots using the default parameters
     */
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
        if (!isNull(tipsView)) {
            error(R.string.tipsView_no_null);
        }
        if (isNull(imageList)) {
            error(R.string.banner_adapterType_null);
        }
        this.isTipsBackground = isBackgroundColor;
        this.isVisibleDots = isVisibleDots;
        this.isVisibleTitle = isVisibleTitle;
        clearBannerTipLayout();
        bannerTipLayout = new RecyclerBannerTipsLayout(getContext());
        bannerTipLayout.removeAllViews();
        if (isVisibleDots) {
            bannerTipLayout.setDots(this);
        }
        if (isVisibleTitle) {
            bannerTipLayout.setTitle(this);
            if (!isNull(onBannerTitleListener)) {
                bannerTipLayout.setTitle(onBannerTitleListener.getTitle(0));
            } else {
                bannerTipLayout.setTitle(imageList.get(0).getTitle());
            }
        }
        bannerTipLayout.setBannerTips(this);
        addView(bannerTipLayout);
        return this;
    }


    /**
     * Initializes a List image resource
     */
    public RecyclerBannerLayout initListResources(@NonNull List<? extends RecyclerBannerModel> imageList) {
        if (isNull(imageList)) {
            error(R.string.list_null);
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
            error(R.string.array_null);
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
            error(R.string.array_null_);
        }
        List<Object> url = Arrays.asList(imageArray);
        List<String> title = Arrays.asList(imageArrayTitle);
        if (url.size() != title.size()) {
            error(R.string.array_size_lnconsistent);
        }
        List<RecyclerBannerModel> imageArrayList = new ArrayList<>();
        RecyclerBannerModel bannerModel;
        for (int i = 0; i < url.size(); i++) {
            bannerModel = new RecyclerBannerModel();
            bannerModel.setImage(url.get(i));
            bannerModel.setTitle(title.get(i));
            imageArrayList.add(bannerModel);
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
        if (isStartRotation && getDotsSize() > 1) {
            bannerHandlerUtils = new RecyclerBannerHandlerUtils(0);
            bannerHandlerUtils.setDelayTime(delayTime);
            this.delayTime = delayTime;
            this.isStartRotation = isStartRotation;
            restoreBanner();
        }
        return this;
    }


    /**
     * glide Loads an error image, called before initAdapter
     */
    public RecyclerBannerLayout setErrorImageView(@DrawableRes int errorImageView) {
        this.errorImageView = errorImageView;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setErrorImage(errorImageView);
        }
        return this;
    }

    /**
     * glide loads the image before the initAdapter is called
     */
    public RecyclerBannerLayout setPlaceImageView(@DrawableRes int placeImageView) {
        this.placeImageView = placeImageView;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setErrorImage(placeImageView);
        }
        return this;
    }


    /**
     * Initialize the custom hint column before calling initAdapter
     */
    public RecyclerBannerLayout setTipsView(@NonNull View view) {
        clearTipsView();
        this.tipsView = view;
        addView(tipsView);
        return this;
    }


    public RecyclerBannerLayout clearBannerClickListener() {
        if (!isNull(onBannerClickListener)) {
            onBannerClickListener = null;
        }
        return this;
    }

    public List<? extends RecyclerBannerModel> getImageList() {
        return imageList;
    }

    public RecyclerBannerLayout clearImageList() {
        if (!isNull(imageList)) {
            imageList.clear();
            imageList = null;
        }
        return this;
    }

    public RecyclerBannerHandlerUtils getBannerHandlerUtils() {
        return bannerHandlerUtils;
    }

    public RecyclerBannerLayout clearHandler() {
        if (!isNull(bannerHandlerUtils)) {
            bannerHandlerUtils.setBannerStatus(-1);
            bannerHandlerUtils.removeCallbacksAndMessages(null);
            bannerHandlerUtils = null;
        }
        return this;
    }

    public RecyclerBannerTipsLayout getBannerTipLayout() {
        return bannerTipLayout;
    }

    public RecyclerBannerLayout clearBannerTipLayout() {
        if (!isNull(bannerTipLayout)) {
            bannerTipLayout.removeAllViews();
            bannerTipLayout = null;
        }
        return this;
    }

    public RecyclerBannerLayout clearImageLoaderManager() {
        if (!isNull(imageLoaderManage)) {
            imageLoaderManage = null;
        }
        return this;
    }

    public View getTipsView() {
        return tipsView;
    }

    public RecyclerBannerLayout clearTipsView() {
        if (!isNull(tipsView)) {
            tipsView = null;
        }
        return this;
    }


    public OnRecyclerBannerTitleListener getOnBannerTitleListener() {
        return onBannerTitleListener;
    }

    public RecyclerBannerLayout clearBannerTitleListener() {
        if (!isNull(onBannerTitleListener)) {
            onBannerTitleListener = null;
        }
        return this;
    }


    public RecyclerBannerPageView getPageView() {
        return pageView;
    }

    public RecyclerBannerLayout clearPageView() {
        if (!isNull(pageView)) {
            pageView = null;
        }
        return this;
    }

    /**
     * get banner rotation status
     */
    public int getBannerStatus() {
        if (isNull(bannerHandlerUtils)) {
            error(R.string.banner_handler_erro);
        }
        return bannerHandlerUtils.getBannerStatus();
    }

    public void startBanner() {
        start(true);
    }

    public void stopBanner() {
        if (!isNull(bannerHandlerUtils)) {
            isStartRotation = false;
            bannerHandlerUtils.sendEmptyMessage(RecyclerBannerHandlerUtils.MSG_KEEP);
            bannerHandlerUtils.removeCallbacksAndMessages(null);
            bannerHandlerUtils.setBannerStatus(-1);
        }
    }

    public void restoreBanner() {
        if (!isNull(bannerHandlerUtils)) {
            stopBanner();
            isStartRotation = true;
            bannerHandlerUtils.sendEmptyMessage(RecyclerBannerHandlerUtils.MSG_BREAK);
        }
    }


    /************************************************************************************************************
     * <p>
     * <p>                          BannerTipsLayout method start
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
     * The call takes effect before the initTips () method
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
        this.onBannerClickListener = onBannerClickListener;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setClickListener(onBannerClickListener);
        }
        return this;
    }

    public RecyclerBannerLayout addOnRecyclerBannerTitleListener(@NonNull OnRecyclerBannerTitleListener onBannerTitleListener) {
        this.onBannerTitleListener = onBannerTitleListener;
        return this;
    }

    public RecyclerBannerLayout setRecyclerImageLoaderManager(@NonNull RecyclerBannerImageLoaderManager loaderManage) {
        this.imageLoaderManage = loaderManage;
        if (!isNull(recyclerAdapter)) {
            recyclerAdapter.setImageLoaderManager(loaderManage);
        }
        return this;
    }


    /************************************************************************************************************
     * <p>
     * <p>                          dotsView default setting    start
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


    /************************************************************************************************************
     * <p>
     * <p>                          titleView default setting    start
     * <p>
     *************************************************************************************************************/

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


    /************************************************************************************************************
     * <p>
     * <p>                          BannerTipsView default setting    start
     * <p>
     *************************************************************************************************************/


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


    /************************************************************************************************************
     * <p>
     * <p>                          BannerPageNumView default setting    start
     * <p>
     *************************************************************************************************************/

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


    private void initAdapter() {
        recyclerAdapter = new RecyclerBannerAdapter(imageList);
        recyclerAdapter.setErrorImage(errorImageView);
        recyclerAdapter.setPlaceImage(placeImageView);
        recyclerAdapter.setClickListener(onBannerClickListener);
        recyclerAdapter.setImageLoaderManager(imageLoaderManage);


        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
        if (isVertical) {
            manager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyclerAdapter);

        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(getScrollToPosition());
        addView(recyclerView);
        start(isStartRotation);
    }

    private int getScrollToPosition() {
        return (Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % getDotsSize());
    }

    private int getDotsSize() {
        if (!isNull(imageList) && imageList.size() > 0) {
            return imageList.size();
        }
        throw error(R.string.list_null);
    }

    private String getString(int id) {
        return getContext().getString(id);
    }

    private BannerException error(int messageId) {
        throw new BannerException(getString(messageId));
    }

    private boolean isNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    private static class BannerException extends RuntimeException {
        BannerException(String s) {
            super(s);
        }
    }

}

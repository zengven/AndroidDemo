package pub.ven.androiddemo.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import pub.ven.androiddemo.R;

/**
 * author: zengven
 * date: 2016/7/18
 * Desc: 自定义下拉刷新
 */
public class SwipeRefreshToLayout extends RelativeLayout implements AbsListView.OnScrollListener {
    private static final String TAG = SwipeRefreshToLayout.class.getSimpleName();

    public static enum Mode {
        Both, PullDown, PullUp
    }

    private static final int DEFAULT_HEAD_HEIGHT = 40;
    private static final float DRAG_RATE = .5f;//阻尼系数
    private boolean mRefreshing = false; //是否处于刷新状态
    private boolean isLoadMore = false;
    private Mode currentMode = Mode.PullDown;//设置刷新模式,默认下拉刷新

    private View mDragView;
    private boolean mIsBeingDragged = false; //
    private int mActivePointerId;
    private float mInitDownY; //初始化按下位置
    private float mRealInitMotionY;//真实的开始移动位置
    private static final int INVALID_POINTER = -1;//无效手指触摸ID
    private int mTouchSlop; //view滑动趋势范围.
    private int mDragViewWidth;
    private int mDragViewHeight;
    private View mHanderView;
    private DisplayMetrics mMetrics;
    private int mDefaultHeight;
    private LayoutInflater layoutInflater;
    private OnRefreshListener listener;
    private View mFooterView;


    public SwipeRefreshToLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshToLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshToLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //获取
        mMetrics = getResources().getDisplayMetrics();
        mDefaultHeight = (int) (DEFAULT_HEAD_HEIGHT * mMetrics.density);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }

        /* 添加头布局 */
        mHanderView = layoutInflater.inflate(R.layout.view_header, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        //layoutParams.gravity = Gravity.TOP;
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mHanderView.setLayoutParams(layoutParams);
        mHanderView.setVisibility(View.GONE);
        addView(mHanderView);

        /*添加脚布局*/
        mFooterView = layoutInflater.inflate(R.layout.view_footer, null);
        LayoutParams layoutParams2 = new LayoutParams(LayoutParams.MATCH_PARENT, mDefaultHeight);
        //layoutParams.gravity = Gravity.BOTTOM;
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mFooterView.setLayoutParams(layoutParams2);
        mFooterView.setVisibility(View.GONE);
        addView(mFooterView);

    }

    /**
     * 能否下拉
     *
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mDragView, -1) || mDragView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mDragView, -1);
        }
    }

    /**
     * 能否上拉
     *
     * @return
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragView;
                if (absListView.getChildCount() > 0) {
                    int lastChildBottom = absListView.getChildAt(absListView.getChildCount() - 1).getBottom();
                    return absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1 && lastChildBottom <= absListView.getMeasuredHeight();
                } else {
                    return false;
                }

            } else {
                return ViewCompat.canScrollVertically(mDragView, 1) || mDragView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mDragView, 1);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    //转交拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ((canChildScrollUp() && canChildScrollDown()) || mRefreshing || isLoadMore) {
            Log.i(TAG, "onInterceptTouchEvent: mRefreshing : " + mRefreshing);
            return false; //不拦截事件
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                float initDownY = getMotionEventY(ev, mActivePointerId);
                if (initDownY == -1) {
                    return false;
                }
                mInitDownY = initDownY;
                Log.i(TAG, "onInterceptTouchEvent: ACTION_DOWN mInitDownY:" + mInitDownY);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false; //不处理事件
                }
                float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) { //无效手指ID
                    return false;
                }
                float diffY = y - mInitDownY;
                Log.i(TAG, "mTouchSlop : " + mTouchSlop + "   diffY :" + diffY);
                if (diffY >= 0 && diffY > mTouchSlop && !mIsBeingDragged && !canChildScrollUp() && (currentMode == Mode.Both || currentMode == Mode.PullDown) && !isLoadMore && !mRefreshing) { //mTouchSlop 触摸溢出范围
                    Log.i(TAG, "下拉刷新--");
                    mRealInitMotionY = mTouchSlop + mInitDownY; //真实开始移动坐标
                    mIsBeingDragged = true;
                    //显示下拉刷新布局
                    mHanderView.setVisibility(View.VISIBLE);
                } else if (diffY < 0 && diffY < -mTouchSlop && !mIsBeingDragged && !canChildScrollDown() && (currentMode == Mode.Both || currentMode == Mode.PullUp) && !mRefreshing && !isLoadMore) {
                    Log.i(TAG, "上啦加载--");
                    mRealInitMotionY = -mTouchSlop + mInitDownY; //真实开始移动坐标
                    //显示上啦刷新布局
                    mIsBeingDragged = true;
                    mFooterView.setVisibility(View.VISIBLE);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return mIsBeingDragged;

    }

    //处理触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((canChildScrollUp() && canChildScrollDown()) || mRefreshing || isLoadMore) {
            return false; //事件不消费
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);//多指触摸,获取第一个触摸的手指ID
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    //当前第一个触摸的手指无效,不消费move事件
                    return false;
                }
                float y = MotionEventCompat.getY(event, pointerIndex);
                float offSetY = y - mRealInitMotionY; //真实滑动偏移量
                Log.i(TAG, "offSetY: " + offSetY);
                if (mIsBeingDragged) {
                    if (offSetY >= 0 && !canChildScrollUp()) {
                        // TODO: 2016/7/19 移动当前控件
                        moveSpinner(offSetY * DRAG_RATE, mHanderView);
                    } else if (offSetY < 0 && !canChildScrollDown()) {
                        moveSpinner(offSetY * DRAG_RATE, mFooterView);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                float translationY = ViewCompat.getTranslationY(mDragView);
                if (translationY >= 0 && !canChildScrollUp()) { //下拉刷新
                    Log.i(TAG, "onTouchEvent: 下拉刷新");
                    if (translationY >= mDefaultHeight) {
                        //执行动画
                        createAnimatorTranslationY(mDragView, mDefaultHeight, mHanderView);
                        mRefreshing = true;
                        listener.onRefresh();
                    } else {
                        createAnimatorTranslationY(mDragView, 0, mHanderView);
                        mRefreshing = false;
                    }
                } else if (translationY < 0 && !canChildScrollDown()) { //上啦加载
                    Log.i(TAG, "onTouchEvent: 上啦加载");
                    if (translationY <= -mDefaultHeight) {
                        //执行动画
                        createAnimatorTranslationY(mDragView, -mDefaultHeight, mFooterView);
                        isLoadMore = true;
                        if (listener != null)
                            listener.onLoadMore();
                    } else {
                        createAnimatorTranslationY(mDragView, 0, mFooterView);
                        isLoadMore = false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return true;
    }

    /**
     * 执行view缩放动画
     *
     * @param dragView    拖拽view
     * @param h
     * @param adjointView 伴随view
     */
    public void createAnimatorTranslationY(final View dragView, final float h, final View adjointView) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(dragView);
        viewPropertyAnimatorCompat.setDuration(250);
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimatorCompat.translationY(h);
        viewPropertyAnimatorCompat.start();
        viewPropertyAnimatorCompat.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                float height = ViewCompat.getTranslationY(dragView);
                //Log.i(TAG, "onAnimationUpdate: height: " + height);
                adjointView.getLayoutParams().height = (int) Math.abs(height);
                adjointView.requestLayout();
            }
        });
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if ((android.os.Build.VERSION.SDK_INT < 21 && mDragView instanceof AbsListView)
                || (mDragView != null && !ViewCompat.isNestedScrollingEnabled(mDragView))) {
            // Nope.
            Log.i(TAG, "requestDisallowInterceptTouchEvent: ----");
        } else {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    /**
     * 根据偏移量移动控件
     *
     * @param offSetY
     * @param adjointView
     */
    private void moveSpinner(float offSetY, View adjointView) {
        //mHanderView.bringToFront();//会调用requestLayout() and invalidate()
        ViewCompat.setTranslationY(mDragView, offSetY);
        adjointView.getLayoutParams().height = (int) Math.abs(offSetY);
        adjointView.requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        mDragView = getChildAt(0);
        if (mDragView != null && mDragView instanceof AbsListView) {
            ((AbsListView) mDragView).setOnScrollListener(this);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (view.getCount() != 0 && (view.getLastVisiblePosition() == view.getCount() - 1) && !isLoadMore && !mRefreshing) {
                Log.i(TAG, "onScroll:  loadmore");
                setAutoLoadMore();
                isLoadMore = true;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDragViewWidth = mDragView.getMeasuredWidth();
        mDragViewHeight = mDragView.getMeasuredHeight();
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        this.listener = l;
    }

    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();
    }

    /**
     * 设置刷新状态
     */
    public void setAutoRefreshing() {
        // TODO: 2016/7/21 自动刷新
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHanderView.setVisibility(View.VISIBLE);
                moveSpinner(mDefaultHeight, mHanderView);
                mRefreshing = true;
                listener.onRefresh();
            }
        }, 10);
    }

    /**
     * 设置是否自动加载
     */
    public void setAutoLoadMore() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFooterView.setVisibility(View.VISIBLE);
                moveSpinner(-mDefaultHeight, mFooterView);
                isLoadMore = true;
                listener.onLoadMore();
            }
        }, 10);
    }

    /**
     * 刷新完成
     */
    public void onRefreshComplete() {
        if (mRefreshing) { //处于刷新状态
            createAnimatorTranslationY(mDragView, 0, mHanderView);
            mRefreshing = false;
            mHanderView.setVisibility(View.GONE);
        }
        if (isLoadMore) {
            createAnimatorTranslationY(mDragView, 0, mFooterView);
            isLoadMore = false;
            mFooterView.setVisibility(View.GONE);
        }

    }

    /**
     * 设置刷新模式
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        currentMode = mode;
    }

}

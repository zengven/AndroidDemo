package pub.ven.androiddemo.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import pub.ven.androiddemo.R;

/**
 * author: zengven
 * date: 2016/7/18
 * Desc: 自定义下拉刷新
 */
public class SwipeRefreshToLayout extends FrameLayout {
    private static final String TAG = "SwipeRefreshLayout";

    private static final int DEFAULT_HEAD_HEIGHT = 60;
    private static final float DRAG_RATE = .43f;//阻尼系数
    private boolean mRefreshing = false; //是否处于刷新状态

    private View mDragView;
    private boolean mIsBeingDragged = false; //
    private int mActivePointerId;
    private float mInitDownY; //初始化按下位置
    private float mRealInitMotionY;//真实的开始移动位置
    private static final int INVALID_POINTER = -1;//无效手指触摸ID
    private int mTouchSlop; //view滑动趋势范围.
    private int mDragViewWidth;
    private int mDragViewHeight;
    private int top;
    private View mHanderView;
    private DisplayMetrics mMetrics;
    private int mHeadHeight;
    private LayoutInflater layoutInflater;


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
        Log.i(TAG, "init: ----go");
        //获取
        mMetrics = getResources().getDisplayMetrics();
        mHeadHeight = (int) (DEFAULT_HEAD_HEIGHT * mMetrics.density);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ----go");
        if (isInEditMode()) {
            return;
        }

        /* 添加头布局 */
        mHanderView = layoutInflater.inflate(R.layout.view_header, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadHeight);
        layoutParams.gravity = Gravity.TOP;
        mHanderView.setLayoutParams(layoutParams);
        mHanderView.setVisibility(View.GONE);
        addView(mHanderView);

        // TODO: 2016/7/20 添加叫布局
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
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
        if (canChildScrollUp()) {
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
                top = mDragView.getTop();
                Log.i(TAG, "top :" + top);
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
                if (diffY > mTouchSlop && !mIsBeingDragged) { //mTouchSlop 触摸溢出范围
                    mRealInitMotionY = mTouchSlop + mInitDownY; //真实开始移动坐标
                    mIsBeingDragged = true;
                    //显示下拉刷新布局
                    mHanderView.setVisibility(View.VISIBLE);
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
        Log.i(TAG, "onTouchEvent-----canChildScrollUp :" + canChildScrollUp());
        if (canChildScrollUp()) {
            return false;
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
                    if (offSetY > 0) {
                        // TODO: 2016/7/19 移动当前控件
                        moveSpinner(offSetY * DRAG_RATE);
                    } else {
                        return false;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (ViewCompat.getTranslationY(mDragView) > mHeadHeight) {
                    //执行动画
                    createAnimatorTranslationY(mDragView, mHeadHeight, mHanderView);
                } else {
                    createAnimatorTranslationY(mDragView, 0, mHanderView);
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
     * @param v
     * @param h
     * @param view
     */
    public void createAnimatorTranslationY(final View v, final float h, final View view) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(250);
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimatorCompat.translationY(h);
        viewPropertyAnimatorCompat.start();
        viewPropertyAnimatorCompat.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                float height = ViewCompat.getTranslationY(v);
                view.getLayoutParams().height = (int) height;
                view.requestLayout();
            }
        });
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if ((android.os.Build.VERSION.SDK_INT < 21 && mDragView instanceof AbsListView)
                || (mDragView != null && !ViewCompat.isNestedScrollingEnabled(mDragView))) {
            Log.i(TAG, "requestDisallowInterceptTouchEvent: -----start");
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    /**
     * 根据偏移量移动控件
     *
     * @param offSetY
     */
    private void moveSpinner(float offSetY) {
        //mHanderView.bringToFront();//会调用requestLayout() and invalidate()
        ViewCompat.setTranslationY(mDragView, offSetY);
        mHanderView.getLayoutParams().height = (int) offSetY;
        mHanderView.requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: ---g0");
        int childCount = getChildCount();
        mDragView = getChildAt(0);
        mDragView.bringToFront();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: ---g0");
        mDragViewWidth = mDragView.getMeasuredWidth();
        mDragViewHeight = mDragView.getMeasuredHeight();
    }


}

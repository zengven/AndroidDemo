package pub.ven.androiddemo.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 侧滑面板
 * 继承FrameLayout, 效率高
 *
 * @author poplar
 */
public class DragLayout extends FrameLayout {

    private ViewDragHelper mDragHelper; // 视图拖拽辅助类
    private ViewGroup mLeftMenu; // 左菜单
    private ViewGroup mMainContent; // 主面板
    private int mWidth; // 控件宽度
    private int mHeight; // 控件高度
    private int mRange; // 拖拽的范围

    private OnDragChangeListener onDragChangeListener; // 拖拽监听

    public void setOnDragChangeListener(
            OnDragChangeListener onDragChangeListener) {
        this.onDragChangeListener = onDragChangeListener;
    }


    public static enum Status {
        Close,
        Open,
        Draging;
    }

    private Status status = Status.Close;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // new
    public DragLayout(Context context) {
        this(context, null);
    }

    // xml
    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // xml - style
    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 1. 初始化ViewDragHelper
        // forParent 拖拽控件所在的父布局
        // sensitivity 敏感度
        // Callback 回调, 提供信息, 接受事件.
        mDragHelper = ViewDragHelper.create(this, 1.0f, mCallBack);

    }

    // 3. 重写事件回调 (重点)
    ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {

        // a. 返回值决定当前child是否可以被拖拽
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // child 被触摸到子控件View
            // pointerId : 多点触摸的手指id
            System.out.println("tryCaptureView: " + child.toString());
            return true;
        }

        /**
         * 返回控件水平方向的拖拽范围
         * 并不限制拖拽的真正位置
         * 1. 计算动画执行时长
         * 2. 返回 >0 值, 水平方向才可以划得开
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        /**
         * 返回值 决定了要移动到的水平方向位置.
         * 此时还未发生真正的移动
         *
         * child 被拖拽的子控件
         * left 将要移动到的位置, ViewDragHelper提供的建议值
         * dx 将要发生的偏移量
         */
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int oldLeft = child.getLeft();
            // left = oldLeft + dx
//			System.out.println("clampViewPositionHorizontal: left: " + left  + " dx: " + dx + " oldLeft: " + oldLeft);
            if (child == mMainContent) {
                // 移动的是主面板
                left = fixLeft(left);
            }
            return left;
        }

        /**
         * 当控件位置发生变化时, 被调用: 做伴随动画, 状态更新, 事件回调
         * changedView 位置变化的子View
         * left 最新的水平位置
         * dx 刚刚发生的偏移量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            System.out.println("onViewPositionChanged: left" + left + " dx: " + dx);

            // 当左面板移动后, 放回原位置. 把发生的水平方向变化量dx传递给主面板
            if (changedView == mLeftMenu) {
                // 左面板放回原位置
                mLeftMenu.layout(0, 0, 0 + mWidth, 0 + mHeight);

                // 计算主面板新的水平位置
                int newLeft = mMainContent.getLeft() + dx;
                newLeft = fixLeft(newLeft);
                mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);
            }

            dispatchDragEvent();

            // 重绘界面, 让低版本也可以更新移动
            invalidate();
        }

        /**
         * 当控件被用户释放时调用. 松手时调用.  结束动画
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            // releasedChild 被释放的子View
            // xvel 水平方向的速度 向右+ ,向左-
            // yvel 竖直方向的速度 向下+ ,向上-
            System.out.println("onViewReleased: " + xvel);
            // 所有打开情况
            if (xvel == 0 && mMainContent.getLeft() > mRange * 0.5f) {
                open();
            } else if (xvel > 0) {
                open();
            } else {
                close();
            }
        }

        /**
         * 拖拽状态变化时调用
         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            System.out.println("onViewDragStateChanged: " + state);
        }
    };

    /**
     * 分发拖拽事件. 做伴随动画, 更新状态, 执行监听
     */
    protected void dispatchDragEvent() {
        float percent = mMainContent.getLeft() * 1.0f / mRange;
        System.out.println("percent: " + percent); // 0.0 -> 1.0

        // 执行伴随动画
        animViews(percent);

        if (onDragChangeListener != null) {
            onDragChangeListener.onDraging(percent);
        }

        Status lastStatus = status;
        // 更新当前状态
        status = updateStatus(percent);

        // 状态发生变化时, 告诉外界
        if (lastStatus != status && onDragChangeListener != null) {
            if (status == Status.Open) {
                onDragChangeListener.onOpen();
            } else if (status == Status.Close) {
                onDragChangeListener.onClose();
            }
        }
    }

    private Status updateStatus(float percent) {
        if (percent == 0f) {
            return Status.Close;
        } else if (percent == 1.0f) {
            return Status.Open;
        }
        return Status.Draging;
    }

    private void animViews(float percent) {
        //		1. 左菜单: 缩放动画, 平移动画, 透明度动画
        //		mLeftMenu.setScaleX(0.5f);
        //		mLeftMenu.setScaleY(0.5f);
        // 0.0 -> 1.0 >>> 0.0 -> 0.5 >>> 0.5 -> 1.0
        // percent * 0.5f + 0.5f
        // 缩放动画
        //		ViewHelper.setScaleX(mLeftMenu, percent * 0.5f + 0.5f);
        //		ViewHelper.setScaleY(mLeftMenu, percent * 0.5f + 0.5f);

        //ViewHelper.setScaleX(mLeftMenu, evaluate(percent, 0.5f, 1.0f));
        //ViewHelper.setScaleY(mLeftMenu, evaluate(percent, 0.5f, 1.0f));
        mLeftMenu.setScaleX(evaluate(percent, 0.5f, 1.0f));
        mLeftMenu.setScaleY(evaluate(percent, 0.5f, 1.0f));

        // 平移动画 -mWidth * 0.5f -> 0
        //ViewHelper.setTranslationX(mLeftMenu, evaluate(percent, -mWidth * 0.5f, 0));
        mLeftMenu.setTranslationX(evaluate(percent, -mWidth * 0.5f, 0));

        // 透明度
        //ViewHelper.setAlpha(mLeftMenu, evaluate(percent, 0.2f, 1.0f));
        mLeftMenu.setAlpha(evaluate(percent, 0.2f, 1.0f));

        //		2. 主面板: 缩放动画
//        ViewHelper.setScaleX(mMainContent, evaluate(percent, 1.0f, 0.8f));
//        ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));
        mMainContent.setScaleX(evaluate(percent, 1.0f, 0.8f));
        mMainContent.setScaleY(evaluate(percent, 1.0f, 0.8f));


        //		3. 背景: 亮度变化 黑色 -> 透明色
//        getBackground().setColorFilter((Integer) evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);
    }

    /**
     * 颜色过度估值器
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * 类型估值器 TypeEvaluator
     *
     * @param fraction   分度值 0.0f -> 1.0f
     * @param startValue 开始值
     * @param endValue   结束值
     * @return
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 限定left范围
     * 0 -> mRange
     *
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if (left < 0) { // 限定左边界
            left = 0;
        } else if (left > mRange) { // 限定右边界
            left = mRange;
        }
        return left;
    }

    ;

    /**
     * 打开
     */
    protected void open() {
        open(true);
    }

    public void open(boolean isSmooth) {
        int finalLeft = mRange;
        if (isSmooth) {
            // 1. 触发一个平滑动画, 封装了Scroller
            if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
                // true 表示控件还没有移动到指定位置, 需要位置修改. 重绘界面.
                ViewCompat.postInvalidateOnAnimation(this); // 执行动画重绘界面
            }
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    // 2. 维持动画的继续
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 动画执行过程中的 每次draw都会被调用(高频)
        if (mDragHelper.continueSettling(true)) {
            // true 动画没有结束, 需要继续重绘界面
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void close() {
        close(true);
    }

    /**
     * 关闭
     */
    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            // 1. 触发一个平滑动画, 封装了Scroller
            if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
                // true 表示控件还没有移动到指定位置, 需要位置修改. 重绘界面.
                ViewCompat.postInvalidateOnAnimation(this); // 执行动画重绘界面
            }
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    // 2. 转交拦截判断, 传递触摸事件
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 传递触摸事件
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 容错性, 健壮性检查
        // 子控件个数检查
        if (getChildCount() < 2) {
            throw new IllegalStateException("控件个数必须至少2个, Your viewgroup must contains 2 children at least.");
        }
        // 子控件类型检查
        if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("子控件必须是ViewGroup的实现类, Your child must be an instance of ViewGroup");
        }

        // 查找子控件
        mLeftMenu = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);

        System.out.println("mLeftMenu: " + mLeftMenu.toString());
        System.out.println("mMainContent: " + mMainContent.toString());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // onMeasure()之后, 发现控件的宽高有变化时, 会被调用

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mRange = (int) (mWidth * 0.6f);

    }

    public interface OnDragChangeListener {
        void onOpen();

        void onClose();

        void onDraging(float percent);
    }

}

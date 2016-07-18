package pub.ven.androiddemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧拉删除
 * @author poplar
 *
 */
public class SwipeLayout extends FrameLayout {

	private ViewDragHelper mDragHelper;
	private View mBackView;
	private View mFrontView;
	private int mRange;
	private int mWidth;
	private int mHeight;
	
	public static enum Status{
		Close, Open, Draging
	}
	private Status status = Status.Close;
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	private OnSwipeListener onSwipeListener;
	
	public OnSwipeListener getOnSwipeListener() {
		return onSwipeListener;
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.onSwipeListener = onSwipeListener;
	}

	public interface OnSwipeListener{
		void onClose(SwipeLayout layout);
		void onOpen(SwipeLayout layout);
		
		void onStartOpen(SwipeLayout layout);
		void onStartClose(SwipeLayout layout);
	}

	public SwipeLayout(Context context) {
		this(context, null);
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// 1. 初始化ViewDragHelper
		mDragHelper = ViewDragHelper.create(this, callback);
		
	}
	
	// 3. 重写事件回调
	ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
		
		@Override
		public boolean tryCaptureView(View view, int pointer) {
			return true;
		}
		
		// 水平方向拖拽范围 >= 0,  不限定真正的范围
		@Override
		public int getViewHorizontalDragRange(View child) {
			return mRange;
		}

		// 修正将要移动到的位置
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			
			if(child == mFrontView){
				if(left < -mRange){
					// 限定前布局, 左边界
					left = -mRange;
				}else if (left > 0) {
					// 限定前布局, 右边界
					left = 0;
				}
			}else if (child == mBackView) {
				if(left < mWidth - mRange){
					// 限定后布局, 左边界
					left = mWidth - mRange;
				}else if (left > mWidth) {
					// 限定后布局, 右边界
					left = mWidth;
				}
			}
			
			return left;
		};

		// 位置发生变化后, 被调用. 联动
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			// dx 刚刚发生的水平方向变化量
			
			if(changedView == mFrontView){
				// 前布局被拖拽, 将水平变化量dx, 传递给后布局
				mBackView.offsetLeftAndRight(dx);
			}else if (changedView == mBackView) {
				// 后布局被拖拽, 将水平变化量dx, 传递给前布局
				mFrontView.offsetLeftAndRight(dx);
			}
			
			dispatchDragEvent();
			
			invalidate(); // 重绘界面
		}


		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			
			// 只先考虑所有打开情况, 其他都是关闭情况
			if(xvel == 0 && mFrontView.getLeft() < -mRange * 0.5f){
				open();
			} else if (xvel < 0) {
				open();
			} else {
				close();
			}
		}

	};
	
	// 2. 转交拦截判断, 传递触摸事件
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDragHelper.shouldInterceptTouchEvent(ev);
	};
	
	protected void dispatchDragEvent() {
		// 更新状态, 执行监听回调
		
		Status lastStatus = status; // 记录上一次状态
		status = updateStatus(); // 获取更新当前状态
		
		// 状态变化的时候才执行监听回调
		if(lastStatus != status && onSwipeListener != null){
			if(status == Status.Open){
				onSwipeListener.onOpen(this);
			}else if (status == Status.Close) {
				onSwipeListener.onClose(this);
			}else if (status == Status.Draging) {
				if(lastStatus == Status.Close){
					onSwipeListener.onStartOpen(this); // 将要打开
				}else if (lastStatus == Status.Open) {
					onSwipeListener.onStartClose(this); // 将要关闭
				}
			}
		}
		
	}

	private Status updateStatus() {
		int left = mFrontView.getLeft();
		if(left == -mRange){
			return Status.Open;
		}else if (left == 0) {
			return Status.Close;
		}
		return Status.Draging;
	}

	public void close() {
		close(true);
	}
	
	public void close(boolean isSmooth){
		int finalLeft = 0;
		if(isSmooth){
			// 1. 触发一个平滑动画
			if(mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(true);
		}
	}

	public void open() {
		open(true);
	}
	
	public void open(boolean isSmooth){
		int finalLeft = -mRange;
		if(isSmooth){
			// 1. 触发一个平滑动画
			if(mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(true);
		}
	}
	
	// 2. onDraw会被调用, 引发下一次重绘
	// 维持动画的继续
	@Override
	public void computeScroll() {
		super.computeScroll();
		
		if(mDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this); // -> onDraw() -> computeScroll();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
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
		
		mBackView = getChildAt(0); // 后布局
		mFrontView = getChildAt(1); // 前布局
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = mFrontView.getMeasuredWidth();
		mHeight = mFrontView.getMeasuredHeight();
		mRange = mBackView.getMeasuredWidth();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		layoutContent(false);
	}

	/**
	 * 摆放布局内容
	 * @param isOpen
	 */
	private void layoutContent(boolean isOpen) {
		Rect frontRect = computeFrontRect(isOpen);
		// 摆放前布局
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
		
		Rect backRect = computeBackRectViaFront(frontRect);
		// 摆放后布局
		mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
		
		// 将某个布局前置
		bringChildToFront(mFrontView);
	}

	/**
	 * 根据前布局的位置, 计算出后布局的位置(矩形区域)
	 * @param frontRect
	 * @return 后布局矩形
	 */
	private Rect computeBackRectViaFront(Rect frontRect) {
		int left = frontRect.right;
		return new Rect(left, 0, left + mRange, 0 + mHeight);
	}

	// 计算出前布局的矩形区域
	// 根据是否打开状态
	private Rect computeFrontRect(boolean isOpen) {
		int left = 0;
		if(isOpen){
			left = -mRange;
		}
		return new Rect(left, 0, left + mWidth, 0 + mHeight);
	}
	
	
	
	
	

	
	
	
}

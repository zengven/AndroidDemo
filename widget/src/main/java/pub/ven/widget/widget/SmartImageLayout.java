package pub.ven.widget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: zengven
 * date: 2016/11/28
 * Desc: 根据图片数量自动排列图片
 */

public class SmartImageLayout extends ViewGroup {

    private static final String TAG = "SmartImageLayout";
    private int mNumColumns = 1; //default one columns

    public SmartImageLayout(Context context) {
        super(context);
    }

    public SmartImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmartImageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //MeasureSpec 测量参数
        if (getChildCount() == 0)
            return;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount == 0)
            return;
        int measuredWidth = getMeasuredWidth();
        int childHeight;
        int childWidth;
        childWidth = childHeight = measuredWidth / mNumColumns; //ensure child width and height
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(childWidth * (i % 3), i / 3 * childHeight, childWidth * (i % 3 + 1), (i / 3 + 1) * childHeight);
//            switch (mNumColumns) {
//                case 3:
//                    Log.i(TAG, "onLayout: l: " + childWidth * i + " t: " + i / 3 * childHeight + " r: " + childWidth * (i + 1) + " b: " + (i / 3 + 1) * childHeight);
//                    childView.layout(childWidth * (i % 3), i / 3 * childHeight, childWidth * (i % 3 + 1), (i / 3 + 1) * childHeight);
//                    break;
//                case 2:
//
//                    break;
//                case 1:
//                    childView.layout(0, 0, childWidth, childHeight);
//                    break;
//                default:
//                    break;
//            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureNumColumns(getChildCount());
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        ensureNumColumns(getChildCount());
    }

    private void ensureNumColumns(int childCount) {
        if (childCount / 3 != 0) {
            mNumColumns = 3;
            return;
        } else if (childCount / 2 != 0) {
            mNumColumns = 2;
            return;
        } else {
            mNumColumns = 1;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }
}

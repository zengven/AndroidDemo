package pub.ven.widget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import pub.ven.widget.R;

/**
 * author: zengven
 * date: 2016/11/25
 * Desc: 带标签Textview
 */

public class TagTextView extends TextView {

    private static final String TAG = "TagTextView";
    // TODO: How can we get this from the XML instead of hardcoding it here?
    private static final int TRIANGLE = 1;
    private static final int CIRCLE = 2;

    private float mTagWidth;
    private float mTagHeight;
    private boolean mTagVisible;
    private int mTagStyle;
    private int mTagColor;
    private float mTagRadius;

    private Paint mPaint;
    private Path mPath;
    private RectF mRectF;

    public TagTextView(Context context) {
        super(context);
        init(context, null);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagTextView);
            mTagWidth = typedArray.getDimensionPixelSize(R.styleable.TagTextView_tagWidth, 0);
            mTagHeight = typedArray.getDimensionPixelSize(R.styleable.TagTextView_tagHeight, 0);
            mTagRadius = typedArray.getDimensionPixelSize(R.styleable.TagTextView_tagRadius, 0);
            mTagColor = typedArray.getColor(R.styleable.TagTextView_tagColor, Color.WHITE);
            mTagVisible = typedArray.getBoolean(R.styleable.TagTextView_tagVisible, false);
            mTagStyle = typedArray.getInt(R.styleable.TagTextView_tagStyle, -1);
            Log.i(TAG, "init: mTagWidth : " + mTagWidth + " mTagHeight: " + mTagHeight + " mTagRadius: " + mTagRadius + " mTagVisible: " + mTagVisible + " mTagStyle: " + mTagStyle);
            typedArray.recycle();
        }
        mPaint = new Paint();
//        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿标志
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setDither(true); //防抖动
        mPaint.setStyle(Paint.Style.FILL); //这个镂空
        mPaint.setStrokeWidth(4); //设置画笔的宽度
//        mPaint.setPathEffect(new CornerPathEffect(12)); //设置画笔效果
//        mPaint.setShader(new LinearGradient(0, 0, 100, 100, new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, null, Shader.TileMode.REPEAT));//设置渐变
        mPath = new Path();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTagVisible) {
            int measuredWidth = getMeasuredWidth();
            mPath.reset();
            mRectF.setEmpty();
            mPaint.setColor(mTagColor);//设置背景颜色
            switch (mTagStyle) {
                case TRIANGLE:
                    mPath.moveTo(measuredWidth - mTagWidth, 0);
                    mPath.lineTo(measuredWidth - mTagRadius, 0);
                    if (mTagRadius != 0) {
                        mRectF.set(measuredWidth - 2 * mTagRadius, 0, measuredWidth, 2 * mTagRadius);
                        mPath.arcTo(mRectF, 270, 90);
                    }
                    mPath.lineTo(measuredWidth, mTagHeight);
                    mPath.close();
                    canvas.drawPath(mPath, mPaint);
                    break;
                case CIRCLE:
                    mRectF.set(measuredWidth - 2 * mTagRadius, 0, measuredWidth, 2 * mTagRadius);
                    mPath.addArc(mRectF, 0, 360);
                    canvas.drawPath(mPath, mPaint);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * set tag width
     *
     * @param pixels
     */
    public void setTagWidth(int pixels) {
        mTagWidth = pixels;
    }

    /**
     * set tag height
     *
     * @param pixels
     */
    public void setTagHeight(int pixels) {
        mTagHeight = pixels;
    }

    /**
     * set tag is visible
     *
     * @param visible
     */
    public void setTagVisible(boolean visible) {
        mTagVisible = visible;
        postInvalidate();
    }

    /**
     * set topright radius
     *
     * @param pixels
     */
    public void setTagTopRightRadius(int pixels) {
        mTagRadius = pixels;
    }

    /**
     * set tag color
     *
     * @param color
     */
    public void setTagColor(int color) {
        mTagColor = color;
        postInvalidate();
    }

    /**
     * set tag style
     *
     * @param style
     */
    public void setTagStyle(int style) {

    }

}

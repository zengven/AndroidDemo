package pub.ven.share.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: zengven
 * date: 2016/9/22
 * Desc: 高自适应viewpager
 */

public class AdaptiveHeightViewPager extends ViewPager {

    public AdaptiveHeightViewPager(Context context) {
        super(context);
    }

    public AdaptiveHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount()>0){
            View child = getChildAt(0);
            child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight(); //获取第一个child高
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);//重新测量viewpager高
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

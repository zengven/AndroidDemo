package pub.ven.share.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import pub.ven.share.widget.GridViewWithoutScroll;
import pub.ven.share.constant.ShareSupportPlatform;

/**
 * author: zengven
 * date: 2016/9/18
 * Desc: 分享页面适配器
 */
public class SharePageAdapter extends PagerAdapter {
    private static final String TAG = "SharePageAdapter";
    private HashMap<Integer, List<ShareSupportPlatform>> mSupportPlatformMap;
    private int numColumns;

    public SharePageAdapter(HashMap<Integer, List<ShareSupportPlatform>> supportPlatformMap, int numColumns) {
        this.mSupportPlatformMap = supportPlatformMap;
        this.numColumns=numColumns;
    }

    @Override
    public int getCount() {
        return mSupportPlatformMap == null ? 0 : mSupportPlatformMap.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridViewWithoutScroll gridView = new GridViewWithoutScroll(container.getContext());
        gridView.setNumColumns(numColumns);
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.height= ViewPager.LayoutParams.WRAP_CONTENT;
        layoutParams.width= ViewPager.LayoutParams.MATCH_PARENT;
        gridView.setLayoutParams(layoutParams);
        gridView.setAdapter(new ShareGridAdapter(mSupportPlatformMap.get(position)));
        container.addView(gridView);
        return gridView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

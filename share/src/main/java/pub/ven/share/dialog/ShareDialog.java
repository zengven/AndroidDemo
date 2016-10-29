package pub.ven.share.dialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pub.ven.share.R;
import pub.ven.share.adapter.SharePageAdapter;
import pub.ven.share.constant.ShareSupportPlatform;
import pub.ven.share.utils.UIUtils;

/**
 * author: zengven
 * date: 2016/9/18
 * Desc: 分享dialog
 */
public class ShareDialog extends BaseDialog {

    private static final String TAG = "ShareDialog";
    private static final int NUM_COLUMNS = 4;//每页显示列数
    private static final int NUM_LINE = 2; //每页显示行数
    private HashMap<Integer, List<ShareSupportPlatform>> mSupportPlatformMap;

    public ShareDialog(Context context) {
        super(context);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void initData() {
        super.initData();
        ShareSupportPlatform[] shareSupportPlatforms = ShareSupportPlatform.values();
        List<ShareSupportPlatform> supportPlatformList = Arrays.asList(shareSupportPlatforms);
        mSupportPlatformMap = new HashMap<>();
        for (int i = 0; i <= supportPlatformList.size(); i++) { //最多循环次数
            if (supportPlatformList.size() / (NUM_COLUMNS * NUM_LINE * (i + 1)) != 0 ) {
                List<ShareSupportPlatform> subList = supportPlatformList.subList(i * NUM_COLUMNS * NUM_LINE, NUM_COLUMNS * NUM_LINE * (i + 1));
                mSupportPlatformMap.put(i, subList);
            } else {
                if (supportPlatformList.size() % (NUM_COLUMNS * NUM_LINE ) != 0){
                    List<ShareSupportPlatform> subList = supportPlatformList.subList(i * NUM_COLUMNS * NUM_LINE, supportPlatformList.size());
                    mSupportPlatformMap.put(i, subList);
                }
                break;
            }
        }
    }

    @Override
    protected void initView() {
        View view = mLayoutInflater.inflate(R.layout.dialog_share, null);
        this.setContentView(view);
        setDialogStyle(Mode.Fill, Gravity.BOTTOM, R.style.DialogWindowAnim);
        ViewPager shareContent = (ViewPager) view.findViewById(R.id.vp_share_content);
        LinearLayout shareInducator = (LinearLayout) view.findViewById(R.id.ll_share_indicator);
        SharePageAdapter sharePageAdapter = new SharePageAdapter(mSupportPlatformMap, NUM_COLUMNS);
        shareContent.setAdapter(sharePageAdapter);
        initDots(mSupportPlatformMap.size(), shareInducator);
        setDotState(0);
        shareContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDotState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private View[] dots;

    private void initDots(int num, LinearLayout container) {
        // 初始化缓存圆点数组
        container.removeAllViews();
        if (num != 0) {
            dots = new View[num];
            for (int i = 0; i < num; i++) {
                dots[i] = new View(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        UIUtils.dip2px(mContext, 5),
                        UIUtils.dip2px(mContext, 5));
                if (i != 0) {
                    params.setMarginStart(UIUtils.dip2px(mContext, 5));
                }
                dots[i].setLayoutParams(params);
                dots[i].setBackgroundResource(R.drawable.dot_selector);
                container.addView(dots[i]);
            }
        }
    }

    private void setDotState(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setSelected(false);
        }
        dots[position].setSelected(true);
    }
}

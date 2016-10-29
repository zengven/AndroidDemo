package pub.ven.share.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import pub.ven.share.R;

/**
 * author: zengven
 * date: 2016/9/18
 * Desc: dialog通用基类
 */
public abstract class BaseDialog extends Dialog {

    private static final String TAG = "BaseDialog";
    //dialog显示样式
    protected enum Mode {
        Fill, Default
    }

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId == 0 ? R.style.CustomDialog : themeResId); //默认主题,子类可自定义
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        initData();
        initView();
    }

    protected void initData(){};

    protected abstract void initView();

    /**
     * 设置dialog样式
     *
     * @param mode    dialog显示样式
     * @param gravity dialog显示位置
     * @param resId   dialog进入退出动画
     */
    protected void setDialogStyle(Mode mode, int gravity, int resId) {
        Window dialogWindow = getWindow();
        if (mode != null && mode == Mode.Fill) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            lp.width = (int) (displayMetrics.widthPixels);//dialog铺满整个屏幕
            dialogWindow.setAttributes(lp);
        }
        if (gravity != 0){
            dialogWindow.setGravity(gravity); //设置在屏幕的位置
        }
        if (resId != 0)
            dialogWindow.setWindowAnimations(resId);//设置动画
    }

}

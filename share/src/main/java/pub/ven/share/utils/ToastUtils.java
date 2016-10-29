package pub.ven.share.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * author: zengven
 * date: 2016/8/26
 * Desc: toast工具类
 */
public class ToastUtils {

    private static Toast toast;

    private ToastUtils(){
    }

    public static void show(Context context,int resId){
        show(context,resId,Toast.LENGTH_SHORT);
    }

    public static void show(Context context,CharSequence text){
        show(context,text,Toast.LENGTH_SHORT);
    }

    public static void show(Context context,CharSequence text,int len){
        if (toast==null){
            toast = Toast.makeText(context,text,len);
        }else{
            toast.setDuration(len);
            toast.setText(text);
        }
        toast.show();
    }

    public static void show(Context context,int resId,int len){
        if (toast==null){
            toast = Toast.makeText(context,resId,len);
        }else{
            toast.setDuration(len);
            toast.setText(resId);
        }
        toast.show();
    }
}

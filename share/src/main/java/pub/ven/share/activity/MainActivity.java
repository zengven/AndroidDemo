package pub.ven.share.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import pub.ven.share.R;
import pub.ven.share.dialog.ShareDialog;

public class MainActivity extends AppCompatActivity {
    private Button mBtnShare;
    private ShareDialog shareDialog;
    private ToggleButton mBtnPpwAnim;
    private Context mContext;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        mBtnShare = (Button) findViewById(R.id.btn_share);
        mBtnPpwAnim = (ToggleButton) findViewById(R.id.tlBtn_anim);
        View view = LayoutInflater.from(mContext).inflate(R.layout.ppw_anim, null);
        popupWindow = new PopupWindow(view, 400,400);
        //popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.PopupWindowAmin);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        mBtnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (shareDialog == null)
                    shareDialog = new ShareDialog(MainActivity.this);
                shareDialog.show();
            }
        });
        mBtnPpwAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(mBtnPpwAnim);
            }
        });
//        mBtnPpwAnim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    popupWindow.showAsDropDown(mBtnPpwAnim);
//                }else{
//                    popupWindow.dismiss();
//                }
//            }
//        });
    }
}

package pub.ven.widget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import pub.ven.widget.R;
import pub.ven.widget.widget.SmartImageLayout;
import pub.ven.widget.widget.TagTextView;

public class MainActivity extends AppCompatActivity {

    private TagTextView mTtvTag;
    private ToggleButton mTbSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTtvTag = (TagTextView) findViewById(R.id.ttv_tag);
        mTbSwitch = (ToggleButton) findViewById(R.id.tb_toggle);
        mTtvTag.setTagVisible(true);
        mTbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTtvTag.setTagVisible(true);
                } else {
                    mTtvTag.setTagVisible(false);
                }
            }
        });

//        mTtvTag.setTagWidth(40);
//        mTtvTag.setTagHeight(40);
//        mTtvTag.setTagTopRightRadius(12);
        SmartImageLayout mImages = (SmartImageLayout) findViewById(R.id.sil_images);
//        for (int i = 0; i < 2; i++) {
//            ImageView imageView = new ImageView(getApplicationContext());
//            imageView.setImageResource(R.mipmap.ic_launcher);
//            if (i==1){
//                imageView.setScaleType(ImageView.ScaleType.CENTER);
//            }
//            mImages.addView(imageView);
//        }
    }
}

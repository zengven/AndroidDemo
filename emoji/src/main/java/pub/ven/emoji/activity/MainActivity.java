package pub.ven.emoji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.ven.emoji.R;
import pub.ven.emoji.utils.PreferencesUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_show)
    TextView mTvShow;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.btn_send)
    Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String emoji = PreferencesUtil.getInstance(getApplication()).getString("emoji", "");
        if (!TextUtils.isEmpty(emoji)){
            mTvShow.setText(emoji);
        }
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.i(TAG, " onTextChanged :" + s);
                //mTvShow.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editableText = mEtInput.getEditableText();
                CharSequence charSequence = editableText.subSequence(0, editableText.length());
                mTvShow.setText(charSequence);
                Log.i(TAG, " onClick :" + charSequence.toString());
                PreferencesUtil.getInstance(getApplicationContext()).putString("emoji",charSequence.toString());
            }
        });
    }


}

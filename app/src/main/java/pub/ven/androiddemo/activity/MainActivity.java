package pub.ven.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import pub.ven.androiddemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button mBtnRefresh;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mBtnRefresh = (Button) findViewById(R.id.btn_pull_refresh);
        mBtnRefresh.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        switch (vid){
            case R.id.btn_pull_refresh:
                start(mContext,RefreshActivity.class);
                break;
        }
    }

    public static void start(Context context,Class cls) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

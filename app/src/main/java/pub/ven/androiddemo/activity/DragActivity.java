package pub.ven.androiddemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import pub.ven.androiddemo.R;

public class DragActivity extends AppCompatActivity {

    private SwipeRefreshLayout mRefresh;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_drag);

    }
}
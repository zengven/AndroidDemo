package pub.ven.androiddemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import pub.ven.androiddemo.R;
import pub.ven.androiddemo.widget.SwipeRefreshToLayout;

public class DragActivity extends AppCompatActivity {

    private SwipeRefreshToLayout mrefresh;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mrefresh.onRefreshComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        mrefresh = (SwipeRefreshToLayout) findViewById(R.id.activity_drag);
        mrefresh.setMode(SwipeRefreshToLayout.Mode.PullUp);
        mrefresh.setOnRefreshListener(new SwipeRefreshToLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(1,2000);
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(1,2000);
            }
        });
    }
}

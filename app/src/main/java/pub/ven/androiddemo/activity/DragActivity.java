package pub.ven.androiddemo.activity;

import android.os.Bundle;
<<<<<<< HEAD
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import pub.ven.androiddemo.R;

public class DragActivity extends AppCompatActivity {

    private SwipeRefreshLayout mRefresh;
=======
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
>>>>>>> 91bba06be752afcf84ab1b65d429aa3a38205c45

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
<<<<<<< HEAD
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_drag);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(false);
=======
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
>>>>>>> 91bba06be752afcf84ab1b65d429aa3a38205c45
            }
        });
    }
}

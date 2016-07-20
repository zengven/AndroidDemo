package pub.ven.androiddemo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pub.ven.androiddemo.R;
import pub.ven.androiddemo.config.Cheeses;
import pub.ven.androiddemo.widget.SwipeRefreshToLayout;


public class RefreshActivity extends AppCompatActivity {

    private Context mContext;
    private SwipeRefreshToLayout refreshLayout;

//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            refreshLayout.setRefreshing(false);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        mContext = getApplicationContext();
        refreshLayout = (SwipeRefreshToLayout) findViewById(R.id.srl_refresh);
        ListView lvRefresh =  (ListView) findViewById(R.id.lv_refresh);
        lvRefresh.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1, Cheeses.NAMES){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(Color.BLACK);
                return view;
            }
        });

//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mHandler.sendEmptyMessageDelayed(1,1000);
//            }
//        });

    }
}

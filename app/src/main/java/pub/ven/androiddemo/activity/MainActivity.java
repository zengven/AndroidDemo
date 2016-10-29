package pub.ven.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import pub.ven.androiddemo.R;
import pub.ven.androiddemo.adapter.MainRecyclerAdapter;
import pub.ven.androiddemo.bean.JoinOrOutBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    private Context mContext;
    private static final String mJson="{\"ret\":3,\"msg\":\"request is time out\",\"data\":[]}";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        Toolbar toolbar= (Toolbar) findViewById(R.id.tb_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        //tbMain.setNavigationIcon(R.drawable.example_appwidget_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //设置toolbar显示返回按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { //给返回按钮设置点击事件
            @Override
            public void onClick(View v) {
            }
        });
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));//设置布局管理器
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加删除特效
        MainRecyclerAdapter mainRecyclerAdapter = new MainRecyclerAdapter();
        mRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        JoinOrOutBean joinOrOutBean= gson.fromJson(mJson, new TypeToken<JoinOrOutBean>() {
        }.getType());
        Log.i(TAG,joinOrOutBean.toString());
    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        switch (vid){
//            case R.id.btn_pull_refresh:
//                start(mContext,RefreshActivity.class);
//                break;
//            case R.id.btn_scrolling:
//                //start(mContext,ScrollingActivity.class);
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("哈哈");
//                builder.setMessage("呵呵呵呵呵呵");
//                final AlertDialog alertDialog = builder.create();
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        alertDialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        alertDialog.dismiss();
//                    }
//                });
//                alertDialog.show();
//                break;
        }
    }

    public static void start(Context context,Class cls) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        // TODO: 2016/9/13 下拉刷新
    }
}

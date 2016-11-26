package pub.ven.reply;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private Button commentButton;		//评论按钮
	private EditText commentEdit;		//评论输入框
	private TextView senderNickname;	//发表者昵称
	private TextView sendTime;			//发表的时间
	private TextView sendContent;		//发表的内容
	private TextView shareText;			//底部分享
	private TextView commentText;		//底部评论
	private TextView praiseText;		//底部点赞
	private TextView collectionText;	//底部收藏
	private ImageView senderImg;		//发送者图片
	private ImageView shareImg;			//分享的图片
	private ImageView commentImg;		//评论的图片
	private ImageView praiseImg;		//点赞的图片
	private NoScrollListView commentList;//评论数据列表
	private LinearLayout bottomLinear;	//底部分享、评论等线性布局
	private LinearLayout commentLinear;	//评论输入框线性布局
	
	private int count;					//记录评论ID
	private int position;				//记录回复评论的索引
	private int[] imgs;					//图片资源ID数组
	private boolean isReply;			//是否是回复
	private String comment = "";		//记录对话框中的内容
	private CommentAdapter adapter;
	private List<CommentBean> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initViews();
		init();
	}
	
	/**
	 * 初始化UI界面
	 */
	private void initViews(){
		commentButton = (Button) findViewById(R.id.commentButton);
		commentEdit = (EditText) findViewById(R.id.commentEdit);
		senderNickname = (TextView) findViewById(R.id.senderNickname);
		sendTime = (TextView) findViewById(R.id.sendTime);
		sendContent = (TextView) findViewById(R.id.sendContent);
		shareText = (TextView) findViewById(R.id.shareText);
		commentText = (TextView) findViewById(R.id.commentText);
		praiseText = (TextView) findViewById(R.id.praiseText);
		collectionText = (TextView) findViewById(R.id.collectionText);
		senderImg = (ImageView) findViewById(R.id.senderImg);
		shareImg = (ImageView) findViewById(R.id.shareImg);
		commentImg = (ImageView) findViewById(R.id.commentImg);
		praiseImg = (ImageView) findViewById(R.id.praiseImg);
		commentList = (NoScrollListView) findViewById(R.id.commentList);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
		commentLinear = (LinearLayout) findViewById(R.id.commentLinear);
		
		ClickListener cl = new ClickListener();
		commentButton.setOnClickListener(cl);
		shareText.setOnClickListener(cl);
		commentText.setOnClickListener(cl);
		praiseText.setOnClickListener(cl);
		collectionText.setOnClickListener(cl);
		shareImg.setOnClickListener(cl);
		commentImg.setOnClickListener(cl);
		praiseImg.setOnClickListener(cl);
	}
	
	/**
	 * 初始化数据
	 */
	private void init(){
		//初始化发布者信息
		InputStream is = getResources().openRawResource(R.drawable.dynamic1);
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		senderImg.setImageBitmap(bitmap);
		senderNickname.setText("5疯子");
		sendTime.setText("13:00");
		sendContent.setText("我不想不想不想出差，要过年过年过年。好好玩玩玩玩玩玩");
		
		adapter = new CommentAdapter(this, getCommentData(),R.layout.comment_item,handler);
		commentList.setAdapter(adapter);
	}
	
	/**
	 * 获取评论列表数据
	 */
	private List<CommentBean> getCommentData(){
		imgs = new int[]{R.drawable.dynamic1,R.drawable.dynamic2,
				R.drawable.dynamic3,R.drawable.dynamic4,R.drawable.dynamic4,R.drawable.dynamic4,R.drawable.dynamic4};
		list = new ArrayList<CommentBean>();
		count = imgs.length;
		for(int i=0;i<imgs.length;i++){
			CommentBean bean = new CommentBean();
			bean.setId(i+1);
			bean.setCommentImgId(imgs[i]);
			bean.setCommentNickname("昵称"+i);
			bean.setCommentTime("13:"+i+"5");
			bean.setCommnetAccount("12345"+i);
			bean.setCommentContent("评论内容评论内容评论内容");
			bean.setReplyList(getReplyData());
			list.add(bean);
		}
		return list;
	}
	
	/**
	 * 获取回复列表数据
	 */
	private List<ReplyBean> getReplyData(){
		List<ReplyBean> replyList = new ArrayList<ReplyBean>();
		return replyList;
	}
	
	/**
	 * 显示或隐藏输入法
	 */
	private void onFocusChange(boolean hasFocus){  
		final boolean isFocus = hasFocus;
	(new Handler()).postDelayed(new Runnable() {  
		public void run() {
			InputMethodManager imm = (InputMethodManager)  
					commentEdit.getContext().getSystemService(INPUT_METHOD_SERVICE);  
			if(isFocus)  {
				//显示输入法
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}else{
				//隐藏输入法
				imm.hideSoftInputFromWindow(commentEdit.getWindowToken(),0);
			}  
		}  
	  }, 100);
	}  
	
	/**
	 * 判断对话框中是否输入内容
	 */
	private boolean isEditEmply(){
		comment = commentEdit.getText().toString().trim();
		if(comment.equals("")){
			Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		commentEdit.setText("");
		return true;
	}
	
	/**
	 * 发表评论
	 */
	private void publishComment(){
		CommentBean bean = new CommentBean();
		bean.setId(count);
		bean.setCommentImgId(imgs[count%4]);
		bean.setCommentNickname("昵称"+count);
		bean.setCommentTime("13:"+count%6+"5");
		bean.setCommnetAccount("12345"+count);
		bean.setCommentContent(comment);
		list.add(bean);
		count++;
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 回复评论
	 */
	private void replyComment(){
		ReplyBean bean = new ReplyBean();
		bean.setId(count+10);
		bean.setCommentNickname(list.get(position).getCommentNickname());
		bean.setReplyNickname("我是回复的人");
		bean.setReplyContent(comment);
		adapter.getReplyComment(bean, position);
		adapter.notifyDataSetChanged();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 10){
				isReply = true;
				position = (Integer)msg.obj;
				commentLinear.setVisibility(View.VISIBLE);
				bottomLinear.setVisibility(View.GONE);
				onFocusChange(true);
			}
		}
	};
	
	/**
	 * 事件点击监听器
	 */
	private final class ClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commentButton:	//发表评论按钮
				if(isEditEmply()){		//判断用户是否输入内容
					if(isReply){
						replyComment();
					}else{
						publishComment();
					}
					bottomLinear.setVisibility(View.VISIBLE);
					commentLinear.setVisibility(View.GONE);
					onFocusChange(false);
				}
				break;
			case R.id.shareImg:			//分享按钮
			case R.id.shareText:		//底部分享按钮
				Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
				break;
			case R.id.commentImg:		//评论按钮
			case R.id.commentText:		//底部评论按钮
				isReply = false;
				commentLinear.setVisibility(View.VISIBLE);
				bottomLinear.setVisibility(View.GONE);
				onFocusChange(true);
				break;
			case R.id.praiseImg:		//点赞按钮
			case R.id.praiseText:		//底部点赞按钮
				Toast.makeText(getApplicationContext(), "点赞成功", Toast.LENGTH_SHORT).show();
				break;
			case R.id.collectionText:	//底部收藏按钮
				Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//判断控件是否显示
		if(commentLinear.getVisibility() == View.VISIBLE){
			commentLinear.setVisibility(View.GONE);
			bottomLinear.setVisibility(View.VISIBLE);
		}
	}
	
}

package pub.ven.emoji.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.ven.emoji.R;

public class RichTextActivity extends AppCompatActivity {

    private static final String TAG = "RichTextActivity";

    private String mRichText = "今天，第十一届中国航展将在广东珠海开幕。在这个大杀器集结的平台上，此次珠海航展到底该看点什么？\n" +
            "\n" +
            "　　从“黑丝带”歼-20首秀起舞，到“胖妞”运-20震撼腾空，再到国产无人机“彩虹”当空……中新网（微信公众号：cns2012）记者梳理此次珠海航展即将上演的十大炫酷镜头，带你先睹为快。今天，第十一届中国航展将在广东珠海开幕。在这个大杀器集结的平台上，此次珠海航展到底该看点什么？\n" +
            "\n" +
            "　　从“黑丝带”歼-20首秀起舞，到“胖妞”运-20震撼腾空，再到国产无人机“彩虹”当空……中新网（微信公众号：cns2012）记者梳理此次珠海航展即将上演的十大炫酷镜头，带你先睹为快。<img src='http://imgsrc.baidu.com/forum/w%3D580/sign=1367ab9109f3d7ca0cf63f7ec21ebe3c/521628f33a87e950be6f1fc315385343fbf2b438.jpg'>";

    @BindView(R.id.tv_text_rich)
    TextView mTvTextRich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Spanned spanned = Html.fromHtml(mRichText, mImageGetter, null);
        mTvTextRich.setText(spanned);
        mTvTextRich.setMovementMethod(LinkMovementMethod.getInstance());
    }

    Html.ImageGetter mImageGetter = new Html.ImageGetter() {

        @Override
        public Drawable getDrawable(String source) {
            Log.i(TAG, "getDrawable: source :" + source);
//            Picasso.with(RichTextActivity.this).load(source).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    drawable = new BitmapDrawable(bitmap);
//                    drawableCurrent = drawable.getCurrent();
//                    drawableCurrent.setBounds(0, 0, drawableCurrent.getIntrinsicWidth(), drawableCurrent.getIntrinsicHeight());
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });
//            Drawable drawable = getApplicationContext().getDrawable(R.mipmap.xiaodaidu);
//            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            Drawable drawable = null;
            InputStream is=null;
            try {
                is= (InputStream) new URL(source).getContent();
                drawable = Drawable.createFromStream(is, "src");
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                is.close();
                return drawable;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return drawable;
        }
    };

    private class DownLoadAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(params[0]).build();
            Bitmap bitmap = null;
            try {
                Response response = okHttpClient.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);

        }
    }
}

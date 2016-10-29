package pub.ven.share.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pub.ven.share.R;
import pub.ven.share.constant.ShareSupportPlatform;

/**
 * author: zengven
 * date: 2016/9/18
 * Desc:
 */
public class ShareGridAdapter extends BaseAdapter {
    private static final String TAG = "ShareGridAdapter";
    private List<ShareSupportPlatform> shareSupportPlatforms;

    public ShareGridAdapter(List<ShareSupportPlatform> shareSupportPlatforms) {
        this.shareSupportPlatforms = shareSupportPlatforms;
    }

    @Override
    public int getCount() {
        return shareSupportPlatforms == null ? 0 : shareSupportPlatforms.size();
    }

    @Override
    public Object getItem(int position) {
        return shareSupportPlatforms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShareSupportPlatform shareSupportPlatform = (ShareSupportPlatform) getItem(position);
        ViewHodler viewHodler;
        if (convertView==null){
            convertView=View.inflate(parent.getContext(), R.layout.item_share_grid,null);
            viewHodler=new ViewHodler(convertView);
            convertView.setTag(viewHodler);
        }else{
            viewHodler=(ViewHodler)convertView.getTag();
        }
        viewHodler.tvShare.setCompoundDrawablesRelativeWithIntrinsicBounds(0,shareSupportPlatform.getDraResId(),0,0);
        viewHodler.tvShare.setText(shareSupportPlatform.getStrResId());
        return convertView;
    }

    static class ViewHodler{
        TextView tvShare;

        public ViewHodler(View convertView) {
            tvShare= (TextView) convertView.findViewById(R.id.tv_item_share_content);
        }
    }
}

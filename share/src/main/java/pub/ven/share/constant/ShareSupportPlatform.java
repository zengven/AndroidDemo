package pub.ven.share.constant;

import pub.ven.share.R;

/**
 * author: zengven
 * date: 2016/9/18
 * Desc: 分享支持平台枚举
 */
public enum ShareSupportPlatform {

    SINAWEIBO(0, R.string.ssdk_sinaweibo, R.mipmap.ic_launcher),
    QZONE(1, R.string.ssdk_qzone, R.mipmap.ic_launcher),
    WECHAT(2, R.string.ssdk_wechat, R.mipmap.ic_launcher),
    WECHATMOMENTS(3, R.string.ssdk_wechatmoments, R.mipmap.ic_launcher),
    FACEBOOK(4, R.string.ssdk_facebook, R.mipmap.ic_launcher),
    TWITTER(5, R.string.ssdk_twitter, R.mipmap.ic_launcher),
    RENREN(6, R.string.ssdk_renren, R.mipmap.ic_launcher),
    KAIXIN(7, R.string.ssdk_kaixin, R.mipmap.ic_launcher),
    EMAIL(8, R.string.ssdk_email, R.mipmap.ic_launcher),
    SHORTMESSAGE(9, R.string.ssdk_shortmessage, R.mipmap.ic_launcher),
    DOUBAN(10, R.string.ssdk_douban, R.mipmap.ic_launcher);

    private int position; //对应的id
    private int strResId; //lable资源id
    private int draResId; //ICON资源id

    ShareSupportPlatform(int position, int strResId, int draResId) {
        this.position = position;
        this.strResId = strResId;
        this.draResId = draResId;
    }

    public int getPosition() {
        return position;
    }

    public int getStrResId() {
        return strResId;
    }

    public int getDraResId() {
        return draResId;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setStrResId(int strResId) {
        this.strResId = strResId;
    }

    public void setDraResId(int draResId) {
        this.draResId = draResId;
    }

    @Override
    public String toString() {
        return "ShareSupportPlatform{" +
                "position=" + position +
                ", strResId=" + strResId +
                ", draResId=" + draResId +
                '}';
    }
}

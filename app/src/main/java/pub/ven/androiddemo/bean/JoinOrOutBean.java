
package pub.ven.androiddemo.bean;

import java.util.List;

/**
 * author: zengven
 * date: 2016/9/2
 * Desc:
 */
public class JoinOrOutBean {
    public String ret;
    public String msg;
    public List<JoinOrOut> data;

    public class JoinOrOut {
        // 当前群组成员总数
        public String membernum;

        @Override
        public String toString() {
            return "JoinOrOut{" +
                    "membernum='" + membernum + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JoinOrOutBean{" +
                "ret='" + ret + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

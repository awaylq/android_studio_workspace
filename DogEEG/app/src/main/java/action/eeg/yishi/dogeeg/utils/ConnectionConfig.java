package action.eeg.yishi.dogeeg.utils;

import android.content.Context;

/**
 * Created by yishikeji_04 on 2017/3/7.
 */

public class ConnectionConfig {
    private Context context;
    private String ip;
    private int port;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

package action.eeg.yishi.dogeeg.utils;

import android.content.Context;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by yishikeji_04 on 2017/3/7.、
 *  封装连接断开连接方法
 */

public class ConnectionManager {
    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext; //弱引用 防止内存溢出
    private InetSocketAddress mAddress;
    private Socket socket;
    public ConnectionManager(ConnectionConfig config){
        this.mConfig=config;
        this.mContext=new WeakReference<Context>(config.getContext());
        init();
    }

    private void init() {
        mAddress=new InetSocketAddress(mConfig.getIp(),mConfig.getPort());
    }
    public boolean connect(){
        socket=new Socket();
        try {
            socket.connect(mAddress);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void disConnect(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

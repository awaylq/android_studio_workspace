package action.eeg.yishi.dogeeg.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * Created by yishikeji_04 on 2017/1/19.
 */

public class SocketUtil {
    public static Socket socket=null;
    public static ExecutorService threadPool;      //线程池


    //发送设备信息 （模式，设备ID，警犬编号，时间）
    public static void sendNum(final String mode, final String DeviceID, final String DogId, final Date time){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream output=new DataOutputStream(socket.getOutputStream());
                    String str=mode+","+DeviceID+DogId+time;
                    output.write(str.getBytes("GBK"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //发送数据(String:Date)
    public static void sendMessage(final String s){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream output=new DataOutputStream(socket.getOutputStream());
                    String str=s+":"+new Date(System.currentTimeMillis());
                    output.write(str.getBytes("GBK"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void setSocket(Socket sockets){
        socket=sockets;
    }
    public static Socket getSocket() {
        return socket;
    }


}

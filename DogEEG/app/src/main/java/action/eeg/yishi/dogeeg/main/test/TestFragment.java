package action.eeg.yishi.dogeeg.main.test;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.util.Date;

import action.eeg.yishi.dogeeg.R;
import action.eeg.yishi.dogeeg.utils.Info;
import action.eeg.yishi.dogeeg.utils.SocketUtil;


public class TestFragment extends Fragment implements View.OnClickListener{
    SocketUtil util;
    TextView tv;
    ScrollView scrollView;
    boolean OnOff=false;
    String type="";
    final int OVER=0;
    final int START=1;
    final int RECIVE=2;
    final int MARK=101;
    Socket socket=null;
    Spinner spinner;
    Message message;
    public  TestFragment() {
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUtil();
        initWidget();


    }

    /*初始化工具*/
    public void initUtil(){
        util=new SocketUtil();
    }

    /*初始化组件*/
    public void initWidget(){
        scrollView= (ScrollView)getActivity().findViewById(R.id.srollView);
        tv= (TextView) getActivity().findViewById(R.id.textView);
        Button start= (Button) getActivity().findViewById(R.id.button);
        Button mark1= (Button) getActivity().findViewById(R.id.button1);
        Button mark2= (Button) getActivity().findViewById(R.id.button2);
        Button mark3= (Button) getActivity().findViewById(R.id.button3);
        spinner= (Spinner) getActivity().findViewById(R.id.spinner);
        start.setOnClickListener(this);
        mark1.setOnClickListener(this);
        mark2.setOnClickListener(this);
        mark3.setOnClickListener(this);
        spinner.setOnItemSelectedListener(spinnerListener);
        socket=SocketUtil.getSocket();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /*
        * 点击事件
        * */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button://开始按钮 发送设备编号及时间戳
                if(!OnOff){
                    tv.setText("实验开始\n");
                    OnOff=true;
                    message=Message.obtain();
                    message.what=START;
                    handler.sendMessage(message);
                }else{
                    tv.setText("实验结束\n");
                    OnOff=false;
                    message=Message.obtain();
                    message.what=OVER;
                    handler.sendMessage(message);
                }
                break;
            case R.id.button1: case R.id.button2: case R.id.button3://mark按钮
                Button button= (Button) v;
                String mark= (String) button.getText();
                message=Message.obtain();
                message.what=MARK;
                message.obj=mark;
                handler.sendMessage(message);
                tv.append("点击了"+mark+"\n");
                break;
        }
    }


    /*
    * spinner监听
    * */
    public AdapterView.OnItemSelectedListener spinnerListener=new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            type=parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getActivity(),"请选择一种类型",Toast.LENGTH_SHORT).show();
        }
    };

    /*
    * handler
    * */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {//本地handler
            switch (msg.what){
                case START:
                    SocketUtil.sendNum("TEST", Info.getDeviceId(),Info.getDogId(),new Date(System.currentTimeMillis()));
                    break;
                case OVER:
                    SocketUtil.sendMessage("OVER");
                    break;
                case RECIVE:
                    tv.append(msg.obj+"\n");
                    break;
                case MARK:
                    String mark=msg.obj.toString();
                    String testType=spinner.getSelectedItem().toString();
                    SocketUtil.sendMessage(testType+":"+mark);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public void setMessage(String textMessage){
        Message message=Message.obtain();
        message.what=RECIVE;
        message.obj=textMessage;
        handler.sendMessage(message);
    }

}

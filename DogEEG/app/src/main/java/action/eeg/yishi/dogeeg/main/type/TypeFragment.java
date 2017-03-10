package action.eeg.yishi.dogeeg.main.type;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import action.eeg.yishi.dogeeg.R;



@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TypeFragment extends Fragment {
    ImageView anim;
    AnimationDrawable drawable;
    public TypeFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_type, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        anim= (ImageView) getActivity().findViewById(R.id.anim);
        setDrawable(R.drawable.no_act);
        drawable.start();
    }
    public void setDrawable(int resource){
            drawable= (AnimationDrawable) getResources().getDrawable(resource,null);
            anim.setBackground(drawable);
    }
    public void isRunning(){
        if(drawable!=null&&!drawable.isRunning()){
            drawable.stop();
            setDrawable(R.drawable.no_act);
            drawable.start();
        }
    }



    @Override
    public void onAttach(Context context) {//嵌入
        super.onAttach(context);
    }

    @Override
    public void onDetach() {//分离
        super.onDetach();
    }
    public void setMessage(String textMessage){
        Message message=Message.obtain();
        message.obj=textMessage;
        handler.sendMessage(message);

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj.equals("module1")){
                drawable.stop();
                setDrawable(R.drawable.act);
                drawable.start();
            }else if(msg.obj.equals("module2")){
                drawable.stop();
                setDrawable(R.drawable.no_act);
                drawable.start();
            }
        }
    };
}

package action.eeg.yishi.dogeeg.main;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import action.eeg.yishi.dogeeg.R;
import action.eeg.yishi.dogeeg.main.test.TestFragment;
import action.eeg.yishi.dogeeg.main.type.TypeFragment;
import action.eeg.yishi.dogeeg.utils.BottomView;
import action.eeg.yishi.dogeeg.utils.SocketUtil;

import static android.widget.Toast.LENGTH_LONG;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private static final String INSTANCE_STATE = "instance_state";
    public final int SUCCESS = 1;
    public final int FAIL = 2;
    public final int DISCON = 0;
    ViewPager viewPager;
    List<Fragment> fragments;
    BottomView[] bottoms;
    ImageView state;
    LinearLayout linearLayout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ExecutorService threadPool;      //线程池
    int[] nor = {R.drawable.shiyan_nor, R.drawable.yanshi_nor, R.drawable.naodian_nor, R.drawable.weizhi_nor};
    int[] sel = {R.drawable.shiyan_sel, R.drawable.yanshi_sel, R.drawable.naodian_sel, R.drawable.weizhi_sel};
    TestFragment testFragment;
    TypeFragment typeFragment;
    private String address = "60.205.137.182";
    private int port = 1112;
    private Socket socket = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    state.setImageResource(R.drawable.weizhi_nor);
                    SocketUtil.setSocket(socket);
                    break;
                case FAIL:case DISCON:
                    Toast.makeText(MainActivity.this,"连接断开",Toast.LENGTH_SHORT).show();
                    state.setImageResource(R.drawable.disconnect);
                    SocketUtil.setSocket(socket);
                    break;
            }

        }
    };
    private boolean flag = false;
    //toolbar点击事件
    private View.OnClickListener stateListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (socket == null) {//点击连接
                connect();
            } else {//再次点击断开连接
                disconnect();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /*
    * 初始化
    * */
    private void init() {
        //toolbar及侧边栏
        initToolbar();
        initDrawerToggle();

        //主布局
        initFragment();
        initViewpager();
        initBottoms();
        initThreadPool();

    }

    private void initThreadPool() {
        threadPool = Executors.newCachedThreadPool();
        SocketUtil.threadPool = threadPool;
    }

    /*
    * ---------------------------初始化toolbar
    */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //侧边栏的按钮
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitle(R.string.app_name);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        state= (ImageView) findViewById(R.id.stateIcon);
        state.setOnClickListener(stateListener);
    }

    /*
    * -----------------------------初始化侧边栏
     */
    private void initDrawerToggle() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
//        mDrawerToggle.setDrawerIndicatorEnabled(false);//禁用系统默认导航图标
//        mDrawerToggle.setHomeAsUpIndicator(R.mipmap.ic_launcher);//启用自定义图标

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {//点击导航栏图标方法
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mDrawerToggle.syncState();//与抽屉同步
        drawerLayout.addDrawerListener(mDrawerToggle);

        initDrawerMenu();//设置菜单功能
    }

    /*
    * 初始化侧边栏菜单
    * */
    private void initDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu:
                        Toast.makeText(MainActivity.this, "Home", LENGTH_LONG).show();
                        drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }

    /*
    * ---------------------------------------------初始化fragment
     */

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        testFragment=new TestFragment();
        typeFragment=new TypeFragment();
        fragments.add(testFragment);
        fragments.add(typeFragment);
//        fragments.add(new LocateFragment());
//        fragments.add(new LineFragment());

    }

    /*
    * ---------------------------------------------初始化viewpager
    * */
    private void initViewpager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
    }

    //-----Viewpager.OnPageChangeListener-----
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("Tag", "onPageScrolled: " + positionOffset);
        if (positionOffset > 0) {
            bottoms[position].setIconAlpha(1 - positionOffset);
            bottoms[position + 1].setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position) {
        restOtherTabs();
        bottoms[position].setIconAlpha(1.0f);
    }
    //------------------------------------------

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /*
    * -----------------------------------------------初始化底栏
    * */
    private void initBottoms() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        bottoms = new BottomView[linearLayout.getChildCount()];
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            bottoms[i] = (BottomView) linearLayout.getChildAt(i);
            bottoms[i].setOnClickListener(this);
            bottoms[i].setTag(i);
        }
        bottoms[0].setIconAlpha(1.0f);
    }

    //底栏点击事件
    @Override
    public void onClick(View v) {
        int i = (int) v.getTag();
        viewPager.setCurrentItem(i);
    }

    private void restOtherTabs() {
        for (BottomView bottomView : bottoms) {
            bottomView.setIconAlpha(0);
        }
    }

    /*
  * 连接
  * */
    public void connect() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(address, port);
                    flag = true;
                    Message message = Message.obtain();
                    message.what = SUCCESS;
                    handler.sendMessage(message);
                    receive();//接受数据
//                    isServerClose();//实时监测联网
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                    Message message = Message.obtain();
                    message.what = FAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /*
    * 断开连接
    * */
    public void disconnect() {
        flag = false;
        if (socket != null) {
            try {
                socket.close();
                Message message = Message.obtain();
                message.what = DISCON;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

    //判断是否断开连接 true为连接 false为断开
    public void isServerClose() {
        threadPool.execute(new Thread() {
            @Override
            public void run() {
                super.run();
                while (flag) {
                    try {
                        socket.sendUrgentData(0xFF);
                        this.sleep(1000);
                    } catch (Exception e) { //发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
                        e.printStackTrace();
                        flag = false;
                    }
                }
                Message message = Message.obtain();
                message.what = DISCON;
                handler.sendMessage(message);
            }
        });
    }

    public void receive() {      //接收服务器发送的消息通过广播方式发送
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    DataInputStream input = null;
                    try {
                        input = new DataInputStream(socket.getInputStream());
                        byte[] buffer = new byte[input.available()];
                        if (buffer.length != 0) {
                            input.read(buffer);
                            String message = new String(buffer, "GBK");
                            if (message.indexOf("signal") != -1) {
                                testFragment.setMessage(message);

                            } else if (message.indexOf("module") != -1) {
                                typeFragment.setMessage(message);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    //--------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }





}

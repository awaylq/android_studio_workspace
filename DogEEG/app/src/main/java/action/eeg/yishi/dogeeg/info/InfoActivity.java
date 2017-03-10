package action.eeg.yishi.dogeeg.info;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import action.eeg.yishi.dogeeg.R;

public class InfoActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
//        ImageView iv= (ImageView) findViewById(R.id.imageView);
//        Glide.with(this).load(R.drawable.dog_gif).into(iv);
         drawerLayout= (DrawerLayout) findViewById(R.id.drawerlayout);
         drawerLayout.addDrawerListener(drawerListener);

    }
    private DrawerLayout.DrawerListener drawerListener=new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}

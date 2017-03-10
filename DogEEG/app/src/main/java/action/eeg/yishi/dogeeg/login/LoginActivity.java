package action.eeg.yishi.dogeeg.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import action.eeg.yishi.dogeeg.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView regist= (TextView) findViewById(R.id.textView);
        regist.setOnClickListener(this);
        Button login= (Button) findViewById(R.id.button);
        login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView:
                Intent intent =new Intent();
                intent.setClass(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.button:
                Toast.makeText(LoginActivity.this,"点击了登陆",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

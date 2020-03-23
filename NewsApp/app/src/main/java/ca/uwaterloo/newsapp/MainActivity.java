package ca.uwaterloo.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import ca.uwaterloo.newsapp.ui.login.LoginActivity;
import ca.uwaterloo.newsapp.ui.news.NewsActivity;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.Globals;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        isLogin();
    }

    private void isLogin(){
        String userNameCache = ACache.get(this).getAsString(Globals.USERNAME_KEY);
//        startActivity(new Intent(this, LoginActivity.class));
        if (userNameCache==null){
            startActivity(new Intent(this,LoginActivity.class));
        }else {
            startActivity(new Intent(this, NewsActivity.class));
        }
    }

}

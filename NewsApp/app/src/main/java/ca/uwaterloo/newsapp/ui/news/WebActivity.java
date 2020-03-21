package ca.uwaterloo.newsapp.ui.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import ca.uwaterloo.newsapp.R;

public class WebActivity extends AppCompatActivity {

    private String urlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

    }
    @Override
    protected void onStart() {
        super.onStart();
        // 获取html页面的连接
        urlData = getIntent().getStringExtra("pageUrl");
        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.loadUrl(urlData);
    }
}

package ca.uwaterloo.newsapp.ui.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.uwaterloo.newsapp.R;

public class NewsActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }
}

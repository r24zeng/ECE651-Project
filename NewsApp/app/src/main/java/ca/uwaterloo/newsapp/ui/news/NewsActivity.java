package ca.uwaterloo.newsapp.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.newsapp.R;

public class NewsActivity  extends AppCompatActivity {
    BottomNavigationView bnView;
    ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        bnView = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.vp);


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fg_Headline());
        fragments.add(new Fg_Search());
        fragments.add(new Fg_Fevorite());
        fragments.add(new Fg_Account());

        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //BottomNavigationView 点击事件监听
        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuId = menuItem.getItemId();
                // 跳转指定页面：Fragment
                switch (menuId) {
                    case R.id.navigation_headline:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_search:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_favourite:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_account:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        // ViewPager 滑动事件监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //将滑动到的页面对应的 menu 设置为选中状态
                bnView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });






    }
}

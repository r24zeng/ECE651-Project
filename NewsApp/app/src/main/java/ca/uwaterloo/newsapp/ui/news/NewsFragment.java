package ca.uwaterloo.newsapp.ui.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.newsapp.R;

import static ca.uwaterloo.newsapp.helper.NewsBeanLocalData.CATEGORIES;
import static ca.uwaterloo.newsapp.helper.NewsBeanLocalData.CATIDS;
import static ca.uwaterloo.newsapp.helper.NewsBeanLocalData.DATA_BY_CATEGORY;

//每个tab下的碎片Fragment
public class NewsFragment extends Fragment {
    private TabLayout tabLayout;
    //新闻列表
    private ListView newsListView;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    //新闻子项
    private List<NewsBean.ResultBean.DataBean> contentItems = new ArrayList<>();

    private static final int UPNEWS_INSERT = 0;

    //分页查询参数，每页显示10条新闻数据
    private int defaultSkip = 0, defaultLimit = 20, skip = defaultSkip, limit = defaultLimit;

    //每一个Fragment页面都有一个浮动按钮，用于快速回到顶部
    private FloatingActionButton fab;

    private static final Gson gson = new Gson();

    //添加此注解的原理：https://blog.csdn.net/androidsj/article/details/79865091
    @SuppressLint("HandlerLeak")
    private Handler newsHandler = new Handler() {
        //主线程
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPNEWS_INSERT:
                default:
                    notifyNewsChange((List<NewsBean.ResultBean.DataBean>) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //每次创建，绘制该Fragment的View组件时回调，将显示的View返回
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载新闻列表
        View view = inflater.inflate(R.layout.news_list, container, false);
        // TODO：加载 tab
        tabLayout = view.findViewById(R.id.tabLayout);
        for (String tabName : CATEGORIES) {
            tabLayout.addTab(tabLayout.newTab().setText(tabName));
        }

        //获取每个实例之后，返回当前视图
        newsListView = (ListView) view.findViewById(R.id.newsListView);
        //tv = (TextView) view.findViewById(R.id.text_response);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        return view;
    }

    //当NewsFragment所在的Activity启动完成后调用，声明周期紧接在onCreateView()之后
    //使用此注解的讲解：https://blog.csdn.net/androidsj/article/details/79865091
    //@SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onAttach(getContext());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO
                if ("University".equals(tab.getText())) {
                    refreshData(1);
                }else if ("Faculties".equals(tab.getText())){
                    refreshData(2);
                }else {
                    refreshData(5);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //滚动到第一个可见的item位置，下标为0，具体讲解：https://www.jianshu.com/p/a5cd3cff2f1b
                newsListView.smoothScrollToPosition(0);
            }
        });
        //实现下拉刷新的功能
        //设置下拉刷新进度条的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.red);
        //实现下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO
                threadLoaderData(CATIDS.get(tabLayout.getSelectedTabPosition()));
            }
        });

        // 轻度按监听新闻列表子项
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 点击事件
                Log.d("子项的数据为", "onItemClick: " + contentItems.get(position));
                //获取点击条目的路径，传值显示WebView页面
                String url = contentItems.get(position).getUrl();
                Log.d("当前新闻子项的连接是：", "onItemClick: " + url);
                String uniquekey = contentItems.get(position).getUniquekey();
                String newsTitle = contentItems.get(position).getTitle();
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("pageUrl", url);
                startActivity(intent);
            }
        });

        // TODO: 网络访问加载数据
        getDataFromNet(CATIDS.get(0));
    }

    //创建Fragment被添加到活动中时回调，且只会被调用一次
    private void threadLoaderData(final Integer category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //沉睡1.5s，本地刷新很快，以防看不到刷新效果
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //若快速点击tab，则会出现getActivity()为空的情况，但是第一次加载肯定不会出错，所以将要拦截，以防app崩溃
                if (getActivity() == null)
                    return;
                //此处的用法：runOnUiThread必须是在主线程中调用，getActivity()获取主线程所在的活动，切换子线程到主线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //重新生成数据，传入tab条目
                        refreshData(category);
                        //表示刷新事件结束，并隐藏刷新进度条
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //加载数据，实现从本地数据库中读取数据刷新到newsListView的适配器中
    private void refreshData(final Integer category) {
        refreshData(category, 0, 10);
    }

    private void refreshData(final Integer category, int skip, int limit) {
//        this.skip = skip;
//        this.limit = limit;
        contentItems = DATA_BY_CATEGORY.get(category);
        notifyNewsChange(contentItems);
    }

    //异步消息处理机制
    private void getDataFromNet(final Integer category) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            // TODO: 需要额外参数就得额外定义，返回是什么取决于需求，这一步的结果会回传到 onPostExecute， 比如 json
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return gson.toJson(DATA_BY_CATEGORY.get(category), new TypeToken<List<NewsBean.ResultBean.DataBean>>(){}.getType());
            }

            // TODO:
            protected void onPostExecute(final String result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = newsHandler.obtainMessage();
                        msg.what = UPNEWS_INSERT;
                        msg.obj = gson.fromJson(result, new TypeToken<List<NewsBean.ResultBean.DataBean>>(){}.getType());
                        // 让工作线程的数据回到主线程
                        newsHandler.sendMessage(msg);
                    }
                }).start();
            }

            //当后台任务中调用了publishProgress(Progress...)方法后，onProgressUpdate方法很快被执行
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        };
        //启动异步加载任务
        task.execute();
    }

    private void notifyNewsChange(List<NewsBean.ResultBean.DataBean> list) {
        contentItems = list;
        ItemAdapter adapter = new ItemAdapter(getActivity(), contentItems);
        newsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
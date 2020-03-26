package ca.uwaterloo.newsapp.helper;

import android.arch.persistence.room.util.StringUtil;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.newsapp.Entity.Category;
import ca.uwaterloo.newsapp.Entity.News;
import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.ui.news.NewsBean;
import ca.uwaterloo.newsapp.utils.HttpUtils;

public class NewsBeanLocalData {

    public static final Map<Integer, List<NewsBean.ResultBean.DataBean>> DATA_BY_CATEGORY = new HashMap<>();
//    public static final List<String> CATEGORIES = Arrays.asList("University", "Faculties", "Departments & Schools");
//    public static final List<Integer> CATIDS = Arrays.asList(1, 2, 5);
    public static Map<Integer,String> idToCatName = new HashMap<>();
    public static Map<String,Integer> catNameToId = new HashMap<>();
    public static final List<String> CATEGORIES = new ArrayList<>();
    public static final List<Integer> CATIDS = new ArrayList<>();
    private static HttpUtils httpUtils = new HttpUtils();

    static {
        List<Category> list = httpUtils.getNewsCategory("/api/v1/news-source");
        for (Category category:list){
            idToCatName.put(category.getId(),category.getName());
            catNameToId.put(category.getName(),category.getId());
        }
        idToCatName.put(0,"All");
        catNameToId.put("All",0);
    }

    public static void loadUserInfo(int id, String token){
        CATEGORIES.clear();
        CATIDS.clear();
        CATEGORIES.add("All");
        CATIDS.add(0);
        User user = httpUtils.getUser2("/api/v1/users/"+id,token);
        String following = user.getFollowing();
        if (following!=null && !following.isEmpty()){
            String[] strings = following.split(",");
            for (String s:strings){
                CATEGORIES.add(idToCatName.get(Integer.valueOf(s)));
                CATIDS.add(Integer.valueOf(s));
            }
        }
        List<News> newsList = httpUtils.getNewsbyUser("/api/v1/news",1,token);
        newsList.addAll(httpUtils.getNewsbyUser("/api/v1/news",2,token));
        List<NewsBean.ResultBean.DataBean> list = new ArrayList<>();
        for (News news : newsList) {
            NewsBean.ResultBean.DataBean dataBean = new NewsBean.ResultBean.DataBean();
            dataBean.setCategory(0);
            dataBean.setTitle(news.getTitle());
            dataBean.setDate(news.getDate());
            dataBean.setThumbnail_pic_s(news.getImage_url());
            dataBean.setUrl(news.getUrl());
            list.add(dataBean);
        }
        DATA_BY_CATEGORY.put(0, list);
    }

    public static void refresh(){
        for (Integer catid : CATIDS) {
            if (catid==0) continue;
            List<News> newsList = httpUtils.getNews("/api/v1/news/" + catid,1);
            newsList.addAll(httpUtils.getNews("/api/v1/news/" + catid,2));
            List<NewsBean.ResultBean.DataBean> list = new ArrayList<>();
            DATA_BY_CATEGORY.put(catid, list);
            for (News news : newsList) {
                NewsBean.ResultBean.DataBean dataBean = new NewsBean.ResultBean.DataBean();
                dataBean.setCategory(catid);
                dataBean.setTitle(news.getTitle());
                dataBean.setDate(news.getDate());
                dataBean.setThumbnail_pic_s(news.getImage_url());
                dataBean.setUrl(news.getUrl());
                list.add(dataBean);
            }
        }
        Log.d("static", "load data");
    }
}

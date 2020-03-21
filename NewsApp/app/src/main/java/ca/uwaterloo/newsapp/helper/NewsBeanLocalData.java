package ca.uwaterloo.newsapp.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.newsapp.Entity.News;
import ca.uwaterloo.newsapp.ui.news.NewsBean;
import ca.uwaterloo.newsapp.utils.HttpUtils;

public class NewsBeanLocalData {

    public static final Map<Integer, List<NewsBean.ResultBean.DataBean>> DATA_BY_CATEGORY = new HashMap<>();
    public static final List<String> CATEGORIES = Arrays.asList("University", "Faculties", "Departments & Schools");
    public static final List<Integer> CATIDS = Arrays.asList(1, 2, 5);

    static {
        HttpUtils httpUtils = new HttpUtils();
        for (Integer catid : CATIDS) {
            List<News> newsList = httpUtils.getNews("/api/v1/news/" + catid);
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
    }
}

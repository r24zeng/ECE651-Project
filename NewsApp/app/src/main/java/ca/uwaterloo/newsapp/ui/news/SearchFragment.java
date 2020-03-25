package ca.uwaterloo.newsapp.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.CheckBox;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.newsapp.Entity.Category;
import ca.uwaterloo.newsapp.R;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.HttpUtils;
import ca.uwaterloo.newsapp.utils.RecyclerSearchAdapter;

public class SearchFragment extends Fragment {
    private List<Category> mStrs = new ArrayList<Category>();
    private SearchView mSearchView;
    public RecyclerView _recycler;
    RecyclerSearchAdapter _recyclerAdapter;
    List<Category> filterString;
    int search = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_search, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        getList(mStrs);
        _recycler = (RecyclerView)view.findViewById(R.id.recyclerSearch);
        String token = ACache.get(this.getActivity()).getAsString("token");
        int id = (int)ACache.get(this.getActivity()).getAsObject("id");

        _recyclerAdapter = new RecyclerSearchAdapter(mStrs, getActivity(), search, token , id);
        _recycler.setAdapter(_recyclerAdapter);
        _recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        _recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    filterString = filter( newText);
                    _recyclerAdapter.setFilter(filterString);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public List<Category>filter(String text){
        filterString=new ArrayList<Category>();

        for (Category word: mStrs){
            if (word.getName().contains(text))
                filterString.add(word);
        }
        return filterString;
    }


    public void getList(List<Category> str){
         HttpUtils httpUtils = new HttpUtils();
        List<Category> list = httpUtils.getNewsCategory("/api/v1/news-source");
        for (Category category:list){
            str.add(category);
        }
    }



}

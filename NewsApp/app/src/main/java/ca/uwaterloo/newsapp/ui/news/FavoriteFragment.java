
package ca.uwaterloo.newsapp.ui.news;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.newsapp.Entity.Category;
import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.HttpUtils;
import ca.uwaterloo.newsapp.utils.RecycleFavoriteAdapter;

public class FavoriteFragment extends Fragment{
    private static String TAG = "FavoriteFragment";
    private Map<Integer,String> mStrs = new HashMap<>();
    public RecyclerView _recycler;
    User user;
    RecycleFavoriteAdapter _recyclerAdapter;
    private List<String> favorite_copy = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_favorite, container, false);
        getList(mStrs);

        String token = ACache.get(this.getActivity()).getAsString("token");
        int id = (int)ACache.get(this.getActivity()).getAsObject("id");
        HttpUtils httpUtils = new HttpUtils();
        favorite_copy.clear();

        user = httpUtils.getUser2("/api/v1/users/"+id,token);
        if(user.getFollowing() != null){
            String[] strings = user.getFollowing().split(",");
            for (String s:strings){
//            System.out.println(TAG + s + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% "+ mStrs.get(Integer.valueOf(s)));
                favorite_copy.add(mStrs.get(Integer.valueOf(s)));
            }
        }

        _recycler = (RecyclerView)view.findViewById(R.id.recyclerSearch);
        _recyclerAdapter = new RecycleFavoriteAdapter(favorite_copy, getActivity(),1);
        _recycler.setAdapter(_recyclerAdapter);
        _recyclerAdapter.notifyDataSetChanged();
//        updateData(favorite_copy);
        _recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        _recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        _recyclerAdapter.notifyDataSetChanged();
        return view;
    }

    public void getList(Map<Integer,String> str){
        HttpUtils httpUtils = new HttpUtils();
        List<Category> list = httpUtils.getNewsCategory("/api/v1/news-source");
        for (Category category:list){
            str.put(category.getId(),category.getName());
        }
    }
    
}


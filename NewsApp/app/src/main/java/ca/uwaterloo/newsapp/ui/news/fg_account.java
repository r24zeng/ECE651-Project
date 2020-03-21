package ca.uwaterloo.newsapp.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.HttpUtils;
import ca.uwaterloo.newsapp.utils.JsonUtil;
import ca.uwaterloo.newsapp.utils.recyclerAdapter;


public class fg_account extends Fragment {
    private static String TAG = "accountFragment";
    View view;
    String password;
    public RecyclerView _recycler;
    recyclerAdapter _recyclerAdapter;
    TextView _username;
    TextView _usergender;
    User user;
    List<String> _userAttr =  new ArrayList<String>();
    Button btn_edit;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "begin on onCreateView");
        String token = ACache.get(this.getActivity()).getAsString("token");
        int id = (int)ACache.get(this.getActivity()).getAsObject("id");
        password = ACache.get(this.getActivity()).getAsString("password");
        Log.d(TAG,password+"$$$$$$$$$$$$$$");

        HttpUtils httpUtils = new HttpUtils();

        user =httpUtils.get("/api/v1/users/" + id, token) ;
        user.setPassword(password);
        user.setId(id);

        view = inflater.inflate(R.layout.fg_account, container, false);
        _username =(TextView)view.findViewById(R.id.user_name);
        _username.setText(user.getUsername());
        _usergender = (TextView)view.findViewById(R.id.user_gender);
        if(user.getGender() == 0){
            _usergender.setText("female");
        }else {
            _usergender.setText("male");
        }

        initRecyclerView();
        btn_click();
        return view;
    }

    public List<String> getUserAttr(User u){
        List<String> attr = new ArrayList<String>();
        attr.add(u.getDepartment());
        attr.add(u.getFaculty());
        attr.add(u.getName());
        return attr;
    }


    private void initRecyclerView(){
        _recycler = (RecyclerView)view.findViewById(R.id.recycler);
        _userAttr = getUserAttr(user);
        _recyclerAdapter = new recyclerAdapter(_userAttr, getActivity(), user);
        _recycler.setAdapter(_recyclerAdapter);
        _recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        _recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//        _recyclerAdapter.setOnItemClick
    }

    private void btn_click(){
        btn_edit = (Button) view.findViewById(R.id.btn_editProfile);
        btn_edit.setOnClickListener(new EditCheckedListener());
    }

    class EditCheckedListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(getActivity(),edit_profile.class);
            try {
                intent.putExtra("userdata", JsonUtil.toJson(user));
                getActivity().startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

package ca.uwaterloo.newsapp.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.IOException;
import ca.uwaterloo.newsapp.R;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.HttpUtils;



public class fg_account extends Fragment {
    private static String TAG = "accountFragment";
    TextView _username;
    String user_name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "begin on onCreateView");
        String token = ACache.get(this.getActivity()).getAsString("token");
        int id = (int)ACache.get(this.getActivity()).getAsObject("id");
        HttpUtils httpUtils = new HttpUtils();

        user_name =httpUtils.get("/api/v1/users/" + id, token) ;

        View view = inflater.inflate(R.layout.fg_account, container, false);
        _username =(TextView)view.findViewById(R.id.user_name);
        _username.setText(user_name);
//        System.out.println("########************************#########"+user_name+"&&&&&&&&&&&&&&&");
        return view;
    }



}

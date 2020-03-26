package ca.uwaterloo.newsapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.newsapp.Entity.Category;
import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.mySViewHolder>{
    private Context context;
    private List<Category> attribute;
    private int check;
    private Map<Integer, Boolean> map = new HashMap<>();
    String token;
    int id;


    public RecyclerSearchAdapter(List<Category> attribute, Context context , int check, String token, int id){
        this.context = context;
        this.attribute = attribute;
        this.check = check;
        this.token = token;
        this.id = id;
        initMap();
    }

    public void initMap(){
        for (int i = 0; i<attribute.size(); i++){
            map.put(i,false);
        }
    }

    @NonNull
    @Override
    public RecyclerSearchAdapter.mySViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_search, null);
        return new mySViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSearchAdapter.mySViewHolder holder, int position) {
        String attrinfo = attribute.get(position).getName();
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+ attrinfo);
        TextView textview = holder._allNews;
        textview.setText(attrinfo);

        holder._favourite_icon.setTag(position);
        holder._favourite_icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                if (isChecked) {
                    map.put(pos, true);
                    HttpUtils httpUtils = new HttpUtils();
                    List<Category> list = httpUtils.getNewsCategory("/api/v1/news-source");
                    for (Category category:list){
                        if(category.getName().equals(attrinfo)){
                            User newU = httpUtils.get("/api/v1/users/" + id, token) ;
                            String[] strings = newU.getFollowing().split(",");
                            int ifExists = 0;
                            for(int i = 0; i< strings.length; i++){
                                if(String.valueOf(category.getId()).equals(strings[i]) ){
                                    ifExists = 1;
                                    break;
                                }
                            }
                            if(ifExists == 0  ){
                                newU.setFollowing(newU.getFollowing() + "," + String.valueOf(category.getId()));
                            }

                            if(newU.getFollowing().contains("n")){
                                newU.setFollowing(newU.getFollowing().substring(5));
                            }
                            
                            try {
                                ResponseBody response = new HttpUtils().patch("/api/v1/users/"+id, JsonUtil.toJson(newU),token);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        if(map.get(position) == null){
            map.put(position,false);
        }
        holder._favourite_icon.setChecked(map.get(position));
    }



    @Override
    public int getItemCount() {
        return attribute.size();
    }

    public void setFilter(List<Category>filterWords){
        attribute=filterWords;
        notifyDataSetChanged();
    }



    class mySViewHolder extends RecyclerView.ViewHolder {
        private TextView _allNews;
        private CheckBox _favourite_icon;


        public mySViewHolder(View itemView) {
            super(itemView);
            _allNews = (TextView) itemView.findViewById(R.id.allNews);
            _favourite_icon = (CheckBox) itemView.findViewById(R.id.favourite_icon);
            this.setIsRecyclable(false);
        }
    }
}

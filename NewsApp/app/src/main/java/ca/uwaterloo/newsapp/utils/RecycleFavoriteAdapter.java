
package ca.uwaterloo.newsapp.utils;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ca.uwaterloo.newsapp.R;

public class RecycleFavoriteAdapter extends RecyclerView.Adapter<RecycleFavoriteAdapter.myFViewHolder>{
    private Context context;

    private List<String> attribute = new ArrayList<>();
    private int check;


    public RecycleFavoriteAdapter(List<String> attribute, Context context , int check){
        this.context = context;
        this.attribute = attribute;
        this.check = check;
    }

    @NonNull
    @Override
    public RecycleFavoriteAdapter.myFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_search, null);
        return new myFViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecycleFavoriteAdapter.myFViewHolder holder, int position) {
        TextView textview = holder._allNews;
        if(!attribute.isEmpty()){
            String attrinfo = attribute.get(position);
            textview.setText(attrinfo);
        }

    }

    @Override
    public int getItemCount() {
        return attribute.size();
    }

//    public void addData(int position) {
//        attribute.add(position, "Insert " + position);
//        notifyItemInserted(position);
//    }






    class myFViewHolder extends RecyclerView.ViewHolder {
        private TextView _allNews;
//        private CheckBox _favourite_icon;


        public myFViewHolder(View itemView) {
            super(itemView);
            _allNews = (TextView) itemView.findViewById(R.id.allNews);

        }
    }
}


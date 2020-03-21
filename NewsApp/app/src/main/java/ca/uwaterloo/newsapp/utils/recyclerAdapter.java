package ca.uwaterloo.newsapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.myViewHolder>{
    private Context context;
    private List<String> attribute;
    private User user;

    public RecyclerAdapter(List<String> attribute, Context context, User u ){
        this.context = context;
        this.user = u;
        this.attribute = attribute;
    }


    @NonNull
    @Override
    public RecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.fg_account_item, null);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.myViewHolder holder, int position) {
//        String attr = holder._user_attr.getText().toString();
        String attrinfo = attribute.get(position);
        TextView textview = holder._user_attrinfo;
        textview.setText(attrinfo);
        TextView attr = holder._user_attr;
        if(position == 0){
            attr.setText("   department:     ");
        }else if(position == 1){
            attr.setText("   faculty:     ");
        }else if(position == 2){
            attr.setText("   name:     ");
        }else {
            attr.setText("   fault to show account infomation");
        }
//        System.out.println("****************"+ attr +"**********");
    }

    @Override
    public int getItemCount() {
        return attribute.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private TextView _user_attr;
        private TextView _user_attrinfo;


        public myViewHolder(View itemView) {
            super(itemView);
            _user_attr = (TextView) itemView.findViewById(R.id.user_attr);
            _user_attrinfo = (TextView)itemView.findViewById(R.id.user_attrinfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"click xxx",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}

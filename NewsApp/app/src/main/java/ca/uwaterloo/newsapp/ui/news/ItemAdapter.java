package ca.uwaterloo.newsapp.ui.news;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import ca.uwaterloo.newsapp.R;

//自定义新闻列表的适配器
public class ItemAdapter extends BaseAdapter {

    private List<NewsBean.ResultBean.DataBean> list;

    private Context context;

    //设置正常加载图片的个数
    //设置正常加载图片的个数
    private int IMAGE_0 = 0;
    private int IMAGE_1 = 1;

    private int VIEW_COUNT = 1;

    public ItemAdapter(Context context, List<NewsBean.ResultBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //得到不同item的总数
    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }

    //得到当前新闻子项item的类型
    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getThumbnail_pic_s() == null) {
            return IMAGE_0;
        }
        return IMAGE_1;
    }

    //提升ListView的运行效率，参数convertView用于将之前加载好的布局进行缓存，以便以后可以重用：https://blog.csdn.net/xiao_ziqiang/article/details/50812471
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == IMAGE_0) {
            Image0_ViewHolder holder;
            Image1_ViewHolder holder1 = new Image1_ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_simple, null);
                holder = new Image0_ViewHolder();
                //查找控件
                holder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            } else {
                try {
                    holder = (Image0_ViewHolder) convertView.getTag();
                }catch (ClassCastException e){
                    holder = null;
                    holder1 = (Image1_ViewHolder) convertView.getTag();
                }
            }
            if (holder == null){
                holder1.title.setText(list.get(position).getTitle());
            }else {
                holder.title.setText(list.get(position).getTitle());
            }

        }else {
            Image1_ViewHolder holder;
            Image0_ViewHolder holder0 = new Image0_ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_layout01, null);
                holder = new Image1_ViewHolder();
                //查找控件
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.image = (ImageView) convertView.findViewById(R.id.image);

                convertView.setTag(holder);
            } else {
                try {
                    holder = (Image1_ViewHolder) convertView.getTag();
                }catch (ClassCastException e){
                    holder = null;
                    holder0 = (Image0_ViewHolder) convertView.getTag();
                }

            }
            //获取数据重新赋值
            if (holder==null){
                holder0.title.setText(list.get(position).getTitle());
            }else {
                holder.title.setText(list.get(position).getTitle());
            }

            /**
             * DiskCacheStrategy.NONE： 表示不缓存任何内容。
             */
            if (holder!=null){
                Glide.with(context)
                        .load(list.get(position).getThumbnail_pic_s())
                        .into(holder.image);
            }

        }
        return convertView;
    }

    public List<NewsBean.ResultBean.DataBean> getList() {
        return list;
    }

    public void setList(List<NewsBean.ResultBean.DataBean> list) {
        this.list = list;
    }

    class Image0_ViewHolder {
        TextView title;
    }
    class Image1_ViewHolder{
        TextView title;
        ImageView image;
    }
}
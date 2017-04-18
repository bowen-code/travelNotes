package com.wenxin.dongyouji.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wenxin.dongyouji.R;
import com.wenxin.dongyouji.Url;
import com.wenxin.dongyouji.model.TravelNotesModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 17/3/11.游记界面的适配器
 */
public class TravelNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int FOOTER_TYPE = 0;//最后一个的类型
    public static final int HAS_IMG_TYPE = 1;//有图片的类型

    private List<TravelNotesModel> dataList;

    private ProgressBar mProgress;
    private TextView mNoMore;
    private Context mContext;

    public TravelNotesAdapter(Context context) {
        mContext = context;
        dataList = new ArrayList<>();
    }

    public void addData(List<TravelNotesModel> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE) {
            return new FooterItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false));
        } else {
            return new AllAddressAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_notes, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == FOOTER_TYPE) {
            bindFooterView((FooterItemViewHolder) holder);
        } else {

            if (dataList.get(position) != null) {
                try {
                    bindView((AllAddressAdapterViewHolder) holder, dataList.get(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        } else {
            TravelNotesModel news = dataList.get(position);
            return HAS_IMG_TYPE;
        }
    }

    private void bindView(final AllAddressAdapterViewHolder holder, TravelNotesModel data) throws IOException {

        holder.title.setText(data.getTitle());
        holder.date.setText(data.getDate());
        Log.i("lin", "---lin's log--->   路径  " + Url.url + "static/images/" + data.getBackground());
        String url = Url.url + "static/images/" + data.getBackground();
        Picasso
                .with(mContext)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        //在这里用图片加载库，将背景图片和用户头像更换就可以
    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size() + 1;
    }

    public static class AllAddressAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private ImageView imageView;

        public AllAddressAdapterViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            imageView = (ImageView) itemView.findViewById(R.id.item_travel_notes_img);
        }
    }

    /**
     * 刷新列表
     */
    public void refreshList(List<TravelNotesModel> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 加载更多
     */
    public void loadMoreList(List<TravelNotesModel> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 显示没有更多
     */
    public void showNoMore() {
        if (getItemCount() > 0) {
            if (mProgress != null && mNoMore != null) {
                mNoMore.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 显示加载更多
     */
    public void showLoadMore() {
        if (mProgress != null && mNoMore != null) {
            mProgress.setVisibility(View.VISIBLE);
            mNoMore.setVisibility(View.GONE);
        }
    }

    private void bindFooterView(FooterItemViewHolder viewHolder) {
        mProgress = viewHolder.mProgress;
        mNoMore = viewHolder.tvNoMore;
    }


    public static class FooterItemViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgress;
        private TextView tvNoMore;

        public FooterItemViewHolder(View itemView) {
            super(itemView);
            mProgress = (ProgressBar) itemView.findViewById(R.id.pb_footer_load_more);
            tvNoMore = (TextView) itemView.findViewById(R.id.tv_footer_no_more);
        }
    }


    /**
     * 获取点击的 item,如果是最后一个,则返回 null
     */
    public TravelNotesModel getClickItem(int position) {
        if (position < dataList.size()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

}

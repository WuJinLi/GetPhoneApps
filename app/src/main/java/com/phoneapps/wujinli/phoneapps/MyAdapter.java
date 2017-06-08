package com.phoneapps.wujinli.phoneapps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoneapps.wujinli.phoneapps.model.AppInfo;

import java.util.List;

/**
 * author: WuJinLi
 * time  : 17/5/26
 * desc  :
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AppInfo> list;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<AppInfo> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_app, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppInfo appInfo = list.get(position);

        if (holder instanceof MyViewHolder) {

            if (appInfo.getAppIcon() != null) {
                ((MyViewHolder) holder).imageView.setImageDrawable(appInfo.getAppIcon());
            }

            if (!TextUtils.isEmpty(appInfo.getAppName())) {
                ((MyViewHolder) holder).textView.setText(appInfo.getAppName());
            }

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}

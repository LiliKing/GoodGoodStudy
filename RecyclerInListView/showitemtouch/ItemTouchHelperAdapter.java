package com.smk.view.showitemtouch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smk.R;
import com.smk.utils.async.UrlToImageFile;
import com.smk.view.showitemtouch.bean.ItemBean;

import java.util.Collections;
import java.util.List;

/**
 * 展台适配器
 */
public class ItemTouchHelperAdapter extends RecyclerView.Adapter<ItemTouchHelperAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ItemBean> mDatas;

    public ItemTouchHelperAdapter(Context context, List<ItemBean> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        TextView mTvName;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_griditem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view.findViewById(R.id.iv_icon);
        viewHolder.mTvName = (TextView) view.findViewById(R.id.tv_name);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        UrlToImageFile.loadImageUrl(mContext, viewHolder.mImg, mDatas.get(i).getIcon());
        viewHolder.mTvName.setText(mDatas.get(i).getAppName());

    }

    public boolean onItemMove(int srcPosition, int targetPos) {
        if (mDatas != null) {
            // 更换数据源中的数据Item的位置
            Collections.swap(mDatas, srcPosition, targetPos);
            // 更新UI中的Item的位置，主要是给用户看到交互效果
            notifyItemMoved(srcPosition, targetPos);
            return true;
        }
        return false;
    }

    public void onItemDismiss(int adapterPosition) {
        // 滑动删除的时候，从数据源移除，并刷新这个Item。
        if (mDatas != null) {
            mDatas.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }


}

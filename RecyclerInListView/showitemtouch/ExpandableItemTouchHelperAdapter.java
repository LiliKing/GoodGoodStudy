package com.smk.view.showitemtouch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smk.R;
import com.smk.view.showitemtouch.bean.SubStands;

import java.util.ArrayList;
import java.util.List;

/**
 * 展台适配器
 */
public class ExpandableItemTouchHelperAdapter extends BaseAdapter {
    private List<SubStands> mSubStands = new ArrayList<>();
    private LayoutInflater mInflater;

    private Context mContext;
    private OnItemClickListener mOnClickListener;


    public interface OnItemClickListener {
        void onItemClick(int pos, int type, String info);

        void onDeleteCard(int pos);
    }

    public ExpandableItemTouchHelperAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = onItemClickListener;

    }

    public void setData(List<SubStands> data) {
        this.mSubStands.clear();
        this.mSubStands.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mSubStands.size();
    }

    @Override
    public SubStands getItem(int position) {
        return mSubStands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.layout_expandable_touch_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mRvItem = (RecyclerView) convertView.findViewById(R.id.rv_item);
            viewHolder.mTvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTvGroupName.setText(mSubStands.get(position).getSubName());

        ItemTouchHelperAdapter adapter = new ItemTouchHelperAdapter(mContext, mSubStands.get(position).getApply());
        MeasuredGridLayoutManager layoutManager = new MeasuredGridLayoutManager(mContext, 4,getCount());
        layoutManager.setSmoothScrollbarEnabled(true);
        viewHolder.mRvItem.setLayoutManager(layoutManager);
        viewHolder.mRvItem.setAdapter(adapter);
        viewHolder.mRvItem.setHasFixedSize(true);
        viewHolder.mRvItem.setNestedScrollingEnabled(false);

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(adapter);
        callback.setDragEnabled(false);
        callback.setSwipeEnabled(false);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(viewHolder.mRvItem);


        return convertView;
    }

    private class ViewHolder {
        RecyclerView mRvItem;
        TextView mTvGroupName;

    }


}


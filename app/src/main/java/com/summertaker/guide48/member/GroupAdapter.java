package com.summertaker.guide48.member;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.summertaker.guide48.R;
import com.summertaker.guide48.common.BaseDataAdapter;
import com.summertaker.guide48.data.Group;

import java.util.ArrayList;

public class GroupAdapter extends BaseDataAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private ArrayList<Group> mDataList = new ArrayList<>();

    public GroupAdapter(Context context, ArrayList<Group> dataList) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = mLayoutInflater.inflate(R.layout.group_item, null);

            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.ivPicture);
            holder.name = (TextView) view.findViewById(R.id.tvCaption);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Group item = mDataList.get(position);
        holder.image.setImageResource(item.getLogo());
        holder.name.setText(item.getName());

        //Log.e(mTag, "item.getName(): " + item.getName());

        return view;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
    }
}

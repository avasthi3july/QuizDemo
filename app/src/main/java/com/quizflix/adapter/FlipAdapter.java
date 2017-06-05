package com.quizflix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quizflix.R;
import com.quizflix.dao.Data;
import com.quizflix.dao.Question;

import java.util.List;

/**
 * Created by kavasthi on 6/3/2017.
 */

public class FlipAdapter extends BaseAdapter {


    public List<Data> parkingList;
    public Context context;
    public static ViewHolder viewHolder;
    public FlipAdapter(List<Data> apps, Context context) {
        this.parkingList = apps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;


        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
            viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
            //viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");

        //Glide.with(context).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);

        return rowView;
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        //public ImageView cardImage;


    }

}


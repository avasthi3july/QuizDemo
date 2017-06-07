package com.quizflix.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quizflix.R;
import com.quizflix.dao.Result;

import java.util.ArrayList;

/**
 * Created by kavasthi on 2/17/2017.
 */

public class LeaderViewAdapter extends Adapter<LeaderViewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Result> adDataArrayList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);

    }

    public LeaderViewAdapter(Context mContext, ArrayList<Result> adData) {
        this.mContext = mContext;
        this.adDataArrayList = adData;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < 10) {
            Result data = adDataArrayList.get(position);
            holder.name.setText(data.getFirstName());
            holder.totalScore.setText(data.getScore());
        }


    }

    @Override
    public int getItemCount() {
        return adDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, totalScore;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            totalScore = (TextView) itemView.findViewById(R.id.total_score);
        }
    }
}

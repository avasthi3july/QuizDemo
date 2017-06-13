package com.quizflix.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quizflix.R;
import com.quizflix.dao.Question;
import com.quizflix.dao.Result;

import java.util.ArrayList;

/**
 * Created by kavasthi on 2/17/2017.
 */

public class ExplainAdapter extends Adapter<ExplainAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Question> adDataArrayList;
    private int cons = 0;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explain, parent, false);
        return new ViewHolder(view);

    }

    public ExplainAdapter(Context mContext, ArrayList<Question> adData) {
        this.mContext = mContext;
        this.adDataArrayList = adData;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Question data = adDataArrayList.get(position);
        //adDataArrayList.get(position).setSelected(true);
        holder.question.setText("Question : " + data.getQuestion());
        holder.answer.setText("Answer : " + data.getAnswer());
        holder.explaination.setText("Exlanation : " + data.getExplanation());
        holder.mRelativeLayout.setTag(position);
        holder.userAnswer.setText("Your Answer : " + data.isUserAnswer());
        if (adDataArrayList.get(position).isSelected()) {
            holder.explaination.setVisibility(View.VISIBLE);
        } else {
            holder.explaination.setVisibility(View.GONE);
        }
        if ((data.isAnswer() && data.isUserAnswer()) || (!data.isAnswer() && !data.isUserAnswer()))
            holder.icon.setImageResource(R.drawable.checked);
        else holder.icon.setImageResource(R.drawable.error);

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Question item : adDataArrayList) {
                    item.setSelected(false);
                }

                if (adDataArrayList.get(position).isSelected()) {
                    adDataArrayList.get(position).setSelected(false);
                } else {
                    adDataArrayList.get(position).setSelected(true);
                }

                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return adDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question, answer, explaination, userAnswer;
        private RelativeLayout mRelativeLayout;
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.ques);
            answer = (TextView) itemView.findViewById(R.id.ans);
            explaination = (TextView) itemView.findViewById(R.id.explain);
            userAnswer = (TextView) itemView.findViewById(R.id.userAns);
            icon = (ImageView) itemView.findViewById(R.id.iconc);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.main_view);
        }
    }
}

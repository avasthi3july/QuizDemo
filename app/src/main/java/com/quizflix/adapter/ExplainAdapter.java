package com.quizflix.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question data = adDataArrayList.get(position);
        holder.question.setText("Question : " + data.getQuestion());
        holder.answer.setText("Answer : " + data.getAnswer());
        holder.explaination.setText("Exlanation : " + data.getExplanation());
        holder.userAnswer.setText("Your Answer : " + data.isUserAnswer());


    }

    @Override
    public int getItemCount() {
        return adDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question, answer, explaination,userAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.ques);
            answer = (TextView) itemView.findViewById(R.id.ans);
            explaination = (TextView) itemView.findViewById(R.id.explain);
            userAnswer = (TextView) itemView.findViewById(R.id.userAns);
        }
    }
}

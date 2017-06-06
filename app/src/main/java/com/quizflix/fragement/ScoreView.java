package com.quizflix.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quizflix.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kavasthi on 6/6/2017.
 */

public class ScoreView extends Fragment implements View.OnClickListener{
    @BindView(R.id.total_score)
    TextView totalScore;
    @BindView(R.id.explanation)
    Button explanation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews();
    }

    private void initViews() {
        explanation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

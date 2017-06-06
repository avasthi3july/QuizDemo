package com.quizflix.fragement;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quizflix.R;
import com.quizflix.Util.Util;
import com.quizflix.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kavasthi on 6/5/2017.
 */

public class StartQuiz extends Fragment implements View.OnClickListener {
    private TextView introText;
    private Dialog dialog;
    @BindView(R.id.start_quiz)
    TextView startQuiz;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void openDialog() {
        try {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.intro_layout);
            // set the custom dialog components - text, image and button
            introText = (TextView) dialog.findViewById(R.id.intro_text);

            dialog.show();

        } catch (Exception e) {

        }
    }

    private void initViews(View view) {
        startQuiz.setOnClickListener(this);
        Util.showDialog1(getActivity(), "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
    }

    @Override
    public void onClick(View v) {
        QuizView mQuizView = new QuizView();
        ((MainActivity) getActivity()).replaceFragment(R.id.contenair, mQuizView, mQuizView.getClass().getName(), mQuizView.getClass().getName());


    }
}

package com.quizflix.fragement;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.quizflix.R;
import com.quizflix.Util.Util;
import com.quizflix.activity.MainActivity;
import com.quizflix.adapter.QuestionAdapter;
import com.quizflix.dao.BaseResponse;
import com.quizflix.dao.Question;
import com.quizflix.delegates.Api;
import com.quizflix.delegates.MyApplication;
import com.quizflix.delegates.ServiceCallBack;
import com.quizflix.webservice.BaseRequest;
import com.quizflix.webservice.JsonDataParser;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by kavasthi on 6/5/2017.
 */

public class QuizView extends Fragment {
    @BindView(R.id.frame)
    SwipeFlingAdapterView flingContainer;
    private String id;
    private ArrayList<Question> mQuestions;
    @BindView(R.id.timer)
    TextView timer;
    int i = 0, quesNum = 1, trueClick = 0, falseClick = 0;
    public QuestionAdapter myAppAdapter;
    MediaPlayer mpRight, mpWorng;
    private Fragment fragment;
    private CountDownTimer mCountDownTimer;
    private MyApplication myApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout(view);
    }

    private void initLayout(View view) {
        myApplication=(MyApplication)getActivity().getApplicationContext();
        fragment = new QuizView();
         mCountDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("Time Over");
                totalScore();
            }

        }.start();
        mQuestions = new ArrayList<>();
        mpRight = MediaPlayer.create(getActivity(), R.raw.popup);
        mpWorng = MediaPlayer.create(getActivity(), R.raw.newmsg);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mQuestions = (ArrayList<Question>) bundle.getSerializable("dataList");
            myAppAdapter = new QuestionAdapter(mQuestions, getActivity());
            flingContainer.setAdapter(myAppAdapter);
            updateView();
        }
    }

    private void updateView() {

           /* flingContainer.post(new Runnable() {
                @Override
                public void run() {
                    myAppAdapter.notifyDataSetChanged();

                }
            })*/
        ;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAppAdapter.notifyDataSetChanged();
            }
        });
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Question mQuestion = mQuestions.get(0);
                if (!mQuestion.getAnswer()) {
                    trueClick = ++trueClick;
                    mpRight.start();

                } else {
                    falseClick = ++falseClick;
                    mpWorng.start();
                }
                mQuestions.remove(0);
                myAppAdapter.notifyDataSetChanged();

                System.out.println("falseClick" + mQuestion.getQuestion());
                myApplication.getmQuestions().get(i).setUserAnswer(false);
                i = ++i;
                if (mQuestions != null && mQuestions.size() < 1) {

                    totalScore();
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                Question mQuestion = mQuestions.get(0);

                if (mQuestion.getAnswer()) {
                    trueClick = ++trueClick;
                    mpRight.start();
                } else {
                    falseClick = ++falseClick;
                    mpWorng.start();
                }

                mQuestions.remove(0);
                myAppAdapter.notifyDataSetChanged();
                System.out.println("trueClick" + trueClick);
                myApplication.getmQuestions().get(i).setUserAnswer(true);
                i = ++i;
                if (mQuestions != null && mQuestions.size() < 1) {

                    totalScore();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                if (view != null) {
                    view.findViewById(R.id.background).setAlpha(0);
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
            }
        });


        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                View view = flingContainer.getSelectedView();
                if (view != null) {
                    view.findViewById(R.id.background).setAlpha(0);
                }
                myAppAdapter.notifyDataSetChanged();
            }
        });

    }


    private void totalScore() {
        mCountDownTimer.cancel();
        int rightScore = trueClick * 1000;
        int falseScore = falseClick * 500;
        int totalScore = rightScore - falseScore;

        ScoreView mScoreView = new ScoreView();
        Bundle mBundle = new Bundle();
        mBundle.putInt("score", totalScore);
        mScoreView.setArguments(mBundle);
        // ((MainActivity) getActivity()).clearBackStackInclusive(fragment.getClass().getName());
        //Fragment currentFragment = getFragmentManager().findFragmentById(R.id.contenair);
        //getActivity().getSupportFragmentManager().popBackStackImmediate(currentFragment.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //System.out.println("currentFragment>>>>>>" + currentFragment);
        // getActivity().getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        if (mScoreView != null) {
            ((MainActivity) getActivity()).replaceFragment(R.id.contenair, mScoreView, mScoreView.getClass().getName(), mScoreView.getClass().getName());
        }

    }

}

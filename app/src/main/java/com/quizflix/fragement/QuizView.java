package com.quizflix.fragement;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class QuizView extends Fragment implements ServiceCallBack {
    @BindView(R.id.frame)
    SwipeFlingAdapterView flingContainer;
    private SharedPreferences mSharedPreferences;
    private String id, name, email;
    private TextView userName, userEmail, question;
    private RecyclerView mRecyclerView;
    private ArrayList<Question> mQuestions;
    @BindView(R.id.timer)
    TextView timer;
    int i = 0, quesNum = 1, trueClick = 0, falseClick = 0;
    public QuestionAdapter myAppAdapter;
    MediaPlayer mpRight, mpWorng;

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
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("Time Over");
                // totalScore();
            }

        }.start();
        mpRight = MediaPlayer.create(getActivity(), R.raw.popup);
        mpWorng = MediaPlayer.create(getActivity(), R.raw.newmsg);
        mSharedPreferences = Util.getSharedPreferences(getActivity());
        id = mSharedPreferences.getString("id", null);
        email = mSharedPreferences.getString("email", null);
        name = mSharedPreferences.getString("name", null);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Index", "1");
            jsonObject.put("UserId", id);
            userRegistration(jsonObject);
        } catch (Exception e) {

        }
    }

    public void userRegistration(JSONObject jsonObject) {
        BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setProgressShow(true);
        baseRequest.setRequestTag(Api.ADD_USER_REGISTRATION);
        baseRequest.setMessage("Please wait...");
        baseRequest.setServiceCallBack(this);
        Api api = (Api) baseRequest.execute(Api.class);
        try {
            TypedByteArray input = new TypedByteArray("application/json", jsonObject.toString().getBytes("UTF-8"));
            api.getQList(input, baseRequest.requestCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int tag, String baseResponse) {
        mQuestions=new ArrayList<>();
        BaseResponse baseData = JsonDataParser.getInternalParser(baseResponse, new TypeToken<BaseResponse>() {
        }.getType());
        if (baseData.getSuccess()) {

//            userName.setText(name);
           // userEmail.setText(email);
            mQuestions.addAll(baseData.getResults());
            //num.setText("Q : 1 ");
            ///ques.setText(mQuestions.get(0).getQuestion());
            myAppAdapter = new QuestionAdapter(mQuestions, getActivity());

            flingContainer.setAdapter(myAppAdapter);
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
                    i = ++i;
                    if (mQuestions != null && mQuestions.size() < 1) {

                        totalScore();
                    }
                    //Do something on the left!
                    //You also have access to the original object.
                    //If you want to use it just cast it (String) dataObject
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


            // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                @Override
                public void onItemClicked(int itemPosition, Object dataObject) {
                    System.out.println("itemPosition>>>" + itemPosition);
                    View view = flingContainer.getSelectedView();
                    if (view != null) {
                        view.findViewById(R.id.background).setAlpha(0);
                    }
                    myAppAdapter.notifyDataSetChanged();
                }
            });
        } else Util.showToast(getActivity(), baseData.getMessage());


    }

    private void totalScore() {
        int rightScore = trueClick * 1000;
        int falseScore = falseClick * 500;
        int totalScore = rightScore - falseScore;
        System.out.println("TotalScore>>>>>>" + totalScore);
        System.out.println("rightScore>>>>>>" + rightScore + "falseScore>>>>" + falseScore);

    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }
}

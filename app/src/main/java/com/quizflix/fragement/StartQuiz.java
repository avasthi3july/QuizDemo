package com.quizflix.fragement;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import com.google.gson.reflect.TypeToken;
import com.quizflix.R;
import com.quizflix.Util.Util;
import com.quizflix.activity.MainActivity;
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

public class StartQuiz extends Fragment implements View.OnClickListener, ServiceCallBack {
    private TextView introText;
    private Dialog dialog;
    private String id;
    @BindView(R.id.start_quiz)
    TextView startQuiz;
    private ArrayList<Question> mQuestions;
    private SharedPreferences mSharedPreferences;
    private MyApplication myApplication;

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

    private void initViews(View view) {
        myApplication = (MyApplication) getActivity().getApplicationContext();
        mSharedPreferences = Util.getSharedPreferences(getActivity());
        id = mSharedPreferences.getString("id", null);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Index", "1");
            jsonObject.put("UserId", id);
            userRegistration(jsonObject);
        } catch (Exception e) {

        }
        startQuiz.setOnClickListener(this);
        if(!myApplication.isAppOpen())
        Util.showDialog1(getActivity(), "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
        myApplication.setAppOpen(true);
    }

    @Override
    public void onClick(View v) {
        if (v == startQuiz) {
            QuizView mQuizView = new QuizView();
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("dataList", mQuestions);
            mQuizView.setArguments(mBundle);
            ((MainActivity) getActivity()).replaceFragment(R.id.contenair, mQuizView, mQuizView.getClass().getName(), mQuizView.getClass().getName());
        }

    }

    @Override
    public void onSuccess(int tag, String baseResponse) {
        mQuestions = new ArrayList<>();
        BaseResponse baseData = JsonDataParser.getInternalParser(baseResponse, new TypeToken<BaseResponse>() {
        }.getType());
        if (baseData.getSuccess()) {

//            userName.setText(name);
            // userEmail.setText(email);
            mQuestions.addAll(baseData.getResults());
            myApplication.setmQuestions(baseData.getResults());
        }

    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }
}

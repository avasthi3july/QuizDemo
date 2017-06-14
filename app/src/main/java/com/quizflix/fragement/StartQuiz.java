package com.quizflix.fragement;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
    private TextView introText1,introText2,introText3,introText4;
    private Dialog dialog;
    private String id;
    @BindView(R.id.start_quiz)
    TextView startQuiz;
    @BindView(R.id.chap_name)
    TextView chapterName;
    private ArrayList<Question> mQuestions;
    private Button dismiss;
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
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.intro_layout);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // set the custom dialog components - text, image and button
            introText1 = (TextView) dialog.findViewById(R.id.intro_text1);
            introText2 = (TextView) dialog.findViewById(R.id.intro_text2);
            introText3 = (TextView) dialog.findViewById(R.id.intro_text3);
            introText4 = (TextView) dialog.findViewById(R.id.intro_text4);
            dismiss = (Button) dialog.findViewById(R.id.dismiss);
            Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/intro.otf");
            introText1.setTypeface(type);
            introText2.setTypeface(type);
            introText3.setTypeface(type);
            introText4.setTypeface(type);
            dismiss.setOnClickListener(this);
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
        ((MainActivity) getActivity()).setHeader("Play Quiz");
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
        /*if (!myApplication.isAppOpen())
            Util.showDialog1(getActivity(), "For Answering questions, Swipe right for true, Swipe left for false.\n" +
                    "For every correct Answer you will get +1000 points and for every incorrect Answer you will be deducted -500 points.\n" +
                    "At the end of quiz you will see your total earned score for that chapter.\n" +
                    "Clicking explanation button at score screen, will show explanation about question, correct answer for questions and your given answer.");
        myApplication.setAppOpen(true);*/

    }

    @Override
    public void onClick(View v) {
        if (v == startQuiz) {
            QuizView mQuizView = new QuizView();
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("dataList", mQuestions);
            mQuizView.setArguments(mBundle);
            ((MainActivity) getActivity()).replaceFragment(R.id.contenair, mQuizView, mQuizView.getClass().getName(), mQuizView.getClass().getName());
        } else if (v == dismiss)
            dialog.dismiss();

    }

    @Override
    public void onSuccess(int tag, String baseResponse) {
        mQuestions = new ArrayList<>();
        BaseResponse baseData = JsonDataParser.getInternalParser(baseResponse, new TypeToken<BaseResponse>() {
        }.getType());
        if (!myApplication.isAppOpen())
            openDialog();
        myApplication.setAppOpen(true);
        if (baseData.getSuccess()) {

//            userName.setText(name);
            // userEmail.setText(email);

            mQuestions.addAll(baseData.getResults());
            chapterName.setText("Chapter : " + mQuestions.get(0).getChapterId());
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

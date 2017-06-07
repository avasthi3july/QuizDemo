package com.quizflix.fragement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quizflix.R;
import com.quizflix.Util.Util;
import com.quizflix.activity.MainActivity;
import com.quizflix.delegates.Api;
import com.quizflix.delegates.MyApplication;
import com.quizflix.delegates.ServiceCallBack;
import com.quizflix.webservice.BaseRequest;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by kavasthi on 6/6/2017.
 */

public class ScoreView extends Fragment implements View.OnClickListener, ServiceCallBack {
    @BindView(R.id.total_score)
    TextView totalScore;
    @BindView(R.id.explanation)
    Button explanation;
    @BindView(R.id.meg)
    TextView message;
    private SharedPreferences mSharedPreferences;
    private MyApplication myApplication;

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
        myApplication = (MyApplication) getActivity().getApplicationContext();
        mSharedPreferences = Util.getSharedPreferences(getActivity());
        String id = mSharedPreferences.getString("id", null);
        int score = getArguments().getInt("score");
        if (score > 0)
            totalScore.setText("" + score);
        if (score < 4000) {
            message.setText("You Failed!");
        }
        explanation.setOnClickListener(this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserId", id);
            jsonObject.put("ChapterId", myApplication.getmQuestions().get(0).getChapterId());
            jsonObject.put("Score", score);
            submitScore(jsonObject);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        ExplainationView mExplainationView = new ExplainationView();
        ((MainActivity) getActivity()).replaceFragment(R.id.contenair, mExplainationView, mExplainationView.getClass().getName(), mExplainationView.getClass().getName());
    }

    public void submitScore(JSONObject jsonObject) {
        BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setProgressShow(true);
        baseRequest.setRequestTag(Api.ADD_SCORE);
        baseRequest.setMessage("Please wait...");
        baseRequest.setServiceCallBack(this);
        Api api = (Api) baseRequest.execute(Api.class);
        try {
            TypedByteArray input = new TypedByteArray("application/json", jsonObject.toString().getBytes("UTF-8"));
            api.getUserInsert(input, baseRequest.requestCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int tag, String baseResponse) {

    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }
}

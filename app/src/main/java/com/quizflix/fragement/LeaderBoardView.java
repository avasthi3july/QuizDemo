package com.quizflix.fragement;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.quizflix.R;
import com.quizflix.activity.MainActivity;
import com.quizflix.adapter.LeaderViewAdapter;
import com.quizflix.adapter.UserAdapter;
import com.quizflix.dao.BaseResponse;
import com.quizflix.dao.Leader;
import com.quizflix.dao.Result;
import com.quizflix.delegates.Api;
import com.quizflix.delegates.Header;
import com.quizflix.delegates.RecyclerItemClickListener;
import com.quizflix.delegates.ServerApi;
import com.quizflix.delegates.ServiceCallBack;
import com.quizflix.webservice.BaseRequest;
import com.quizflix.webservice.JsonDataParser;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by kavasthi on 6/6/2017.
 */

public class LeaderBoardView extends Fragment implements ServiceCallBack {
    private ArrayList<Result> userListData;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leader_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    public void getUserList() {
        BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setProgressShow(true);
        baseRequest.setRequestTag(Api.ADD_USER_REGISTRATION);
        baseRequest.setMessage("Please wait...");
        baseRequest.setServiceCallBack(this);
        Api api = (Api) baseRequest.execute(Api.class);
        try {
            //TypedByteArray input = new TypedByteArray("application/json", jsonObject.toString().getBytes("UTF-8"));
            api.getUserQList(baseRequest.requestCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void initViews(View view) {
        ((MainActivity)getActivity()).setHeader("LeaderBoard");
        getUserList();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(ll);

      /*  mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String userId = userListData.get(position).getId();
                String score = userListData.get(position).getScore();
                System.out.println("userId" + userId + "score>>>>" + score);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserId", userId);
                    //jsonObject.put("ChapterId", "1");
                    jsonObject.put("Score", score);
                    submitScore(jsonObject);
                } catch (Exception e) {

                }

                //getAdData(adDataList.get(position).getId());
            }
        }));*/
    }

    @Override
    public void onSuccess(int tag, String baseResponse) {
        if (tag == Api.ADD_USER_REGISTRATION) {
            Leader baseData = JsonDataParser.getInternalParser(baseResponse, new TypeToken<Leader>() {
            }.getType());
            userListData = new ArrayList<>();
            if (baseData.getSuccess()) {
                for (int i = 0; i < 10; i++) {
                    userListData.add(baseData.getResults().get(i));
                }
                LeaderViewAdapter mLeaderViewAdapter = new LeaderViewAdapter(getActivity(), userListData);
                mRecyclerView.setAdapter(mLeaderViewAdapter);
            }
        } else if (tag == Api.ADD_SCORE) {

        }
    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }


}

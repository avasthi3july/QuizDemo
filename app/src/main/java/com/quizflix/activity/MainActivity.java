package com.quizflix.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.quizflix.R;
import com.quizflix.Util.Util;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServiceCallBack, View.OnClickListener {
    private SharedPreferences mSharedPreferences;
    private String id, name, email;
    private TextView userName, userEmail, question;
    private RecyclerView mRecyclerView;
    private ArrayList<Question> mQuestions;
    @BindView(R.id.ques)
    TextView ques;
    @BindView(R.id.btnNext)
    TextView btnNext;
    @BindView(R.id.num)
    TextView num;
    int i = 0, quesNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       // getSupportActionBar().setCustomView(R.layout.header_view);
        mQuestions = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.name);
        userEmail = (TextView) header.findViewById(R.id.input_email);
        btnNext.setOnClickListener(this);
        mSharedPreferences = Util.getSharedPreferences(this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.logout) {
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putBoolean("isLogin", false);
            edit.commit();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void userRegistration(JSONObject jsonObject) {
        BaseRequest baseRequest = new BaseRequest(this);
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
        BaseResponse baseData = JsonDataParser.getInternalParser(baseResponse, new TypeToken<BaseResponse>() {
        }.getType());
        if (baseData.getSuccess()) {
            userName.setText(name);
            userEmail.setText(email);
            mQuestions.addAll(baseData.getResults());
            num.setText("Q : 1 ");
            ques.setText(mQuestions.get(0).getQuestion());
        } else Util.showToast(this, baseData.getMessage());


    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }

    @Override
    public void onClick(View v) {
        i = ++i;
        quesNum = ++quesNum;
        if (mQuestions != null && i < mQuestions.size()) {
            num.setText("Q : " + quesNum + " ");
            ques.setText(mQuestions.get(i).getQuestion());
        } else Util.showToast(this, "No More Question");

    }
}

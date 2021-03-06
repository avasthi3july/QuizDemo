package com.quizflix.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.quizflix.R;
import com.quizflix.Util.Util;
import com.quizflix.adapter.QuestionAdapter;
import com.quizflix.dao.BaseResponse;
import com.quizflix.dao.Data;
import com.quizflix.dao.Question;
import com.quizflix.delegates.Api;
import com.quizflix.delegates.Header;
import com.quizflix.delegates.ServiceCallBack;
import com.quizflix.fragement.LeaderBoardView;
import com.quizflix.fragement.QuizView;
import com.quizflix.fragement.StartQuiz;
import com.quizflix.fragement.UserListView;
import com.quizflix.webservice.BaseRequest;
import com.quizflix.webservice.JsonDataParser;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServiceCallBack, View.OnClickListener, Header {
    private SharedPreferences mSharedPreferences;
    private TextView userName, userEmail;
    private String nameUser, emailUser;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.input_email)
    TextView inputEmail;
    /*@BindView(R.id.toolbar_title)
    TextView toolbarTitle;*/
    TextView headerName;

    @Override
    protected int getView() {
        return R.layout.activity_main2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mSharedPreferences = Util.getSharedPreferences(this);
        emailUser = mSharedPreferences.getString("email", null);
        nameUser = mSharedPreferences.getString("name", null);
        boolean isLog = mSharedPreferences.getBoolean("isLogin", false);
        String id = mSharedPreferences.getString("is", null);
        System.out.println("IDD>>" + id + "is" + isLog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // getSupportActionBar().setCustomView(R.layout.header_view);
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
        ButterKnife.bind(this, header);
        name.setText(nameUser);
        inputEmail.setText(emailUser);

        headerName = (TextView) findViewById(R.id.toolbar_title);
        // userName = (TextView) header.findViewById(R.id.name);
        //userEmail = (TextView) header.findViewById(R.id.input_email);
        StartQuiz mStartQuiz = new StartQuiz();
        replaceFragment(R.id.contenair, mStartQuiz, mStartQuiz.getClass().getName(), mStartQuiz.getClass().getName());
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
        getMenuInflater().inflate(R.menu.main2, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    LeaderBoardView mLeaderBoardView = new LeaderBoardView();
    StartQuiz mStartQuiz = new StartQuiz();
    UserListView mUserListView = new UserListView();

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            replaceFragment(R.id.contenair, mStartQuiz, mStartQuiz.getClass().getName(), mStartQuiz.getClass().getName());
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            replaceFragment(R.id.contenair, mUserListView, mUserListView.getClass().getName(), mUserListView.getClass().getName());

        } else if (id == R.id.nav_slideshow) {
            replaceFragment(R.id.contenair, mLeaderBoardView, mLeaderBoardView.getClass().getName(), mLeaderBoardView.getClass().getName());

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

    @Override
    public void onSuccess(int tag, String baseResponse) {
    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }

    public void clearBackStackInclusive(String tag) {
        try {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {/*
        i = ++i;
        quesNum = ++quesNum;
        if (mQuestions != null && i < mQuestions.size()) {
            num.setText("Q : " + quesNum + " ");
            ques.setText(mQuestions.get(i).getQuestion());
        } else Util.showToast(this, "No More Question");
*/
    }

    public Fragment currentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.contenair);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

    @Override
    public void setHeader(String name) {
        headerName.setText(name);

    }
}

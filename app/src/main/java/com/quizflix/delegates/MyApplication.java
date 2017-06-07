package com.quizflix.delegates;

import android.app.Application;

import com.quizflix.dao.Question;

import java.util.ArrayList;

/**
 * Created by rukumari on 4/9/2016.
 */
public class MyApplication extends Application {
    ArrayList<Question> mQuestions;
    private boolean appOpen;

    public boolean isAppOpen() {
        return appOpen;
    }

    public void setAppOpen(boolean appOpen) {
        this.appOpen = appOpen;
    }

    public ArrayList<Question> getmQuestions() {
        return mQuestions;
    }

    public void setmQuestions(ArrayList<Question> mQuestions) {
        this.mQuestions = mQuestions;
    }
}
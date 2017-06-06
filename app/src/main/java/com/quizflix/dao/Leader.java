package com.quizflix.dao;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kavasthi on 6/6/2017.
 */

public class Leader {
    @SerializedName("Results")
    private ArrayList<Result> results;
    @SerializedName("Success")
    private boolean success;

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

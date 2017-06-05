package com.quizflix.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 6/1/2017.
 */

public class Question {
    @SerializedName("QuestionId")
    private String questionId;
    @SerializedName("Question")
    private String question;
    @SerializedName("Explanation")
    private String explanation;
    @SerializedName("Answer")
    private boolean answer;
    @SerializedName("IsActive")
    private String isActive;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}

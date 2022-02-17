package com.aro.trivia.model;

import androidx.annotation.NonNull;

public class Question {

    //this class holds a single question at a time

    private String questionStatement;
    private boolean answer;

    public Question(){}
    public Question(String questionStatement, boolean answer) {
        this.questionStatement = questionStatement;
        this.answer = answer;
    }


    public String getQuestionStatement() {
        return questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "question = " + questionStatement +
                " , answer = " + answer + "}";
    }
}

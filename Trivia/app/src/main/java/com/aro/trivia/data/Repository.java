package com.aro.trivia.data;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aro.trivia.controller.AppController;
import com.aro.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository{

    //this class is meant to hold all the JSON data for our questions

    //url is being passed by the Main Activity. it is being constructed there based on user's selected category
   //String url = "https://opentdb.com/api.php?amount=20&category=9&type=boolean";


    ArrayList<Question> questionArrayList = new ArrayList<>();

    JSONArray results = null;


    public List<Question> getTriviaQuestions (String url, final AnswerListAsyncResponse callBack){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        results = response.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {

                            JSONObject arrayObject = results.getJSONObject(i);

                            String questionString = arrayObject.getString("question");
                            String answerString = arrayObject.getString("correct_answer");

                            if (questionString.contains("&quot;")){
                                questionString = questionString.replace("&quot;", "\"");
                            }
                            if(questionString.contains("&#039;")){
                                questionString = questionString.replace("&#039;", "'");
                            }
                            if(questionString.contains("&eacute;")){
                                questionString = questionString.replace("&eacute;", "e");
                            }
                            if(questionString.contains("&rsquo;")){
                                questionString = questionString.replace("&rsquo;", "'");
                            }
                            if(questionString.contains("&deg;")){
                                questionString = questionString.replace("&deg;", " degrees");
                            }


                            boolean answer;

                            if(answerString.equals("True")){
                                answer = true;
                            }
                            else{
                                answer = false;
                            }

                            Log.d("test", "question - " + questionString + " answer - " + answerString);

                            Question question = new Question(
                                    questionString,
                                    answer

                            );
                            //add question to arraylist
                            questionArrayList.add(question);
                        }

                        Log.d("test", "question list size " + questionArrayList.size());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(callBack != null){
                            callBack.processFinished(questionArrayList);
                    }


                },
                error -> Log.d("json", "failed to retrieve json array " + error)
        );

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return questionArrayList;

    }


}

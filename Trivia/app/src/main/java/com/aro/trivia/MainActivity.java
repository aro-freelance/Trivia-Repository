package com.aro.trivia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aro.trivia.data.Repository;
import com.aro.trivia.databinding.ActivityMainBinding;
import com.aro.trivia.model.Question;
import com.aro.trivia.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    /*
    This app is a Trivia game that pulls questions from opentdb.com and shows them based on category.

    New url https://opentdb.com/api.php?amount=10&category=21&difficulty=medium&type=boolean

    any difficulty: https://opentdb.com/api.php?amount=20&category=9&type=boolean

    Can be used to set different categories. 9 - 32

    9 General Knowledge
    10 Books ---- not enough questions
    11 Film
    12 Music
    13 Musicals  --- not enough questions
    14 Television --- 10 questions
    15 Video Games
    16 Board Games --- not enough questions
    17 Nature
    18 Computers
    19 Math --- 10 questions
    20 Mythology --- 10 questions
    21 Sports --- 10 questions
    22 Geography
    23 History
    24 Politics --- 10 questions
    25 Art  ---not enough questions
    26 Celebrities --- not enough questions
    27 Animals --- 20 questions
    28 Vehicles --- 10 questions
    29 Comics --- not enough questions
    30 Gadgets --- not enough questions
    31 Japanese Manga -- 20 questions
    32 Cartoons and Animation --- 10 questions


     */



    Handler mHandler = new Handler();
    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    List<Question> questionList;

    private final int numberOfQuestions = 10;
    private List<Integer> currentGameQuestionList;

    private int currentScore = 0;
    private int highScore = 0;
    private int questionsCorrect = 0;

    private Prefs prefs;

    private int delayBetweenQuestions = 2000; //ms
    private String speedOption;

    private String category = "";
    private int catInt = 0;


    private String url = "https://opentdb.com/api.php?amount=20&category=9&type=boolean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main); //this replaces setContentView

        prefs = new Prefs(this);


        currentGameQuestionList = new ArrayList<>();

        binding.questionCounterTextview.setText(R.string.loading_questions);
        binding.questionTextview.setText("");
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.trueButton.setEnabled(false);
        binding.falseButton.setEnabled(false);
        binding.shareButton.setEnabled(false);

        binding.previousButton.setVisibility(View.GONE);
        binding.nextButton.setVisibility(View.GONE);
        binding.endGameButton.setVisibility(View.GONE);



        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            category = bundle.getString("category", "General Knowledge");

            speedOption = bundle.getString("speed_option", "Medium");

            setSpeed();

            binding.categoryHeaderTextMainActivity.setText(category);

            setUrl();
        }

        highScore = prefs.getHighScore(category);


       //this is setting the view for the first question
        //.getQuestions or .getTriviaQuestions
        questionList = new Repository().getTriviaQuestions(url, questionArrayList -> {

            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.trueButton.setEnabled(true);
            binding.falseButton.setEnabled(true);
            binding.shareButton.setEnabled(true);

            //shuffle the list
            setQuestionList();

            //set the first question oncreate
            binding.questionTextview.setText(questionArrayList.get(currentGameQuestionList.get(currentQuestionIndex)).getQuestionStatement());

            updateQuestionCounter();

        });

        updateScore();

        binding.nextButton.setOnClickListener(this::nextButton);
        binding.trueButton.setOnClickListener(this::trueButton);
        binding.falseButton.setOnClickListener(this::falseButton);
        binding.shareButton.setOnClickListener(this::shareButton);

        binding.previousButton.setOnClickListener(this::previousButton);
        binding.endGameButton.setOnClickListener(this::endGameButton);

    }

    private void endGameButton(View view) {

        //if the current score is a high score save it
        prefs.saveHighestScore(currentScore, category);
        highScore = prefs.getHighScore(category);

        Intent intent = new Intent (MainActivity.this, EndScreenActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("speed_option", speedOption);

        intent.putExtra("score", currentScore);
        intent.putExtra("high_score", highScore);
        intent.putExtra("questions_correct", questionsCorrect);

        startActivity(intent);
        finish();
    }

    private void endScreen(){

        binding.previousButton.setVisibility(View.VISIBLE);
        binding.nextButton.setVisibility(View.VISIBLE);
        binding.endGameButton.setVisibility(View.VISIBLE);

        binding.trueButton.setEnabled(false);
        binding.falseButton.setEnabled(false);

        showAnswer();

    }

    private void showAnswer(){

        //make the correct answer show green and wrong show red

        boolean answer = questionList.get(currentQuestionIndex).isAnswer();

        if(answer){
            binding.trueButton.setTextColor(Color.GREEN);
            binding.falseButton.setTextColor(Color.RED);
        }
        else{
            binding.trueButton.setTextColor(Color.RED);
            binding.falseButton.setTextColor(Color.GREEN);

        }


    }

    private void setSpeed(){

        switch(speedOption){

            case "Medium":
                delayBetweenQuestions = 2000;
                break;
            case "Slow":
                delayBetweenQuestions = 4000;
                break;
            case "Fast":
                delayBetweenQuestions = 1200;
                break;
        }

    }

    private void setUrl(){

        switch (category){

            case "General Knowledge":
                catInt = 9;
                break;
            case "Film":
                catInt = 11;
                break;
            case "Music":
                catInt = 12;
                break;
            case "Television":
                catInt = 14;
                break;
            case "Video Games":
                catInt = 15;
                break;
            case "Nature":
                catInt = 17;
                break;
            case "Computers":
                catInt = 18;
                break;
            case "Math":
                catInt = 19;
                break;
            case "Mythology":
                catInt = 20;
                break;
            case "Sports":
                catInt = 21;
                break;
            case "Geography":
                catInt = 22;
                break;
            case "History":
                catInt = 23;
                break;
            case "Politics":
                catInt = 24;
                break;
            case "Animals":
                catInt = 27;
                break;
            case "Vehicles":
                catInt = 28;
                break;
            case "Japanese Manga":
                catInt = 31;
                break;
            case "Cartoons and Animation":
                catInt = 32;
                break;

        }

        url = "https://opentdb.com/api.php?amount=10&category=" + catInt + "&type=boolean";

    }

    private void setQuestionList(){

        for (int i = 0; i < questionList.size(); i++) {

            currentGameQuestionList.add(i);

        }

        Collections.shuffle(currentGameQuestionList);
    }

    private void shareButton(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out my Trivia Scores!");
        intent.putExtra(Intent.EXTRA_TEXT, "I am playing the Arora Trivia app. Check out these scores I got! " +
                "My current score is " + currentScore + ". And my high score is " + highScore + ".");

        //todo add Playstore Link. after the period in the above string:  You can check it out here: PLAYSTORE LINK

        startActivity(intent);
    }

    private void updateScore() {

        //set the views for current and high score
        binding.scoreTextview.setText(String.format(getString(R.string.current_score), currentScore));
        binding.highscoreTextview.setText(String.format(getString(R.string.high_score), highScore));
    }

    private void updateQuestionCounter() {
        //the +1 here is to fix for array starting at zero. not to increment anything
        binding.questionCounterTextview.setText(String.format(getString(R.string.question_counter),
                currentQuestionIndex+1, numberOfQuestions));
    }

    private void falseButton(View view) {

        checkAnswer(false);
        updateQuestion();
        updateScore();

    }

    private void trueButton(View view) {

        checkAnswer(true);
        updateQuestion();
        updateScore();

    }

    private void nextButton(View view) {
        goToNextQuestion();
    }

    private void previousButton(View view){
        goToPreviousQuestion();
    }

    private void goToPreviousQuestion(){

        if((currentQuestionIndex) > 0) {

            currentQuestionIndex = currentQuestionIndex - 1;
            updateQuestion();

            showAnswer();
        }
    }

    private void autoNext(){
        //go to the next question
        if((currentQuestionIndex + 1) < numberOfQuestions){
            currentQuestionIndex++;
            updateQuestion();

            //set the buttons back to default after the next button is selected (after disabling them so multiple answers could not be chosen)
            binding.trueButton.setEnabled(true);
            binding.falseButton.setEnabled(true);
            binding.trueButton.setBackgroundColor(getResources().getColor(R.color.button_color_2));
            binding.falseButton.setBackgroundColor(getResources().getColor(R.color.button_color_2));



        }
        //or if there are no more questions...
        else{
            endScreen();

        }

    }



    private void goToNextQuestion(){

        //go to the next question
        if((currentQuestionIndex + 1) < numberOfQuestions) {
            currentQuestionIndex++;
            updateQuestion();
            showAnswer();
        }
    }

    private void updateQuestion() {
        //get the question string
        String question = questionList.get(currentGameQuestionList.get(currentQuestionIndex)).getQuestionStatement();
        //set the question string to the question textview
        binding.questionTextview.setText(question);


        //increment the question counter header
        updateQuestionCounter();
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswer();
        int snackMessageId;
        //user is right
        if(userAnswer == answer){
            snackMessageId = R.string.correct_answer;
            fadeAnimation();

            //earn points
            currentScore = currentScore + 100;

            questionsCorrect = questionsCorrect +1;

        }
        //user is wrong
        else{
            snackMessageId = R.string.incorrect_answer;
            shakeAnimation();

            //lose points
            currentScore = currentScore - 50;
            //points can't go below zero
            if(currentScore < 0){currentScore = 0;}

        }
        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_SHORT)
                .show();


        //after the set amount of delay go to the next question
        mHandler.postDelayed(this::autoNext, delayBetweenQuestions);

    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);

                //don't let user select multiple answers and help them see they need to click the next button
                binding.trueButton.setEnabled(false);
                binding.falseButton.setEnabled(false);
                binding.trueButton.setBackgroundColor(Color.GRAY);
                binding.falseButton.setBackgroundColor(Color.GRAY);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);

                //don't let user select multiple answers and help them see they need to click the next button
                binding.trueButton.setEnabled(false);
                binding.falseButton.setEnabled(false);
                binding.trueButton.setBackgroundColor(Color.GRAY);
                binding.falseButton.setBackgroundColor(Color.GRAY);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
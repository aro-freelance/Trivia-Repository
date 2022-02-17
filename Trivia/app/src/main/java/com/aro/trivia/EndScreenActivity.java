package com.aro.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndScreenActivity extends AppCompatActivity {

    TextView scoreTextView;
    TextView numberCorrectTextView;
    TextView highScoreTextView;
    TextView catSubHeadTextView;

    Button returnToStartButton;

    String speedOption;
    String category;

    int score = 0;
    int highScore = 0;
    int numberCorrect = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        scoreTextView = findViewById(R.id.score_end_screen_textview);
        numberCorrectTextView = findViewById(R.id.number_correct_end_screen);
        highScoreTextView = findViewById(R.id.high_score_end_screen_text);
        catSubHeadTextView = findViewById(R.id.endscreen_cat_text);

        returnToStartButton = findViewById(R.id.end_screen_button);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            speedOption = bundle.getString("speed_option", "Medium");
            category = bundle.getString("category", "General Knowledge");

            score = bundle.getInt("score", 0);
            highScore = bundle.getInt("high_score", 0);
            numberCorrect = bundle.getInt("questions_correct", 0);
        }

        catSubHeadTextView.setText(category);
        scoreTextView.setText(String.valueOf(score));
        numberCorrectTextView.setText(String.valueOf(numberCorrect));
        highScoreTextView.setText(String.valueOf(highScore));

        returnToStartButton.setOnClickListener(this::ReturnToStartButtonMethod);

    }

    private void ReturnToStartButtonMethod(View view) {

        Intent intent = new Intent(EndScreenActivity.this, StartActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("speed_option", speedOption);
        startActivity(intent);
        finish();
    }
}
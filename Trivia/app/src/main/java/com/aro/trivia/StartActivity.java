package com.aro.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class StartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    Button startButton;
    Button optionsButton;


    String category;

    String speedOption;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_start);


        startButton = findViewById(R.id.start_button);
        optionsButton = findViewById(R.id.options_button);
        spinner = findViewById(R.id.spinner);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            speedOption = bundle.getString("speed_option", "Medium");
            category = bundle.getString("category", "General Knowledge");
        }

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.trivia_categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if(category != null){
            setCatSpinner();
        }

        startButton.setOnClickListener(this::StartButtonMethod);
        optionsButton.setOnClickListener(this::OptionsButtonMethod);

    }

    private void setCatSpinner() {

        int catPos = 0;

        switch(category){
            case "General Knowledge":
                catPos = 0;
                break;
            case "Film":
                catPos = 1;
                break;
            case "Music":
                catPos = 2;
                break;
            case "Television":
                catPos = 3;
                break;
            case "Video Games":
                catPos = 4;
                break;
            case "Nature":
                catPos = 5;
                break;
            case "Computers":
                catPos = 6;
                break;
            case "Math":
                catPos = 7;
                break;
            case "Mythology":
                catPos = 8;
                break;
            case "Sports":
                catPos = 9;
                break;
            case "Geography":
                catPos = 10;
                break;
            case "History":
                catPos = 11;
                break;
            case "Politics":
                catPos = 12;
                break;
            case "Animals":
                catPos = 13;
                break;
            case "Vehicles":
                catPos = 14;
                break;
            case "Japanese Manga":
                catPos = 15;
                break;
            case "Cartoons and Animation":
                catPos = 16;
                break;
        }

        spinner.setSelection(catPos);

    }

    private void OptionsButtonMethod(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("speed_option", speedOption);
        startActivity(intent);
    }

    private void StartButtonMethod(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("speed_option", speedOption);
        startActivity(intent);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        category = parent.getItemAtPosition(pos).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }
}
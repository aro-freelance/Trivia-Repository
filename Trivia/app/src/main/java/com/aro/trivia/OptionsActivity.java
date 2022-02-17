package com.aro.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    Spinner speedSpinner;
    Button saveOptionsButton;

    String category;
    String speedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);



        speedSpinner = findViewById(R.id.speed_option_spinner);
        saveOptionsButton = findViewById(R.id.options_save_button);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            speedString = bundle.getString("speed_option", "Medium");
            category = bundle.getString("category", "General Knowledge");

        }

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speed_options_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        speedSpinner.setAdapter(adapter);
        speedSpinner.setOnItemSelectedListener(this);

        if(speedString != null){
            int spinnerPos;
            if(speedString.equals("Slow")){
                spinnerPos = 1;
            }
            else if(speedString.equals("Fast")){
                spinnerPos = 2;
            }
            else{
                spinnerPos = 0;
            }

            speedSpinner.setSelection(spinnerPos);
        }

        saveOptionsButton.setOnClickListener(this::SaveOptionsButtonMethod);
    }

    private void SaveOptionsButtonMethod(View view) {

        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("speed_option", speedString);

        //ADD MORE OPTIONS HERE

        startActivity(intent);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        speedString = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        speedString = "Medium";

    }
}
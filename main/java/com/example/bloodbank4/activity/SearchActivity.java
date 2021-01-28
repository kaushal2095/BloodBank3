package com.example.bloodbank4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank4.R;

public class SearchActivity extends AppCompatActivity {

    private Spinner bgps;
    private Button submitButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initComponent();

        applyListener();
    }

    private void applyListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty()){
                    Intent intent=new Intent(SearchActivity.this,ResultActivity.class);
                    intent.putExtra("bloodgp",bgps.getSelectedItem().toString());
                    startActivity(intent);

                }   else{
                    Toast.makeText(SearchActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initComponent() {
        submitButton = findViewById(R.id.search_submit);
        editText = findViewById(R.id.search_name);
        bgps = findViewById(R.id.search_blood_group_spinner);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_dropdown_item);
        bgps.setAdapter(arrayAdapter);
    }
}

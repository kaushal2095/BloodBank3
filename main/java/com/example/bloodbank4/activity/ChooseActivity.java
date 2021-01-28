package com.example.bloodbank4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bloodbank4.R;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.choose_donate:
                Intent intent=new Intent(this,DonateActivity.class);
                startActivity(intent);
                break;
            case R.id.choose_search:
                Intent searchIntent=new Intent(this,SearchActivity.class);
                startActivity(searchIntent);
                break;
            default:
                break;


        }
    }
}

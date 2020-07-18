package com.example.rxjavaretrofitroomdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChooserActivity extends AppCompatActivity {

    Button roomDBBtn, retrofitBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        roomDBBtn = (Button)findViewById(R.id.room_db_activity);
        retrofitBtn = (Button)findViewById(R.id.retrofit_activity);

        roomDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(MainActivity.class);
            }
        });

        retrofitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(RetrofitActivity.class);
            }
        });
    }

    private void launchActivity(Class targetActivity){
        Intent intent = new Intent(this,targetActivity);
        startActivity(intent);
    }
}

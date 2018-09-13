package com.example.chodry.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    public void reporting(View view){
        startActivity(new Intent(this, pdfActivity.class));
    }


    public void camera(View view) {
        startActivity(new Intent(this, camera.class));
    }

    public void gallery(View view) {
        startActivity(new Intent(this, gallery.class));
    }

    public void manual(View view) {
        startActivity(new Intent(this, secondpdf.class));
    }
}

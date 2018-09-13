package com.example.chodry.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

public class secondpdf extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypdf);

        pdfView = (PDFView)findViewById(R.id.pdfviewer);
        pdfView.fromAsset("manual.pdf").load();


    }

}

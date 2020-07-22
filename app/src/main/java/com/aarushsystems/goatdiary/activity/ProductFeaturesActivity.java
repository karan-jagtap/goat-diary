package com.aarushsystems.goatdiary.activity;

import android.os.Bundle;

import com.aarushsystems.goatdiary.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.util.Objects;

public class ProductFeaturesActivity extends AppCompatActivity {

    private TextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_features);
        Toolbar toolbar = findViewById(R.id.toolbar_HighlightsFeaturesActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.textView_detailed_HighlightsFeaturesActivity);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(Html.fromHtml(getString(R.string.product_features_detail)));
        textView2 = findViewById(R.id.textView2_detailed_HighlightsFeaturesActivity);
        textView2.setMovementMethod(new ScrollingMovementMethod());
        textView2.setText(Html.fromHtml(getString(R.string.product_features_details2)));
    }

}

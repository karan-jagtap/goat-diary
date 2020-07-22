package com.aarushsystems.goatdiary.activity;

import android.os.Bundle;

import com.aarushsystems.goatdiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.toolbar_ContactUsActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.textView_contact_details_ContactUsActivity);
        textView.setText(Html.fromHtml(getString(R.string.contact_us_details)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}

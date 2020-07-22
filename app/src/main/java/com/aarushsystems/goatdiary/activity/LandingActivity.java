package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.SessionManager;

import java.util.Objects;

public class LandingActivity extends AppCompatActivity {

    private TextView continueTV;
    private Spinner spinner;
    private Button contactBT, linkBT, featuresBT, userManualBT;
    private String facebookUrl = "https://www.facebook.com/Goatmatesoftware/";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar_LandingActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
        checkIfAlreadyLoggedIn();
    }

    private void declarations() {
        sessionManager = new SessionManager(LandingActivity.this);
        sessionManager.setPrefLanguage(0);
        continueTV = findViewById(R.id.textView_continue_MainActivity);
        linkBT = findViewById(R.id.button_social_LandingActivity);
        contactBT = findViewById(R.id.button_contact_LandingActivity);
        featuresBT = findViewById(R.id.button_product_features_LandingActivity);
        userManualBT = findViewById(R.id.button_user_manual_LandingActivity);
        spinner = findViewById(R.id.spinner_language_LandingActivity);
        spinner.setSelection(1);
    }

    private void listeners() {
        userManualBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.USER_MANUAL));
                    startActivity(browserIntent);
                } catch (Exception ignore) {
                }
            }
        });
        featuresBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this, ProductFeaturesActivity.class));
            }
        });
        contactBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this, ContactUsActivity.class));
            }
        });
        linkBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(facebookUrl));
                startActivity(i);
            }
        });
        continueTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItemPosition() > 0) {
                    sessionManager.setPrefLanguage(spinner.getSelectedItemPosition() - 1);
                } else {
                    sessionManager.setPrefLanguage(0);
                }
                startActivity(new Intent(LandingActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void checkIfAlreadyLoggedIn() {
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LandingActivity.this, DashBoardActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about_us) {
            startActivity(new Intent(LandingActivity.this, AboutUsActivity.class).putExtra("from", "landing_activity"));
            finish();
        }
        if (id == R.id.action_terms_and_cond) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.TERMS_AND_CONDITIONS));
                startActivity(browserIntent);
            } catch (Exception ignore) {
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

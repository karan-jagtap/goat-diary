package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.activity.reports.ReportBreedingActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportDailyActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportExpenseActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportFeedStockActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportIncomeActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportLivestockActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportMilkActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportVaccinationActivity;
import com.aarushsystems.goatdiary.activity.reports.ReportWeightActivity;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.LocalDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ReportsMenuActivity extends AppCompatActivity {

    private CardView livestockCV, weightCV, vaccinationCV, breedingCV, feedStockCV, milkCV, expenseCV, incomeCV, dailyCV;
    private TextView livestockTV, weightTV, vaccinationTV, breedingTV, feedStockTV, milkTV, expenseTV, incomeTV;
    private LocalDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_menu);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportsMenuActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        loadSubText();
        listeners();
    }

    private void loadSubText() {
        final ArrayList<Float> arrayList = db.getReportMenuValues();
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    livestockTV.setText("Total Animals : " + Math.round(arrayList.get(0)));
                    weightTV.setText("Total Weight : " + arrayList.get(1) + " kg.");
                    vaccinationTV.setText("Total Vaccinations Done : " + Math.round(arrayList.get(2)));
                    breedingTV.setText("Total Births : " + Math.round(arrayList.get(3)));
                    feedStockTV.setText("Total Food Stock : " + String.format(Locale.getDefault(), "%.3f", arrayList.get(4).floatValue()) + " kg.");
                    milkTV.setText("Total Milk : " + String.format(Locale.getDefault(), "%.3f", arrayList.get(5)) + " lts.");
                    expenseTV.setText("Total Expense : Rs. " + String.format(Locale.getDefault(), "%.2f", arrayList.get(6).floatValue()));
                    incomeTV.setText("Total Income : Rs. " + String.format(Locale.getDefault(), "%.2f", arrayList.get(7).floatValue()));
                    Log.i("CUSTOM", "RUN ENDED");
                }
            });
        } catch (Exception ignore) { }
    }

    private void listeners() {
        livestockCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportLivestockActivity.class));
            }
        });
        weightCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportWeightActivity.class));
            }
        });
        vaccinationCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportVaccinationActivity.class));
            }
        });
        breedingCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportBreedingActivity.class));
            }
        });
        feedStockCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportFeedStockActivity.class));
            }
        });
        milkCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportMilkActivity.class));
            }
        });
        expenseCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportExpenseActivity.class));
            }
        });
        incomeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportIncomeActivity.class));
            }
        });
        dailyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsMenuActivity.this, ReportDailyActivity.class));
            }
        });
    }

    private void declarations() {
        livestockCV = findViewById(R.id.card_view_livestock_ReportsMenuActivity);
        weightCV = findViewById(R.id.card_view_weight_ReportsMenuActivity);
        vaccinationCV = findViewById(R.id.card_view_vaccination_ReportsMenuActivity);
        breedingCV = findViewById(R.id.card_view_breeding_ReportsMenuActivity);
        feedStockCV = findViewById(R.id.card_view_feed_stock_ReportsMenuActivity);
        milkCV = findViewById(R.id.card_view_milk_ReportsMenuActivity);
        expenseCV = findViewById(R.id.card_view_expense_ReportsMenuActivity);
        incomeCV = findViewById(R.id.card_view_income_ReportsMenuActivity);
        dailyCV = findViewById(R.id.card_view_daily_report_ReportsMenuActivity);

        livestockTV = findViewById(R.id.textView_livestock_message_ReportsMenuActivity);
        weightTV = findViewById(R.id.textView_weight_message_ReportsMenuActivity);
        vaccinationTV = findViewById(R.id.textView_vaccination_message_ReportsMenuActivity);
        breedingTV = findViewById(R.id.textView_breeding_message_ReportsMenuActivity);
        feedStockTV = findViewById(R.id.textView_feed_stock_message_ReportsMenuActivity);
        milkTV = findViewById(R.id.textView_milk_message_ReportsMenuActivity);
        expenseTV = findViewById(R.id.textView_expense_message_ReportsMenuActivity);
        incomeTV = findViewById(R.id.textView_income_message_ReportsMenuActivity);

        db = new LocalDatabase(ReportsMenuActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_terms_and_cond) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.TERMS_AND_CONDITIONS));
                startActivity(browserIntent);
            } catch (Exception e) {
                Log.i("CUSTOM","e = "+e.getMessage());
            }
        }
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(ReportsMenuActivity.this, AboutUsActivity.class)
                    .putExtra("from", "reports_menu_activity"));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

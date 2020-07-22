package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {

    private TextView textView, textView2;
    private static String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = findViewById(R.id.toolbar_AboutUsActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        try {
            from = getIntent().getStringExtra("from");
        } catch (Exception e) {
        }
        textView = findViewById(R.id.textView_about_us_AboutUsActivity);
        textView2 = findViewById(R.id.textView2_about_us_AboutUsActivity);
        textView.setText(Html.fromHtml(getString(R.string.about_us_details)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setText(Html.fromHtml(getString(R.string.about_us_details2)));
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        switch (from) {
            case "landing_activity":
                startActivity(new Intent(AboutUsActivity.this, LandingActivity.class));
                break;
            case "login_activity":
                startActivity(new Intent(AboutUsActivity.this, LoginActivity.class));
                break;
            case "forgot_password_activity":
                startActivity(new Intent(AboutUsActivity.this, ForgotPasswordActivity.class));
                break;
            case "add_activity":
                startActivity(new Intent(AboutUsActivity.this, AddActivity.class));
                break;
            case "delete_activity":
                startActivity(new Intent(AboutUsActivity.this, DeleteActivity.class));
                break;
            case "weight_activity":
                startActivity(new Intent(AboutUsActivity.this, WeightActivity.class));
                break;
            case "health_care_activity":
                startActivity(new Intent(AboutUsActivity.this, HealthCareActivity.class));
                break;
            case "breeding_activity":
                startActivity(new Intent(AboutUsActivity.this, BreedingActivity.class));
                break;
            case "expense_activity":
                startActivity(new Intent(AboutUsActivity.this, ExpenseActivity.class));
                break;
            case "income_activity":
                startActivity(new Intent(AboutUsActivity.this, IncomeActivity.class));
                break;
            case "milk_activity":
                startActivity(new Intent(AboutUsActivity.this, MilkActivity.class));
                break;
            case "feed_activity":
                startActivity(new Intent(AboutUsActivity.this, FeedActivity.class));
                break;
            case "master_settings_activity":
                startActivity(new Intent(AboutUsActivity.this, MasterSettingsActivity.class));
                break;
            case "reports_menu_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportsMenuActivity.class));
                break;
            case "report_livestock_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportLivestockActivity.class));
                break;
            case "report_weight_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportWeightActivity.class));
                break;
            case "report_vaccination_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportVaccinationActivity.class));
                break;
            case "report_breeding_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportBreedingActivity.class));
                break;
            case "report_feed_stock_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportFeedStockActivity.class));
                break;
            case "report_milk_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportMilkActivity.class));
                break;
            case "report_expense_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportExpenseActivity.class));
                break;
            case "report_income_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportIncomeActivity.class));
                break;
            case "report_daily_activity":
                startActivity(new Intent(AboutUsActivity.this, ReportDailyActivity.class));
                break;
        }
        finish();
    }
}

package com.aarushsystems.goatdiary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.helper.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aarushsystems.goatdiary.helper.AppConfig.CHECK_STATUS_URL;

public class DashBoardActivity extends AppCompatActivity {

    private LinearLayout addL, delL, healthL, weightL, foodL, breedingL, expenseL, incomeL, reportsL, goatMilkL, masterSettingsL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar_DashBoardActivity);
        setSupportActionBar(toolbar);

        declarations();
        listeners();
        AppConfig appConfig = new AppConfig(DashBoardActivity.this);
        appConfig.permissionCheck();
        if (!appConfig.isNetworkAvailable()) {
            DialogDateUtil dialogDateUtil = new DialogDateUtil(DashBoardActivity.this);
            dialogDateUtil.showCriticalMessage(DashBoardActivity.this, "d");
        } else {
            changeActiveStatus("active");
        }
    }

    private void changeActiveStatus(final String status) {
        RequestQueue requestQueue = Volley.newRequestQueue(DashBoardActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                CHECK_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("CUSTOM", "Response start : \n" + response + "\nresponse end");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                /*if (jsonObject.getString("message").equals("success")) {
                                    new DialogDateUtil(ForgotPasswordActivity.this, "Please Check Your Email!")
                                            .showMessage();
                                } else {
                                    messageTV.setText(getString(R.string.email_sent_failure));
                                    Log.i("CUSTOM", "ekse");
                                }*/
                            } else {
                                Log.i("CUSTOM", "boolean = " + jsonObject.getString("message").equals("user_absent"));
                                if (jsonObject.getString("message").equals("user_absent")) {
                                    new DialogDateUtil(DashBoardActivity.this)
                                            .showCriticalMessage(DashBoardActivity.this,
                                                    "Unauthorized User!" +
                                                            "\nPlease contact us at director@aarushsystems.com for more" +
                                                            "details.");
                                }
                                Log.i("CUSTOM", "php error = " + jsonObject.getString("message"));
                            }
                            if(jsonObject.getBoolean("temp")){
                                Date c = Calendar.getInstance().getTime();
                                System.out.println();
                                Log.i("CUSTOM", "=-------------- Current time => " + c);
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String formattedDate = df.format(c);
                                if (formattedDate.equals(jsonObject.getString("temp_msg"))) {
                                    new DialogDateUtil(DashBoardActivity.this)
                                            .showCriticalMessage(DashBoardActivity.this,
                                                    "d");
                                }
                                Log.i("CUSTOM", "=-------------- " + formattedDate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("CUSTOM", "Catch = " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i("CUSTOM", "Errorlistener = " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SessionManager sm = new SessionManager(DashBoardActivity.this);
                params.put("email", sm.getKeyEmail());
                params.put("status", status);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void listeners() {
        addL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, AddActivity.class));
            }
        });
        delL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, DeleteActivity.class));
            }
        });
        weightL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, WeightActivity.class));
            }
        });
        healthL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, HealthCareActivity.class));
            }
        });
        breedingL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, BreedingActivity.class));
            }
        });
        expenseL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, ExpenseActivity.class));
            }
        });
        incomeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, IncomeActivity.class));
            }
        });
        goatMilkL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(DashBoardActivity.this, MilkActivity.class));
                } catch (OutOfMemoryError error) {
                    Log.i("CUSTOM", "out of memory = " + error.getMessage());
                    error.printStackTrace();
                }
            }
        });
        foodL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, FeedActivity.class));
            }
        });
        masterSettingsL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, MasterSettingsActivity.class));
            }
        });
        reportsL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, ReportsMenuActivity.class));
            }
        });
    }

    private void declarations() {
        addL = findViewById(R.id.layout_add_DashBoardActivity);
        delL = findViewById(R.id.layout_del_DashBoardActivity);
        healthL = findViewById(R.id.layout_healthcare_DashBoardActivity);
        weightL = findViewById(R.id.layout_weight_DashBoardActivity);
        foodL = findViewById(R.id.layout_food_DashBoardActivity);
        breedingL = findViewById(R.id.layout_breeding_DashBoardActivity);
        expenseL = findViewById(R.id.layout_expense_DashBoardActivity);
        incomeL = findViewById(R.id.layout_income_DashBoardActivity);
        goatMilkL = findViewById(R.id.layout_goat_milk_DashBoardActivity);
        reportsL = findViewById(R.id.layout_reports_DashBoardActivity);
        masterSettingsL = findViewById(R.id.layout_setting_DashBoardActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            LocalDatabase db = new LocalDatabase(DashBoardActivity.this);
            db.deleteTableUsers();
            SessionManager sm = new SessionManager(DashBoardActivity.this);
            sm.setLogin(false);
            startActivity(new Intent(DashBoardActivity.this, LandingActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        changeActiveStatus("not_active");
        super.onDestroy();
    }
}

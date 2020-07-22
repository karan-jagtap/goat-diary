package com.aarushsystems.goatdiary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.helper.SessionManager;
import com.aarushsystems.goatdiary.model.UserModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aarushsystems.goatdiary.helper.AppConfig.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {

    private TextView showHintTV;
    private EditText emailED, passED;
    private Button nextBT, signupTV, forgotTV;
    private String emailData, passData, secureId;
    private boolean hintStatus = false;
    private ProgressDialog progressDialog;
    private DialogDateUtil dialogDateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar_LoginActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        declarations();
        listeners();
    }

    @SuppressLint("HardwareIds")
    private void declarations() {
        forgotTV = findViewById(R.id.button_forgot_pass_LoginActivity);
        signupTV = findViewById(R.id.button_signup_LoginActivity);
        emailED = findViewById(R.id.editText_email_LoginActivity);
        passED = findViewById(R.id.editText_password_LoginActivity);
        nextBT = findViewById(R.id.button_next_loginActivity);
        showHintTV = findViewById(R.id.textView_hint_LoginActivity);
        progressDialog = new ProgressDialog(LoginActivity.this);
        secureId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (secureId != null) {
            if (secureId.isEmpty()) {
                new DialogDateUtil(LoginActivity.this, "Unable to retrieve Secure Id")
                        .showMessage();
            }
        } else {
            new DialogDateUtil(LoginActivity.this, "Unable to retrieve Secure Id")
                    .showMessage();
        }
        dialogDateUtil = new DialogDateUtil(LoginActivity.this);
        if (getIntent().getStringExtra("message") != null) {
            dialogDateUtil.showMessage(getIntent().getStringExtra("message"));
        }
    }

    private void listeners() {
        forgotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        nextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "ckicked");
                if (checkData()) {
                    Log.i("CUSTOM", "check oass");
                    checkLogin();
                }
            }
        });

        showHintTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hintStatus) {
                    showHintTV.setText(getString(R.string.hint_details));
                    hintStatus = !hintStatus;
                } else {
                    showHintTV.setText(getString(R.string.show_hint));
                    hintStatus = !hintStatus;
                }
            }
        });
    }

    private void checkLogin() {
        /**
         * after success
         * **/
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.loading));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                if (jsonObject.getString("message").equals("success")) {
                                    UserModel userModel = new UserModel(
                                            jsonObject.getString("name"),
                                            emailData,
                                            jsonObject.getString("phone"),
                                            jsonObject.getString("city"),
                                            jsonObject.getString("country"));
                                    LocalDatabase db = new LocalDatabase(LoginActivity.this);
                                    if (db.userLogin(userModel)) {
                                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                        sessionManager.setLogin(true);
                                        sessionManager.setPrefLanguage(Integer.parseInt(jsonObject.getString("pref_language")));
                                        sessionManager.setKeyEmail(emailData);
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this,
                                                "Login Successful!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                "Unable to create local Database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (jsonObject.getString("message").equals("invalid")) {
                                    new DialogDateUtil(LoginActivity.this, "Invalid Credentials.\nPlease Try Again!").showMessage();
                                } else if (jsonObject.getString("message").equals("empty_params")) {
                                    Log.i("CUSTOM", "Empty Params");
                                }  else if (jsonObject.getString("message").equals("failure_imei")) {
                                    new DialogDateUtil(LoginActivity.this,
                                            "Account can be only accessed from the registered device." +
                                                    "\nIn case you need to change please contact support@aarushsystems.com")
                                            .showMessage();
                                    Log.i("CUSTOM", "Empty Params");
                                }
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.i("CUSTOM", "Response Error Listener = " + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("CUSTOM", "Response Error Listener = " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailData);
                params.put("imei", secureId);
                params.put("password", passData);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean checkData() {
        if (emailED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(LoginActivity.this, "Email cannot be empty!").showMessage();
            return false;
        }
        if (passED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(LoginActivity.this, "PIN cannot be empty!").showMessage();
            return false;
        } else if (passED.getText().toString().trim().length() != 4) {
            new DialogDateUtil(LoginActivity.this, "PIN should be 4 - digit only!").showMessage();
            return false;
        }
        emailData = emailED.getText().toString().trim();
        passData = passED.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
            new DialogDateUtil(LoginActivity.this, "Invalid Email Address!").showMessage();
            return false;
        }
        return true;
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
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_about_us) {
            startActivity(new Intent(LoginActivity.this, AboutUsActivity.class).putExtra("from", "login_activity"));
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, LandingActivity.class));
        finish();
    }


}

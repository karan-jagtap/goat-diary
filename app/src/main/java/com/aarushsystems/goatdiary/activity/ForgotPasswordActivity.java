package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.aarushsystems.goatdiary.helper.AppConfig.FORGOT_PASSWORD_URL;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailED, et1, et2, et3, et4;
    private Button sendBT, verifyBT;
    private TextView messageTV;
    private String otpSent, otpEntered, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar_ForgotPasswordActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        declarations();
        listeners();
    }

    private void listeners() {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 1) {
                    //et1.clearFocus();
                    et2.requestFocus();
                    et2.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 1) {
                    //et2.clearFocus();
                    et3.requestFocus();
                    et3.setCursorVisible(true);
                } else if (i2 == 0) {
                    //et2.clearFocus();
                    et1.requestFocus();
                    et1.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 1) {
                    //et3.clearFocus();
                    et4.requestFocus();
                    et4.setCursorVisible(true);
                } else if (i2 == 0) {
                    //et3.clearFocus();
                    et2.requestFocus();
                    et2.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 0) {
                    //et4.clearFocus();
                    et3.requestFocus();
                    et3.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailED.getText().toString().trim().isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailED.getText().toString().trim()).matches()) {
                        messageTV.setText(getString(R.string.sending_email));
                        sendBT.setText(getString(R.string.resend));
                        email = emailED.getText().toString().trim();
                        sendOTPMail();
                    } else {
                        new DialogDateUtil(ForgotPasswordActivity.this, "Invalid Email Address!")
                                .showMessage();
                    }
                } else {
                    new DialogDateUtil(ForgotPasswordActivity.this, "Enter Email Address")
                            .showMessage();
                }
            }
        });

        verifyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et1.getText().toString().isEmpty() &&
                        !et2.getText().toString().isEmpty() &&
                        !et3.getText().toString().isEmpty() &&
                        !et4.getText().toString().isEmpty()) {
                    otpEntered = et1.getText().toString() +
                            et2.getText().toString() +
                            et3.getText().toString() +
                            et4.getText().toString();
                    Log.i("CUSTOM", "Otp Entered = " + otpEntered);
                    if (otpEntered.equals(otpSent)) {
                        //TODO :: Redirect to password change page
                        startActivity(new Intent(ForgotPasswordActivity.this, PasswordResetActivity.class)
                                .putExtra("email", email));
                        finish();
                    } else {
                        new DialogDateUtil(ForgotPasswordActivity.this, "Invalid OTP")
                                .showMessage();
                    }
                } else {
                    new DialogDateUtil(ForgotPasswordActivity.this, "Enter 4-digit OTP")
                            .showMessage();
                }
            }
        });
    }

    private void declarations() {
        emailED = findViewById(R.id.editText_email_ForgotPasswordActivity);
        sendBT = findViewById(R.id.button_send_ForgotPasswordActivity);
        verifyBT = findViewById(R.id.button_verify_ForgotPasswordActivity);
        messageTV = findViewById(R.id.textView_message_ForgotPasswordActivity);
        et1 = findViewById(R.id.et1_ForgotPasswordActivity);
        et2 = findViewById(R.id.et2_ForgotPasswordActivity);
        et3 = findViewById(R.id.et3_ForgotPasswordActivity);
        et4 = findViewById(R.id.et4_ForgotPasswordActivity);

        int charLength = 4;
        otpSent = String.valueOf(new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }

    private void sendOTPMail() {
        Log.i("CUSTOM", "OTP : " + otpSent);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    FORGOT_PASSWORD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("CUSTOM", "Response start : \n" + response + "\nresponse end");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    if (jsonObject.getString("message").equals("success")) {
                                        messageTV.setText(getString(R.string.email_sent_success));
                                        new DialogDateUtil(ForgotPasswordActivity.this, "Please Check Your Email!")
                                                .showMessage();
                                    } else {
                                        messageTV.setText(getString(R.string.email_sent_failure));
                                        Log.i("CUSTOM", "ekse");
                                    }
                                } else {
                                    Log.i("CUSTOM", "boolean = " + jsonObject.getString("message").equals("user_absent"));
                                    if (jsonObject.getString("message").equals("user_absent")) {
                                        messageTV.setText(getString(R.string.user_email_absent));
                                        new DialogDateUtil(ForgotPasswordActivity.this, getString(R.string.user_email_absent))
                                                .showMessage();
                                    }
                                    Log.i("CUSTOM", "php error = " + jsonObject.getString("message"));
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
                    params.put("email", email);
                    params.put("otp", otpSent);
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
        } catch (Exception e) {
            Log.i("CUSTOM", "exception = " + e.getMessage());
        }

        Log.i("CUSTOM", "ending sync");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_about_us) {
            startActivity(new Intent(ForgotPasswordActivity.this, AboutUsActivity.class).putExtra("from", "forgot_password_activity"));
            finish();
        }
        if (item.getItemId() == R.id.action_terms_and_cond) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.TERMS_AND_CONDITIONS));
                startActivity(browserIntent);
            } catch (Exception ignore) {
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

}

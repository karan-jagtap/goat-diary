package com.aarushsystems.goatdiary.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.aarushsystems.goatdiary.helper.AppConfig.EMAIL_VERIFICATION_URL;
import static com.aarushsystems.goatdiary.helper.AppConfig.PLAY_STORE_APK;
import static com.aarushsystems.goatdiary.helper.AppConfig.REGISTER_URL;

public class RegisterActivity extends AppCompatActivity {

    private static final int IMEI_PERMISSION_REQ_CODE = 524;
    private ProgressDialog progressDialog;
    private Button proceedBT;
    private TextView showHintTV, otpTV;
    private boolean hintStatus = false;
    private CheckBox checkBox;
    private SessionManager sessionManager;
    private LinearLayout otp2Layout;
    private EditText nameED, emailED, passED, cpassED, phoneED, cityED, countryED, otpED, otp2ED;
    private String nameData, emailData, passData, cpassData, otpSent, otp2Sent, phoneData, cityData, countryData, imeiNo, custId, secureId;

    private Handler handler;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar_RegisterActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        declarations();
        listeners();
        AppConfig appConfig = new AppConfig(RegisterActivity.this);
        appConfig.permissionCheck();
        if (!appConfig.isNetworkAvailable()) {
            DialogDateUtil dialogDateUtil = new DialogDateUtil(RegisterActivity.this);
            dialogDateUtil.showCriticalMessage(RegisterActivity.this, "r");
        }
        //loadPromptDialog();
    }

    private void sendOTPMail() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                new DialogDateUtil(RegisterActivity.this, "Generating OTP and sending it to your email address.")
                        .showMessage();
            }
        });
        timer.start();
        Log.i("CUSTOM", "OTP : " + otpSent);
        Toast.makeText(this, "Sending OTP...", Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                EMAIL_VERIFICATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("CUSTOM", "Response start : \n" + response + "\nresponse end");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                if (jsonObject.getString("message").equals("success")) {
                                    //messageTV.setText(getString(R.string.email_sent_success));
                                    timer.onFinish();
                                    if (PLAY_STORE_APK) {
                                        new DialogDateUtil(RegisterActivity.this, "Please Check Your Email for OTP!")
                                                .showMessage();
                                    } else {
                                        new DialogDateUtil(RegisterActivity.this, "Please Check Your Email for OTP 1!\nContact Admin for OTP 2.")
                                                .showMessage();
                                    }
                                } else {
                                    //messageTV.setText(getString(R.string.email_sent_failure));
                                    Log.i("CUSTOM", "ekse");
                                    timer.onFinish();
                                }
                            } else {
                                Log.i("CUSTOM", "boolean = " + jsonObject.getString("message").equals("user_absent"));
                                if (jsonObject.getString("message").equals("user_absent")) {
                                    //messageTV.setText(getString(R.string.user_email_absent));
                                    new DialogDateUtil(RegisterActivity.this, getString(R.string.user_email_absent))
                                            .showMessage();
                                } else if (jsonObject.getBoolean("error")) {
                                    new DialogDateUtil(RegisterActivity.this,
                                            "Server Error...\nPlease try again after some time.")
                                            .showMessage();
                                }
                                timer.onFinish();
                                Log.i("CUSTOM", "php error = " + jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("CUSTOM", "Catch = " + e.getMessage());
                            timer.onFinish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        new DialogDateUtil(RegisterActivity.this).showMessage("Something went wrong.\nPlease try again later.");
                        Log.i("CUSTOM", "Errorlistener = " + error.getMessage());
                        timer.onFinish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailED.getText().toString().trim());
                params.put("otp", otpSent);
                if (!PLAY_STORE_APK) {
                    params.put("otp2", otp2Sent);
                }
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
        otpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailED.getText().toString().trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailED.getText().toString().trim()).matches()) {
                    if (!otpTV.getText().toString().contains("Retry")) {
                        sendOTPMail();
                    }
                } else {
                    new DialogDateUtil(RegisterActivity.this, "Invalid Email Address!").showMessage();
                }
            }
        });

        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    //TODO :: Payment Gateway prompt
                    //loadAlertDialog();
                    loadPromptDialog();
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

    private void loadPromptDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.confirm_details));
        String message = "";
        if (cityData == null && countryData == null) {
            message = getString(R.string.confirm_details_message) +
                    "\nName : " + nameData +
                    "\nEmail : " + emailData +
                    "\nPIN : " + passData +
                    "\nPhone : " + phoneData;
        } else if (cityData == null && countryData != null) {
            message = getString(R.string.confirm_details_message) +
                    "\nName : " + nameData +
                    "\nEmail : " + emailData +
                    "\nPIN : " + passData +
                    "\nPhone : " + phoneData +
                    "\nCountry : " + countryData;
        } else if (cityData != null && countryData == null) {
            message = getString(R.string.confirm_details_message) +
                    "\nName : " + nameData +
                    "\nEmail : " + emailData +
                    "\nPIN : " + passData +
                    "\nPhone : " + phoneData +
                    "\nCity : " + cityData;
        } else if (cityData != null && countryData != null) {
            message = getString(R.string.confirm_details_message) +
                    "\nName : " + nameData +
                    "\nEmail : " + emailData +
                    "\nPIN : " + passData +
                    "\nPhone : " + phoneData +
                    "\nCity : " + cityData +
                    "\nCountry : " + countryData;
        }
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadDataMySQL();
                    }
                });
        builder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private void uploadDataMySQL() {
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.loading));
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("CUSTOM", "String Response = " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if (message.equals("user_present_email")) {
                                progressDialog.dismiss();
                                new DialogDateUtil(RegisterActivity.this, "User with this Email is already registered.")
                                        .showMessage();
                            } else if (message.equals("user_present_imei")) {
                                progressDialog.dismiss();
                                new DialogDateUtil(RegisterActivity.this,
                                        "This device is linked to Email - "
                                                + jsonObject.getString("email")
                                                + ". In case you need to change please contact support@aarushsystems.com")
                                        .showMessage();
                            } else if (message.equals("success")) {
                                //save locally
                                Toast.makeText(RegisterActivity.this,
                                        "Registration Successful.\nLogin Now.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            progressDialog.dismiss();
                            Log.i("CUSTOM", "Error Json Response = " + e.getMessage());
                            //Log.i("CUSTOM", "Error Json Response cause = " + Arrays.toString(e.getStackTrace()));

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        Log.i("CUSTOM", "Error from Response.ErrorListener = " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", custId);
                params.put("name", nameData);
                params.put("email", emailData);
                params.put("password", passData);
                params.put("phone", phoneData);
                params.put("imei", secureId);
                if (countryData != null) {
                    params.put("country", countryData);
                }
                if (cityData != null) {
                    params.put("city", cityData);
                }
                params.put("pref_language", String.valueOf(sessionManager.getPrefLanguage()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //TODO :: Include this for payment gateway
    public void loadAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = layoutInflater.inflate(R.layout.dialog_choose_payment_method, null);
        builder.setView(dialogView);

        Button paypalBT = dialogView.findViewById(R.id.paypal_method_dialog);
        Button paytmBT = dialogView.findViewById(R.id.paytm_method_dialog);
        builder.setCancelable(true);
        builder.setTitle(R.string.choose_payment_method);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        paypalBT
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        redirectToPaypal();
                        Toast.makeText(RegisterActivity.this, "Paypal", Toast.LENGTH_SHORT).show();
                    }
                });
        paytmBT
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterActivity.this, "PayTm", Toast.LENGTH_SHORT).show();
                        redirectToPaytm();
                    }
                });
    }

    private void redirectToPaytm() {
    }

    private void redirectToPaypal() {
    }

    private boolean checkData() {
        if (nameED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(RegisterActivity.this, "Name Field cannot be empty!").showMessage();
            return false;
        }
        if (emailED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(RegisterActivity.this, "Email Field cannot be empty!").showMessage();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailED.getText().toString().trim()).matches()) {
            new DialogDateUtil(RegisterActivity.this, "Invalid Email Address!").showMessage();
            return false;
        }
        if (passED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(RegisterActivity.this, "PIN Field cannot be empty!").showMessage();
            return false;
        } else if (passED.getText().toString().trim().length() != 4) {
            new DialogDateUtil(RegisterActivity.this, "PIN should be 4 digits only!").showMessage();
            return false;
        }
        if (cpassED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(RegisterActivity.this, "Confirm PIN Field cannot be empty!").showMessage();
            return false;
        } else if (cpassED.getText().toString().trim().length() != 4) {
            new DialogDateUtil(RegisterActivity.this, "Confirm PIN should be 4 digits only!").showMessage();
            return false;
        }
        if (!passED.getText().toString().trim().equals(cpassED.getText().toString().trim())) {
            new DialogDateUtil(RegisterActivity.this, "PIN Mismatched!").showMessage();
            return false;
        }
        if (phoneED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(RegisterActivity.this, "Phone Field cannot be empty!").showMessage();
            return false;
        } else if (!Patterns.PHONE.matcher(phoneED.getText().toString().trim()).matches() ||
                phoneED.getText().toString().trim().length() < 10 ||
                phoneED.getText().toString().trim().length() > 15) {
            new DialogDateUtil(RegisterActivity.this, "Please Enter a Valid Phone Number!").showMessage();
            return false;
        }
        if (!checkBox.isChecked()) {
            new DialogDateUtil(RegisterActivity.this, "Please Accept the Terms & Conditions and Privacy Policy.").showMessage();
            return false;
        }
        if (PLAY_STORE_APK) {
            if (!otpSent.equals(otpED.getText().toString().trim())) {
                new DialogDateUtil(RegisterActivity.this, "Invalid OTP Entered.").showMessage();
                return false;
            }
        } else {
            if (!otpSent.equals(otpED.getText().toString().trim()) && !otp2Sent.equals(otp2ED.getText().toString().trim())) {
                new DialogDateUtil(RegisterActivity.this, "Invalid OTPs Entered.").showMessage();
                return false;
            } else if (!otpSent.equals(otpED.getText().toString().trim())) {
                new DialogDateUtil(RegisterActivity.this, "Invalid OTP 1 Entered.").showMessage();
                return false;
            } else if (!otp2Sent.equals(otp2ED.getText().toString().trim())) {
                new DialogDateUtil(RegisterActivity.this, "Invalid OTP 2 Entered.").showMessage();
                return false;
            }
        }
        //TODO:: If payment is from payment gateway then include this
        /*if (imeiNo == null) {
            Toast.makeText(RegisterActivity.this, "Cannot proceed further, lack of permission.", Toast.LENGTH_LONG).show();
            return false;
        }*/
        nameData = nameED.getText().toString().trim();
        emailData = emailED.getText().toString().trim();
        passData = passED.getText().toString().trim();
        phoneData = phoneED.getText().toString().trim();
        custId = generateCustomerId();
        if (!cityED.getText().toString().trim().isEmpty()) {
            cityData = cityED.getText().toString().trim();
        } else {
            cityData = null;
        }
        if (!countryED.getText().toString().trim().isEmpty()) {
            countryData = countryED.getText().toString().trim();
        } else {
            countryData = null;
        }
        return true;
    }

    @SuppressLint("HardwareIds")
    private void declarations() {
        nameED = findViewById(R.id.editText_name_RegisterActivity);
        emailED = findViewById(R.id.editText_email_RegisterActivity);
        passED = findViewById(R.id.editText_password_RegisterActivity);
        cpassED = findViewById(R.id.editText_c_password_RegisterActivity);
        phoneED = findViewById(R.id.editText_phone_RegisterActivity);
        cityED = findViewById(R.id.editText_city_RegisterActivity);
        countryED = findViewById(R.id.editText_country_RegisterActivity);
        proceedBT = findViewById(R.id.button_proceed_RegisterActivity);
        showHintTV = findViewById(R.id.textView_show_hint_RegisterActivity);
        checkBox = findViewById(R.id.checkbox_terms_conditions);
        otpTV = findViewById(R.id.textView_email_verification_RegisterActivity);
        otpED = findViewById(R.id.editText_email_verification_RegisterActivity);
        otp2ED = findViewById(R.id.editText_otp_verification_RegisterActivity);
        otp2Layout = findViewById(R.id.linearLayout_otp_verification_RegisterActivity);

        if (PLAY_STORE_APK) {
            otp2Layout.setVisibility(View.GONE);
        } else {
            otpTV.setText("Send OTPs");
            otp2Layout.setVisibility(View.VISIBLE);
        }
        progressDialog = new ProgressDialog(RegisterActivity.this);
        sessionManager = new SessionManager(RegisterActivity.this);
        handler = new Handler();
        timer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                otpTV.setText("Retry in " + (millisUntilFinished / 1000) + "...");
                otpTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            }

            @Override
            public void onFinish() {
                otpTV.setText("Send OTPs");
                otpTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
        };
        secureId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (secureId != null) {
            if (secureId.isEmpty()) {
                new DialogDateUtil(RegisterActivity.this, "Unable to retrieve Secure Id")
                        .showMessage();
            }
        } else {
            new DialogDateUtil(RegisterActivity.this, "Unable to retrieve Secure Id")
                    .showMessage();
        }
        Log.i("CUSTOM", secureId);
        //Toast.makeText(this, ""+secureId, Toast.LENGTH_SHORT).show();
        // TODO:: For payment gateway
        //checkForPhoneStatePermission();

        int charLength = 4;
        otpSent = String.valueOf(new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));

        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String values = Capital_chars + Small_chars +
                numbers;
        // Using random method
        Random rndm_method = new Random();
        char[] password = new char[6];
        for (int i = 0; i < 6; i++) {
            password[i] = values.charAt(rndm_method.nextInt(values.length()));
        }

        otp2Sent = String.valueOf(password);
        Log.i("CUSTOM", "otp2 = " + otp2Sent);

    }

    /*private void checkForPhoneStatePermission() {
        Log.i("CUSTOM", "Version Code = " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, IMEI_PERMISSION_REQ_CODE);
                return;
            }
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                imeiNo = tm.getImei();
            }
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == IMEI_PERMISSION_REQ_CODE) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (tm != null) {
                            imeiNo = tm.getImei();
                        }
                    }
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Cannot proceed further, lack of permission.", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            startActivity(new Intent(RegisterActivity.this, LandingActivity.class));
            finish();
        } else if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private String generateCustomerId() {
        int charLength = 10;
        return String.valueOf(new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }

}

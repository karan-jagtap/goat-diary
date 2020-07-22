package com.aarushsystems.goatdiary.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
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

import static com.aarushsystems.goatdiary.helper.AppConfig.PASSWORD_RESET_URL;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText passED, cPassED;
    private Button submitBT;
    private String passData, emailData;
    private TextView showHintTV;
    private boolean hintStatus = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Toolbar toolbar = findViewById(R.id.toolbar_PasswordResetActivity);
        setSupportActionBar(toolbar);

        declarations();
        listeners();
    }

    private void listeners() {
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    updatePasswordToDB();
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

    private void updatePasswordToDB() {
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.loading));
        RequestQueue requestQueue = Volley.newRequestQueue(PasswordResetActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                PASSWORD_RESET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("CUSTOM", "String Response = " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                //save locally
                                Toast.makeText(PasswordResetActivity.this,
                                        "Successful Password Reset.\nTry logging in.",
                                        Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(PasswordResetActivity.this, LoginActivity.class));
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
                        //error.printStackTrace();
                        Log.i("CUSTOM", "Error from Response.ErrorListener = " + error.getCause());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailData);
                params.put("password", passData);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean checkData() {
        if (passED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(PasswordResetActivity.this, "New PIN Field cannot be empty!")
                    .showMessage();
            return false;
        }
        if (cPassED.getText().toString().trim().isEmpty()) {
            new DialogDateUtil(PasswordResetActivity.this, "Confirm PIN Field cannot be empty!")
                    .showMessage();
            return false;
        }
        if (passED.getText().toString().trim().length() != 4 || cPassED.getText().toString().trim().length() != 4) {
            new DialogDateUtil(PasswordResetActivity.this, "PIN Length should be 4 - digits only!")
                    .showMessage();
            return false;
        }
        if (!passED.getText().toString().trim().equals(cPassED.getText().toString().trim())) {
            new DialogDateUtil(PasswordResetActivity.this, "PIN Mismatched!")
                    .showMessage();
            return false;
        }
        passData = passED.getText().toString().trim();
        return true;
    }

    private void declarations() {
        passED = findViewById(R.id.editText_pass_PasswordResetActivity);
        cPassED = findViewById(R.id.editText_c_pass_PasswordResetActivity);
        submitBT = findViewById(R.id.button_submit_PasswordResetActivity);
        showHintTV = findViewById(R.id.textView_show_hint_PasswordResetActivity);
        progressDialog = new ProgressDialog(PasswordResetActivity.this);
        emailData = getIntent().getStringExtra("email");
    }

}

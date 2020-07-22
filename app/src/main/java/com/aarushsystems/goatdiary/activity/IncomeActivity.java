package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.model.IncomeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class IncomeActivity extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext;
    private LocalDatabase db;
    private ScrollView scrollView;
    private RadioGroup paymentRG;
    private RadioButton cashRB, chequeRB;
    private IncomeModel incomeModel;
    private ArrayList<IncomeModel> incomeArrayList;
    private DialogDateUtil dialogDateUtil;
    private Spinner headSpinner;
    private TextView lastUpdatedTV, totalIncomeTV, prevTV, saveTV;
    private LinearLayout chequeL;
    private EditText ddED, mmED, yyyyED, amountED, receivedFromED, chequeNoED, bankED, remarksED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        Toolbar toolbar = findViewById(R.id.toolbar_IncomeActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
    }

    private void listeners() {
        ddED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 2) {
                    //ddED.clearFocus();
                    mmED.requestFocus();
                    mmED.setCursorVisible(true);
                }
            }
        });

        mmED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 2) {
                    //et2.clearFocus();
                    yyyyED.requestFocus();
                    yyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    ddED.requestFocus();
                    ddED.setCursorVisible(true);
                }
            }
        });

        yyyyED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    //et3.clearFocus();
                    mmED.requestFocus();
                    mmED.setCursorVisible(true);
                }
            }
        });

        paymentRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == cashRB.getId()) {
                    chequeL.setVisibility(View.GONE);
                } else {
                    chequeL.setVisibility(View.VISIBLE);
                }
            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count);
                if (incomeArrayList.size() > 0 && count < incomeArrayList.size() && count >= 0) {
                    if (justNext) {
                        count++;
                        justNext = false;
                    }
                    loadPreviousContent(count);
                    disableComponents();
                    count++;
                    saveTV.setText(getString(R.string.next));
                    justPrev = true;
                } else {
                    dialogDateUtil.showMessage("No Record Found.");
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "save button count = " + count);
                if (saveTV.getText().toString().equals(getString(R.string.save))) {
                    justNext = false;
                    if (checkData()) {
                        updateDetailsToLocalDatabase();
                    }
                } else if (saveTV.getText().toString().equals(getString(R.string.next))) {
                    if (justPrev) {
                        count--;
                        justPrev = false;
                    }
                    count--;
                    if (count < incomeArrayList.size() && count >= 0) {
                        loadPreviousContent(count);
                        disableComponents();
                        justNext = true;
                    } else {
                        clearUIElements();
                        enableComponents();
                        loadPrerequisiteContent(-1);
                        saveTV.setText(getString(R.string.save));
                    }
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void loadPreviousContent(int count) {
        IncomeModel incomeModel = incomeArrayList.get(count);
        Log.i("CUSTOM","amount = "+incomeModel.getAmount());
        headSpinner.setSelection(incomeModel.getHead());
        String[] tempDate = incomeModel.getDate().split("/");
        ddED.setText(tempDate[0]);
        mmED.setText(tempDate[1]);
        yyyyED.setText(tempDate[2]);
        if (incomeModel.getCash_cheque().equals("cash")) {
            cashRB.setChecked(true);
        } else {
            chequeRB.setChecked(true);
            chequeNoED.setText(String.valueOf(incomeModel.getChequeNo()));
            bankED.setText(incomeModel.getBank());
        }
        amountED.setText(incomeModel.getAmount());
        receivedFromED.setText(incomeModel.getReceivedFrom());
        if (incomeModel.getRemarks() != null) {
            if (!incomeModel.getRemarks().isEmpty()) {
                remarksED.setText(incomeModel.getRemarks());
            } else {
                remarksED.setText("");
                Log.i("CUSTOM", "remark empty");
            }
        } else {
            remarksED.setText("");
            Log.i("CUSTOM", "remark null");
        }
    }

    private void updateDetailsToLocalDatabase() {
        Log.i("CUSTOM", "updateDetailsToLocalDatabase()");
        HashMap<String, String> response = db.addAnimalIncomeDetails(incomeModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Income Added Successfully!");
            count = 0;
            incomeArrayList = db.getAllIncomeRecords();
            Collections.sort(incomeArrayList, Collections.<IncomeModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
    }

    private void clearUIElements() {
        headSpinner.setSelection(0);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        amountED.setText("");
        receivedFromED.setText("");
        cashRB.setChecked(true);
        amountED.setText("");
        chequeNoED.setText("");
        bankED.setText("");
        remarksED.setText("");
    }

    private boolean checkData() {
        incomeModel = new IncomeModel();
        if (headSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Action Field cannot be empty.");
            return false;
        }
        incomeModel.setHead(headSpinner.getSelectedItemPosition());
        if (ddED.getText().toString().trim().isEmpty() ||
                mmED.getText().toString().trim().isEmpty() ||
                yyyyED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Date Fields cannot be empty!");
            return false;
        } else if (yyyyED.getText().toString().length() != 4 ||
                (Integer.parseInt(ddED.getText().toString().trim()) < 1 || Integer.parseInt(ddED.getText().toString().trim()) > 31) ||
                (Integer.parseInt(mmED.getText().toString().trim()) < 1 || Integer.parseInt(mmED.getText().toString().trim()) > 12)) {
            dialogDateUtil.showMessage("Invalid Date!");
            return false;
        }
        if (paymentRG.getCheckedRadioButtonId() == cashRB.getId()) {
            incomeModel.setCash_cheque("cash");
        } else {
            incomeModel.setCash_cheque("cheque");
            if (chequeNoED.getText().toString().isEmpty()) {
                dialogDateUtil.showMessage("Cheque No Field cannot be empty.");
                return false;
            }
            if (bankED.getText().toString().isEmpty()) {
                dialogDateUtil.showMessage("Bank Field cannot be empty.");
                return false;
            }
            incomeModel.setChequeNo(Integer.parseInt(chequeNoED.getText().toString()));
            incomeModel.setBank(bankED.getText().toString().trim());
        }
        if (amountED.getText().toString().isEmpty()) {
            dialogDateUtil.showMessage("Amount Field cannot be empty!");
            return false;
        }
        if (receivedFromED.getText().toString().isEmpty()) {
            dialogDateUtil.showMessage("Paid To Field cannot be empty!");
            return false;
        }
        if (!remarksED.getText().toString().isEmpty()) {
            incomeModel.setRemarks(remarksED.getText().toString().trim());
        }
        String[] tempPrice = amountED.getText().toString().split("\\.");
        if (tempPrice.length == 1) {
            amountED.setText(amountED.getText().toString().trim().concat(".00"));
        } else {
            if (tempPrice[1].length() != 2) {
                dialogDateUtil.showMessage("Price Ps. should be 2 digit long only.");
                return false;
            }
        }
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        incomeModel.setDate(tempDate);
        incomeModel.setAmount(amountED.getText().toString());
        incomeModel.setReceivedFrom(receivedFromED.getText().toString().trim());
        try {
            if (dialogDateUtil.isDateInFuture(incomeModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be today's Date or past Date.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("CUSTOM",
                "\nSrno = " + incomeModel.getSrno() +
                        "\nhead = " + incomeModel.getHead() +
                        "\ncash_cheque = " + incomeModel.getCash_cheque() +
                        "\ndate = " + incomeModel.getDate() +
                        "\namount = " + incomeModel.getAmount() +
                        "\nReceived from = " + incomeModel.getReceivedFrom() +
                        "\ncheque no = " + incomeModel.getChequeNo() +
                        "\nbank = " + incomeModel.getBank() +
                        "\nremarks = " + incomeModel.getRemarks()
        );
        return true;
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_IncomeActivity);
        lastUpdatedTV = findViewById(R.id.textView_last_updated_IncomeActivity);
        totalIncomeTV = findViewById(R.id.textView_total_expense_IncomeActivity);
        prevTV = findViewById(R.id.textView_prev_IncomeActivity);
        saveTV = findViewById(R.id.textView_next_IncomeActivity);
        ddED = findViewById(R.id.et1_date_IncomeActivity);
        mmED = findViewById(R.id.et2_date_IncomeActivity);
        yyyyED = findViewById(R.id.et3_date_IncomeActivity);
        amountED = findViewById(R.id.editText_amount_IncomeActivity);
        chequeL = findViewById(R.id.layout_cheque_enabled_IncomeActivity);
        headSpinner = findViewById(R.id.spinner_head_IncomeActivity);
        remarksED = findViewById(R.id.editText_remarks_IncomeActivity);
        receivedFromED = findViewById(R.id.editText_received_from_IncomeActivity);
        chequeNoED = findViewById(R.id.editText_cheque_no_IncomeActivity);
        bankED = findViewById(R.id.editText_bank_IncomeActivity);
        paymentRG = findViewById(R.id.radioGroup_payment_by_IncomeActivity);
        cashRB = findViewById(R.id.radio_cash_IncomeActivity);
        chequeRB = findViewById(R.id.radio_cheque_IncomeActivity);

        saveTV.setText(getString(R.string.save));
        chequeL.setVisibility(View.GONE);
        dialogDateUtil = new DialogDateUtil(IncomeActivity.this);
        db = new LocalDatabase(IncomeActivity.this);
        incomeArrayList = new ArrayList<>();

        loadPrerequisiteContent(-1);
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> al = db.getDataForMastersTable(LocalDatabase.TABLE_INCOME);
        ArrayAdapter aa = new ArrayAdapter<String>(IncomeActivity.this,
                R.layout.layout_text_view_black,
                al);
        headSpinner.setAdapter(aa);
        Log.i("CUSTOM", "Masters table record for " + LocalDatabase.TABLE_INCOME+ " :: ");
        for (String data : al) {
            Log.i("CUSTOM", data);
        }
        //String text = "Last Updated\n" + db.getLastUpdatedDateIncomeTable();
        String text = "Last Updated\n" + dialogDateUtil.getTodaysDate();
        lastUpdatedTV.setText(text);
        text = "Total Income\n" + db.getTotalIncome();
        totalIncomeTV.setText(text);
        incomeArrayList = db.getAllIncomeRecords();
        if (incomeArrayList.size() == 0) {
            text = "Last Updated\n";
            lastUpdatedTV.setText(text);
        }
        Log.i("CUSTOM","expense records size = "+ incomeArrayList.size());
        Collections.sort(incomeArrayList, Collections.<IncomeModel>reverseOrder());
        count = 0;
        justNext = false;
    }

    private void disableComponents() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    ((TextView) headSpinner.getSelectedView()).setTextColor(Color.BLACK);
                } catch (Exception e) {
                }
            }
        });
        headSpinner.setEnabled(false);
        headSpinner.setFocusable(false);
        ddED.setEnabled(false);
        ddED.setTextColor(getResources().getColor(android.R.color.black));
        mmED.setEnabled(false);
        mmED.setTextColor(getResources().getColor(android.R.color.black));
        yyyyED.setEnabled(false);
        yyyyED.setTextColor(getResources().getColor(android.R.color.black));
        ((TextView) cashRB).setTextColor(Color.BLACK);
        cashRB.setEnabled(false);
        ((TextView) chequeRB).setTextColor(Color.BLACK);
        chequeRB.setEnabled(false);
        amountED.setEnabled(false);
        amountED.setTextColor(getResources().getColor(android.R.color.black));
        receivedFromED.setEnabled(false);
        receivedFromED.setTextColor(getResources().getColor(android.R.color.black));
        chequeNoED.setEnabled(false);
        chequeNoED.setTextColor(getResources().getColor(android.R.color.black));
        bankED.setEnabled(false);
        bankED.setTextColor(getResources().getColor(android.R.color.black));
        remarksED.setEnabled(false);
        remarksED.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void enableComponents() {
        headSpinner.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        cashRB.setEnabled(true);
        chequeRB.setEnabled(true);
        amountED.setEnabled(true);
        receivedFromED.setEnabled(true);
        chequeNoED.setEnabled(true);
        bankED.setEnabled(true);
        remarksED.setEnabled(true);
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
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(IncomeActivity.this, AboutUsActivity.class)
                    .putExtra("from", "income_activity"));
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
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


}

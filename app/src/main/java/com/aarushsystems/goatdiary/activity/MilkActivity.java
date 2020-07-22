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
import android.widget.AdapterView;
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
import com.aarushsystems.goatdiary.model.MilkModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class MilkActivity extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext;
    private LocalDatabase db;
    private ScrollView scrollView;
    private RadioGroup productionRG;
    private RadioButton farmRB, individualRB;
    private MilkModel milkModel;
    private ArrayList<MilkModel> milkArrayList;
    private DialogDateUtil dialogDateUtil;
    private Spinner actionSpinner, timeSpinner, tagIdSpinner;
    private TextView totalProductionTV, totalSalesTV, prevTV, saveTV;
    private EditText ddED, mmED, yyyyED, priceED, quantityED;
    private LinearLayout radioL, tagIdL, priceL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk);
        Toolbar toolbar = findViewById(R.id.toolbar_MilkActivity);
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

        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    farmRB.setChecked(true);
                    radioL.setVisibility(View.GONE);
                    priceL.setVisibility(View.GONE);
                    tagIdL.setVisibility(View.GONE);
                } else if (i == 1) {
                    radioL.setVisibility(View.VISIBLE);
                    //farmRB.setChecked(true);
                    priceL.setVisibility(View.GONE);
                } else {
                    priceL.setVisibility(View.VISIBLE);
                    //farmRB.setChecked(true);
                    radioL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        productionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == farmRB.getId()) {
                    tagIdL.setVisibility(View.GONE);
                } else {
                    tagIdL.setVisibility(View.VISIBLE);
                    if (tagIdSpinner.getCount() == 0) {
                        if (actionSpinner.getSelectedItemPosition() == 1 && individualRB.isChecked()) {
                            dialogDateUtil.showMessage("No Record Found. Please add Female Animals first.");
                        }
                        tagIdL.setVisibility(View.GONE);
                    }
                }
            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count);
                if (milkArrayList.size() > 0 && count < milkArrayList.size() && count >= 0) {
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
                    if (count < milkArrayList.size() && count >= 0) {
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
        MilkModel milkModel = milkArrayList.get(count);
        Log.i("CUSTOM", "amount = " + milkModel.getPrice());
        actionSpinner.setSelection(milkModel.getAction(),false);
        String[] tempDate = milkModel.getDate().split("/");
        ddED.setText(tempDate[0]);
        mmED.setText(tempDate[1]);
        yyyyED.setText(tempDate[2]);
        if (milkModel.getAction() == 2) {
            priceED.setText(milkModel.getPrice());
        } else if (milkModel.getAction() == 1) {
            if (milkModel.getLot() == 1) {
                farmRB.setChecked(true);
            } else {
                individualRB.setChecked(true);
                Log.i("CUSTOM","individual button set to true");
                ArrayList<String> al = new ArrayList<>();
                al.add(String.valueOf(milkModel.getTagId()));
                ArrayAdapter aa = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.layout_text_view_black,
                        al);
                tagIdSpinner.setAdapter(aa);
            }
        }
        if (milkModel.getTime() != 0) {
            timeSpinner.setSelection(milkModel.getTime(), false);
        } else {
            timeSpinner.setSelection(0);
        }
        quantityED.setText(milkModel.getQuantity());
    }

    private void updateDetailsToLocalDatabase() {
        Log.i("CUSTOM", "updateDetailsToLocalDatabase()");
        HashMap<String, String> response = db.addAnimalMilkDetails(milkModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Milk Details Added Successfully!");
            count = 0;
            milkArrayList = db.getAllMilkRecords();
            Collections.sort(milkArrayList, Collections.<MilkModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
        } else {
            if (response.get("message").equals("action_error")) {
                dialogDateUtil.showMessage("Record cannot be added." +
                        "\nAction 'Sales' is more than Action 'Production' added till "+milkModel.getDate());
            } else {
                dialogDateUtil.showMessage("Internal Local Database Error");
            }
        }
    }

    private void clearUIElements() {
        actionSpinner.setSelection(0, false);
        timeSpinner.setSelection(0, false);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        priceED.setText("");
        quantityED.setText("");
        farmRB.setChecked(true);
        priceED.setText("");
        radioL.setVisibility(View.GONE);
        tagIdL.setVisibility(View.GONE);
        priceL.setVisibility(View.GONE);
    }

    private boolean checkData() {
        milkModel = new MilkModel();
        if (actionSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Action Field cannot be empty!");
            return false;
        }
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
        if (productionRG.getCheckedRadioButtonId() == farmRB.getId()) {
            milkModel.setLot(1);
        } else {
            milkModel.setLot(2);
            if (tagIdSpinner.getCount()>0) {
                milkModel.setTagId(Integer.parseInt(tagIdSpinner.getSelectedItem().toString()));
            } else {
                dialogDateUtil.showMessage("Cannot proceed. Please add Female Animals first.");
                return false;
            }
        }
        if (actionSpinner.getSelectedItemPosition() == 2) {
            if (priceED.getText().toString().isEmpty()) {
                dialogDateUtil.showMessage("Price Field cannot be empty!");
                return false;
            }
            String[] tempPrice = priceED.getText().toString().split("\\.");
            if (tempPrice.length == 2) {
                if (tempPrice[1].length() != 2) {
                    dialogDateUtil.showMessage("Price Ps. should be 2 digit long only.");
                    return false;
                }
            }
            milkModel.setPrice(priceED.getText().toString().trim());
        }
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        milkModel.setDate(tempDate);
        try {
            if (dialogDateUtil.isDateInFuture(milkModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be today's Date or past Date.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (timeSpinner.getSelectedItemPosition() == 0) {
            milkModel.setTime(0);
        }
        milkModel.setTime(timeSpinner.getSelectedItemPosition());
        if (quantityED.getText().toString().isEmpty()) {
            dialogDateUtil.showMessage("Quantity Field cannot be empty!");
            return false;
        }
        String[] tempQuantity = quantityED.getText().toString().trim().split("\\.");
        if(tempQuantity.length==2) {
            if(tempQuantity[1].length()!=3){
                dialogDateUtil.showMessage("Quantity ml. must be 3 digit long.");
                return false;
            }
        }
        milkModel.setQuantity(quantityED.getText().toString().trim());
        milkModel.setAction(actionSpinner.getSelectedItemPosition());
        Log.i("CUSTOM",
                "\nSrno = " + milkModel.getSrno() +
                        "\naction = " + milkModel.getAction() +
                        "\nlot = " + milkModel.getLot() +
                        "\ndate = " + milkModel.getDate() +
                        "\nprice = " + milkModel.getPrice() +
                        "\nquantity = " + milkModel.getQuantity() +
                        "\ntime = " + milkModel.getTime() +
                        "\ntag id = " + milkModel.getTagId()
        );
        return true;
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_MilkActivity);
        totalProductionTV = findViewById(R.id.textView_total_production_MilkActivity);
        totalSalesTV = findViewById(R.id.textView_total_sales_MilkActivity);
        prevTV = findViewById(R.id.textView_prev_MilkActivity);
        saveTV = findViewById(R.id.textView_next_MilkActivity);
        ddED = findViewById(R.id.et1_date_MilkActivity);
        mmED = findViewById(R.id.et2_date_MilkActivity);
        yyyyED = findViewById(R.id.et3_date_MilkActivity);
        priceED = findViewById(R.id.editText_price_MilkActivity);
        actionSpinner = findViewById(R.id.spinner_action_MilkActivity);
        timeSpinner = findViewById(R.id.spinner_time_MilkActivity);
        tagIdSpinner = findViewById(R.id.spinner_tag_id_MilkActivity);
        productionRG = findViewById(R.id.radioGroup_production_MilkActivity);
        farmRB = findViewById(R.id.radio_farm_MilkActivity);
        individualRB = findViewById(R.id.radio_individual_MilkActivity);
        quantityED = findViewById(R.id.editText_quantity_MilkActivity);
        radioL = findViewById(R.id.layout_radio_MilkActivity);
        priceL = findViewById(R.id.layout_price_MilkActivity);
        tagIdL = findViewById(R.id.layout_tag_id_MilkActivity);

        saveTV.setText(getString(R.string.save));
        radioL.setVisibility(View.GONE);
        tagIdL.setVisibility(View.GONE);
        priceL.setVisibility(View.GONE);
        dialogDateUtil = new DialogDateUtil(MilkActivity.this);
        db = new LocalDatabase(MilkActivity.this);
        milkArrayList = new ArrayList<>();

        loadPrerequisiteContent(-1);
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> al = db.getDataForMastersTable(LocalDatabase.TABLE_ACTION_MILK);
        ArrayAdapter aa = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.layout_text_view_black,
                al);
        actionSpinner.setAdapter(aa);
        actionSpinner.setSelection(0, false);
        ArrayList<String> al2 = db.getAllFemalesTagIds(-1);
        ArrayAdapter aa2 = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.layout_text_view_black,
                al2);
        tagIdSpinner.setAdapter(aa2);
        String text = "Total\nProduction\n" + db.getTotalProduction();
        totalProductionTV.setText(text);
        text = "Total\nSales\n" + db.getTotalSales();
        totalSalesTV.setText(text);
        milkArrayList = db.getAllMilkRecords();
        Log.i("CUSTOM", "milk records size = " + milkArrayList.size());
        Collections.sort(milkArrayList, Collections.<MilkModel>reverseOrder());
        count = 0;
        justNext = false;
    }

    private void disableComponents() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.i("MILK", "" + actionSpinner.getCount());
                try {
                    if (actionSpinner.getCount() > 0) {
                        ((TextView) actionSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                } catch (Exception e) {
                }
            }
        });
        actionSpinner.setEnabled(false);
        actionSpinner.setFocusable(false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (tagIdSpinner.getCount() > 0) {
                        ((TextView) tagIdSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                } catch (Exception e) {
                }
            }
        });
        tagIdSpinner.setEnabled(false);
        tagIdSpinner.setFocusable(false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (timeSpinner.getCount() > 0) {
                        ((TextView) timeSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                } catch (Exception e) {
                }
            }
        });
        timeSpinner.setEnabled(false);
        timeSpinner.setFocusable(false);

        ddED.setEnabled(false);
        ddED.setTextColor(getResources().getColor(android.R.color.black));
        mmED.setEnabled(false);
        mmED.setTextColor(getResources().getColor(android.R.color.black));
        yyyyED.setEnabled(false);
        yyyyED.setTextColor(getResources().getColor(android.R.color.black));
        ((TextView) farmRB).setTextColor(Color.BLACK);
        farmRB.setEnabled(false);
        ((TextView) individualRB).setTextColor(Color.BLACK);
        individualRB.setEnabled(false);
        priceED.setEnabled(false);
        priceED.setTextColor(getResources().getColor(android.R.color.black));
        quantityED.setEnabled(false);
        quantityED.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void enableComponents() {
        actionSpinner.setEnabled(true);
        tagIdSpinner.setEnabled(true);
        timeSpinner.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        farmRB.setEnabled(true);
        individualRB.setEnabled(true);
        priceED.setEnabled(true);
        quantityED.setEnabled(true);
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
            } catch (Exception ignore) {
            }
        }
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(MilkActivity.this, AboutUsActivity.class)
                    .putExtra("from", "milk_activity"));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

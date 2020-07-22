package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.model.AddAnimalModel;
import com.aarushsystems.goatdiary.model.WeightModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class WeightActivity extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext;
    private LocalDatabase db;
    private WeightModel weightModel;
    private DialogDateUtil dialogDateUtil;
    private ScrollView scrollView;
    private AddAnimalModel addAnimalModel;
    private ArrayList<WeightModel> weightsArrayList;
    private Spinner tagIdSpinner;
    private TextView totalAnimalsCountTV, monthAnimalsCountTV, prevTV, saveTV, animalTypeTV, breedTV, genderTV, dateTV, weightTV;
    private EditText ddED, mmED, yyyyED, wtkgED, wtgmED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        Toolbar toolbar = findViewById(R.id.toolbar_WeightActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_WeightActivity);
        totalAnimalsCountTV = findViewById(R.id.textView_total_animals_WeightActivity);
        monthAnimalsCountTV = findViewById(R.id.textView_unique_animals_month_DeleteActivity);
        prevTV = findViewById(R.id.textView_prev_WeightActivity);
        saveTV = findViewById(R.id.textView_next_WeightActivity);
        wtkgED = findViewById(R.id.et_kg_weight_WeightActivity);
        wtgmED = findViewById(R.id.et_gm_weight_WeightActivity);
        ddED = findViewById(R.id.et1_date_WeightActivity);
        mmED = findViewById(R.id.et2_date_WeightActivity);
        yyyyED = findViewById(R.id.et3_date_WeightActivity);
        tagIdSpinner = findViewById(R.id.spinner_tag_id_WeightActivity);
        animalTypeTV = findViewById(R.id.textView_animal_type_WeightActivity);
        breedTV = findViewById(R.id.textView_breed_WeightActivity);
        genderTV = findViewById(R.id.textView_gender_WeightActivity);
        dateTV = findViewById(R.id.textView_date_WeightActivity);
        weightTV = findViewById(R.id.textView_weight_WeightActivity);

        saveTV.setText(getString(R.string.save));
        dialogDateUtil = new DialogDateUtil(WeightActivity.this);
        db = new LocalDatabase(WeightActivity.this);
        weightsArrayList = new ArrayList<>();

        loadPrerequisiteContent(-1);
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

        wtkgED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 3) {
                    //ddED.clearFocus();
                    wtgmED.requestFocus();
                    wtgmED.setCursorVisible(true);
                }
            }
        });

        wtgmED.addTextChangedListener(new TextWatcher() {
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
                    wtkgED.requestFocus();
                    wtkgED.setCursorVisible(true);
                }
            }
        });

        tagIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadDisplayBox(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count);
                if (weightsArrayList.size() > 0 && count < weightsArrayList.size() && count >= 0) {
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
                        addDetailsToLocalDatabase();
                    }
                } else if (saveTV.getText().toString().equals(getString(R.string.next))) {
                    if (justPrev) {
                        count--;
                        justPrev = false;
                    }
                    count--;
                    if (count < weightsArrayList.size() && count >= 0) {
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
        WeightModel weightModel = weightsArrayList.get(count);
        Log.i("CUSTOM", "Previous content\n" +
                "tag id = " + weightModel.getTagId());
        ArrayList<String> al = new ArrayList<>();
        al.add(String.valueOf(weightModel.getTagId()));
        ArrayAdapter<String> aa = new ArrayAdapter<>(WeightActivity.this, R.layout.layout_text_view_black, al);
        tagIdSpinner.setAdapter(aa);
        tagIdSpinner.setSelection(0);
        Log.i("CUSTOM", "Tag id spinner prev size = " + tagIdSpinner.getCount());
        loadDisplayBox(String.valueOf(weightModel.getTagId()));
        String[] tempDate = weightModel.getDate().split("/");
        ddED.setText(tempDate[0]);
        mmED.setText(tempDate[1]);
        yyyyED.setText(tempDate[2]);

        String[] tempWeight = weightModel.getWeight().split("\\.");
        wtkgED.setText(String.valueOf(tempWeight[0]));
        wtgmED.setText(String.valueOf(tempWeight[1]));
    }

    private boolean checkData() {
        weightModel = new WeightModel();
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
        if (wtkgED.getText().toString().trim().isEmpty() ||
                wtgmED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Weight Fields cannot be empty!\nPut 0 to fill blank.");
            return false;
        }
        weightModel.setTagId(Integer.parseInt(tagIdSpinner.getSelectedItem().toString().trim()));
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        weightModel.setDate(tempDate);
        try {
            if (dialogDateUtil.isProposedDateBeforeDate(weightModel.getDate(), addAnimalModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be today's date or from past.");
                return false;
            }
            if (dialogDateUtil.isDateInFuture(weightModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be today's date or from past.");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String tempWt = wtkgED.getText().toString().trim()
                + "."
                + wtgmED.getText().toString().trim();
        weightModel.setWeight(tempWt);
        Log.i("CUSTOM",
                "\nTag id = " + weightModel.getTagId() +
                        "\nsr no = " + weightModel.getSrno() +
                        "\ndate = " + weightModel.getDate() +
                        "\nweight = " + weightModel.getWeight());
        return true;
    }

    private void addDetailsToLocalDatabase() {
        HashMap<String, String> response = db.addAnimalWeightDetails(weightModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Animal's Weight added Successfully!");
            count = 0;
            weightsArrayList = db.getAllWeightsRecords();
            Collections.sort(weightsArrayList, Collections.<WeightModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
    }

    private void loadPrerequisiteContent(int status) {
        Calendar calendar = Calendar.getInstance();
        totalAnimalsCountTV.setText(String.valueOf(db.getTotalAnimalsWeightsCount()));
        monthAnimalsCountTV.setText(String.valueOf(db.getThisMonthAnimalsCount(String.valueOf(calendar.get(Calendar.MONTH) + 1),
                String.valueOf(calendar.get(Calendar.YEAR)))));
        Log.i("CUSTOM", "Fetched data for month = " + (calendar.get(Calendar.MONTH) + 1));
        ArrayList<String> list = db.getAllNonDeletedTagIds();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                WeightActivity.this,
                R.layout.layout_text_view_black,
                list);
        if (list.isEmpty()) {
            Log.i("CUSTOM", "Tag id list is empty");
            if (status != 1) {
                dialogDateUtil.showMessage("No Record Found. Please Add one before you add their weights.");
            }
            disableComponents();
        } else {
            Log.i("CUSTOM", "Tag id list is not empty");
            tagIdSpinner.setAdapter(arrayAdapter);
            loadDisplayBox(tagIdSpinner.getSelectedItem().toString());
        }
        tagIdSpinner.setAdapter(arrayAdapter);
        weightsArrayList = db.getAllWeightsRecords();
        Collections.sort(weightsArrayList, Collections.<WeightModel>reverseOrder());
        count = 0;
        justNext = false;
    }

    private void disableComponents() {
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (tagIdSpinner.getCount() > 0) {
                        ((TextView) tagIdSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            });
        } catch (Exception e) {
        }
        tagIdSpinner.setEnabled(false);
        tagIdSpinner.setFocusable(false);
        ddED.setEnabled(false);
        ddED.setTextColor(getResources().getColor(android.R.color.black));
        mmED.setEnabled(false);
        mmED.setTextColor(getResources().getColor(android.R.color.black));
        yyyyED.setEnabled(false);
        yyyyED.setTextColor(getResources().getColor(android.R.color.black));
        wtkgED.setEnabled(false);
        wtkgED.setTextColor(getResources().getColor(android.R.color.black));
        wtgmED.setEnabled(false);
        wtgmED.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void enableComponents() {
        tagIdSpinner.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        wtkgED.setEnabled(true);
        wtgmED.setEnabled(true);
    }

    private void clearUIElements() {
        tagIdSpinner.setSelection(0);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        wtkgED.setText("");
        wtgmED.setText("");
    }

    private void loadDisplayBox(String tagId) {
        addAnimalModel = db.getDetailsForTagID(tagId);
        animalTypeTV.setText(db.getFieldBySrNo(addAnimalModel.getAnimalType(), LocalDatabase.TABLE_ANIMAL_TYPE));
        breedTV.setText(db.getBreedBySrNoAndAnimalType(addAnimalModel.getBreed(),addAnimalModel.getAnimalType()));
        dateTV.setText(addAnimalModel.getDate());
        weightTV.setText(addAnimalModel.getWeight());
        if (addAnimalModel.getGender().equals("M")) {
            genderTV.setText(getString(R.string.male));
        } else {
            genderTV.setText(getString(R.string.female));
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
            startActivity(new Intent(WeightActivity.this, AboutUsActivity.class)
                    .putExtra("from", "weight_activity"));
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

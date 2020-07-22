package com.aarushsystems.goatdiary.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.adapter.HealthCareAdapter;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.model.AddAnimalModel;
import com.aarushsystems.goatdiary.model.VaccinationModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class HealthCareActivity extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext;
    private LocalDatabase db;
    private VaccinationModel vaccinationModel;
    private DialogDateUtil dialogDateUtil;
    private ScrollView scrollView;
    private AddAnimalModel addAnimalModel;
    private LinearLayout linkLayout;
    private ArrayList<VaccinationModel> vaccinationArrayList;
    private Spinner tagIdSpinner, vaccineSpinner, doseSpinner, boosterSpinner;
    private TextView totalAnimalsCountTV, monthVaccinationDueCountTV, prevTV, saveTV, animalTypeTV,
            breedTV, genderTV, dateTV, weightTV;
    private EditText ddED, mmED, yyyyED, pddED, pmmED, pyyyyED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_care);
        Toolbar toolbar = findViewById(R.id.toolbar_HealthCareActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_HealthCareActivity);
        totalAnimalsCountTV = findViewById(R.id.textView_total_animals_HealthCareActivity);
        monthVaccinationDueCountTV = findViewById(R.id.textView_vaccination_month_HealthCareActivity);
        tagIdSpinner = findViewById(R.id.spinner_tag_id_HealthCareActivity);
        vaccineSpinner = findViewById(R.id.spinner_vaccine_HealthCareActivity);
        doseSpinner = findViewById(R.id.spinner_dose_HealthCareActivity);
        boosterSpinner = findViewById(R.id.spinner_booster_HealthCareActivity);
        prevTV = findViewById(R.id.textView_prev_HealthCareActivity);
        saveTV = findViewById(R.id.textView_next_HealthCareActivity);
        ddED = findViewById(R.id.et1_date_HealthCareActivity);
        mmED = findViewById(R.id.et2_date_HealthCareActivity);
        yyyyED = findViewById(R.id.et3_date_HealthCareActivity);
        pddED = findViewById(R.id.et1_p_date_HealthCareActivity);
        pmmED = findViewById(R.id.et2_p_date_HealthCareActivity);
        pyyyyED = findViewById(R.id.et3_p_date_HealthCareActivity);
        animalTypeTV = findViewById(R.id.textView_animal_type_HealthCareActivity);
        breedTV = findViewById(R.id.textView_breed_HealthCareActivity);
        genderTV = findViewById(R.id.textView_gender_HealthCareActivity);
        dateTV = findViewById(R.id.textView_date_HealthCareActivity);
        weightTV = findViewById(R.id.textView_weight_HealthCareActivity);
        linkLayout = findViewById(R.id.layout_link_HealthCareActivity);

        dialogDateUtil = new DialogDateUtil(HealthCareActivity.this);
        db = new LocalDatabase(HealthCareActivity.this);
        vaccinationArrayList = new ArrayList<>();

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

        pddED.addTextChangedListener(new TextWatcher() {
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
                    pmmED.requestFocus();
                    pmmED.setCursorVisible(true);
                }
            }
        });

        pmmED.addTextChangedListener(new TextWatcher() {
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
                    pyyyyED.requestFocus();
                    pyyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    pddED.requestFocus();
                    pddED.setCursorVisible(true);
                }
            }
        });

        pyyyyED.addTextChangedListener(new TextWatcher() {
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
                    pmmED.requestFocus();
                    pmmED.setCursorVisible(true);
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

        linkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vaccinationArrayList.isEmpty()) {
                    openLinkDialog();
                } else {
                    dialogDateUtil.showMessage("No Record Found. Please Add one before you set up their Health Care.");
                }
            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count);
                if (vaccinationArrayList.size() > 0 && count < vaccinationArrayList.size() && count >= 0) {
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
                    if (count < vaccinationArrayList.size() && count >= 0) {
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

    public void openLinkDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HealthCareActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = layoutInflater.inflate(R.layout.dialog_vaccination_link, null);
        builder.setView(dialogView);

        Log.i("CUSTOM", "\n");
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
        ArrayList<VaccinationModel> arrayList = new ArrayList<>();
        for (VaccinationModel model : vaccinationArrayList) {
            if (dialogDateUtil.isDateInFuture(model.getProposedDate()) || today.equals(model.getProposedDate())) {
                arrayList.add(model);
                Log.i("CUSTOM", "model = " + model.getProposedDate());
            }
        }
        Collections.sort(arrayList);
        ListView listView = dialogView.findViewById(R.id.listView_dialog_vaccination_link);
        HealthCareAdapter adapter = new HealthCareAdapter(HealthCareActivity.this, arrayList);
        listView.setAdapter(adapter);

        builder.setCancelable(false)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.setTitle(R.string.info);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadPreviousContent(int count) {
        VaccinationModel vaccinationModel = vaccinationArrayList.get(count);
        Log.i("CUSTOM", "Previous content tag id = " + vaccinationModel.getTagId());
        ArrayList<String> al = new ArrayList<>();
        al.add(String.valueOf(vaccinationModel.getTagId()));
        ArrayAdapter<String> aa = new ArrayAdapter<>(HealthCareActivity.this, R.layout.layout_text_view_black, al);
        tagIdSpinner.setAdapter(aa);
        tagIdSpinner.setSelection(0);
        Log.i("CUSTOM", "Tag id spinner prev size = " + tagIdSpinner.getCount());
        loadDisplayBox(String.valueOf(vaccinationModel.getTagId()));
        String[] tempDate = vaccinationModel.getDate().split("/");
        String[] ptempDate = vaccinationModel.getProposedDate().split("/");
        ddED.setText(tempDate[0]);
        mmED.setText(tempDate[1]);
        yyyyED.setText(tempDate[2]);
        pddED.setText(ptempDate[0]);
        pmmED.setText(ptempDate[1]);
        pyyyyED.setText(ptempDate[2]);
        vaccineSpinner.setSelection(vaccinationModel.getVaccine());
        doseSpinner.setSelection(vaccinationModel.getDose());
        boosterSpinner.setSelection(vaccinationModel.getBooster());
    }

    private boolean checkData() {
        vaccinationModel = new VaccinationModel();
        if (vaccineSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Please select Vaccine.");
            return false;
        } else {
            if (boosterSpinner.getSelectedItemPosition() == 0) {
                dialogDateUtil.showMessage("Please select Booster.");
                return false;
            } else {

            }
        }
        if (doseSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Dose Field cannot be empty.");
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
        if (pddED.getText().toString().trim().isEmpty() ||
                pmmED.getText().toString().trim().isEmpty() ||
                pyyyyED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Date Fields cannot be empty!");
            return false;
        } else if (pyyyyED.getText().toString().length() != 4 ||
                (Integer.parseInt(pddED.getText().toString().trim()) < 1 || Integer.parseInt(pddED.getText().toString().trim()) > 31) ||
                (Integer.parseInt(pmmED.getText().toString().trim()) < 1 || Integer.parseInt(pmmED.getText().toString().trim()) > 12)) {
            dialogDateUtil.showMessage("Invalid Proposed Date!");
            return false;
        }
        vaccinationModel.setTagId(Integer.parseInt(tagIdSpinner.getSelectedItem().toString().trim()));
        vaccinationModel.setVaccine(vaccineSpinner.getSelectedItemPosition());
        vaccinationModel.setBooster(boosterSpinner.getSelectedItemPosition());
        vaccinationModel.setDose(doseSpinner.getSelectedItemPosition());
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        vaccinationModel.setDate(tempDate);
        String tempProposedDate = (pddED.getText().toString().trim().length() == 1 ? "0" + pddED.getText().toString().trim() : pddED.getText().toString().trim())
                + "/" + (pmmED.getText().toString().trim().length() == 1 ? "0" + pmmED.getText().toString().trim() : pmmED.getText().toString().trim()) + "/"
                + pyyyyED.getText().toString().trim();
        vaccinationModel.setProposedDate(tempProposedDate);
        try {
            if (dialogDateUtil.isProposedDateBeforeDate(vaccinationModel.getDate(), addAnimalModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be after Animal's addition date.");
                return false;
            }
            if (dialogDateUtil.isProposedDateBeforeDate(vaccinationModel.getProposedDate(), vaccinationModel.getDate())) {
                Log.i("CUSTOM", "Proposed Date = " + vaccinationModel.getProposedDate());
                dialogDateUtil.showMessage("Invalid Proposed Date.\nProposed Date should be after " + vaccinationModel.getDate());
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("CUSTOM",
                "\nTag id = " + vaccinationModel.getTagId() +
                        "\nsr no = " + vaccinationModel.getSrno() +
                        "\ndate = " + vaccinationModel.getDate() +
                        "\nproposed date = " + vaccinationModel.getProposedDate() +
                        "\nbooster = " + vaccinationModel.getBooster() +
                        "\ndose = " + vaccinationModel.getDose() +
                        "\nvaccine = " + vaccinationModel.getVaccine());
        return true;
    }

    private void addDetailsToLocalDatabase() {
        HashMap<String, String> response = db.addAnimalVaccinationDetails(vaccinationModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Animal's Vaccination added Successfully!");
            count = 0;
            vaccinationArrayList = db.getAllVaccinationsRecords();
            Collections.sort(vaccinationArrayList, Collections.<VaccinationModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> doseList = new ArrayList<>();
        doseList.add("SELECT");
        for (int i = 1; i <= 1000; i++) {
            doseList.add(i + " ml.");
        }
        ArrayAdapter<String> doseAdapter = new ArrayAdapter<>(HealthCareActivity.this,
                R.layout.layout_text_view_black, doseList);
        doseSpinner.setAdapter(doseAdapter);

        Calendar calendar = Calendar.getInstance();
        totalAnimalsCountTV.setText(String.valueOf(db.getTotalAnimalsCount()));
        try {
            monthVaccinationDueCountTV.setText(String.valueOf(
                    db.getVaccinesDueThisMonthAnimalsCount(
                            String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                            String.valueOf(calendar.get(Calendar.MONTH) + 1),
                            String.valueOf(calendar.get(Calendar.YEAR)))));
        } catch (ParseException e) {
            Log.i("CUSTOM", "Due Vaccinations this month exception = " + e.getMessage());
        }
        Log.i("CUSTOM", "Fetched data for after date = " + calendar.get(Calendar.DAY_OF_MONTH) +
                (calendar.get(Calendar.MONTH) + 1) +
                calendar.get(Calendar.YEAR));
        ArrayList<String> list = db.getAllNonDeletedTagIds();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                HealthCareActivity.this,
                R.layout.layout_text_view_black,
                list);
        if (list.isEmpty()) {
            Log.i("CUSTOM", "Tag id list is empty");
            if (status != 1) {
                dialogDateUtil.showMessage("No Record Found. Please Add one before you set up their Health Care.");
            }
            disableComponents();
        } else {
            //Log.i("CUSTOM", "Tag id list is not empty");
            tagIdSpinner.setAdapter(arrayAdapter);
            loadDisplayBox(tagIdSpinner.getSelectedItem().toString());
        }
        tagIdSpinner.setAdapter(arrayAdapter);
        vaccinationArrayList = db.getAllVaccinationsRecords();
        Collections.sort(vaccinationArrayList, Collections.<VaccinationModel>reverseOrder());
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
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (vaccineSpinner.getCount() > 0) {
                        ((TextView) vaccineSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            });
        } catch (Exception e) {
        }
        vaccineSpinner.setEnabled(false);
        vaccineSpinner.setFocusable(false);
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (doseSpinner.getCount() > 0) {
                        ((TextView) doseSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            });
        } catch (Exception e) {
        }
        doseSpinner.setEnabled(false);
        doseSpinner.setFocusable(false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (boosterSpinner.getCount() > 0) {
                    try {
                        ((TextView) boosterSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    } catch (Exception e) {
                        Log.i("CUSTOM", "exception at boosterspinner = " + e.getMessage());
                    }
                }
            }
        });
        boosterSpinner.setEnabled(false);
        boosterSpinner.setFocusable(false);
        ddED.setEnabled(false);
        ddED.setTextColor(getResources().getColor(android.R.color.black));
        mmED.setEnabled(false);
        mmED.setTextColor(getResources().getColor(android.R.color.black));
        yyyyED.setEnabled(false);
        yyyyED.setTextColor(getResources().getColor(android.R.color.black));
        pddED.setEnabled(false);
        pddED.setTextColor(getResources().getColor(android.R.color.black));
        pmmED.setEnabled(false);
        pmmED.setTextColor(getResources().getColor(android.R.color.black));
        pyyyyED.setEnabled(false);
        pyyyyED.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void enableComponents() {
        tagIdSpinner.setEnabled(true);
        vaccineSpinner.setEnabled(true);
        boosterSpinner.setEnabled(true);
        doseSpinner.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        pddED.setEnabled(true);
        pmmED.setEnabled(true);
        pyyyyED.setEnabled(true);
    }

    private void clearUIElements() {
        tagIdSpinner.setSelection(0);
        vaccineSpinner.setSelection(0);
        doseSpinner.setSelection(0);
        boosterSpinner.setSelection(0);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        pddED.setText("");
        pmmED.setText("");
        pyyyyED.setText("");
    }

    private void loadDisplayBox(String tagId) {
        addAnimalModel = db.getDetailsForTagID(tagId);
        animalTypeTV.setText(db.getFieldBySrNo(addAnimalModel.getAnimalType(), LocalDatabase.TABLE_ANIMAL_TYPE));
        breedTV.setText(db.getFieldBySrNo(addAnimalModel.getBreed(), LocalDatabase.TABLE_BREED));
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
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(HealthCareActivity.this, AboutUsActivity.class)
                    .putExtra("from", "health_care_activity"));
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

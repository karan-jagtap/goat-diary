package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.model.AddAnimalModel;
import com.aarushsystems.goatdiary.model.BreedingModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class BreedingActivityBackup extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext, fromBlank;
    private LocalDatabase db;
    private BreedingModel breedingModel;
    private DialogDateUtil dialogDateUtil;
    private ScrollView scrollView;
    private AddAnimalModel maleModel, femaleModel;
    private LinearLayout deliveryLayout;
    private ArrayList<BreedingModel> breedingArrayList;
    private CheckBox matingCB, confCB, deliveryCB;
    private Spinner maleIdSpinner, femaleIdSpinner, abortionSpinner, stillbornSpinner, maleChildSpinner, femaleChildSpinner;
    private TextView prevTV, saveTV, animalTypeMaleTV, breedMaleTV, ageMaleTV, weightMaleTV, animalTypeFemaleTV,
            breedFemaleTV, ageFemaleTV, weightFemaleTV;
    private EditText matingddED, matingmmED, matingyyyyED, confddED, confmmED, confyyyyED, delddED, delmmED, delyyyyED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeding);
        Toolbar toolbar = findViewById(R.id.toolbar_BreedingActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_BreedingActivity);
        maleIdSpinner = findViewById(R.id.spinner_male_id_BreedingActivity);
        femaleIdSpinner = findViewById(R.id.spinner_female_id_BreedingActivity);
        abortionSpinner = findViewById(R.id.spinner_abortion_BreedingActivity);
        stillbornSpinner = findViewById(R.id.spinner_stillborn_BreedingActivity);
        maleChildSpinner = findViewById(R.id.spinner_male_child_BreedingActivity);
        femaleChildSpinner = findViewById(R.id.spinner_female_child_BreedingActivity);
        prevTV = findViewById(R.id.textView_prev_BreedingActivity);
        saveTV = findViewById(R.id.textView_next_BreedingActivity);
        matingddED = findViewById(R.id.et1_mating_date_BreedingActivity);
        matingmmED = findViewById(R.id.et2_mating_date_BreedingActivity);
        matingyyyyED = findViewById(R.id.et3_mating_date_BreedingActivity);
        confddED = findViewById(R.id.et1_conf_date_BreedingActivity);
        confmmED = findViewById(R.id.et2_conf_date_BreedingActivity);
        confyyyyED = findViewById(R.id.et3_conf_date_BreedingActivity);
        delddED = findViewById(R.id.et1_delivery_date_BreedingActivity);
        delmmED = findViewById(R.id.et2_delivery_date_BreedingActivity);
        delyyyyED = findViewById(R.id.et3_delivery_date_BreedingActivity);
        animalTypeMaleTV = findViewById(R.id.textView_animal_type_male_BreedingActivity);
        breedMaleTV = findViewById(R.id.textView_breed_male_BreedingActivity);
        ageMaleTV = findViewById(R.id.textView_age_male_BreedingActivity);
        weightMaleTV = findViewById(R.id.textView_weight_male_BreedingActivity);
        animalTypeFemaleTV = findViewById(R.id.textView_animal_type_female_BreedingActivity);
        breedFemaleTV = findViewById(R.id.textView_breed_female_BreedingActivity);
        ageFemaleTV = findViewById(R.id.textView_age_female_BreedingActivity);
        weightFemaleTV = findViewById(R.id.textView_weight_female_BreedingActivity);
        deliveryLayout = findViewById(R.id.layout_delivery_checked_BreedingActivity);
        matingCB = findViewById(R.id.checkbox_mating_BreedingActivity);
        confCB = findViewById(R.id.checkbox_confirmation_BreedingActivity);
        deliveryCB = findViewById(R.id.checkbox_delivery_BreedingActivity);

        dialogDateUtil = new DialogDateUtil(BreedingActivityBackup.this);
        db = new LocalDatabase(BreedingActivityBackup.this);
        breedingArrayList = new ArrayList<>();

        deliveryLayout.setVisibility(View.GONE);
        loadPrerequisiteContent(-1);
    }

    private void listeners() {
        matingddED.addTextChangedListener(new TextWatcher() {
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
                    matingmmED.requestFocus();
                    matingmmED.setCursorVisible(true);
                }
            }
        });

        matingmmED.addTextChangedListener(new TextWatcher() {
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
                    matingyyyyED.requestFocus();
                    matingyyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    matingddED.requestFocus();
                    matingddED.setCursorVisible(true);
                }
            }
        });

        matingyyyyED.addTextChangedListener(new TextWatcher() {
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
                    matingmmED.requestFocus();
                    matingmmED.setCursorVisible(true);
                }
            }
        });

        confddED.addTextChangedListener(new TextWatcher() {
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
                    confmmED.requestFocus();
                    confmmED.setCursorVisible(true);
                }
            }
        });

        confmmED.addTextChangedListener(new TextWatcher() {
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
                    confyyyyED.requestFocus();
                    confyyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    confddED.requestFocus();
                    confddED.setCursorVisible(true);
                }
            }
        });

        confyyyyED.addTextChangedListener(new TextWatcher() {
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
                    confmmED.requestFocus();
                    confmmED.setCursorVisible(true);
                }
            }
        });

        delddED.addTextChangedListener(new TextWatcher() {
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
                    delmmED.requestFocus();
                    delmmED.setCursorVisible(true);
                }
            }
        });

        delmmED.addTextChangedListener(new TextWatcher() {
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
                    delyyyyED.requestFocus();
                    delyyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    delddED.requestFocus();
                    delddED.setCursorVisible(true);
                }
            }
        });

        delyyyyED.addTextChangedListener(new TextWatcher() {
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
                    delmmED.requestFocus();
                    delmmED.setCursorVisible(true);
                }
            }
        });

        maleIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadDisplayBox(adapterView.getSelectedItem().toString(), femaleIdSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        femaleIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadDisplayBox(maleIdSpinner.getSelectedItem().toString(), adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deliveryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    deliveryLayout.setVisibility(View.VISIBLE);
                } else {
                    deliveryLayout.setVisibility(View.GONE);
                }
            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count + " fromBlank = " + fromBlank);
                if (breedingArrayList.size() > 0 && count < breedingArrayList.size() && count >= 0) {
                    if (justNext) {
                        count++;
                        justNext = false;
                    }
                    Log.i("CUSTOM", "Check Data from PREV");
                    if (!fromBlank) {
                        Log.i("CUSTOM", "not from blank");
                        breedingModel = breedingArrayList.get(0);
                        if (checkUpdateData()) {
                            Log.i("CUSTOM", "Prev update data is valid");
                            db.updateBreedingDetails(breedingModel);
                            breedingArrayList = db.getAllBreedingRecords();
                        }
                    }
                    loadPreviousContent(count);
                    disableComponents();
                    count++;
                    saveTV.setText(getString(R.string.next));
                    justPrev = true;
                    fromBlank = false;
                } else {
                    boolean status = false;
                    if (!fromBlank) {
                        Log.i("CUSTOM", "not from blank");
                        breedingModel = breedingArrayList.get(0);
                        status = checkUpdateData();
                        if (status) {
                            Log.i("CUSTOM", "Prev update data is valid");
                            db.updateBreedingDetails(breedingModel);
                            breedingArrayList = db.getAllBreedingRecords();
                        }
                    } else {
                        dialogDateUtil.showMessage("No Record Found.");
                    }
                    if (status) {
                        dialogDateUtil.showMessage("No Record Found.");
                    }
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
                    if (checkInsertData()) {
                        addDetailsToLocalDatabase();
                        Log.i("CUSTOM", "Save checkInsertUpdate()");
                    }
                } else if (saveTV.getText().toString().equals(getString(R.string.next))) {
                    if (justPrev) {
                        count--;
                        justPrev = false;
                    }
                    count--;
                    if (count < breedingArrayList.size() && count >= 0) {
                        breedingModel = breedingArrayList.get(0);
                        if (checkUpdateData()) {
                            Log.i("CUSTOM", "Next update data is valid");
                            db.updateBreedingDetails(breedingModel);
                            breedingArrayList = db.getAllBreedingRecords();
                            loadPreviousContent(count);
                            disableComponents();
                            justNext = true;
                        } else {
                            Log.i("CUSTOM", "next update data is not valid");
                        }
                    } else {
                        breedingModel = breedingArrayList.get(0);
                        if (checkUpdateData()) {
                            Log.i("CUSTOM", "Next update data is valid");
                            db.updateBreedingDetails(breedingModel);
                            breedingArrayList = db.getAllBreedingRecords();
                            clearUIElements();
                            enableComponents();
                            loadPrerequisiteContent(-1);
                            saveTV.setText(getString(R.string.save));
                        }
                    }
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void loadPreviousContent(int count) {
        Log.i("CUSTOM", "Loading Content for index = " + count);
        BreedingModel breedingModel = breedingArrayList.get(count);
        Log.i("CUSTOM", "Previous content Male id = " + breedingModel.getMaleId() + " Female Id = " + breedingModel.getFemaleId());
        ArrayList<String> al = new ArrayList<>();
        al.add(String.valueOf(breedingModel.getMaleId()));
        ArrayAdapter<String> aa = new ArrayAdapter<>(BreedingActivityBackup.this, android.R.layout.simple_list_item_1, al);
        maleIdSpinner.setAdapter(aa);
        maleIdSpinner.setSelection(0);
        ArrayList<String> alF = new ArrayList<>();
        alF.add(String.valueOf(breedingModel.getFemaleId()));
        ArrayAdapter<String> aaF = new ArrayAdapter<>(BreedingActivityBackup.this, android.R.layout.simple_list_item_1, alF);
        femaleIdSpinner.setAdapter(aaF);
        femaleIdSpinner.setSelection(0);
        loadDisplayBox(String.valueOf(breedingModel.getMaleId()), String.valueOf(breedingModel.getFemaleId()));
        String[] tempMatingDate = breedingModel.getMatingDate().split("/");
        matingddED.setText(tempMatingDate[0]);
        matingmmED.setText(tempMatingDate[1]);
        matingyyyyED.setText(tempMatingDate[2]);
        matingyyyyED.setTextColor(getResources().getColor(android.R.color.black));
        matingddED.setTextColor(getResources().getColor(android.R.color.black));
        matingmmED.setTextColor(getResources().getColor(android.R.color.black));
        matingddED.setEnabled(false);
        matingmmED.setEnabled(false);
        matingyyyyED.setEnabled(false);
        if (breedingModel.getConfDate() != null) {
            String[] tempConfDate = breedingModel.getConfDate().split("/");
            confddED.setText(tempConfDate[0]);
            confmmED.setText(tempConfDate[1]);
            confyyyyED.setText(tempConfDate[2]);
            confddED.setTextColor(getResources().getColor(android.R.color.black));
            confmmED.setTextColor(getResources().getColor(android.R.color.black));
            confyyyyED.setTextColor(getResources().getColor(android.R.color.black));
            confddED.setEnabled(false);
            confmmED.setEnabled(false);
            confyyyyED.setEnabled(false);
        }
        if (breedingModel.getDeliveryDate() != null) {
            String[] tempDeliveryDate = breedingModel.getDeliveryDate().split("/");
            delddED.setText(tempDeliveryDate[0]);
            delmmED.setText(tempDeliveryDate[1]);
            delyyyyED.setText(tempDeliveryDate[2]);
            delddED.setTextColor(getResources().getColor(android.R.color.black));
            delmmED.setTextColor(getResources().getColor(android.R.color.black));
            delyyyyED.setTextColor(getResources().getColor(android.R.color.black));
            delddED.setEnabled(false);
            delmmED.setEnabled(false);
            delyyyyED.setEnabled(false);
        }
        if (breedingModel.getMatingFlag().equals("Y")) {
            matingCB.setChecked(true);
        }
        if (breedingModel.getConfFlag().equals("Y")) {
            confCB.setChecked(true);
        }
        if (breedingModel.getDeliveryFlag().equals("Y")) {
            deliveryCB.setChecked(true);
        }
        if (deliveryCB.isChecked()) {
            abortionSpinner.setSelection(breedingModel.getAbortion());
            stillbornSpinner.setSelection(breedingModel.getStillborn());
            maleChildSpinner.setSelection(breedingModel.getChildMale());
            femaleChildSpinner.setSelection(breedingModel.getChildFemale());
        }
    }

    private boolean checkInsertData() {
        breedingModel = new BreedingModel();
        if (maleIdSpinner.getCount() == 0) {
            dialogDateUtil.showMessage("Record cannot be added.\nNo Male Id Found");
            return false;
        }
        if (femaleIdSpinner.getCount() == 0) {
            dialogDateUtil.showMessage("Record cannot be added.\nNo Female Id Found");
            return false;
        }
        if (matingddED.getText().toString().trim().isEmpty() ||
                matingmmED.getText().toString().trim().isEmpty() ||
                matingyyyyED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Mating Date Fields cannot be empty!");
            return false;
        } else if (matingyyyyED.getText().toString().length() != 4 ||
                (Integer.parseInt(matingddED.getText().toString().trim()) < 1 || Integer.parseInt(matingddED.getText().toString().trim()) > 31) ||
                (Integer.parseInt(matingmmED.getText().toString().trim()) < 1 || Integer.parseInt(matingmmED.getText().toString().trim()) > 12)) {
            dialogDateUtil.showMessage("Invalid Mating Date!");
            return false;
        }
        if (!confddED.getText().toString().trim().isEmpty() ||
                !confmmED.getText().toString().trim().isEmpty() ||
                !confyyyyED.getText().toString().trim().isEmpty()) {
            if (confddED.getText().toString().trim().isEmpty() ||
                    confmmED.getText().toString().trim().isEmpty() ||
                    confyyyyED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Confirmation Date Fields cannot be empty!");
                return false;
            } else if (confyyyyED.getText().toString().length() != 4 ||
                    (Integer.parseInt(confddED.getText().toString().trim()) < 1 || Integer.parseInt(confddED.getText().toString().trim()) > 31) ||
                    (Integer.parseInt(confmmED.getText().toString().trim()) < 1 || Integer.parseInt(confmmED.getText().toString().trim()) > 12)) {
                dialogDateUtil.showMessage("Invalid Confirmation Date!");
                return false;
            }
            String tempConfDate = (confddED.getText().toString().trim().length() == 1 ? "0" + confddED.getText().toString().trim() : confddED.getText().toString().trim())
                    + "/" + (confmmED.getText().toString().trim().length() == 1 ? "0" + confmmED.getText().toString().trim() : confmmED.getText().toString().trim()) + "/"
                    + confyyyyED.getText().toString().trim();
            breedingModel.setConfDate(tempConfDate);
            try {
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getConfDate(), maleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Confirmation Date.\nDate must be after Animal's addition date.");
                    return false;
                }
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getConfDate(), femaleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Confirmation Date.\nDate must be after Animal's addition date.");
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!delddED.getText().toString().trim().isEmpty() ||
                !delmmED.getText().toString().trim().isEmpty() ||
                !delyyyyED.getText().toString().trim().isEmpty()) {
            if (delddED.getText().toString().trim().isEmpty() ||
                    delmmED.getText().toString().trim().isEmpty() ||
                    delyyyyED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Delivery Date Fields cannot be empty!");
                return false;
            } else if (delyyyyED.getText().toString().length() != 4 ||
                    (Integer.parseInt(delddED.getText().toString().trim()) < 1 || Integer.parseInt(delddED.getText().toString().trim()) > 31) ||
                    (Integer.parseInt(delmmED.getText().toString().trim()) < 1 || Integer.parseInt(delmmED.getText().toString().trim()) > 12)) {
                dialogDateUtil.showMessage("Invalid Delivery Date!");
                return false;
            }
            String tempDelDate = (delddED.getText().toString().trim().length() == 1 ? "0" + delddED.getText().toString().trim() : delddED.getText().toString().trim())
                    + "/" + (delmmED.getText().toString().trim().length() == 1 ? "0" + delmmED.getText().toString().trim() : delmmED.getText().toString().trim()) + "/"
                    + delyyyyED.getText().toString().trim();
            breedingModel.setDeliveryDate(tempDelDate);
            try {
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getDeliveryDate(), maleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Delivery Date.\nDate must be after Animal's addition date.");
                    return false;
                }
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getDeliveryDate(), femaleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Delivery Date.\nDate must be after Animal's addition date.");
                    return false;
                }
            } catch (ParseException e) {
            }
        }
        String tempMatingDate = (matingddED.getText().toString().trim().length() == 1 ? "0" + matingddED.getText().toString().trim() : matingddED.getText().toString().trim())
                + "/" + (matingmmED.getText().toString().trim().length() == 1 ? "0" + matingmmED.getText().toString().trim() : matingmmED.getText().toString().trim()) + "/"
                + matingyyyyED.getText().toString().trim();
        breedingModel.setMatingDate(tempMatingDate);

        try {
            if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getMatingDate(), maleModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Mating Date.\nDate must be after Animal's addition date.");
                return false;
            }
            if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getMatingDate(), femaleModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Mating Date.\nDate must be after Animal's addition date.");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        breedingModel.setMaleId(Integer.parseInt(maleIdSpinner.getSelectedItem().toString()));
        breedingModel.setFemaleId(Integer.parseInt(femaleIdSpinner.getSelectedItem().toString()));
        if (matingCB.isChecked()) {
            breedingModel.setMatingFlag("Y");
        } else {
            breedingModel.setMatingFlag("N");
        }
        if (confCB.isChecked()) {
            breedingModel.setConfFlag("Y");
        } else {
            breedingModel.setConfFlag("N");
        }
        if (deliveryCB.isChecked()) {
            breedingModel.setDeliveryFlag("Y");
        } else {
            breedingModel.setDeliveryFlag("N");
        }
        if (breedingModel.getDeliveryFlag().equals("Y")) {
            breedingModel.setAbortion(abortionSpinner.getSelectedItemPosition());
            breedingModel.setStillborn(stillbornSpinner.getSelectedItemPosition());
            breedingModel.setChildMale(maleChildSpinner.getSelectedItemPosition());
            breedingModel.setChildFemale(femaleChildSpinner.getSelectedItemPosition());
        }
        Log.i("CUSTOM",
                "\n\nSr no = " + breedingModel.getSrno() +
                        "\nMale id = " + breedingModel.getMaleId() +
                        "\nFeMale id = " + breedingModel.getFemaleId() +
                        "\nMating Flag = " + breedingModel.getMatingFlag() +
                        "\nMating Date = " + breedingModel.getMatingDate() +
                        "\nConfirmation Flag = " + breedingModel.getConfFlag() +
                        "\nConfirmation Date = " + breedingModel.getConfDate() +
                        "\nDelivery Flag = " + breedingModel.getDeliveryFlag() +
                        "\nDelivery Date = " + breedingModel.getDeliveryDate() +
                        "\nAbortion = " + breedingModel.getAbortion() +
                        "\nStillborn = " + breedingModel.getStillborn() +
                        "\nChild Male = " + breedingModel.getChildMale() +
                        "\nChild Female = " + breedingModel.getChildFemale());
        return true;
    }

    private boolean checkUpdateData() {
        for (BreedingModel bm : breedingArrayList) {
            if (bm.getSrno() == breedingModel.getSrno()) {
                breedingModel = bm;
            }
        }
        if (maleIdSpinner.getCount() == 0) {
            dialogDateUtil.showMessage("Record cannot be added.\nNo Male Id Found");
            return false;
        }
        if (femaleIdSpinner.getCount() == 0) {
            dialogDateUtil.showMessage("Record cannot be added.\nNo Female Id Found");
            return false;
        }
        if (matingddED.getText().toString().trim().isEmpty() ||
                matingmmED.getText().toString().trim().isEmpty() ||
                matingyyyyED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Mating Date Fields cannot be empty!");
            return false;
        } else if (matingyyyyED.getText().toString().length() != 4 ||
                (Integer.parseInt(matingddED.getText().toString().trim()) < 1 || Integer.parseInt(matingddED.getText().toString().trim()) > 31) ||
                (Integer.parseInt(matingmmED.getText().toString().trim()) < 1 || Integer.parseInt(matingmmED.getText().toString().trim()) > 12)) {
            dialogDateUtil.showMessage("Invalid Mating Date!");
            return false;
        }
        if (!confddED.getText().toString().trim().isEmpty() ||
                !confmmED.getText().toString().trim().isEmpty() ||
                !confyyyyED.getText().toString().trim().isEmpty()) {
            if (confddED.getText().toString().trim().isEmpty() ||
                    confmmED.getText().toString().trim().isEmpty() ||
                    confyyyyED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Confirmation Date Fields cannot be empty!");
                return false;
            } else if (confyyyyED.getText().toString().length() != 4 ||
                    (Integer.parseInt(confddED.getText().toString().trim()) < 1 || Integer.parseInt(confddED.getText().toString().trim()) > 31) ||
                    (Integer.parseInt(confmmED.getText().toString().trim()) < 1 || Integer.parseInt(confmmED.getText().toString().trim()) > 12)) {
                dialogDateUtil.showMessage("Invalid Confirmation Date!");
                return false;
            }
            String tempConfDate = (confddED.getText().toString().trim().length() == 1 ? "0" + confddED.getText().toString().trim() : confddED.getText().toString().trim())
                    + "/" + (confmmED.getText().toString().trim().length() == 1 ? "0" + confmmED.getText().toString().trim() : confmmED.getText().toString().trim()) + "/"
                    + confyyyyED.getText().toString().trim();
            breedingModel.setConfDate(tempConfDate);
            try {
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getConfDate(), maleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Confirmation Date.\nDate must be after Animal's addition date.");
                    return false;
                }
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getConfDate(), femaleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Confirmation Date.\nDate must be after Animal's addition date.");
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!delddED.getText().toString().trim().isEmpty() ||
                !delmmED.getText().toString().trim().isEmpty() ||
                !delyyyyED.getText().toString().trim().isEmpty()) {
            if (delddED.getText().toString().trim().isEmpty() ||
                    delmmED.getText().toString().trim().isEmpty() ||
                    delyyyyED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Delivery Date Fields cannot be empty!");
                return false;
            } else if (delyyyyED.getText().toString().length() != 4 ||
                    (Integer.parseInt(delddED.getText().toString().trim()) < 1 || Integer.parseInt(delddED.getText().toString().trim()) > 31) ||
                    (Integer.parseInt(delmmED.getText().toString().trim()) < 1 || Integer.parseInt(delmmED.getText().toString().trim()) > 12)) {
                dialogDateUtil.showMessage("Invalid Delivery Date!");
                return false;
            }
            String tempDelDate = (delddED.getText().toString().trim().length() == 1 ? "0" + delddED.getText().toString().trim() : delddED.getText().toString().trim())
                    + "/" + (delmmED.getText().toString().trim().length() == 1 ? "0" + delmmED.getText().toString().trim() : delmmED.getText().toString().trim()) + "/"
                    + delyyyyED.getText().toString().trim();
            breedingModel.setDeliveryDate(tempDelDate);
            try {
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getDeliveryDate(), maleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Delivery Date.\nDate must be after Animal's addition date.");
                    return false;
                }
                if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getDeliveryDate(), femaleModel.getDate())) {
                    dialogDateUtil.showMessage("Invalid Delivery Date.\nDate must be after Animal's addition date.");
                    return false;
                }
            } catch (ParseException e) {
            }
        }
        String tempMatingDate = (matingddED.getText().toString().trim().length() == 1 ? "0" + matingddED.getText().toString().trim() : matingddED.getText().toString().trim())
                + "/" + (matingmmED.getText().toString().trim().length() == 1 ? "0" + matingmmED.getText().toString().trim() : matingmmED.getText().toString().trim()) + "/"
                + matingyyyyED.getText().toString().trim();
        breedingModel.setMatingDate(tempMatingDate);
        try {
            if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getMatingDate(), maleModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Mating Date.\nDate must be after Animal's addition date.");
                return false;
            }
            if (dialogDateUtil.isProposedDateBeforeDate(breedingModel.getMatingDate(), femaleModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Mating Date.\nDate must be after Animal's addition date.");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        breedingModel.setMaleId(Integer.parseInt(maleIdSpinner.getSelectedItem().toString()));
        breedingModel.setFemaleId(Integer.parseInt(femaleIdSpinner.getSelectedItem().toString()));
        if (matingCB.isChecked()) {
            breedingModel.setMatingFlag("Y");
        } else {
            breedingModel.setMatingFlag("N");
        }
        if (confCB.isChecked()) {
            breedingModel.setConfFlag("Y");
        } else {
            breedingModel.setConfFlag("N");
        }
        if (deliveryCB.isChecked()) {
            breedingModel.setDeliveryFlag("Y");
        } else {
            breedingModel.setDeliveryFlag("N");
        }
        if (breedingModel.getDeliveryFlag().equals("Y")) {
            breedingModel.setAbortion(abortionSpinner.getSelectedItemPosition());
            breedingModel.setStillborn(stillbornSpinner.getSelectedItemPosition());
            breedingModel.setChildMale(maleChildSpinner.getSelectedItemPosition());
            breedingModel.setChildFemale(femaleChildSpinner.getSelectedItemPosition());
        }
        Log.i("CUSTOM",
                "\n\nSr no = " + breedingModel.getSrno() +
                        "\nMale id = " + breedingModel.getMaleId() +
                        "\nFeMale id = " + breedingModel.getFemaleId() +
                        "\nMating Flag = " + breedingModel.getMatingFlag() +
                        "\nMating Date = " + breedingModel.getMatingDate() +
                        "\nConfirmation Flag = " + breedingModel.getConfFlag() +
                        "\nConfirmation Date = " + breedingModel.getConfDate() +
                        "\nDelivery Flag = " + breedingModel.getDeliveryFlag() +
                        "\nDelivery Date = " + breedingModel.getDeliveryDate() +
                        "\nAbortion = " + breedingModel.getAbortion() +
                        "\nStillborn = " + breedingModel.getStillborn() +
                        "\nChild Male = " + breedingModel.getChildMale() +
                        "\nChild Female = " + breedingModel.getChildFemale());
        return true;
    }

    private void addDetailsToLocalDatabase() {
        HashMap<String, String> response = db.addAnimalBreedingDetails(breedingModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Animal's Breeding Details added Successfully!");
            count = 0;
            breedingArrayList = db.getAllBreedingRecords();
            Collections.sort(breedingArrayList, Collections.<BreedingModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> femaleList = db.getAllNonPregnantFemalesTagIds();
        ArrayList<String> maleList = db.getAllMalesTagIds();
        ArrayAdapter<String> femaleAdapter = new ArrayAdapter<>(
                BreedingActivityBackup.this,
                android.R.layout.simple_list_item_1,
                femaleList);
        ArrayAdapter<String> maleAdapter = new ArrayAdapter<>(
                BreedingActivityBackup.this,
                android.R.layout.simple_list_item_1,
                maleList);
        if (femaleList.isEmpty() && maleList.isEmpty()) {
            Log.i("CUSTOM", "Tag id list is empty");
            if (status != 1) {
                dialogDateUtil.showMessage("No Record Found. Please add Animals first.");
            }
            disableComponents();
        } else if (femaleList.isEmpty()) {
            if (status != 1) {
                dialogDateUtil.showMessage("No Record Found. Please add Female Animals.");
            }
            disableComponents();
        } else if (maleList.isEmpty()) {
            if (status != 1) {
                dialogDateUtil.showMessage("No Record Found. Please add Male Animals.");
            }
            disableComponents();
        } else {
            //Log.i("CUSTOM", "Tag id list is not empty");
            maleIdSpinner.setAdapter(maleAdapter);
            femaleIdSpinner.setAdapter(femaleAdapter);
            loadDisplayBox(maleIdSpinner.getSelectedItem().toString(), femaleIdSpinner.getSelectedItem().toString());
        }
        breedingArrayList = db.getAllBreedingRecords();
        Log.i("CUSTOM", "breeding array list size = "+breedingArrayList.size());
        Collections.sort(breedingArrayList, Collections.<BreedingModel>reverseOrder());
        count = 0;
        justNext = false;
        fromBlank = true;
    }

    private void disableComponents() {
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (maleIdSpinner.getCount() > 0) {
                        ((TextView) maleIdSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            });
        } catch (Exception e) {
        }
        maleIdSpinner.setEnabled(false);
        maleIdSpinner.setFocusable(false);
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (femaleIdSpinner.getCount() > 0) {
                        ((TextView) femaleIdSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            });
        } catch (Exception e) {
        }
        femaleIdSpinner.setEnabled(false);
        femaleIdSpinner.setFocusable(false);
    }

    private void enableComponents() {
        maleIdSpinner.setEnabled(true);
        femaleIdSpinner.setEnabled(true);
        delmmED.setEnabled(true);
        delddED.setEnabled(true);
        delyyyyED.setEnabled(true);
        matingmmED.setEnabled(true);
        matingddED.setEnabled(true);
        matingyyyyED.setEnabled(true);
        confyyyyED.setEnabled(true);
        confmmED.setEnabled(true);
        confddED.setEnabled(true);
    }

    private void clearUIElements() {
        maleIdSpinner.setSelection(0);
        femaleIdSpinner.setSelection(0);
        abortionSpinner.setSelection(0);
        stillbornSpinner.setSelection(0);
        maleChildSpinner.setSelection(0);
        femaleChildSpinner.setSelection(0);
        matingddED.setText("");
        matingmmED.setText("");
        matingyyyyED.setText("");
        confddED.setText("");
        confmmED.setText("");
        confyyyyED.setText("");
        delddED.setText("");
        delmmED.setText("");
        delyyyyED.setText("");
        matingCB.setChecked(false);
        confCB.setChecked(false);
        deliveryCB.setChecked(false);
    }

    private void loadDisplayBox(String maleId, String femaleId) {
        maleModel = db.getDetailsForTagID(maleId);
        femaleModel = db.getDetailsForTagID(femaleId);
        animalTypeMaleTV.setText(db.getFieldBySrNo(maleModel.getAnimalType(), LocalDatabase.TABLE_ANIMAL_TYPE));
        breedMaleTV.setText(db.getFieldBySrNo(maleModel.getBreed(), LocalDatabase.TABLE_BREED));
        //ageMaleTV.setText(addAnimalModel.getDate());
        weightMaleTV.setText(maleModel.getWeight());

        animalTypeFemaleTV.setText(db.getFieldBySrNo(femaleModel.getAnimalType(), LocalDatabase.TABLE_ANIMAL_TYPE));
        breedFemaleTV.setText(db.getFieldBySrNo(femaleModel.getBreed(), LocalDatabase.TABLE_BREED));
        //ageFemaleTV.setText(addAnimalModel.getDate());
        weightFemaleTV.setText(femaleModel.getWeight());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(BreedingActivityBackup.this, AboutUsActivity.class)
                    .putExtra("from", "breeding_activity"));
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

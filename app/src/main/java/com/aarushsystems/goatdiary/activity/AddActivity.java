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
import com.aarushsystems.goatdiary.model.AddAnimalModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private LocalDatabase db;
    private DialogDateUtil dialogDateUtil;
    private ScrollView scrollView;
    private int count = 0;
    private boolean justPrev, justNext, newScreen = true;
    private RadioGroup genderRG;
    private AddAnimalModel addAnimalModel;
    private ArrayList<String> motherIdList;
    private ArrayList<AddAnimalModel> addAnimalArrayList;
    private LinearLayout motherIdLayout, priceLayout, breedLayout;
    private TextView totalAnimalsCountTV, prevTV, saveTV, motherIdLine;
    private Spinner animalTypeSpinner, aquisationSpinner, breedSpinner, purposeSpinner, motherIdSpinner;
    private EditText tagIdED, ddED, mmED, yyyyED, wtkgED, wtgmED, priceED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar_AddActivity);
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

        /*genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_male_AddActivity) {
                    femaleLayout.setVisibility(View.GONE);
                    femaleLine.setVisibility(View.GONE);
                } else {
                    if (aquisationSpinner.getSelectedItemPosition() == 1) {
                        femaleLayout.setVisibility(View.VISIBLE);
                        femaleLine.setVisibility(View.VISIBLE);
                    } else {
                        femaleLayout.setVisibility(View.GONE);
                        femaleLine.setVisibility(View.GONE);
                    }
                }
            }
        });*/

        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Log.i("CUSTOM", "animalTypeSpinner.setOnItemSelectedListener()");
                    breedLayout.setVisibility(View.VISIBLE);
                    ArrayList<String> al = db.getDataForBreedTable(i);
                    ArrayAdapter<String> aa = new ArrayAdapter<>(
                            AddActivity.this,
                            R.layout.layout_text_view_black,
                            al);
                    al.add(0, "SELECT");
                    if (al.size() == 1) {
                        dialogDateUtil.showMessage("Please Add Breed for Animal Type " + adapterView.getSelectedItem().toString() + " from Master Settings Tab.");
                        breedLayout.setVisibility(View.GONE);
                    }
                    breedSpinner.setAdapter(aa);
                    if (addAnimalModel != null && !newScreen) {
                        Log.i("CUSTOM", "animal type inside breed = " + addAnimalModel.getBreed());
                        breedSpinner.setSelection(addAnimalModel.getBreed(), true);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) breedSpinner.getSelectedView()).setTextColor(Color.BLACK);
                                } catch (Exception e) {
                                }
                            }
                        });
                        breedSpinner.setEnabled(false);
                        breedSpinner.setFocusable(false);
                    } else if (newScreen) {
                        aquisationSpinner.setSelection(0);
                    }
                } else {
                    breedLayout.setVisibility(View.GONE);
                }
                new String("1,3,4,5,6,7,9,10,12");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        aquisationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    priceLayout.setVisibility(View.GONE);
                    motherIdLayout.setVisibility(View.GONE);
                    motherIdLine.setVisibility(View.GONE);
                } else if (i == 1) {
                    priceLayout.setVisibility(View.VISIBLE);
                    motherIdLayout.setVisibility(View.GONE);
                    motherIdLine.setVisibility(View.GONE);
                } else if (i == 2) {
                    priceLayout.setVisibility(View.GONE);
                    motherIdLayout.setVisibility(View.VISIBLE);
                    motherIdLine.setVisibility(View.VISIBLE);
                    motherIdList = db.getAllFemalesTagIds(animalTypeSpinner.getSelectedItemPosition());
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                            AddActivity.this,
                            R.layout.layout_text_view_black, motherIdList);
                    if (!motherIdList.isEmpty()) {
                        motherIdList.add(0, "SELECT");
                        motherIdSpinner.setAdapter(arrayAdapter);
                        if (addAnimalModel != null && !newScreen) {
                            motherIdSpinner.setSelection(motherIdList.indexOf(addAnimalModel.getMotherId()));
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.i("CUSTOM", "animal type spinner count = " + animalTypeSpinner.getCount() + " position = " + animalTypeSpinner.getSelectedItemPosition());
                                        if (motherIdSpinner.getCount() > 0) {
                                            ((TextView) motherIdSpinner.getSelectedView()).setTextColor(Color.BLACK);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("CUSTOM", "exception = " + e.getMessage());
                                    }
                                }
                            });
                            motherIdSpinner.setEnabled(false);
                            motherIdSpinner.setFocusable(false);
                        }
                        Log.i("CUSTOM", "Masters table record for mother_id spinner :: ");
                        for (String data : motherIdList) {
                            Log.i("CUSTOM", data);
                        }
                    } else {
                        if (animalTypeSpinner.getSelectedItemPosition()!=0) {
                            dialogDateUtil.showMessage("To Select Aquisation \"BY BIRTH\" please add Animal's Mother first.");
                        } else {
                            dialogDateUtil.showMessage("No Females found for Animal Type "+animalTypeSpinner.getSelectedItem().toString()+".");
                        }
                        aquisationSpinner.setSelection(0);
                    }
                }
                if (newScreen) {
                    ((RadioButton) findViewById(R.id.radio_male_AddActivity)).setChecked(true);
                    //((RadioButton) findViewById(R.id.radio_non_pregnant_AddActivity)).setChecked(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveTV.getText().toString().equals(getString(R.string.save))) {
                    justNext = false;
                    newScreen = true;
                    if (checkData()) {
                        addDetailsToLocalDatabase();
                    }
                } else if (saveTV.getText().toString().equals(getString(R.string.next))) {
                    if (justPrev) {
                        count--;
                        justPrev = false;
                    }
                    count--;
                    if (count < addAnimalArrayList.size() && count >= 0) {
                        loadPreviousContent(count);
                        disableComponents();
                        justNext = true;
                    } else {
                        clearUIElements();
                        enableComponents();
                        //loadPrerequisiteContent(-1);
                        saveTV.setText(getString(R.string.save));
                        newScreen = true;
                    }
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                Log.i("CUSTOM", "after save click count = " + count);
            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addAnimalArrayList.size() > 0 && count < addAnimalArrayList.size() && count >= 0) {
                    if (justNext) {
                        Log.i("CUSTOM", "in just next true");
                        count++;
                        justNext = false;
                    }
                    loadPreviousContent(count);
                    disableComponents();
                    count++;
                    saveTV.setText(getString(R.string.next));
                    justPrev = true;
                    newScreen = false;
                } else {
                    dialogDateUtil.showMessage("No Record Found.");
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                Log.i("CUSTOM", "after prev click count = " + count);
            }
        });
    }

    private void loadPreviousContent(int count) {
        addAnimalModel = addAnimalArrayList.get(count);
        Log.i("CUSTOM", "getting data at " + count);
        //addAnimalModel = db.getAnimalDetailsBySrNo(sr_no);
        if (addAnimalModel != null) {
            Log.i("CUSTOM", "loading started");
            tagIdED.setText(String.valueOf(addAnimalModel.getTagId()));
            animalTypeSpinner.setSelection(addAnimalModel.getAnimalType(), false);
            aquisationSpinner.setSelection(addAnimalModel.getAquisation(), false);
            if (breedSpinner.getCount() > addAnimalModel.getBreed()) {
                breedSpinner.setSelection(addAnimalModel.getBreed(), false);
            }
            if (addAnimalModel.getGender().equals("M")) {
                Log.i("CUSTOM", "male checked");
                RadioButton male = findViewById(R.id.radio_male_AddActivity);
                male.setChecked(true);
            } else {
                Log.i("CUSTOM", "female checked");
                RadioButton female = findViewById(R.id.radio_female_AddActivity);
                female.setChecked(true);
                /*if (addAnimalModel.getFemaleStatus().equals("Preg")) {
                    RadioButton preg = findViewById(R.id.radio_pregnant_AddActivity);
                    preg.setChecked(true);
                } else {
                    RadioButton nonPreg = findViewById(R.id.radio_non_pregnant_AddActivity);
                    nonPreg.setChecked(true);
                }*/
            }

            String[] tempDate = addAnimalModel.getDate().split("/");
            ddED.setText(tempDate[0]);
            mmED.setText(tempDate[1]);
            yyyyED.setText(tempDate[2]);
            String[] tempWeight = addAnimalModel.getWeight().split("\\.");
            wtkgED.setText(String.valueOf(tempWeight[0]));
            wtgmED.setText(String.valueOf(tempWeight[1]));
            purposeSpinner.setSelection(addAnimalModel.getPurpose(), true);
            if (aquisationSpinner.getSelectedItemPosition() == 1) {
                priceED.setText(addAnimalModel.getPrice());
            } else if (aquisationSpinner.getSelectedItemPosition() == 2) {
                ArrayList<String> al = new ArrayList<>();
                al.add(String.valueOf(addAnimalModel.getMotherId()));
                Log.i("CUSTOM", "setting mother id = " + al.get(0));
                ArrayAdapter<String> aa = new ArrayAdapter<>(
                        AddActivity.this,
                        R.layout.layout_text_view_black,
                        al);
                motherIdSpinner.setAdapter(aa);
                motherIdSpinner.setSelection(0, true);
            }
            Log.i("CUSTOM", "done loading");
        }
    }

    private void addDetailsToLocalDatabase() {
        HashMap<String, String> response = db.userAddAnimalDetails(addAnimalModel);
        if (Objects.equals(response.get("error"), "0")) {
            count = 0;
            dialogDateUtil.showMessage("Animal Added Successfully!");
            clearUIElements();
            addAnimalArrayList = db.getAllAnimalDetailsRecords();
            Collections.sort(addAnimalArrayList, Collections.<AddAnimalModel>reverseOrder());
        } else {
            if (Objects.equals(response.get("message"), "tag_id_present")) {
                dialogDateUtil.showMessage("Animal with TAG ID : " + addAnimalModel.getTagId() + " is already present." +
                        "\nPlease use another TAG ID and then try again.");
            } else {
                dialogDateUtil.showMessage("Internal Local Database Error");
            }
        }
    }

    private void clearUIElements() {
        count = 0;
        justNext = false;
        tagIdED.setText("");
        animalTypeSpinner.setSelection(0);
        aquisationSpinner.setSelection(0);
        RadioButton male = findViewById(R.id.radio_male_AddActivity);
        male.setChecked(true);
        breedSpinner.setSelection(0);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        wtkgED.setText("");
        wtgmED.setText("000");
        purposeSpinner.setSelection(0);
        priceED.setText("");
        motherIdSpinner.setSelection(0);
        totalAnimalsCountTV.setText(String.valueOf(db.getTotalAnimalsCount()));
    }

    private void disableComponents() {
        Log.i("CUSTOM", "started disable");
        tagIdED.setEnabled(false);
        tagIdED.setTextColor(getResources().getColor(android.R.color.black));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("CUSTOM", "animal type spinner count = " + animalTypeSpinner.getCount() + " position = " + animalTypeSpinner.getSelectedItemPosition());
                    if (animalTypeSpinner.getCount() > 0) {
                        TextView tv = (TextView) animalTypeSpinner.getSelectedView();
                        tv.setTextColor(Color.BLACK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("CUSTOM", "exception = " + e.getMessage());
                }
            }
        });
        animalTypeSpinner.setEnabled(false);
        animalTypeSpinner.setFocusable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    ((TextView) aquisationSpinner.getSelectedView()).setTextColor(Color.BLACK);
                } catch (Exception e) {
                }
            }
        });
        aquisationSpinner.setEnabled(false);
        aquisationSpinner.setFocusable(false);
        ((TextView) findViewById(R.id.radio_male_AddActivity)).setTextColor(Color.BLACK);
        findViewById(R.id.radio_male_AddActivity).setEnabled(false);
        ((TextView) findViewById(R.id.radio_female_AddActivity)).setTextColor(Color.BLACK);
        findViewById(R.id.radio_female_AddActivity).setEnabled(false);
        /*((TextView) findViewById(R.id.radio_pregnant_AddActivity)).setTextColor(Color.BLACK);
        findViewById(R.id.radio_pregnant_AddActivity).setEnabled(false);
        ((TextView) findViewById(R.id.radio_non_pregnant_AddActivity)).setTextColor(Color.BLACK);
        findViewById(R.id.radio_non_pregnant_AddActivity).setEnabled(false);*/
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    ((TextView) breedSpinner.getSelectedView()).setTextColor(Color.BLACK);
                } catch (Exception e) {
                }
            }
        });
        breedSpinner.setEnabled(false);
        breedSpinner.setFocusable(false);
        try {
            ((TextView) purposeSpinner.getSelectedView()).setTextColor(Color.BLACK);
            purposeSpinner.setEnabled(false);
            purposeSpinner.setFocusable(false);
        } catch (Exception e) {
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("CUSTOM", "animal type spinner count = " + animalTypeSpinner.getCount() + " position = " + animalTypeSpinner.getSelectedItemPosition());
                    if (motherIdSpinner.getCount() > 0) {
                        ((TextView) motherIdSpinner.getSelectedView()).setTextColor(Color.BLACK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("CUSTOM", "exception = " + e.getMessage());
                }
            }
        });
        motherIdSpinner.setEnabled(false);
        motherIdSpinner.setFocusable(false);
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
        priceED.setEnabled(false);
        priceED.setTextColor(getResources().getColor(android.R.color.black));
        Log.i("CUSTOM", "ended disable");
    }

    private void enableComponents() {
        tagIdED.setEnabled(true);
        animalTypeSpinner.setEnabled(true);
        aquisationSpinner.setEnabled(true);
        findViewById(R.id.radio_male_AddActivity).setEnabled(true);
        findViewById(R.id.radio_female_AddActivity).setEnabled(true);
        /*findViewById(R.id.radio_pregnant_AddActivity).setEnabled(true);
        findViewById(R.id.radio_non_pregnant_AddActivity).setEnabled(true);*/
        breedSpinner.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        wtkgED.setEnabled(true);
        wtgmED.setEnabled(true);
        purposeSpinner.setEnabled(true);
        priceED.setEnabled(true);
        motherIdSpinner.setEnabled(true);
    }

    private boolean checkData() {
        addAnimalModel = new AddAnimalModel();
        if (animalTypeSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Animal Type Field cannot be empty.");
            return false;
        }
        if (aquisationSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Aquisation Field cannot be empty.");
            return false;
        }
        if (breedSpinner.getSelectedItemPosition() == 0) {
            if (breedSpinner.getCount() == 1) {
                dialogDateUtil.showMessage("Please Add Breed for Animal Type " +
                        animalTypeSpinner.getSelectedItem().toString() +
                        " from Master Settings Tab.");
            } else {
                dialogDateUtil.showMessage("Breed Field cannot be empty.");
            }
            return false;
        }
        if (purposeSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Purpose Field cannot be empty.");
            return false;
        }
        if (tagIdED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Tag Id Field cannot be empty!");
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
        if (wtkgED.getText().toString().trim().isEmpty() ||
                wtgmED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Weight Fields cannot be empty!\nPut 0 to fill blank.");
            return false;
        }
        if (aquisationSpinner.getSelectedItemPosition() == 1) {
            if (priceED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Price Field cannot be empty!");
                return false;
            } else {
                String[] tempPrice = priceED.getText().toString().trim().split("\\.");
                if (tempPrice.length == 1) {
                    priceED.setText(priceED.getText().toString().trim().concat(".00"));
                } else {
                    if (tempPrice[1].length() > 2) {
                        dialogDateUtil.showMessage("Invalid Price.\nPaise field should of length 2.");
                        return false;
                    }
                }
            }
        } else if (aquisationSpinner.getSelectedItemPosition() == 2) {
            ArrayList<String> list = db.getAllFemalesTagIds(animalTypeSpinner.getSelectedItemPosition());
            if (list.isEmpty()) {
                dialogDateUtil.showMessage("No Mother Ids found.\nAdd Mother Animal first and then try again.");
                return false;
            }
            //addAnimalModel.setFemaleStatus("NonPreg");
        }
        addAnimalModel.setTagId(Integer.parseInt(tagIdED.getText().toString().trim()));
        addAnimalModel.setAnimalType(animalTypeSpinner.getSelectedItemPosition());
        addAnimalModel.setAquisation(aquisationSpinner.getSelectedItemPosition());
        addAnimalModel.setBreed(breedSpinner.getSelectedItemPosition());
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        addAnimalModel.setDate(tempDate);
        if (dialogDateUtil.isDateInFuture(addAnimalModel.getDate())) {
            dialogDateUtil.showMessage("Invalid Date.\nDate must be today's date or from past.");
            return false;
        }
        String tempWt = wtkgED.getText().toString().trim()
                + "."
                + wtgmED.getText().toString().trim();
        addAnimalModel.setWeight(tempWt);
        addAnimalModel.setPurpose(purposeSpinner.getSelectedItemPosition());
        if (addAnimalModel.getAquisation() == 1) {
            addAnimalModel.setPrice(priceED.getText().toString().trim());
        } else {
            if (motherIdSpinner.getSelectedItemPosition() == 0) {
                dialogDateUtil.showMessage("Please Select Mother Id.");
                return false;
            }
            addAnimalModel.setMotherId(motherIdSpinner.getSelectedItem().toString());
        }
        if (genderRG.getCheckedRadioButtonId() == R.id.radio_male_AddActivity) {
            addAnimalModel.setGender("M");
        } else {
            addAnimalModel.setGender("F");
            /*if (femaleRG.getCheckedRadioButtonId() == R.id.radio_pregnant_AddActivity) {
                addAnimalModel.setFemaleStatus("Preg");
            } else {
                addAnimalModel.setFemaleStatus("NonPreg");
            }*/
        }
        addAnimalModel.setDeleted(0);
        Log.i("CUSTOM",
                "\nTag id = " + addAnimalModel.getTagId() +
                        "\nanimal type = " + addAnimalModel.getAnimalType() +
                        "\naquisation = " + addAnimalModel.getAquisation() +
                        "\ngender = " + addAnimalModel.getGender() +
                        "\nbreed = " + addAnimalModel.getBreed() +
                        "\ndate = " + addAnimalModel.getDate() +
                        "\nweight = " + addAnimalModel.getWeight() +
                        "\npurpose = " + addAnimalModel.getPurpose() +
                        "\nprice = " + addAnimalModel.getPrice() +
                        "\nmother id = " + addAnimalModel.getMotherId());
        return true;
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_AddActivity);
        totalAnimalsCountTV = findViewById(R.id.textView_total_animals_AddActivity);
        prevTV = findViewById(R.id.textView_prev_AddActivity);
        saveTV = findViewById(R.id.textView_save_AddActivity);
        genderRG = findViewById(R.id.radioGroup_gender_AddActivity);
        animalTypeSpinner = findViewById(R.id.spinner_animal_type_AddActivity);
        aquisationSpinner = findViewById(R.id.spinner_aquisation_AddActivity);
        breedSpinner = findViewById(R.id.spinner_breed_AddActivity);
        purposeSpinner = findViewById(R.id.spinner_purpose_AddActivity);
        motherIdSpinner = findViewById(R.id.spinner_mother_id_AddActivity);
        tagIdED = findViewById(R.id.editText_tag_id_AddActivity);
        wtkgED = findViewById(R.id.et_kg_weight_AddActivity);
        wtgmED = findViewById(R.id.et_gm_weight_AddActivity);
        ddED = findViewById(R.id.et1_date_AddActivity);
        mmED = findViewById(R.id.et2_date_AddActivity);
        yyyyED = findViewById(R.id.et3_date_AddActivity);
        priceED = findViewById(R.id.editText_price_AddActivity);
        priceLayout = findViewById(R.id.layout_price_AddActivity);
        motherIdLayout = findViewById(R.id.layout_mother_id_AddActivity);
        motherIdLine = findViewById(R.id.line_mother_id_AddActivity);
        breedLayout = findViewById(R.id.layout_breed_AddActivity);
        //femaleLayout = findViewById(R.id.layout_female_group_AddActivity);
        //femaleLine = findViewById(R.id.line_female_group_AddActivity);
        //femaleRG = findViewById(R.id.radioGroup_female_AddActivity);
        //femaleLayout.setVisibility(View.GONE);
        //femaleLine.setVisibility(View.GONE);

        breedLayout.setVisibility(View.GONE);
        dialogDateUtil = new DialogDateUtil(AddActivity.this);
        db = new LocalDatabase(AddActivity.this);
        totalAnimalsCountTV.setText(String.valueOf(db.getTotalAnimalsCount()));
        addAnimalArrayList = db.getAllAnimalDetailsRecords();
        Collections.sort(addAnimalArrayList, Collections.<AddAnimalModel>reverseOrder());

        ArrayList<String> animalType = db.getDataForMastersTable(LocalDatabase.TABLE_ANIMAL_TYPE);
        ArrayAdapter animalTypeAdapter = new ArrayAdapter<String>(AddActivity.this,
                R.layout.layout_text_view_black,
                animalType);
        animalTypeSpinner.setAdapter(animalTypeAdapter);
        /*Log.i("CUSTOM", "Masters table record for " + LocalDatabase.TABLE_ANIMAL_TYPE + " :: ");
        for (String data : animalType) {
            Log.i("CUSTOM", data);
        }
*/
        ArrayList<String> aquisationType = db.getDataForMastersTable(LocalDatabase.TABLE_AQUISATION);
        ArrayAdapter aquisationTypeAdapter = new ArrayAdapter<String>(AddActivity.this,
                R.layout.layout_text_view_black,
                aquisationType);
        aquisationSpinner.setAdapter(aquisationTypeAdapter);
        /*Log.i("CUSTOM", "Masters table record for " + LocalDatabase.TABLE_AQUISATION + " :: ");
        for (String data : aquisationType) {
            Log.i("CUSTOM", data);
        }*/

        /*ArrayList<String> breedType = db.getDataForMastersTable(LocalDatabase.TABLE_BREED);
        ArrayAdapter breedTypeAdapter = new ArrayAdapter<String>(AddActivity.this,
                R.layout.layout_text_view_black,
                breedType);
        breedSpinner.setAdapter(breedTypeAdapter);*/
        /*Log.i("CUSTOM", "Masters table record for " + LocalDatabase.TABLE_BREED + " :: ");
        for (String data : breedType) {
            Log.i("CUSTOM", data);
        }*/

        ArrayList<String> purposeType = db.getDataForMastersTable(LocalDatabase.TABLE_PURPOSE);
        ArrayAdapter purposeTypeAdapter = new ArrayAdapter<String>(AddActivity.this,
                R.layout.layout_text_view_black,
                purposeType);
        purposeSpinner.setAdapter(purposeTypeAdapter);
        /*Log.i("CUSTOM", "Masters table record for " + LocalDatabase.TABLE_PURPOSE + " :: ");
        for (String data : purposeType) {
            Log.i("CUSTOM", data);
        }*/
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
            startActivity(new Intent(AddActivity.this, AboutUsActivity.class)
                    .putExtra("from", "add_activity"));
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

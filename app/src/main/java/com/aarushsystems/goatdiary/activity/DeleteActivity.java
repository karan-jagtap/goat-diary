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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class DeleteActivity extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext;
    private LocalDatabase db;
    private ScrollView scrollView;
    private AddAnimalModel addAnimalModel;
    private ArrayList<AddAnimalModel> deletedArrayList;
    private DialogDateUtil dialogDateUtil;
    private Spinner tagIdSpinner, releaseSpinner;
    private TextView totalAnimalsCountTV, deletedAnimalsCountTV, prevTV, saveTV, priceLine, weightLine, animalTypeTV, breedTV, genderTV, dateTV, weightTV;
    private LinearLayout weightLayout, priceLayout;
    private EditText ddED, mmED, yyyyED, wtkgED, wtgmED, priceED, remarksED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Toolbar toolbar = findViewById(R.id.toolbar_DeleteActivity);
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

        tagIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadDisplayBox(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        releaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    priceLayout.setVisibility(View.GONE);
                    priceLine.setVisibility(View.GONE);
                    weightLayout.setVisibility(View.GONE);
                    weightLine.setVisibility(View.GONE);
                } else if (i == 1) {
                    priceLayout.setVisibility(View.VISIBLE);
                    priceLine.setVisibility(View.VISIBLE);
                    weightLayout.setVisibility(View.GONE);
                    weightLine.setVisibility(View.GONE);
                } else if (i == 2) {
                    weightLayout.setVisibility(View.VISIBLE);
                    weightLine.setVisibility(View.VISIBLE);
                    priceLayout.setVisibility(View.GONE);
                    priceLine.setVisibility(View.GONE);
                } else if (i == 3) {
                    weightLayout.setVisibility(View.GONE);
                    weightLine.setVisibility(View.GONE);
                    priceLayout.setVisibility(View.GONE);
                    priceLine.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count);
                if (deletedArrayList.size() > 0 && count < deletedArrayList.size() && count >= 0) {
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
                if (saveTV.getText().toString().equals(getString(R.string.delete))) {
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
                    if (count < deletedArrayList.size() && count >= 0) {
                        loadPreviousContent(count);
                        disableComponents();
                        justNext = true;
                    } else {
                        clearUIElements();
                        enableComponents();
                        loadPrerequisiteContent(-1);
                        saveTV.setText(getString(R.string.delete));
                    }
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void loadPreviousContent(int count) {
        AddAnimalModel addAnimalModel = deletedArrayList.get(count);
        ArrayList<String> al = new ArrayList<>();
        al.add(String.valueOf(addAnimalModel.getTagId()));
        ArrayAdapter<String> aa = new ArrayAdapter<>(DeleteActivity.this, R.layout.layout_text_view_black, al);
        tagIdSpinner.setAdapter(aa);
        tagIdSpinner.setSelection(0);
        loadDisplayBox(String.valueOf(addAnimalModel.getTagId()));
        releaseSpinner.setSelection(addAnimalModel.getRelease());
        String[] tempDate = addAnimalModel.getdDate().split("/");
        Log.i("CUSTOM","daye = "+tempDate);
        ddED.setText(tempDate[0]);
        mmED.setText(tempDate[1]);
        yyyyED.setText(tempDate[2]);

        if (releaseSpinner.getSelectedItemPosition() == 1) {
            priceED.setText(addAnimalModel.getReleasePrice());
        } else if (releaseSpinner.getSelectedItemPosition() == 2) {
            String[] tempWeight = addAnimalModel.getReleaseWeight().split("\\.");
            wtkgED.setText(String.valueOf(tempWeight[0]));
            wtgmED.setText(String.valueOf(tempWeight[1]));
        }
        if (addAnimalModel.getRemarks() != null) {
            if (!addAnimalModel.getRemarks().isEmpty()) {
                remarksED.setText(addAnimalModel.getRemarks());
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
        HashMap<String, String> response = db.userDeleteAnimalDetails(addAnimalModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Animal Deleted Successfully!");
            count = 0;
            deletedArrayList = db.getAllDeletedRecords();
            Collections.sort(deletedArrayList, Collections.<AddAnimalModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
            //max_sr_no = db.getNewestSrNo();
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
    }

    private void clearUIElements() {
        releaseSpinner.setSelection(0);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        wtkgED.setText("");
        wtgmED.setText("000");
        priceED.setText("");
        remarksED.setText("");
        totalAnimalsCountTV.setText(String.valueOf(db.getTotalAnimalsCount()));
    }

    private boolean checkData() {
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
        if (releaseSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Release Field cannot be empty.");
            return false;
        } else if (releaseSpinner.getSelectedItemPosition() == 1) {
            if (priceED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Price Fields cannot be empty!");
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
            wtkgED.setText("");
            wtgmED.setText("");
        } else if (releaseSpinner.getSelectedItemPosition() == 2) {
            if (wtkgED.getText().toString().trim().isEmpty() ||
                    wtgmED.getText().toString().trim().isEmpty()) {
                dialogDateUtil.showMessage("Weight Fields cannot be empty!\nPut 0 to fill blank.");
                return false;
            } else {
                String tempWt = wtkgED.getText().toString().trim()
                        + "."
                        + wtgmED.getText().toString().trim();
                addAnimalModel.setReleaseWeight(tempWt);
            }
            priceED.setText("");
        }
        if (!remarksED.getText().toString().isEmpty()) {
            addAnimalModel.setRemarks(remarksED.getText().toString().trim());
        }
        addAnimalModel.setRelease(releaseSpinner.getSelectedItemPosition());
        addAnimalModel.setReleasePrice(priceED.getText().toString().trim());
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        addAnimalModel.setdDate(tempDate);
        try {
            if (dialogDateUtil.isProposedDateBeforeDate(addAnimalModel.getdDate(), addAnimalModel.getDate())) {
                dialogDateUtil.showMessage("Delete Date must be after Animal's Addition Date.");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (dialogDateUtil.isProposedDateBeforeDate(addAnimalModel.getdDate(), addAnimalModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be after Animal Addition Date.");
                return false;
            }
            if (dialogDateUtil.isDateInFuture(addAnimalModel.getdDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must today's Date or past Date.");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addAnimalModel.setDeleted(1);
        Log.i("CUSTOM",
                "\nTag id = " + addAnimalModel.getTagId() +
                        "\nanimal type = " + addAnimalModel.getAnimalType() +
                        "\naquisation = " + addAnimalModel.getAquisation() +
                        "\ngender = " + addAnimalModel.getGender() +
                        //"\nfemale status = " + addAnimalModel.getFemaleStatus() +
                        "\nbreed = " + addAnimalModel.getBreed() +
                        "\ndate = " + addAnimalModel.getDate() +
                        "\nweight = " + addAnimalModel.getWeight() +
                        "\npurpose = " + addAnimalModel.getPurpose() +
                        "\nprice = " + addAnimalModel.getPrice() +
                        "\nmother id = " + addAnimalModel.getMotherId() +
                        "\nrelease = " + addAnimalModel.getRelease() +
                        "\nrelease price = " + addAnimalModel.getReleasePrice() +
                        "\nrelease weight = " + addAnimalModel.getReleaseWeight() +
                        "\ndeleted date = " + addAnimalModel.getdDate() +
                        "\nremarks = " + addAnimalModel.getRemarks()
        );
        return true;
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_DeleteActivity);
        totalAnimalsCountTV = findViewById(R.id.textView_total_animals_DeleteActivity);
        deletedAnimalsCountTV = findViewById(R.id.textView_total_animals_deleted_DeleteActivity);
        prevTV = findViewById(R.id.textView_prev_DeleteActivity);
        saveTV = findViewById(R.id.textView_next_DeleteActivity);
        wtkgED = findViewById(R.id.et_kg_weight_DeleteActivity);
        wtgmED = findViewById(R.id.et_gm_weight_DeleteActivity);
        ddED = findViewById(R.id.et1_date_DeleteActivity);
        mmED = findViewById(R.id.et2_date_DeleteActivity);
        yyyyED = findViewById(R.id.et3_date_DeleteActivity);
        priceED = findViewById(R.id.editText_price_DeleteActivity);
        priceLayout = findViewById(R.id.layout_price_DeleteActivity);
        weightLayout = findViewById(R.id.layout_weight_DeleteActivity);
        priceLine = findViewById(R.id.line_price_DeleteActivity);
        weightLine = findViewById(R.id.line_weight_DeleteActivity);
        tagIdSpinner = findViewById(R.id.spinner_tag_id_DeleteActivity);
        releaseSpinner = findViewById(R.id.spinner_release_DeleteActivity);
        remarksED = findViewById(R.id.editText_remarks_DeleteActivity);
        animalTypeTV = findViewById(R.id.textView_animal_type_DeleteActivity);
        breedTV = findViewById(R.id.textView_breed_DeleteActivity);
        genderTV = findViewById(R.id.textView_gender_DeleteActivity);
        dateTV = findViewById(R.id.textView_date_DeleteActivity);
        weightTV = findViewById(R.id.textView_weight_DeleteActivity);

        saveTV.setText(getString(R.string.delete));
        weightLine.setVisibility(View.GONE);
        weightLayout.setVisibility(View.GONE);
        dialogDateUtil = new DialogDateUtil(DeleteActivity.this);
        db = new LocalDatabase(DeleteActivity.this);
        deletedArrayList = new ArrayList<>();

        loadPrerequisiteContent(-1);
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> al = db.getDataForMastersTable(LocalDatabase.TABLE_RELEASE);
        ArrayAdapter aa = new ArrayAdapter<String>(
                DeleteActivity.this,
                R.layout.layout_text_view_black,
                al
        );
        releaseSpinner.setAdapter(aa);
        Log.i("CUSTOM", "Masters table record for " + LocalDatabase.TABLE_RELEASE + " :: ");
        for (String data : al) {
            Log.i("CUSTOM", data);
        }
        totalAnimalsCountTV.setText(String.valueOf(db.getTotalAnimalsCount()));
        deletedAnimalsCountTV.setText(String.valueOf(db.getDeletedAnimalsCount()));
        ArrayList<String> list = db.getAllNonDeletedTagIds();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                DeleteActivity.this,
                R.layout.layout_text_view_black,
                list);
        if (list.isEmpty()) {
            Log.i("CUSTOM", "Tag id list is empty");
            if (status != 1) {
                dialogDateUtil.showMessage("No Record Found. Please Add one before you delete it.");
            }
            disableComponents();
        } else {
            Log.i("CUSTOM", "Tag id list is not empty");
            tagIdSpinner.setAdapter(arrayAdapter);
            loadDisplayBox(tagIdSpinner.getSelectedItem().toString());
        }
        tagIdSpinner.setAdapter(arrayAdapter);
        deletedArrayList = db.getAllDeletedRecords();
        Collections.sort(deletedArrayList, Collections.<AddAnimalModel>reverseOrder());
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (releaseSpinner.getCount() > 0) {
                        ((TextView) releaseSpinner.getSelectedView()).setTextColor(Color.BLACK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("CUSTOM", "exception = " + e.getMessage());
                }
            }
        });
        releaseSpinner.setEnabled(false);
        releaseSpinner.setFocusable(false);
        remarksED.setEnabled(false);
        remarksED.setTextColor(getResources().getColor(android.R.color.black));
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
    }

    private void enableComponents() {
        tagIdSpinner.setEnabled(true);
        releaseSpinner.setEnabled(true);
        remarksED.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        wtkgED.setEnabled(true);
        wtgmED.setEnabled(true);
        priceED.setEnabled(true);
    }

    private void loadDisplayBox(String tagId) {
        addAnimalModel = db.getDetailsForTagID(tagId);
        animalTypeTV.setText(db.getFieldBySrNo(addAnimalModel.getAnimalType(), LocalDatabase.TABLE_ANIMAL_TYPE));
        breedTV.setText(db.getBreedBySrNoAndAnimalType(addAnimalModel.getBreed(), addAnimalModel.getAnimalType()));
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
            startActivity(new Intent(DeleteActivity.this, AboutUsActivity.class)
                    .putExtra("from", "delete_activity"));
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

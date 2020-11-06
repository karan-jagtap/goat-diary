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
import android.widget.Toast;

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

public class DeleteEditActivity extends AppCompatActivity {

    private LocalDatabase db;
    private ScrollView scrollView;
    private AddAnimalModel addAnimalModel;
    private DialogDateUtil dialogDateUtil;
    private Spinner  releaseSpinner;
    private TextView cancelTV, saveTV, priceLine, weightLine, animalTypeTV, breedTV, genderTV, dateTV, weightTV;
    private LinearLayout weightLayout, priceLayout;
    private EditText ddED, mmED, yyyyED, wtkgED, wtgmED, priceED, remarksED, tagIdED;
    private String tagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_edit);
        Toolbar toolbar = findViewById(R.id.toolbar_DeleteEditActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getStringExtra("tag_id") != null) {
            tagId = getIntent().getStringExtra("tag_id");
        } else {
            finish();
        }

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

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    updateDetailsToLocalDatabase();
                }
            }
        });
    }

    private void updateDetailsToLocalDatabase() {
        // true for real deletion
        HashMap<String, String> response = db.userDeleteAnimalDetails(addAnimalModel,true);
        if (Objects.equals(response.get("error"), "0")) {
            Toast.makeText(DeleteEditActivity.this, "Animal Edited Successfully!", Toast.LENGTH_SHORT).show();
            finishInStyle();
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
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
        } else {
            addAnimalModel.setRemarks(null);
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
        scrollView = findViewById(R.id.scrollView_DeleteEditActivity);
        cancelTV = findViewById(R.id.textView_cancel_DeleteEditActivity);
        saveTV = findViewById(R.id.textView_next_DeleteEditActivity);
        wtkgED = findViewById(R.id.et_kg_weight_DeleteEditActivity);
        wtgmED = findViewById(R.id.et_gm_weight_DeleteEditActivity);
        ddED = findViewById(R.id.et1_date_DeleteEditActivity);
        mmED = findViewById(R.id.et2_date_DeleteEditActivity);
        yyyyED = findViewById(R.id.et3_date_DeleteEditActivity);
        priceED = findViewById(R.id.editText_price_DeleteEditActivity);
        tagIdED = findViewById(R.id.editText_tag_id_DeleteEditActivity);
        priceLayout = findViewById(R.id.layout_price_DeleteEditActivity);
        weightLayout = findViewById(R.id.layout_weight_DeleteEditActivity);
        priceLine = findViewById(R.id.line_price_DeleteEditActivity);
        weightLine = findViewById(R.id.line_weight_DeleteEditActivity);
        releaseSpinner = findViewById(R.id.spinner_release_DeleteEditActivity);
        remarksED = findViewById(R.id.editText_remarks_DeleteEditActivity);
        animalTypeTV = findViewById(R.id.textView_animal_type_DeleteEditActivity);
        breedTV = findViewById(R.id.textView_breed_DeleteEditActivity);
        genderTV = findViewById(R.id.textView_gender_DeleteEditActivity);
        dateTV = findViewById(R.id.textView_date_DeleteEditActivity);
        weightTV = findViewById(R.id.textView_weight_DeleteEditActivity);

        weightLine.setVisibility(View.GONE);
        weightLayout.setVisibility(View.GONE);
        dialogDateUtil = new DialogDateUtil(DeleteEditActivity.this);
        db = new LocalDatabase(DeleteEditActivity.this);
        addAnimalModel = db.getDetailsForDeletedTagID(tagId);

        loadPrerequisiteContent(-1);
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> al = db.getDataForMastersTable(LocalDatabase.TABLE_RELEASE);
        ArrayAdapter aa = new ArrayAdapter<String>(
                DeleteEditActivity.this,
                R.layout.layout_text_view_black,
                al
        );
        tagIdED.setText(String.valueOf(addAnimalModel.getTagId()));
        releaseSpinner.setAdapter(aa);
        loadDisplayBox(tagId);
        releaseSpinner.setSelection(addAnimalModel.getRelease());
        String[] tempDate = addAnimalModel.getdDate().split("/");
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

    private void loadDisplayBox(String tagId) {
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
            startActivity(new Intent(DeleteEditActivity.this, AboutUsActivity.class)
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

    private void finishInStyle(){
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

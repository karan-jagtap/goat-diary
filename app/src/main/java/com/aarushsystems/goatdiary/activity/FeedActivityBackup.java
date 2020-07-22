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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.model.FeedModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class FeedActivityBackup extends AppCompatActivity {

    private int count = 0;
    private boolean justPrev, justNext;
    private LocalDatabase db;
    private ScrollView scrollView;
    private FeedModel feedModel;
    private ArrayList<FeedModel> feedArrayList;
    private DialogDateUtil dialogDateUtil;
    private Spinner actionSpinner, timeSpinner, feedTypeSpinner;
    private TextView totalInputTV, totalOutputTV, prevTV, saveTV;
    private EditText ddED, mmED, yyyyED,wtkgED, wtgmED, remarksED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar_FeedActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
    }

    private void listeners() {
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

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CUSTOM", "prev button count = " + count);
                if (feedArrayList.size() > 0 && count < feedArrayList.size() && count >= 0) {
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
                    if (count < feedArrayList.size() && count >= 0) {
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
        FeedModel feedModel = feedArrayList.get(count);
        Log.i("CUSTOM", "amount = " + feedModel.getWeight());
        actionSpinner.setSelection(feedModel.getAction(),false);
        feedTypeSpinner.setSelection(feedModel.getFeedType(),false);

        String[] tempDate = feedModel.getDate().split("/");
        ddED.setText(tempDate[0]);
        mmED.setText(tempDate[1]);
        yyyyED.setText(tempDate[2]);
        String[] tempWeight= feedModel.getWeight().split("\\.");
        wtkgED.setText(tempWeight[0]);
        wtgmED.setText(tempWeight[1]);
        if (feedModel.getTime() != 0) {
            timeSpinner.setSelection(feedModel.getTime(), false);
        } else {
            timeSpinner.setSelection(0);
        }
        remarksED.setText(feedModel.getRemarks());
    }

    private void updateDetailsToLocalDatabase() {
        Log.i("CUSTOM", "updateDetailsToLocalDatabase()");
        HashMap<String, String> response = db.addAnimalFeedDetails(feedModel);
        if (Objects.equals(response.get("error"), "0")) {
            dialogDateUtil.showMessage("Food Details Added Successfully!");
            count = 0;
            feedArrayList = db.getAllFeedRecords();
            Collections.sort(feedArrayList, Collections.<FeedModel>reverseOrder());
            clearUIElements();
            loadPrerequisiteContent(1);
        } else {
            dialogDateUtil.showMessage("Internal Local Database Error");
        }
    }

    private void clearUIElements() {
        actionSpinner.setSelection(0, false);
        timeSpinner.setSelection(0, false);
        feedTypeSpinner.setSelection(0, false);
        ddED.setText("");
        mmED.setText("");
        yyyyED.setText("");
        wtgmED.setText("");
        wtkgED.setText("");
        remarksED.setText("");
    }

    private boolean checkData() {
        feedModel = new FeedModel();
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
        if (feedTypeSpinner.getSelectedItemPosition() == 0) {
            dialogDateUtil.showMessage("Feed Type cannot be empty.");
            return false;
        }
        if (wtkgED.getText().toString().trim().isEmpty() ||
                wtgmED.getText().toString().trim().isEmpty()) {
            dialogDateUtil.showMessage("Weight Fields cannot be empty!\nPut 0 to fill blank.");
            return false;
        }
        String tempDate = (ddED.getText().toString().trim().length() == 1 ? "0" + ddED.getText().toString().trim() : ddED.getText().toString().trim())
                + "/" + (mmED.getText().toString().trim().length() == 1 ? "0" + mmED.getText().toString().trim() : mmED.getText().toString().trim()) + "/"
                + yyyyED.getText().toString().trim();
        feedModel.setDate(tempDate);
        String tempWt = wtkgED.getText().toString().trim()
                + "."
                + wtgmED.getText().toString().trim();
        feedModel.setWeight(tempWt);
        try {
            if (dialogDateUtil.isDateInFuture(feedModel.getDate())) {
                dialogDateUtil.showMessage("Invalid Date.\nDate must be today's Date or past Date.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        feedModel.setTime(timeSpinner.getSelectedItemPosition());
        if (remarksED.getText().toString().isEmpty()) {
            dialogDateUtil.showMessage("Remarks Field cannot be empty!");
            return false;
        } else if (remarksED.getText().toString().trim().length() >= 150) {
            dialogDateUtil.showMessage("Remark cannot be more than 150 characters.");
            return false;
        }
        feedModel.setRemarks(remarksED.getText().toString().trim());
        feedModel.setAction(actionSpinner.getSelectedItemPosition());
        feedModel.setFeedType(feedTypeSpinner.getSelectedItemPosition());
        Log.i("CUSTOM",
                "\nSrno = " + feedModel.getSrno() +
                        "\naction = " + feedModel.getAction() +
                        "\nfeed type = " + feedModel.getFeedType() +
                        "\ndate = " + feedModel.getDate() +
                        "\nweight = " + feedModel.getWeight() +
                        "\ntime = " + feedModel.getTime() +
                        "\nremarks = " + feedModel.getRemarks()
        );
        return true;
    }

    private void declarations() {
        scrollView = findViewById(R.id.scrollView_FeedActivity);
        totalInputTV = findViewById(R.id.textView_total_input_FeedActivity);
        totalOutputTV = findViewById(R.id.textView_total_output_FeedActivity);
        actionSpinner = findViewById(R.id.spinner_action_FeedActivity);
        ddED = findViewById(R.id.et1_date_FeedActivity);
        mmED = findViewById(R.id.et2_date_FeedActivity);
        yyyyED = findViewById(R.id.et3_date_FeedActivity);
        timeSpinner = findViewById(R.id.spinner_time_FeedActivity);
        feedTypeSpinner = findViewById(R.id.spinner_feed_type_FeedActivity);
        wtkgED = findViewById(R.id.et_kg_weight_FeedActivity);
        wtgmED = findViewById(R.id.et_gm_weight_FeedActivity);
        remarksED = findViewById(R.id.editText_remarks_FeedActivity);
        prevTV = findViewById(R.id.textView_prev_FeedActivity);
        saveTV = findViewById(R.id.textView_next_FeedActivity);

        saveTV.setText(getString(R.string.save));
        dialogDateUtil = new DialogDateUtil(getApplicationContext());
        db = new LocalDatabase(getApplicationContext());
        feedArrayList = new ArrayList<>();

        loadPrerequisiteContent(-1);
    }

    private void loadPrerequisiteContent(int status) {
        ArrayList<String> al = db.getDataForMastersTable(LocalDatabase.TABLE_ACTION_FEED);
        ArrayAdapter aa = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                al);
        actionSpinner.setAdapter(aa);
        actionSpinner.setSelection(0, false);
        ArrayList<String> al2 = db.getDataForMastersTable(LocalDatabase.TABLE_FEED_TYPE);
        ArrayAdapter aa2 = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                al2);
        feedTypeSpinner.setAdapter(aa2);
        feedTypeSpinner.setSelection(0, false);
        String text = "Total\nInput\n" + db.getTotalInput()+" kg.";
        totalInputTV.setText(text);
        text = "Total\nOutput\n" + db.getTotalOutput()+" kg.";
        totalOutputTV.setText(text);
        feedArrayList = db.getAllFeedRecords();
        Log.i("CUSTOM", "Feed records size = " + feedArrayList.size());
        Collections.sort(feedArrayList, Collections.<FeedModel>reverseOrder());
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
                    if (feedTypeSpinner.getCount() > 0) {
                        ((TextView) feedTypeSpinner.getSelectedView()).setTextColor(getResources().getColor(android.R.color.black));
                    }
                } catch (Exception e) {
                }
            }
        });
        feedTypeSpinner.setEnabled(false);
        feedTypeSpinner.setFocusable(false);

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
        wtkgED.setEnabled(false);
        wtkgED.setTextColor(getResources().getColor(android.R.color.black));
        wtgmED.setEnabled(false);
        wtgmED.setTextColor(getResources().getColor(android.R.color.black));
        remarksED.setEnabled(false);
        remarksED.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void enableComponents() {
        actionSpinner.setEnabled(true);
        feedTypeSpinner.setEnabled(true);
        timeSpinner.setEnabled(true);
        ddED.setEnabled(true);
        mmED.setEnabled(true);
        yyyyED.setEnabled(true);
        wtkgED.setEnabled(true);
        wtgmED.setEnabled(true);
        remarksED.setEnabled(true);
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
            startActivity(new Intent(FeedActivityBackup.this, AboutUsActivity.class)
                    .putExtra("from", "feed_activity"));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        db.close();
        db = null;
        feedModel = null;

        super.onDestroy();
    }
}

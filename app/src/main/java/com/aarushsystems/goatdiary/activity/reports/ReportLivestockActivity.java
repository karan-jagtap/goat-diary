package com.aarushsystems.goatdiary.activity.reports;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.activity.AboutUsActivity;
import com.aarushsystems.goatdiary.adapter.ReportsAdapter;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.helper.ReportsListener;
import com.aarushsystems.goatdiary.model.Cell;
import com.aarushsystems.goatdiary.model.ColumnHeader;
import com.aarushsystems.goatdiary.model.RowHeader;
import com.aarushsystems.goatdiary.model.reports.ReportLiveStockModel;
import com.evrencoskun.tableview.TableView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ReportLivestockActivity extends AppCompatActivity {

    private boolean detailView = true, filterView = false;
    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private LocalDatabase db;
    private TableView tableView;
    private ReportsAdapter reportsAdapter;
    private DialogDateUtil dialogDateUtil;
    private ArrayList<ReportLiveStockModel> detailArrayList;
    private ArrayList<ReportLiveStockModel> summaryArrayList;
    private ArrayList<ReportLiveStockModel> rawArrayList;
    private String fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_livestock);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportsActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        loadDetailView("", "");
        loadAdapter();
    }

    private void loadAdapter() {
        reportsAdapter = new ReportsAdapter(ReportLivestockActivity.this, "livestock");
        tableView.setAdapter(reportsAdapter);
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        tableView.setTableViewListener(new ReportsListener());
    }

    private void declarations() {
        tableView = findViewById(R.id.table_view_ReportsActivity);
        db = new LocalDatabase(ReportLivestockActivity.this);
        dialogDateUtil = new DialogDateUtil(ReportLivestockActivity.this);
        detailArrayList = new ArrayList<>();
        summaryArrayList = new ArrayList<>();
        rawArrayList = new ArrayList<>();
    }

    private void loadDetailView(String fromDate, String toDate) {
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            Log.i("CUSTOM", "searching records in this period = " + fromDate + " - " + toDate);
            rawArrayList = db.getReportLiveStock(fromDate, toDate);
        }
        if (rawArrayList.isEmpty() || (fromDate.isEmpty() && toDate.isEmpty())) {
            Log.i("CUSTOM", "loading all records available");
            rawArrayList = db.getReportLiveStock("", "");
            filterView = false;
        }
        if (rawArrayList.isEmpty()) {
            tableView.setVisibility(View.GONE);
            if (filterView) {
                dialogDateUtil.showMessage("No Record Found\nFrom : " + fromDate + " To : " + toDate);
            } else {
                dialogDateUtil.showMessage("No Record Found.");
            }
        } else {
            detailArrayList.clear();
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Animal Type"));
            columnHeaderList.add(new ColumnHeader("Breed"));
            columnHeaderList.add(new ColumnHeader("Action"));
            columnHeaderList.add(new ColumnHeader("Male"));
            columnHeaderList.add(new ColumnHeader("Female"));
            columnHeaderList.add(new ColumnHeader("Total"));
            int subMale = 0, subFemale = 0;
            int rowCount = 0, grandMale = 0, grandFemale = 0;
            int insertCount = 0;
            int count = 0;
            //detailArrayList = new ArrayList<>();
            if (detailArrayList.isEmpty()) {
                /**
                 *
                 * This for loop
                 * works like many to many relationship
                 *
                 * **/
                for (ReportLiveStockModel model : rawArrayList) {
                    Log.i("CUSTOM", "first arraylist");
                    int maleCount = 0, femaleCount = 0;
                    int maleCountR = 0, femaleCountR = 0;
                    /**
                     *
                     * This for loop
                     * calculates male and female count
                     *
                     * **/
                    for (ReportLiveStockModel rm : rawArrayList) {
                        Log.i("CUSTOM", "second arraylist");
                        if (model.getAnimalType().equals(rm.getAnimalType())) {
                            Log.i("CUSTOM", "second arraylist - equal animal_type");
                            if (model.getBreed().equals(rm.getBreed())) {
                                Log.i("CUSTOM", "second arraylist - equal breed");
                                if (!model.getAction().equals("SELECT")) {
                                    if (model.getAquisation().equals(rm.getAquisation())) {
                                        Log.i("CUSTOM", "second arraylist - equal aquisation");
                                        if (rm.getGender().equals("M")) {
                                            maleCount++;
                                            Log.i("CUSTOM", "second arraylist - gender male count = " + maleCount);
                                        } else {
                                            Log.i("CUSTOM", "second arraylist - gender female count = " + femaleCount);
                                            femaleCount++;
                                        }
                                    }
                                    if (model.getRelease().equals(rm.getRelease())) {
                                        Log.i("CUSTOM", "second arraylist - equal aquisation");
                                        if (rm.getGender().equals("M")) {
                                            maleCountR--;
                                            Log.i("CUSTOM", "second arraylist - gender male count = " + maleCount);
                                        } else {
                                            Log.i("CUSTOM", "second arraylist - gender female count = " + femaleCount);
                                            femaleCountR--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ReportLiveStockModel finalModel = new ReportLiveStockModel();
                    finalModel.setAnimalType(model.getAnimalType());
                    finalModel.setBreed(model.getBreed());
                    finalModel.setAction(model.getAction());
                    finalModel.setAquisation(model.getAquisation());
                    finalModel.setRelease(model.getRelease());
                    if (model.getAction().equals(model.getAquisation())) {
                        finalModel.setMaleCount(maleCount / 2);
                        finalModel.setFemaleCount(femaleCount / 2);
                    } else {
                        finalModel.setMaleCount(maleCountR / 2);
                        finalModel.setFemaleCount(femaleCountR / 2);
                    }
                    if (!detailArrayList.contains(finalModel)) {
                        detailArrayList.add(finalModel);
                    }
                    Log.i("CUSTOM", "first arraylist - cell added with male count = " + maleCount + " female count = " + femaleCount);
                }
            }

            Map<Integer, ReportLiveStockModel> indexHashMap = new TreeMap<>();
            /**
             *
             * This for loop
             * sub count purpose
             *
             * **/
            for (ReportLiveStockModel rm : detailArrayList) {
                Log.i("CUSTOM", "---------------------------------------------");
                subMale = 0;
                subFemale = 0;
                insertCount = 0;
                Log.i("CUSTOM", "- " + rm.getBreed() + " - " + rm.getAnimalType());
                if (!rm.getAction().equals("Total")) {
                    for (int j = 0; j < detailArrayList.size(); j++) {
                        ReportLiveStockModel inner = detailArrayList.get(j);
                        //Log.i("CUSTOM","- "+inner.getGender());
                        if (rm.getAnimalType().equals(inner.getAnimalType()) &&
                                rm.getBreed().equals(inner.getBreed()) &&
                                !inner.getAction().equals("Total")) {
                            subMale += inner.getMaleCount();
                            subFemale += inner.getFemaleCount();
                            Log.i("CUSTOM", "index = " + j);
                            Log.i("CUSTOM", "inner :: male =" + inner.getMaleCount() + " female = " + inner.getFemaleCount());
                            Log.i("CUSTOM", "curnt :: male =" + subMale + " female = " + subFemale);
                            insertCount = j + 1;
                        }
                    }
                    Log.i("CUSTOM", "action = " + rm.getAction() + " submale = " + subMale + " subfemale = " + subFemale);
                    ReportLiveStockModel finalModel = new ReportLiveStockModel();
                    finalModel.setAnimalType(rm.getAnimalType());
                    finalModel.setBreed(rm.getBreed());
                    finalModel.setAction("Total");
                    finalModel.setMaleCount(subMale);
                    finalModel.setFemaleCount(subFemale);
                    if (!indexHashMap.containsValue(finalModel)) {
                        indexHashMap.put(insertCount + indexHashMap.size(), finalModel);
                        Log.i("CUSTOM", "indexArrayList after size = " + indexHashMap.size() + " inserted = " + (insertCount + indexHashMap.size() - 1));
                        Log.i("CUSTOM", "indexArrayList before size = " + indexHashMap.size());
                    }
                }
            }
            summaryArrayList.clear();
            Log.i("CUSTOM", "detail array list() ");
            count = 0;
            for (ReportLiveStockModel model : detailArrayList) {
                Log.i("CUSTOM", "Record " + (++count) +
                        " animal type = " + model.getAnimalType() +
                        " breed = " + model.getBreed() +
                        " action = " + model.getAction() +
                        " aquisation = " + model.getAquisation() +
                        " release = " + model.getRelease() +
                        " gender = " + model.getGender() +
                        " male count = " + model.getMaleCount() +
                        " female count = " + model.getFemaleCount() +
                        " total = " + model.getMaleCount() + model.getFemaleCount());
            }
            Log.i("CUSTOM", "indexHashMap() - detail size = " + detailArrayList.size());
            count = 0;
            for (Integer i : indexHashMap.keySet()) {
                Log.i("CUSTOM", "indexHashMap i = " + i);
                ReportLiveStockModel model = indexHashMap.get(i);
                Log.i("CUSTOM", "Record " + (++count) +
                        " animal type = " + model.getAnimalType() +
                        " breed = " + model.getBreed() +
                        " action = " + model.getAction() +
                        " aquisation = " + model.getAquisation() +
                        " release = " + model.getRelease() +
                        " gender = " + model.getGender() +
                        " male count = " + model.getMaleCount() +
                        " female count = " + model.getFemaleCount() +
                        " total = " + model.getMaleCount() + model.getFemaleCount());
            }
            Map<Integer, ReportLiveStockModel> map = new TreeMap<>(indexHashMap);
            for (Integer i : map.keySet()) {
                Log.i("CUSTOM", "index = " + i);
                detailArrayList.add(i, map.get(i));
                summaryArrayList.add(map.get(i));
            }
            Log.i("CUSTOM", "DETAIL ARRAY LIST");
            for (ReportLiveStockModel model : detailArrayList) {
                Log.i("CUSTOM", "Record " + (++count) +
                        " animal type = " + model.getAnimalType() +
                        " breed = " + model.getBreed() +
                        " action = " + model.getAction() +
                        " aquisation = " + model.getAquisation() +
                        " release = " + model.getRelease() +
                        " gender = " + model.getGender() +
                        " male count = " + model.getMaleCount() +
                        " female count = " + model.getFemaleCount() +
                        " total = " + model.getMaleCount() + model.getFemaleCount());
                List<Cell> cell = new ArrayList<>();
                if (model.getAction().equals("Total")) {
                    rowHeaderList.add(new RowHeader(""));
                    cell.add(new Cell(""));
                    cell.add(new Cell(""));
                } else {
                    rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                    cell.add(new Cell(model.getAnimalType()));
                    cell.add(new Cell(model.getBreed()));
                    grandMale += model.getMaleCount();
                    grandFemale += model.getFemaleCount();
                }
                //Log.i("CUSTOM", "ro count = " + rowCount);
                cell.add(new Cell(model.getAction()));
                cell.add(new Cell(String.valueOf(model.getMaleCount())));
                cell.add(new Cell(String.valueOf(model.getFemaleCount())));
                cell.add(new Cell(String.valueOf(model.getMaleCount() + model.getFemaleCount())));
                cellDataList.add(cell);
            }
            List<Cell> cell = new ArrayList<>();
            rowHeaderList.add(new RowHeader(""));
            cell.add(new Cell(""));
            cell.add(new Cell(""));
            cell.add(new Cell("Grand Total"));
            cell.add(new Cell(String.valueOf(grandMale)));
            cell.add(new Cell(String.valueOf(grandFemale)));
            cell.add(new Cell(String.valueOf(grandMale + grandFemale)));
            cellDataList.add(cell);
            ReportLiveStockModel rm = new ReportLiveStockModel();
            rm.setAnimalType("");
            rm.setBreed("");
            rm.setAction("Grand Total");
            rm.setMaleCount(grandMale);
            rm.setFemaleCount(grandFemale);
            rm.setGender("");
            rm.setAquisation("");
            rm.setRelease("");
            summaryArrayList.add(rm);
            detailArrayList.add(rm);
            count = 0;
            Log.i("CUSTOM", "SUMMARY ARRAY LIST");
            for (ReportLiveStockModel model : summaryArrayList) {
                Log.i("CUSTOM", "Record " + (++count) +
                        " animal type = " + model.getAnimalType() +
                        " breed = " + model.getBreed() +
                        " action = " + model.getAction() +
                        " aquisation = " + model.getAquisation() +
                        " release = " + model.getRelease() +
                        " gender = " + model.getGender() +
                        " male count = " + model.getMaleCount() +
                        " female count = " + model.getFemaleCount() +
                        " total = " + model.getMaleCount() + model.getFemaleCount());
            }
        }
        detailView = true;
        loadAdapter();
    }

    private void loadSummaryView(String fromDate, String toDate) {
        if (!fromDate.isEmpty() && !toDate.isEmpty() && filterView) {
            Log.i("CUSTOM", "searching records in this period = " + fromDate + " - " + toDate);
            //TODO :: new function db.()
            loadDetailView(fromDate, toDate);
        } else if (fromDate.isEmpty() && toDate.isEmpty() && filterView) {
            loadDetailView("", "");
        }
        detailView = false;
        if (summaryArrayList.isEmpty()) {
            tableView.setVisibility(View.GONE);
            if (filterView) {
                dialogDateUtil.showMessage("No Record Found\nFrom : " + fromDate + " To : " + toDate);
            } else {
                dialogDateUtil.showMessage("No Record Found.");
            }
        } else {
            //if (!filterView) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Animal Type"));
            columnHeaderList.add(new ColumnHeader("Breed"));
            columnHeaderList.add(new ColumnHeader("Action"));
            columnHeaderList.add(new ColumnHeader("Male"));
            columnHeaderList.add(new ColumnHeader("Female"));
            columnHeaderList.add(new ColumnHeader("Total"));
            int rowCount = 0;
            for (ReportLiveStockModel model : summaryArrayList) {
                Log.i("CUSTOM", "Record " + (rowCount + 1) +
                        " animal type = " + model.getAnimalType() +
                        " breed = " + model.getBreed() +
                        " action = " + model.getAction() +
                        " aquisation = " + model.getAquisation() +
                        " release = " + model.getRelease() +
                        " gender = " + model.getGender() +
                        " male count = " + model.getMaleCount() +
                        " female count = " + model.getFemaleCount() +
                        " total = " + model.getMaleCount() + model.getFemaleCount());
                List<Cell> cell = new ArrayList<>();
                if (!model.getAction().equals("Grand Total")) {
                    rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                    cell.add(new Cell(model.getAnimalType()));
                    cell.add(new Cell(model.getBreed()));
                } else {
                    rowHeaderList.add(new RowHeader(""));
                    cell.add(new Cell(""));
                    cell.add(new Cell(""));
                }
                cell.add(new Cell(model.getAction()));
                cell.add(new Cell(String.valueOf(model.getMaleCount())));
                cell.add(new Cell(String.valueOf(model.getFemaleCount())));
                cell.add(new Cell(String.valueOf(model.getMaleCount() + model.getFemaleCount())));
                cellDataList.add(cell);
            }
            //}
        }
        detailView = false;
        loadAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(ReportLivestockActivity.this, AboutUsActivity.class)
                    .putExtra("from", "report_livestock_activity"));
            finish();
        }
        if (id == R.id.action_filter) {
            openFilterDialog();
        }
        if (id == R.id.action_summary_view) {
            Log.i("CUSTOM", "option menu clicked (summary) - detailView = " + detailView + " filterView = " + filterView);
            if (detailView || filterView) {
                loadSummaryView("", "");
                filterView = false;
            }
        }
        if (id == R.id.action_detail_view) {
            Log.i("CUSTOM", "option menu clicked (detail) - detailView = " + detailView + " filterView = " + filterView);
            if (!detailView || filterView) {
                loadDetailView("", "");
                filterView = false;
            }
        }
        if (id == R.id.action_export) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            String rationale = "Please provide Storage Permission to save all Reports on your device.\nThank You!";
            Permissions.Options options = new Permissions.Options()
                    .setRationaleDialogTitle("Info")
                    .setSettingsDialogTitle("Warning");

            Permissions.check(ReportLivestockActivity.this/*context*/, permissions, rationale, options, new PermissionHandler() {
                @Override
                public void onGranted() {
                    // do your task.
                    String appDir = Environment.getExternalStorageDirectory().toString() + "/Goat Diary";
                    File file = new File(appDir);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            Log.i("CUSTOM", "folder created successfully");
                            if (detailView) {
                                exportToCsv("detail");
                            } else {
                                exportToCsv("summary");
                            }
                        } else {
                            Log.i("CUSTOM", "failed");
                        }
                    } else {
                        Log.i("CUSTOM", "exists");
                        if (detailView) {
                            exportToCsv("detail");
                        } else {
                            exportToCsv("summary");
                        }
                    }
                }

                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                    DialogDateUtil dialogDateUtil = new DialogDateUtil(ReportLivestockActivity.this);
                    dialogDateUtil.showMessage("Reports cannot be generated.\nStorage Access needed.");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFilterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ReportLivestockActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = layoutInflater.inflate(R.layout.layout_filter_date, null);
        builder.setView(dialogView);

        final EditText fddED = dialogView.findViewById(R.id.et1_fdate_layout_filter);
        final EditText fmmED = dialogView.findViewById(R.id.et2_fdate_layout_filter);
        final EditText fyyyyED = dialogView.findViewById(R.id.et3_fdate_layout_filter);
        final EditText tddED = dialogView.findViewById(R.id.et1_tdate_layout_filter);
        final EditText tmmED = dialogView.findViewById(R.id.et2_tdate_layout_filter);
        final EditText tyyyyED = dialogView.findViewById(R.id.et3_tdate_layout_filter);
        final RadioGroup viewRG = dialogView.findViewById(R.id.radioGroup_report_type_layout_filter);
        final RadioButton detailRB = dialogView.findViewById(R.id.radio_detail_layout_filter);
        final RadioButton summaryRB = dialogView.findViewById(R.id.radio_summary_layout_filter);
        fddED.addTextChangedListener(new TextWatcher() {
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
                    fmmED.requestFocus();
                    fmmED.setCursorVisible(true);
                }
            }
        });

        fmmED.addTextChangedListener(new TextWatcher() {
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
                    fyyyyED.requestFocus();
                    fyyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    fddED.requestFocus();
                    fddED.setCursorVisible(true);
                }
            }
        });

        fyyyyED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 4) {
                    //et2.clearFocus();
                    tddED.requestFocus();
                    tddED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et3.clearFocus();
                    fmmED.requestFocus();
                    fmmED.setCursorVisible(true);
                }
            }
        });

        tddED.addTextChangedListener(new TextWatcher() {
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
                    tmmED.requestFocus();
                    tmmED.setCursorVisible(true);
                }
            }
        });

        tmmED.addTextChangedListener(new TextWatcher() {
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
                    tyyyyED.requestFocus();
                    tyyyyED.setCursorVisible(true);
                } else if (editable.toString().length() == 0) {
                    //et2.clearFocus();
                    tddED.requestFocus();
                    tddED.setCursorVisible(true);
                }
            }
        });

        tyyyyED.addTextChangedListener(new TextWatcher() {
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
                    tmmED.requestFocus();
                    tmmED.setCursorVisible(true);
                } else if (editable.toString().length() == 4) {
                    viewRG.requestFocus();
                }
            }
        });

        builder.setCancelable(false)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        builder.setTitle(R.string.filter);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ddF = fddED.getText().toString().trim();
                String mmF = fmmED.getText().toString().trim();
                String yyyyF = fyyyyED.getText().toString().trim();
                String ddT = tddED.getText().toString().trim();
                String mmT = tmmED.getText().toString().trim();
                String yyyyT = tyyyyED.getText().toString().trim();
                if (ddF.isEmpty() ||
                        mmF.isEmpty() ||
                        yyyyF.isEmpty() ||
                        ddT.isEmpty() ||
                        mmT.isEmpty() ||
                        yyyyT.isEmpty()) {
                    dialogDateUtil.showMessage("Please Enter All Dates.");
                    alertDialog.show();
                } else if (yyyyF.length() != 4 ||
                        (Integer.parseInt(ddF) < 1 || Integer.parseInt(ddF) > 31) ||
                        (Integer.parseInt(mmF) < 1 || Integer.parseInt(mmF) > 12) ||
                        yyyyT.length() != 4 ||
                        (Integer.parseInt(ddT) < 1 || Integer.parseInt(ddT) > 31) ||
                        (Integer.parseInt(mmT) < 1 || Integer.parseInt(mmT) > 12)) {
                    dialogDateUtil.showMessage("Invalid Date!");
                    alertDialog.show();
                } else {
                    fromDate = ddF + "/" + mmF + "/" + yyyyF;
                    toDate = ddT + "/" + mmT + "/" + yyyyT;
                    filterView = true;
                    if (viewRG.getCheckedRadioButtonId() == detailRB.getId()) {
                        detailView = true;
                        loadDetailView(fromDate, toDate);
                    } else {
                        detailView = false;
                        loadSummaryView(fromDate, toDate);
                    }
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void exportToCsv(String type) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Goat Diary/Livestock Report.csv");
        FileWriter fileWriter = null;
        CSVWriter csvWriter = null;
        try {
            fileWriter = new FileWriter(file);
            csvWriter = new CSVWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collection<String[]> data = new ArrayList<>();
        data.add(new String[]{"Sr no.", "Animal Type", "Breed", "Action", "Male", "Female", "Total"});
        int srno = 1;
        if (type.equals("detail")) {
            for (ReportLiveStockModel rm : detailArrayList) {
                data.add(new String[]{
                        String.valueOf(srno),
                        rm.getAnimalType(),
                        rm.getBreed(),
                        rm.getAction(),
                        String.valueOf(rm.getMaleCount()),
                        String.valueOf(rm.getFemaleCount()),
                        String.valueOf(rm.getMaleCount() + rm.getFemaleCount())});
                Log.i("CUSTOM", srno + "," +
                        rm.getAnimalType() + "," +
                        rm.getBreed() + "," +
                        rm.getAction() + "," +
                        rm.getMaleCount() + "," +
                        rm.getFemaleCount() + "," +
                        (rm.getMaleCount() + rm.getFemaleCount()));
                srno++;
            }
        } else if (type.equals("summary")) {
            for (ReportLiveStockModel rm : summaryArrayList) {
                data.add(new String[]{
                        String.valueOf(srno),
                        rm.getAnimalType(),
                        rm.getBreed(),
                        rm.getAction(),
                        String.valueOf(rm.getMaleCount()),
                        String.valueOf(rm.getFemaleCount()),
                        String.valueOf(rm.getMaleCount() + rm.getFemaleCount())});
                Log.i("CUSTOM", srno + "," +
                        rm.getAnimalType() + "," +
                        rm.getBreed() + "," +
                        rm.getAction() + "," +
                        rm.getMaleCount() + "," +
                        rm.getFemaleCount() + "," +
                        (rm.getMaleCount() + rm.getFemaleCount()));
                srno++;
            }
        }
        try {
            Objects.requireNonNull(csvWriter).writeAll(data);
            dialogDateUtil.showMessage("Report Saved.\nLocation : " + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("CUSTOM", "error = " + e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (csvWriter != null) {
                    csvWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.aarushsystems.goatdiary.activity.reports;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import com.aarushsystems.goatdiary.model.reports.ReportWeightModel;
import com.evrencoskun.tableview.TableView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ReportWeightActivity extends AppCompatActivity {

    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private LocalDatabase db;
    private TableView tableView;
    private ReportsAdapter reportsAdapter;
    private DialogDateUtil dialogDateUtil;
    private ArrayList<ReportWeightModel> detailArrayList;
    private ArrayList<ReportWeightModel> rawArrayList;
    private Spinner animalTypeSpinner, breedSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_weight);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportWeightActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
        //loadDetailView();
        //loadAdapter();
    }

    private void listeners() {
        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    breedSpinner.setEnabled(true);
                    ArrayList<String> al = db.getDataForBreedTable(position);
                    ArrayAdapter<String> aa = new ArrayAdapter<>(
                            ReportWeightActivity.this,
                            R.layout.layout_text_view_black,
                            al
                    );
                    al.add(0, "SELECT");
                    breedSpinner.setAdapter(aa);
                } else {
                    tableView.setVisibility(View.GONE);
                    breedSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    tableView.setVisibility(View.VISIBLE);
                    //loadAdapter();
                    loadDetailView(animalTypeSpinner.getSelectedItemPosition(), position);
                } else {
                    tableView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadDetailView(int animalType, int breed) {
        if (rawArrayList.isEmpty()) {
            Log.i("CUSTOM", "RAW ARRAY LIST");
            Log.i("CUSTOM","ANIMAL TYPE = "+animalType+" BREED = "+breed);
            rawArrayList = db.getReportWeight(animalType, breed);
        }
        if (rawArrayList.isEmpty()) {
            tableView.setVisibility(View.GONE);
            dialogDateUtil.showMessage("No Record Found.");
            detailArrayList.clear();
        } else {
            detailArrayList.clear();
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.tag_id)));
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.date)));
            columnHeaderList.add(new ColumnHeader("1st\nWeight"));
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.date)));
            columnHeaderList.add(new ColumnHeader("Last\nWeight"));
            columnHeaderList.add(new ColumnHeader("%\nChange"));
            columnHeaderList.add(new ColumnHeader("Weight Gain\nPer Day"));

            int count = 0;
            float totalWeight = 0.0f;
            int index = -1;
            ArrayList<Integer> keepIndexArrayList = new ArrayList<>();
            Collections.sort(rawArrayList);
            for (ReportWeightModel rm : rawArrayList) {
                if (!rm.getLastDate().equals("NA")) {
                    totalWeight = rm.getFirstWeight();
                    for (ReportWeightModel inner : rawArrayList) {
                        try {
                            if (!inner.getLastDate().equals("NA")) {
                                if (rm.getTagId() == inner.getTagId()) {
                                    if (!dialogDateUtil.isProposedDateBeforeDate(inner.getDate(), rm.getDate())) {
                                        Log.i("CUSTOM", "inner date is after rm date - " + inner.getDate() + " - " + rm.getDate());
                                        inner.setLastDate(inner.getDate());
                                        inner.setLastWeight(inner.getWeight());
                                        if (!detailArrayList.contains(inner)) {
                                            totalWeight += inner.getWeight();
                                            inner.setTotalWeight(totalWeight);
                                            detailArrayList.add(inner);
                                            index = detailArrayList.indexOf(inner);
                                        }
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    keepIndexArrayList.add(index);
                } else {
                    detailArrayList.add(rm);
                    keepIndexArrayList.add(detailArrayList.indexOf(rm));
                    Log.i("CUSTOM", "add added");
                }
            }
            Log.i("CUSTOM", "HASH MAP");
            Collections.sort(keepIndexArrayList, Collections.<Integer>reverseOrder());
            Log.i("CUSTOM", "DETAIL ARRAY LIST BEOFRE size = " + detailArrayList.size());
            for (ReportWeightModel rm : detailArrayList) {
                Log.i("CUSTOM", "BEFORE Record " + (++count) +
                        " animal type = " + animalType +
                        " breed = " + breed +
                        " tag_id = " + rm.getTagId() +
                        " first_date = " + rm.getFirstDate() +
                        " first_weight = " + rm.getFirstWeight() +
                        " date = " + rm.getDate() +
                        " weight = " + rm.getWeight() +
                        " last_date = " + rm.getLastDate() +
                        " last_weight = " + rm.getLastWeight() +
                        " total_weight = " + rm.getTotalWeight()
                );
            }
            for (int i = (detailArrayList.size() - 1); i >= 0; i--) {
                if (!keepIndexArrayList.contains(i)) {
                    detailArrayList.remove(i);
                }
            }

            count = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date start;
            Date end;
            Log.i("CUSTOM", "DETAIL ARRAY LIST AFTER size = " + detailArrayList.size());
            DecimalFormat df = new DecimalFormat("00.00");
            float totalLastWeight = 0.0f, totalFirstWeight = 0.0f;
            for (ReportWeightModel rm : detailArrayList) {
                Log.i("CUSTOM", "BEFORE Record " + (count) +
                        " animal type = " + animalType +
                        " breed = " + breed +
                        " tag_id = " + rm.getTagId() +
                        " first_date = " + rm.getFirstDate() +
                        " first_weight = " + rm.getFirstWeight() +
                        " date = " + rm.getDate() +
                        " weight = " + rm.getWeight() +
                        " last_date = " + rm.getLastDate() +
                        " last_weight = " + rm.getLastWeight() +
                        " total_weight = " + rm.getTotalWeight()
                );
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++count)));
                totalFirstWeight += rm.getFirstWeight();
                if (rm.getLastDate().equals("NA")) {
                    cell.add(new Cell(String.valueOf(rm.getTagId())));
                    cell.add(new Cell(rm.getFirstDate()));
                    cell.add(new Cell(String.valueOf(rm.getFirstWeight())));
                    cell.add(new Cell("NA"));
                    cell.add(new Cell("NA"));
                    cell.add(new Cell("NA"));
                    cell.add(new Cell("NA"));
                } else {
                    totalLastWeight += rm.getLastWeight();
                    cell.add(new Cell(String.valueOf(rm.getTagId())));
                    cell.add(new Cell(rm.getFirstDate()));
                    cell.add(new Cell(String.valueOf(rm.getFirstWeight())));
                    cell.add(new Cell(rm.getLastDate()));
                    cell.add(new Cell(String.valueOf(rm.getLastWeight())));
                    try {
                        start = simpleDateFormat.parse(rm.getFirstDate());
                        end = simpleDateFormat.parse(rm.getLastDate());
                        long difference = end.getTime() - start.getTime();
                        float daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
                        /**CLIENT CHANGE : 25/11/2020**/
                        // float perday = rm.getTotalWeight() / daysBetween;
                        float perday = (rm.getLastWeight() - rm.getFirstWeight())/daysBetween;
                        float perchange = (((rm.getLastWeight() - rm.getFirstWeight()) / rm.getFirstWeight()) * 100);
                        if (perchange < 0) {
                            rm.setWeightGainPerDay(perday * -1);
                        } else {
                            rm.setWeightGainPerDay(perday);
                        }
                        rm.setPerChange(perchange);
                        Log.i("CUSTOM", "---------------diff = " + difference + " daysbetween = " + daysBetween +
                                " perday = " + perday + " perchange = " + perchange+ " total weight = "+rm.getTotalWeight());
                        if (difference != 0) {
                            cell.add(new Cell(df.format(rm.getPerChange())));
                            cell.add(new Cell(df.format(rm.getWeightGainPerDay())));
                        } else {
                            rm.setWeightGainPerDay(0);
                            rm.setPerChange(0);
                            cell.add(new Cell("0"));
                            cell.add(new Cell("0"));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                cellDataList.add(cell);
            }
            List<Cell> cell = new ArrayList<>();
            rowHeaderList.add(new RowHeader(""));
            cell.add(new Cell(""));
            cell.add(new Cell("Total"));
            cell.add(new Cell(String.valueOf(totalFirstWeight)));
            cell.add(new Cell(""));
            cell.add(new Cell(String.valueOf(totalLastWeight)));
            cell.add(new Cell(""));
            cell.add(new Cell(""));
            cellDataList.add(cell);
            ReportWeightModel rm = new ReportWeightModel();
            rm.setTagId(0);
            rm.setFirstDate("Total");
            rm.setFirstWeight(totalFirstWeight);
            rm.setLastWeight(totalLastWeight);
            detailArrayList.add(rm);
            cell = new ArrayList<>();
            rowHeaderList.add(new RowHeader(""));
            cell.add(new Cell(""));
            cell.add(new Cell("Grand Total"));
            cell.add(new Cell(""));
            cell.add(new Cell(""));
            cell.add(new Cell(String.valueOf(totalLastWeight - totalFirstWeight)));
            cell.add(new Cell(""));
            cell.add(new Cell(""));
            cellDataList.add(cell);
            rm = new ReportWeightModel();
            rm.setTagId(-1);
            rm.setFirstDate("Grand Total");
            rm.setLastWeight(totalLastWeight - totalFirstWeight);
            detailArrayList.add(rm);
            rawArrayList.clear();
            Log.i("CUSTOM", ".\n\n\n\n\n.");
        }
        loadAdapter();
    }

    private void declarations() {
        tableView = findViewById(R.id.table_view_ReportWeightActivity);
        animalTypeSpinner = findViewById(R.id.spinner_animal_type_ReportWeightActivity);
        breedSpinner = findViewById(R.id.spinner_breed_ReportWeightActivity);
        db = new LocalDatabase(ReportWeightActivity.this);
        dialogDateUtil = new DialogDateUtil(ReportWeightActivity.this);
        detailArrayList = new ArrayList<>();
        rawArrayList = new ArrayList<>();

        ArrayList<String> animalType = db.getDataForMastersTable(LocalDatabase.TABLE_ANIMAL_TYPE);
        ArrayAdapter<String> animalTypeAdapter = new ArrayAdapter<String>(ReportWeightActivity.this,
                R.layout.layout_text_view_black,
                animalType);
        animalTypeSpinner.setAdapter(animalTypeAdapter);
    }

    private void loadAdapter() {
        reportsAdapter = new ReportsAdapter(ReportWeightActivity.this, "weight");
        tableView.setAdapter(reportsAdapter);
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        tableView.setTableViewListener(new ReportsListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_weight_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(ReportWeightActivity.this, AboutUsActivity.class)
                    .putExtra("from", "reports_weight_activity"));
            finish();
        }
        if (id == R.id.action_export) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            String rationale = "Please provide Storage Permission to save all Reports on your device.\nThank You!";
            Permissions.Options options = new Permissions.Options()
                    .setRationaleDialogTitle("Info")
                    .setSettingsDialogTitle("Warning");

            Permissions.check(ReportWeightActivity.this/*context*/, permissions, rationale, options, new PermissionHandler() {
                @Override
                public void onGranted() {
                    // do your task.
                    String appDir = Environment.getExternalStorageDirectory().toString() + "/Goat Diary";
                    File file = new File(appDir);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            Log.i("CUSTOM", "folder created successfully");

                            if (animalTypeSpinner.getSelectedItemPosition() != 0 && breedSpinner.getSelectedItemPosition() != 0) {
                                exportToCsv();
                            } else if (animalTypeSpinner.getSelectedItemPosition() == 0) {
                                dialogDateUtil.showMessage("Please Select Animal Type.");
                            } else if (breedSpinner.getSelectedItemPosition() == 0) {
                                dialogDateUtil.showMessage("Please Select Breed.");
                            }
                        } else {
                            Log.i("CUSTOM", "failed");
                        }
                    } else {
                        Log.i("CUSTOM", "exists");
                        if (animalTypeSpinner.getSelectedItemPosition() != 0 && breedSpinner.getSelectedItemPosition() != 0) {
                            exportToCsv();
                        } else if (animalTypeSpinner.getSelectedItemPosition() == 0) {
                            dialogDateUtil.showMessage("Please Select Animal Type.");
                        } else if (breedSpinner.getSelectedItemPosition() == 0) {
                            dialogDateUtil.showMessage("Please Select Breed.");
                        }
                    }
                }

                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                    DialogDateUtil dialogDateUtil = new DialogDateUtil(ReportWeightActivity.this);
                    dialogDateUtil.showMessage("Reports cannot be generated.\nStorage Access needed.");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportToCsv() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Goat Diary/Weights Report.csv");
        FileWriter fileWriter = null;
        CSVWriter csvWriter = null;
        try {
            fileWriter = new FileWriter(file);
            csvWriter = new CSVWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collection<String[]> data = new ArrayList<>();
        data.add(new String[]{"Sr no.", "Tag Id", "Date", "1st Weight", "Date", "Last Weight", "% Change", "Weight Gain Per Day"});
        int srno = 1;

        for (ReportWeightModel rm : detailArrayList) {
            if (rm.getTagId() > 0) {
                data.add(new String[]{
                        String.valueOf(srno),
                        String.valueOf(rm.getTagId()),
                        rm.getFirstDate(),
                        String.valueOf(rm.getFirstWeight()),
                        rm.getLastDate(),
                        String.valueOf(rm.getLastWeight()),
                        String.valueOf(rm.getPerChange()),
                        String.valueOf(rm.getWeightGainPerDay())});
                Log.i("CUSTOM", String.valueOf(srno) + "," +
                        String.valueOf(rm.getTagId()) + "," +
                        rm.getFirstDate() + "," +
                        String.valueOf(rm.getFirstWeight()) + "," +
                        rm.getLastDate() + "," +
                        String.valueOf(rm.getLastWeight()) + "," +
                        String.valueOf(rm.getPerChange()) + "," +
                        String.valueOf(rm.getWeightGainPerDay()));
            } else if (rm.getTagId() == 0) {
                data.add(new String[]{
                        String.valueOf(srno),
                        "",
                        rm.getFirstDate(),
                        String.valueOf(rm.getFirstWeight()),
                        "",
                        String.valueOf(rm.getLastWeight()),
                        "",
                        ""});
            } else if (rm.getTagId() == -1) {
                data.add(new String[]{
                        String.valueOf(srno),
                        "",
                        rm.getFirstDate(),
                        "",
                        "",
                        String.valueOf(rm.getLastWeight()),
                        "",
                        ""});
            }
            srno++;
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

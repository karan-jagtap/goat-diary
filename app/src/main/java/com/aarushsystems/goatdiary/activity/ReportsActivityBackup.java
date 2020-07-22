/*
package com.aarushsystems.goatdiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.adapter.ReportsAdapter;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.helper.ReportsListener;
import com.aarushsystems.goatdiary.model.Cell;
import com.aarushsystems.goatdiary.model.ColumnHeader;
import com.aarushsystems.goatdiary.model.reports.ReportLiveStockModel;
import com.aarushsystems.goatdiary.model.RowHeader;
import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReportsActivityBackup extends AppCompatActivity {

    private boolean detailView = true, filterView=false;
    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private LocalDatabase db;
    private TableView tableView;
    private ReportsAdapter reportsAdapter;
    private DialogDateUtil dialogDateUtil;
    private ArrayList<ReportLiveStockModel> detailArrayList;
    private ArrayList<ReportLiveStockModel> summaryArrayList;
    ArrayList<ReportLiveStockModel> rawArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_livestock);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportsActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        loadDetailView();

        // Let's set datas of the TableView on the Adapter
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);

        tableView.setTableViewListener(new ReportsListener());
    }

    private void declarations() {
        tableView = findViewById(R.id.table_view_ReportsActivity);
        reportsAdapter = new ReportsAdapter(ReportsActivityBackup.this);
        tableView.setAdapter(reportsAdapter);
        db = new LocalDatabase(ReportsActivityBackup.this);
        dialogDateUtil = new DialogDateUtil(ReportsActivityBackup.this);
        detailArrayList = new ArrayList<>();
        summaryArrayList = new ArrayList<>();
        rawArrayList = new ArrayList<>();

    }

    private void loadDetailView() {
        if (rawArrayList.isEmpty()) {
            rawArrayList = db.getReportLiveStock();
        }
        if (rawArrayList.isEmpty()) {
            tableView.setVisibility(View.GONE);
            dialogDateUtil.showMessage("No Record Found.");
        } else {
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
            */
/**
             *
             * This for loop
             * works like many to many relationship
             *
             * **//*

            for (ReportLiveStockModel model : rawArrayList) {
                Log.i("CUSTOM", "first arraylist");
                int maleCount = 0, femaleCount = 0;
                int maleCountR = 0, femaleCountR = 0;
                */
/**
                 *
                 * This for loop
                 * calculates male and female count
                 *
                 * **//*

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

            HashMap<Integer, ReportLiveStockModel> indexArrayList = new HashMap<>();
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
                    if (!indexArrayList.containsValue(finalModel)) {
                        indexArrayList.put(insertCount + indexArrayList.size(), finalModel);
                        Log.i("CUSTOM", "indexArrayList after size = " + indexArrayList.size() + " inserted = " + (insertCount + indexArrayList.size() - 1));
                        Log.i("CUSTOM", "indexArrayList before size = " + indexArrayList.size());
                    }
                }
            }
            for (Integer i : indexArrayList.keySet()) {
                Log.i("CUSTOM", "index = " + i);
                detailArrayList.add(i, indexArrayList.get(i));
            }
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
                Log.i("CUSTOM", "ro count = " + rowCount);
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
        }
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
            startActivity(new Intent(ReportsActivityBackup.this, AboutUsActivity.class)
                    .putExtra("from", "reports_activity"));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
*/

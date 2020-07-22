package com.aarushsystems.goatdiary.activity.reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.activity.AboutUsActivity;
import com.aarushsystems.goatdiary.adapter.ReportsAdapter;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.helper.ReportsListener;
import com.aarushsystems.goatdiary.model.Cell;
import com.aarushsystems.goatdiary.model.ColumnHeader;
import com.aarushsystems.goatdiary.model.RowHeader;
import com.aarushsystems.goatdiary.model.reports.ReportExpenseModel;
import com.aarushsystems.goatdiary.model.reports.ReportIncomeModel;
import com.aarushsystems.goatdiary.model.reports.ReportLiveStockModel;
import com.aarushsystems.goatdiary.model.reports.ReportVaccinationModel;
import com.aarushsystems.goatdiary.model.reports.ReportWeightModel;
import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ReportDailyActivity extends AppCompatActivity {

    private TextView goTV, addTV, delTV, weightTV, vaccTV, feedTV, milkTV, expenseTV, incomeTV;
    private TableView addTable, delTable, weightTable, vaccTable, expenseTable, incomeTable;
    private EditText fddED, fmmED, fyyyyED;
    private ProgressDialog progressDialog;

    private DialogDateUtil dialogDateUtil;
    private String selectedDate, ddF, mmF, yyyyF;
    private Calendar calendar;
    private LocalDatabase db;

    private ArrayList<ReportLiveStockModel> addSummaryList;
    private ArrayList<ReportLiveStockModel> delSummaryList;
    private ArrayList<ReportWeightModel> weightList;
    private ArrayList<ReportVaccinationModel> vaccList;
    private ArrayList<ReportExpenseModel> expenseList;
    private ArrayList<ReportIncomeModel> incomeList;

    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private ReportsAdapter reportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_daily);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportDailyActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
    }

    private void listeners() {
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
                if (editable.toString().length() == 0) {
                    //et3.clearFocus();
                    fmmED.requestFocus();
                    fmmED.setCursorVisible(true);
                } else if (editable.toString().length() == 4) {
                    goTV.requestFocus();
                }
            }
        });

        goTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddF = fddED.getText().toString().trim();
                if (ddF.length() == 1) {
                    ddF = "0" + ddF;
                }
                mmF = fmmED.getText().toString().trim();
                if (mmF.length() == 1) {
                    mmF = "0" + mmF;
                }
                yyyyF = fyyyyED.getText().toString().trim();
                if (ddF.isEmpty() ||
                        mmF.isEmpty() ||
                        yyyyF.isEmpty()) {
                    dialogDateUtil.showMessage("Please Enter All Dates.");
                } else if (yyyyF.length() != 4 ||
                        (Integer.parseInt(ddF) < 1 || Integer.parseInt(ddF) > 31) ||
                        (Integer.parseInt(mmF) < 1 || Integer.parseInt(mmF) > 12)) {
                    dialogDateUtil.showMessage("Invalid Date!");
                } else if (dialogDateUtil.isDateInFuture(ddF + "/" + mmF + "/" + yyyyF)) {
                    dialogDateUtil.showMessage("Date must be today's date or from past.");
                } else {
                    Toast.makeText(ReportDailyActivity.this,
                            "Generating report...", Toast.LENGTH_SHORT).show();
                    selectedDate = ddF + "/" + mmF + "/" + yyyyF;
                    loadData();
                }
            }
        });
    }

    private void loadData() {
        progressDialog.show();
        progressDialog.setTitle("Loading...");
        loadAddSummaryTable();
        loadDeleteSummaryTable();
        loadWeightsTable();
        loadVaccinationTable();
        loadFeedStockAndMilkData();
        loadExpenseTable();
        loadIncomeTable();
        progressDialog.dismiss();
    }

    /**
     * ADD SUMMARY TABLE
     **/
    private void loadAddSummaryTable() {
        addSummaryList = db.getReportDailyAddSummary(selectedDate);
        int totalQty = addSummaryList.size();
        if (!addSummaryList.isEmpty()) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Animal Type"));
            columnHeaderList.add(new ColumnHeader("Breed"));
            columnHeaderList.add(new ColumnHeader("Action"));
            columnHeaderList.add(new ColumnHeader("Qty."));

            ArrayList<ReportLiveStockModel> finalList = new ArrayList<>();
            for (int i = 0; i < addSummaryList.size(); i++) {
                ReportLiveStockModel outer = addSummaryList.get(i);
                int qty = 1;
                for (int j = 0; j < addSummaryList.size(); j++) {
                    ReportLiveStockModel inner = addSummaryList.get(j);
                    if (i != j) {
                        if (outer.getAnimalType().equals(inner.getAnimalType())) {
                            if (outer.getBreed().equals(inner.getBreed())) {
                                if (outer.getAction().equals(inner.getAction())) {
                                    qty++;
                                }
                            }
                        }
                    }
                }
                ReportLiveStockModel finalModel = new ReportLiveStockModel();
                finalModel.setAnimalType(outer.getAnimalType());
                finalModel.setBreed(outer.getBreed());
                finalModel.setAction(outer.getAction());
                finalModel.setMaleCount(qty);
                finalModel.setFemaleCount(0);
                if (!finalList.contains(finalModel)) {
                    finalList.add(finalModel);
                }
            }
            int rowCount = 0;
            for (ReportLiveStockModel model : finalList) {
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(model.getAnimalType()));
                cell.add(new Cell(model.getBreed()));
                cell.add(new Cell(model.getAction()));
                cell.add(new Cell(String.valueOf(model.getMaleCount())));
                cellDataList.add(cell);
            }
            loadAdapter(addTable);
            addTable.setVisibility(View.VISIBLE);
        } else {
            addTable.setVisibility(View.GONE);
        }
        String title = "A) Add Summary - " + totalQty + " animals";
        addTV.setText(title);
    }

    /**
     * DELETE SUMMARY TABLE
     **/
    private void loadDeleteSummaryTable() {
        delSummaryList = db.getReportDailyDeleteSummary(selectedDate);
        int totalQty = delSummaryList.size();
        if (!delSummaryList.isEmpty()) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Animal Type"));
            columnHeaderList.add(new ColumnHeader("Breed"));
            columnHeaderList.add(new ColumnHeader("Action"));
            columnHeaderList.add(new ColumnHeader("Qty."));

            ArrayList<ReportLiveStockModel> finalList = new ArrayList<>();
            for (int i = 0; i < delSummaryList.size(); i++) {
                ReportLiveStockModel outer = delSummaryList.get(i);
                int qty = 1;
                for (int j = 0; j < delSummaryList.size(); j++) {
                    ReportLiveStockModel inner = delSummaryList.get(j);
                    if (i != j) {
                        if (outer.getAnimalType().equals(inner.getAnimalType())) {
                            if (outer.getBreed().equals(inner.getBreed())) {
                                if (outer.getAction().equals(inner.getAction())) {
                                    qty++;
                                }
                            }
                        }
                    }
                }
                ReportLiveStockModel finalModel = new ReportLiveStockModel();
                finalModel.setAnimalType(outer.getAnimalType());
                finalModel.setBreed(outer.getBreed());
                finalModel.setAction(outer.getAction());
                finalModel.setMaleCount(qty);
                finalModel.setFemaleCount(0);
                if (!finalList.contains(finalModel)) {
                    finalList.add(finalModel);
                }
            }
            int rowCount = 0;
            for (ReportLiveStockModel model : finalList) {
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(model.getAnimalType()));
                cell.add(new Cell(model.getBreed()));
                cell.add(new Cell(model.getAction()));
                cell.add(new Cell(String.valueOf(model.getMaleCount())));
                cellDataList.add(cell);
            }
            loadAdapter(delTable);
            delTable.setVisibility(View.VISIBLE);
        } else {
            delTable.setVisibility(View.GONE);
        }
        String title = "B) Delete Summary - " + totalQty + " animals";
        delTV.setText(title);
    }

    /**
     * WEIGHTS TABLE
     **/
    private void loadWeightsTable() {
        weightList = db.getReportDailyWeightDone(selectedDate);
        if (!weightList.isEmpty()) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Animal Id"));
            columnHeaderList.add(new ColumnHeader("Animal Type"));
            columnHeaderList.add(new ColumnHeader("Breed"));
            columnHeaderList.add(new ColumnHeader("Weight"));

            int rowCount = 0;
            for (ReportWeightModel model : weightList) {
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(String.valueOf(model.getTagId())));
                cell.add(new Cell(model.getAnimalType()));
                cell.add(new Cell(model.getBreed()));
                cell.add(new Cell(String.valueOf(model.getWeight())));
                cellDataList.add(cell);
            }
            loadAdapter(weightTable);
            weightTable.setVisibility(View.VISIBLE);
        } else {
            weightTable.setVisibility(View.GONE);
        }
        String title = "C) Weight Done - " + weightList.size() + " animals";
        weightTV.setText(title);
    }

    /**
     * VACCINATION TABLE
     **/
    private void loadVaccinationTable() {
        vaccList = db.getReportDailyVaccinationDone(selectedDate);
        if (!vaccList.isEmpty()) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Animal Id"));
            columnHeaderList.add(new ColumnHeader("Animal Type"));
            columnHeaderList.add(new ColumnHeader("Breed"));
            columnHeaderList.add(new ColumnHeader("Vaccine"));

            int rowCount = 0;
            for (ReportVaccinationModel model : vaccList) {
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(String.valueOf(model.getTagId())));
                cell.add(new Cell(model.getAnimalTypeString()));
                cell.add(new Cell(model.getBreedString()));
                cell.add(new Cell(model.getVaccine()));
                cellDataList.add(cell);
            }
            loadAdapter(vaccTable);
            vaccTable.setVisibility(View.VISIBLE);
        } else {
            vaccTable.setVisibility(View.GONE);
        }
        String title = "D) Vaccination Done - " + vaccList.size() + " animals";
        vaccTV.setText(title);
    }

    private void loadFeedStockAndMilkData() {
        HashMap<String, Float> hashMap = db.getReportDailyFeedStockAndMilk(selectedDate);
        feedTV.setText("E) Feed Stock :  Input - " + hashMap.get("feed_input") + " kg." + " Output - " + hashMap.get("feed_output") + " kg.");
        milkTV.setText("F) Milk :  Produced - " + hashMap.get("milk_input") + " lt." + " Sold - " + hashMap.get("milk_output") + " lt.");
    }

    /**
     * EXPENSE TABLE
     **/
    private void loadExpenseTable() {
        float totalAmount = 0;
        expenseList = db.getReportDailyExpense(selectedDate);
        if (!expenseList.isEmpty()) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Expense Head"));
            columnHeaderList.add(new ColumnHeader("Amount"));
            columnHeaderList.add(new ColumnHeader("Cash/Cheque"));
            columnHeaderList.add(new ColumnHeader("Paid To"));

            int rowCount = 0;
            for (ReportExpenseModel model : expenseList) {
                totalAmount += model.getAmount();
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(model.getHead()));
                cell.add(new Cell(String.valueOf(model.getAmount())));
                cell.add(new Cell(model.getCashCheque()));
                cell.add(new Cell(model.getPaidTo()));
                cellDataList.add(cell);
            }
            loadAdapter(expenseTable);
            expenseTable.setVisibility(View.VISIBLE);
        } else {
            expenseTable.setVisibility(View.GONE);
        }
        String title = "G) Expense Total - " + totalAmount + " Rs.";
        expenseTV.setText(title);
    }

    /**
     * INCOME TABLE
     **/
    private void loadIncomeTable() {
        float totalAmount = 0;
        incomeList = db.getReportDailyIncome(selectedDate);
        if (!incomeList.isEmpty()) {
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Income Head"));
            columnHeaderList.add(new ColumnHeader("Amount"));
            columnHeaderList.add(new ColumnHeader("Cash/Cheque"));
            columnHeaderList.add(new ColumnHeader("Received From"));
            int rowCount = 0;
            for (ReportIncomeModel model : incomeList) {
                totalAmount += model.getAmount();
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(model.getHead()));
                cell.add(new Cell(String.valueOf(model.getAmount())));
                cell.add(new Cell(model.getCashCheque()));
                cell.add(new Cell(model.getReceivedFrom()));
                cellDataList.add(cell);
            }

            loadAdapter(incomeTable);
            incomeTable.setVisibility(View.VISIBLE);
        } else {
            incomeTable.setVisibility(View.GONE);
        }
        String title = "H) Income Total - " + totalAmount + " Rs.";
        incomeTV.setText(title);
    }

    private void loadAdapter(TableView table) {
        reportsAdapter = new ReportsAdapter(ReportDailyActivity.this, "expense");
        table.setAdapter(reportsAdapter);
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        table.setTableViewListener(new ReportsListener());
    }

    private void declarations() {
        goTV = findViewById(R.id.textView_go_ReportDailyActivity);
        addTV = findViewById(R.id.textView_title_add_summary_ReportDailyActivity);
        delTV = findViewById(R.id.textView_title_delete_summary_ReportDailyActivity);
        weightTV = findViewById(R.id.textView_title_weight_done_ReportDailyActivity);
        vaccTV = findViewById(R.id.textView_title_vaccination_done_ReportDailyActivity);
        feedTV = findViewById(R.id.textView_title_feed_stock_ReportDailyActivity);
        milkTV = findViewById(R.id.textView_title_milk_produced_ReportDailyActivity);
        expenseTV = findViewById(R.id.textView_title_expense_total_ReportDailyActivity);
        incomeTV = findViewById(R.id.textView_title_income_total_ReportDailyActivity);

        addTable = findViewById(R.id.table_view_add_summary_ReportDailyActivity);
        delTable = findViewById(R.id.table_view_delete_summary_ReportDailyActivity);
        weightTable = findViewById(R.id.table_view_weight_done_ReportDailyActivity);
        vaccTable = findViewById(R.id.table_view_vaccination_done_ReportDailyActivity);
        expenseTable = findViewById(R.id.table_view_expense_total_ReportDailyActivity);
        incomeTable = findViewById(R.id.table_view_income_total_ReportDailyActivity);

        fddED = findViewById(R.id.et1_fdate_ReportDailyActivity);
        fmmED = findViewById(R.id.et2_fdate_ReportDailyActivity);
        fyyyyED = findViewById(R.id.et3_fdate_ReportDailyActivity);

        dialogDateUtil = new DialogDateUtil(ReportDailyActivity.this);
        db = new LocalDatabase(ReportDailyActivity.this);
        progressDialog = new ProgressDialog(ReportDailyActivity.this);
        calendar = Calendar.getInstance();
        ddF = String.valueOf((calendar.get(Calendar.DAY_OF_MONTH) - 1) < 10 ? "0" + (calendar.get(Calendar.DAY_OF_MONTH) - 1) : (calendar.get(Calendar.DAY_OF_MONTH) - 1));
        mmF = String.valueOf((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1));
        yyyyF = String.valueOf(calendar.get(Calendar.YEAR));
        fddED.setText(ddF);
        fmmED.setText(mmF);
        fyyyyED.setText(yyyyF);
        selectedDate = ddF + "/" + mmF + "/" + yyyyF;
        loadData();
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
            startActivity(new Intent(ReportDailyActivity.this, AboutUsActivity.class)
                    .putExtra("from", "report_daily_activity"));
            finish();
        }
        if (id == R.id.action_terms_and_cond) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.TERMS_AND_CONDITIONS));
                startActivity(browserIntent);
            } catch (Exception e) {
                Log.i("CUSTOM","e = "+e.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

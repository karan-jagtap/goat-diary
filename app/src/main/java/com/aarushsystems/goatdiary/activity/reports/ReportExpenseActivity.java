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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.aarushsystems.goatdiary.model.reports.ReportExpenseModel;
import com.evrencoskun.tableview.TableView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ReportExpenseActivity extends AppCompatActivity {

    private boolean detailView = true, filterView = false;
    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private LocalDatabase db;
    private TableView tableView;
    private ReportsAdapter reportsAdapter;
    private DialogDateUtil dialogDateUtil;
    private ArrayList<ReportExpenseModel> detailArrayList;
    private ArrayList<ReportExpenseModel> summaryArrayList;
    private ArrayList<ReportExpenseModel> rawArrayList;
    private String fromDate, toDate;
    private Spinner itemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_expense);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportExpenseActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*String abc = " .%^&() asbdjA aAZsjdja 6512j .";
        String reg = "( )+|[a-zA-Z]+";
        String a[] = abc.split(reg);
        int totallen = abc.length();
        int lenmin=0;
        for (int i = 0; i < a.length; i++) {
            lenmin += a[i].length();
        }
        totallen -= lenmin;*/

        declarations();
        listeners();
    }

    private void declarations() {
        tableView = findViewById(R.id.table_view_ReportExpenseActivity);
        itemSpinner = findViewById(R.id.spinner_item_ReportExpenseActivity);
        db = new LocalDatabase(ReportExpenseActivity.this);
        dialogDateUtil = new DialogDateUtil(ReportExpenseActivity.this);
        detailArrayList = new ArrayList<>();
        summaryArrayList = new ArrayList<>();
        rawArrayList = new ArrayList<>();

        ArrayList<String> itemType = db.getDataForMastersTable(LocalDatabase.TABLE_EXPENSE);
        ArrayAdapter itemAdapter = new ArrayAdapter<String>(ReportExpenseActivity.this,
                R.layout.layout_text_view_black,
                itemType);
        itemSpinner.setAdapter(itemAdapter);
    }

    private void listeners() {
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    tableView.setVisibility(View.VISIBLE);
                    loadDetailView("", "", position);
                } else {
                    if (summaryArrayList.isEmpty()) {
                        tableView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadDetailView(String fromDate, String toDate, int itemType) {
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            Log.i("CUSTOM", "searching records in this period = " + fromDate + " - " + toDate);
            rawArrayList = db.getReportDetailExpense(fromDate, toDate, itemSpinner.getSelectedItemPosition());
        }
        if ((fromDate.isEmpty() && toDate.isEmpty())) {
            Log.i("CUSTOM", "loading all records available");
            rawArrayList = db.getReportDetailExpense("", "", itemSpinner.getSelectedItemPosition());
            filterView = false;
        }
        if (rawArrayList.isEmpty()) {
            tableView.setVisibility(View.GONE);
            if (filterView) {
                dialogDateUtil.showMessage("No Record Found\nFrom : " + fromDate + " To : " + toDate);
                itemSpinner.setSelection(0);
            } else {
                dialogDateUtil.showMessage("No Record Found.");
            }
        } else {
            detailArrayList.clear();
            summaryArrayList.clear();
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Date"));
            columnHeaderList.add(new ColumnHeader("Cash/Cheque"));
            columnHeaderList.add(new ColumnHeader("Amount"));
            columnHeaderList.add(new ColumnHeader("Paid To"));
            int rowCount = 0;
            float totalAmount = 0;
            int count = 0;
            Collections.sort(rawArrayList);
            Log.i("CUSTOM", "DETAIL ARRAY LIST");
            for (ReportExpenseModel model : rawArrayList) {
                Log.i("CUSTOM", "Record " + (++count) +
                        " head = " + model.getItemType() +
                        " date = " + model.getDate() +
                        " amount = " + model.getAmount() +
                        " paid to = " + model.getPaidTo() +
                        " cash_cheque = " + model.getCashCheque()
                );
                totalAmount += model.getAmount();
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(model.getDate()));
                cell.add(new Cell(model.getCashCheque()));
                cell.add(new Cell(String.valueOf(model.getAmount())));
                cell.add(new Cell(model.getPaidTo()));
                cellDataList.add(cell);
            }
            List<Cell> cell = new ArrayList<>();
            rowHeaderList.add(new RowHeader(""));
            cell.add(new Cell(""));
            cell.add(new Cell("Total"));
            cell.add(new Cell(String.valueOf(totalAmount)));
            cell.add(new Cell(""));
            cellDataList.add(cell);
            ReportExpenseModel rm = new ReportExpenseModel();
            rm.setCashCheque("Total");
            rm.setAmount(totalAmount);
            rawArrayList.add(rm);
            loadAdapter();
        }
    }

    private void loadSummaryView(String fromDate, String toDate) {
        itemSpinner.setSelection(0);
        if (!fromDate.isEmpty() && !toDate.isEmpty() && filterView) {
            summaryArrayList = db.getReportSummaryExpense(fromDate, toDate);
        }/* else if (fromDate.isEmpty() && toDate.isEmpty() && filterView) {
            loadDetailView("", "", itemType);
        }*/
        if ((fromDate.isEmpty() && toDate.isEmpty())) {
            Log.i("CUSTOM", "loading all records available");
            summaryArrayList = db.getReportSummaryExpense("", "");
            filterView = false;
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
            tableView.setVisibility(View.VISIBLE);
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader("Head"));
            columnHeaderList.add(new ColumnHeader("Amount"));
            int rowCount = 0;
            float totalAmount = 0;
            int count = 0;
            Log.i("CUSTOM", "SUMMARY ARRAY LIST");
            for (ReportExpenseModel model : summaryArrayList) {
                Log.i("CUSTOM", "Record " + (++count) +
                        " head = " + model.getItemType() +
                        " amount = " + model.getAmount()
                );
                totalAmount += model.getAmount();
                List<Cell> cell = new ArrayList<>();
                rowHeaderList.add(new RowHeader(String.valueOf(++rowCount)));
                cell.add(new Cell(model.getHead()));
                cell.add(new Cell(String.valueOf(model.getAmount())));
                cellDataList.add(cell);
            }

            List<Cell> cell = new ArrayList<>();
            rowHeaderList.add(new RowHeader(""));
            cell.add(new Cell("Total"));
            cell.add(new Cell(String.valueOf(totalAmount)));
            cellDataList.add(cell);
            ReportExpenseModel rm = new ReportExpenseModel();
            rm.setHead("Total");
            rm.setAmount(totalAmount);
            summaryArrayList.add(rm);
            loadAdapter();
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
            startActivity(new Intent(ReportExpenseActivity.this, AboutUsActivity.class)
                    .putExtra("from", "report_expense_activity"));
            finish();
        }
        if (id == R.id.action_filter) {
            openFilterDialog();
        }
        if (id == R.id.action_summary_view) {
            Log.i("CUSTOM", "option menu clicked (summary) - detailView = " + detailView + " filterView = " + filterView);
            itemSpinner.setSelection(0);
            loadSummaryView("", "");
        }
        if (id == R.id.action_detail_view) {
            Log.i("CUSTOM", "option menu clicked (detail) - detailView = " + detailView + " filterView = " + filterView);
            if (!detailView || filterView) {
                if (itemSpinner.getSelectedItemPosition() != 0) {
                    loadDetailView("", "", itemSpinner.getSelectedItemPosition());
                    filterView = false;
                } else {
                    dialogDateUtil.showMessage("Please Select Item.");
                }
            }
        }
        if (id == R.id.action_export) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            String rationale = "Please provide Storage Permission to save all Reports on your device.\nThank You!";
            Permissions.Options options = new Permissions.Options()
                    .setRationaleDialogTitle("Info")
                    .setSettingsDialogTitle("Warning");

            Permissions.check(ReportExpenseActivity.this/*context*/, permissions, rationale, options, new PermissionHandler() {
                @Override
                public void onGranted() {
                    // do your task.
                    String appDir = Environment.getExternalStorageDirectory().toString() + "/Goat Diary";
                    File file = new File(appDir);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            Log.i("CUSTOM", "folder created successfully");
                            if (itemSpinner.getSelectedItemPosition() != 0) {
                                if (detailView) {
                                    exportToCsv("detail");
                                } else {
                                    exportToCsv("summary");
                                }
                            } else {
                                if (!detailView) {
                                    exportToCsv("summary");
                                } else {
                                    dialogDateUtil.showMessage("Please select Item.");
                                }
                            }
                        } else {
                            Log.i("CUSTOM", "failed");
                        }
                    } else {
                        Log.i("CUSTOM", "exists");
                        if (itemSpinner.getSelectedItemPosition() != 0) {
                            if (detailView) {
                                exportToCsv("detail");
                            } else {
                                exportToCsv("summary");
                            }
                        } else {
                            if (!detailView) {
                                exportToCsv("summary");
                            } else {
                                dialogDateUtil.showMessage("Please select Item.");
                            }
                        }
                    }
                }

                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                    DialogDateUtil dialogDateUtil = new DialogDateUtil(ReportExpenseActivity.this);
                    dialogDateUtil.showMessage("Reports cannot be generated.\nStorage Access needed.");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFilterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ReportExpenseActivity.this);
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
                        if (itemSpinner.getSelectedItemPosition() != 0) {
                            alertDialog.dismiss();
                            Toast.makeText(ReportExpenseActivity.this, "Loading Data...", Toast.LENGTH_LONG).show();
                            detailView = true;
                            loadDetailView(fromDate, toDate, itemSpinner.getSelectedItemPosition());
                        } else {
                            dialogDateUtil.showMessage("Please Select Item before you apply any Filter.");
                        }
                    } else {
                        alertDialog.dismiss();
                        detailView = false;
                        loadSummaryView(fromDate, toDate);
                    }
                }
            }
        });
    }

    private void loadAdapter() {
        reportsAdapter = new ReportsAdapter(ReportExpenseActivity.this, "expense");
        tableView.setAdapter(reportsAdapter);
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        tableView.setTableViewListener(new ReportsListener());
    }

    private void exportToCsv(String type) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Goat Diary/Expense Report.csv");
        FileWriter fileWriter = null;
        CSVWriter csvWriter = null;
        try {
            fileWriter = new FileWriter(file);
            csvWriter = new CSVWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<String[]> data = new ArrayList<>();
        int srno = 1;
        if (type.equals("detail")) {
            data.add(new String[]{"Sr no.", "Date", "Cash/Cheque", "Amount", "Paid To"});
            for (ReportExpenseModel rm : rawArrayList) {
                if (!rm.getCashCheque().equals("Total")) {
                    data.add(new String[]{
                            String.valueOf(srno),
                            rm.getDate(),
                            rm.getCashCheque(),
                            String.valueOf(rm.getAmount()),
                            rm.getPaidTo()});
                } else {
                    data.add(new String[]{
                            String.valueOf(srno),
                            "",
                            rm.getCashCheque(),
                            String.valueOf(rm.getAmount()),
                            ""});
                }
                Log.i("CUSTOM", String.valueOf(srno) + "" +
                        rm.getCashCheque() + "" +
                        String.valueOf(rm.getAmount()) + "" +
                        rm.getPaidTo());
                srno++;
            }
        } else if (type.equals("summary")) {
            data.add(new String[]{"Sr no.", "Head", "Amount"});
            for (ReportExpenseModel rm : summaryArrayList) {
                data.add(new String[]{
                        String.valueOf(srno),
                        rm.getHead(),
                        String.valueOf(rm.getAmount())});
                Log.i("CUSTOM",
                        String.valueOf(srno) + "" +
                                rm.getHead() + "" +
                                String.valueOf(rm.getAmount()));
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

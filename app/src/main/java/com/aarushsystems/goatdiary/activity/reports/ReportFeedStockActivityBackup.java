package com.aarushsystems.goatdiary.activity.reports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.aarushsystems.goatdiary.model.reports.ReportFeedStockModel;
import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ReportFeedStockActivityBackup extends AppCompatActivity {

    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private LocalDatabase db;
    private TableView tableView;
    private ReportsAdapter reportsAdapter;
    private ArrayList<ReportFeedStockModel> rawArrayList;
    private ArrayList<ReportFeedStockModel> detailArrayList;
    private Spinner itemSpinner;
    DialogDateUtil dialogDateUtil;
    private boolean filterView = false;
    private String fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_feed_stock);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportFeedStockActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
        //loadView("", "");
    }

    private void listeners() {
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    tableView.setVisibility(View.VISIBLE);
                    loadView("", "", position);
                } else {
                    tableView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void declarations() {
        tableView = findViewById(R.id.table_view_ReportFeedStockActivity);
        itemSpinner = findViewById(R.id.spinner_item_ReportFeedStockActivity);
        dialogDateUtil = new DialogDateUtil(ReportFeedStockActivityBackup.this);
        db = new LocalDatabase(ReportFeedStockActivityBackup.this);
        rawArrayList = new ArrayList<>();
        detailArrayList = new ArrayList<>();

        ArrayList<String> itemType = db.getDataForMastersTable(LocalDatabase.TABLE_FEED_TYPE);
        ArrayAdapter animalTypeAdapter = new ArrayAdapter<String>(ReportFeedStockActivityBackup.this,
                android.R.layout.simple_list_item_1,
                itemType);
        itemSpinner.setAdapter(animalTypeAdapter);
    }

    private void loadView(String fromDate, String toDate, int itemType) {
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            rawArrayList = db.getReportFeedStock(fromDate, toDate, itemType);
        } else {
            rawArrayList = db.getReportFeedStock("", "", itemType);
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
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.date)));
            columnHeaderList.add(new ColumnHeader("Qty. (kg.)"));
            columnHeaderList.add(new ColumnHeader("Total Qty."));
            int count = 0;
            float totalQuantity = 0;
            Log.i("CUSTOM", "sorting raw arraylists");
            Collections.sort(rawArrayList);
            try {
                if (rawArrayList.size() > 1) {
                    for (int i = 0; i < rawArrayList.size(); i++) {
                        ReportFeedStockModel addModel = rawArrayList.get(i);
                        for (int j = 0; j < rawArrayList.size(); j++) {
                            if (i != j) {
                                ReportFeedStockModel delModel = rawArrayList.get(j);
                                if (addModel.getDate().equals(delModel.getDate())) {
                                    if (delModel.getAction() == 2 && addModel.getAction() == 1) {
                                        Log.i("CUSTOM", addModel.getQuantity() + " - " + delModel.getQuantity());
                                        addModel.setQuantity(addModel.getQuantity() - delModel.getQuantity());
                                    }
                                }
                            }
                        }
                        if (!detailArrayList.contains(addModel)) {
                            if (addModel.getAction() == 2) {
                                addModel.setQuantity(-addModel.getQuantity());
                            }
                            detailArrayList.add(addModel);
                            Log.i("CUSTOM", "Record CAL D " +
                                    " item = " + itemType +
                                    " action = " + addModel.getAction() +
                                    " date = " + addModel.getDate() +
                                    " quantity = " + addModel.getQuantity() +
                                    " total_quantity = " + addModel.getTotalQuantity()
                            );
                        }
                    }
                } else {
                    Log.i("CUSTOM", "only 1 item");
                    ReportFeedStockModel model = rawArrayList.get(0);
                    if (model.getAction() == 2) {
                        Log.i("CUSTOM", "Shpuld be negatoive");
                        model.setQuantity(-model.getQuantity());
                    } else {
                        Log.i("CUSTOM", "Shpuld be positive6");
                    }
                    model.setTotalQuantity(model.getQuantity());
                    detailArrayList.add(model);
                }
            } catch (Exception e) {

            }
            count = 0;
            Log.i("CUSTOM", "sorting detail arraylists");
            Collections.sort(detailArrayList);
            Log.i("CUSTOM", "AFTER");
            for (ReportFeedStockModel rm : detailArrayList) {
                Log.i("CUSTOM", "Record " + (++count) +
                        " item = " + itemType +
                        " action = " + rm.getAction() +
                        " date = " + rm.getDate() +
                        " quantity = " + rm.getQuantity() +
                        " total_quantity = " + rm.getTotalQuantity()
                );
            }

            count = 0;
            for (ReportFeedStockModel rm : detailArrayList) {
                rowHeaderList.add(new RowHeader(String.valueOf(++count)));
                if (detailArrayList.indexOf(rm) == 0) {
                    rm.setTotalQuantity(rm.getQuantity());
                } else {
                    rm.setTotalQuantity(detailArrayList.get(detailArrayList.indexOf(rm) - 1).getTotalQuantity() + rm.getQuantity());
                }
                List<Cell> cell = new ArrayList<>();
                cell.add(new Cell(rm.getDate()));
                cell.add(new Cell(String.format(Locale.getDefault(), "%.3f", rm.getQuantity())));
                cell.add(new Cell(String.format(Locale.getDefault(), "%.3f", rm.getTotalQuantity())));
                cellDataList.add(cell);
            }
            rowHeaderList.add(new RowHeader(""));
            List<Cell> cell = new ArrayList<>();
            cell.add(new Cell(""));
            cell.add(new Cell("Total"));
            cell.add(new Cell(String.format(Locale.getDefault(), "%.3f", detailArrayList.get(detailArrayList.size() - 1).getTotalQuantity())));
            cellDataList.add(cell);
            loadAdapter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reports_filters, menu);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_filter) {
            openFilterDialog();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(ReportFeedStockActivityBackup.this, AboutUsActivity.class)
                    .putExtra("from", "report_feed_stock_activity"));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFilterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ReportFeedStockActivityBackup.this);
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
        viewRG.setVisibility(View.GONE);
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
                if (itemSpinner.getSelectedItemPosition() != 0) {
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
                        loadView(fromDate, toDate, itemSpinner.getSelectedItemPosition());
                        alertDialog.dismiss();
                        Toast.makeText(ReportFeedStockActivityBackup.this, "Loading Data...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    dialogDateUtil.showMessage("Please Select Item before you apply any filter.");
                }
            }
        });
    }

    private void loadAdapter() {
        reportsAdapter = new ReportsAdapter(ReportFeedStockActivityBackup.this, "feed_stock");
        tableView.setAdapter(reportsAdapter);
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        tableView.setTableViewListener(new ReportsListener());
    }

}

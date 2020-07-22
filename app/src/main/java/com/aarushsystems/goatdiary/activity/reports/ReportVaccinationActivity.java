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
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.aarushsystems.goatdiary.model.reports.ReportVaccinationModel;
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
import java.util.Objects;

public class ReportVaccinationActivity extends AppCompatActivity {

    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<List<Cell>> cellDataList;
    private LocalDatabase db;
    private TableView tableView;
    private ReportsAdapter reportsAdapter;
    private ArrayList<ReportVaccinationModel> rawArrayList;
    private Spinner animalTypeSpinner, breedSpinner;
    private TextView animalsTV, vaccinationTV;
    DialogDateUtil dialogDateUtil;
    private boolean filterView = false;
    private String fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_vaccination);
        Toolbar toolbar = findViewById(R.id.toolbar_ReportVaccinationActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        listeners();
        //loadView("", "");
    }

    private void listeners() {
        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    breedSpinner.setEnabled(true);
                    ArrayList<String> al = db.getDataForBreedTable(position);
                    ArrayAdapter<String> aa = new ArrayAdapter<>(
                            ReportVaccinationActivity.this,
                            R.layout.layout_text_view_black,
                            al
                    );
                    al.add(0, "SELECT");
                    breedSpinner.setAdapter(aa);
                } else {
                    tableView.setVisibility(View.GONE);
                    breedSpinner.setEnabled(false);
                    loadTotalView("", "", 0, 0);
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
                    loadView("", "", animalTypeSpinner.getSelectedItemPosition(), position);
                } else {
                    tableView.setVisibility(View.GONE);
                    loadTotalView("", "", 0, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void declarations() {
        tableView = findViewById(R.id.table_view_ReportVaccinationActivity);
        animalTypeSpinner = findViewById(R.id.spinner_animal_type_ReportVaccinationActivity);
        breedSpinner = findViewById(R.id.spinner_breed_ReportVaccinationActivity);
        animalsTV = findViewById(R.id.textView_total_animals_ReportVaccinationActivity);
        vaccinationTV = findViewById(R.id.textView_total_vaccinations_ReportVaccinationActivity);
        dialogDateUtil = new DialogDateUtil(ReportVaccinationActivity.this);
        db = new LocalDatabase(ReportVaccinationActivity.this);
        rawArrayList = new ArrayList<>();

        ArrayList<String> animalType = db.getDataForMastersTable(LocalDatabase.TABLE_ANIMAL_TYPE);
        ArrayAdapter animalTypeAdapter = new ArrayAdapter<String>(ReportVaccinationActivity.this,
                R.layout.layout_text_view_black,
                animalType);
        animalTypeSpinner.setAdapter(animalTypeAdapter);
    }

    private void loadView(String fromDate, String toDate, int animalType, int breed) {
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            rawArrayList = db.getReportVaccination(fromDate, toDate, animalType, breed);
        } else {
            rawArrayList = db.getReportVaccination("", "", animalType, breed);
            filterView = false;
        }
        if (rawArrayList.isEmpty()) {
            tableView.setVisibility(View.GONE);
            if (filterView) {
                dialogDateUtil.showMessage("No Record Found\nFrom : " + fromDate + " To : " + toDate);
            } else {
                dialogDateUtil.showMessage("No Record Found.");
            }
            loadTotalView(fromDate, toDate, animalType, breed);
        } else {
            loadTotalView(fromDate, toDate, animalType, breed);
            columnHeaderList = new ArrayList<>();
            rowHeaderList = new ArrayList<>();
            cellDataList = new ArrayList<>();
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.date)));
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.vaccine)));
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.tag_id)));
            columnHeaderList.add(new ColumnHeader(getResources().getString(R.string.dose)));
            int count = 0;
            for (ReportVaccinationModel rm : rawArrayList) {
                rowHeaderList.add(new RowHeader(String.valueOf(++count)));
                List<Cell> cell = new ArrayList<>();
                cell.add(new Cell(rm.getDate()));
                cell.add(new Cell(rm.getVaccine()));
                cell.add(new Cell(String.valueOf(rm.getTagId())));
                cell.add(new Cell(String.valueOf(rm.getDose())));
                cellDataList.add(cell);
            }
            loadAdapter();
        }
    }

    private void loadTotalView(String fromDate, String toDate, int animalType, int breed) {
        SparseIntArray sparseIntArray = db.getReportSubVaccination(fromDate, toDate, animalType, breed);
        animalsTV.setText("Total Number of Animals in Farm : " + sparseIntArray.get(1, 0));
        vaccinationTV.setText("Total Vaccinations Done till Date : " + sparseIntArray.get(2, 0));
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
            startActivity(new Intent(ReportVaccinationActivity.this, AboutUsActivity.class)
                    .putExtra("from", "reports_vaccination_activity"));
            finish();
        }
        if (id == R.id.action_export) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            String rationale = "Please provide Storage Permission to save all Reports on your device.\nThank You!";
            Permissions.Options options = new Permissions.Options()
                    .setRationaleDialogTitle("Info")
                    .setSettingsDialogTitle("Warning");

            Permissions.check(ReportVaccinationActivity.this/*context*/, permissions, rationale, options, new PermissionHandler() {
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
                    DialogDateUtil dialogDateUtil = new DialogDateUtil(ReportVaccinationActivity.this);
                    dialogDateUtil.showMessage("Reports cannot be generated.\nStorage Access needed.");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFilterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ReportVaccinationActivity.this);
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
                if (animalTypeSpinner.getSelectedItemPosition() != 0 && breedSpinner.getSelectedItemPosition() != 0) {
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
                        loadView(fromDate, toDate, animalTypeSpinner.getSelectedItemPosition(), breedSpinner.getSelectedItemPosition());
                        alertDialog.dismiss();
                    }
                } else {
                    if (animalTypeSpinner.getSelectedItemPosition() == 0) {
                        dialogDateUtil.showMessage("Please Select Animal Type before you apply any filter.");
                    } else {
                        dialogDateUtil.showMessage("Please Select Breed before you apply any filter.");
                    }
                }
            }
        });
    }

    private void loadAdapter() {
        reportsAdapter = new ReportsAdapter(ReportVaccinationActivity.this, "vaccination");
        tableView.setAdapter(reportsAdapter);
        reportsAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        tableView.setTableViewListener(new ReportsListener());
    }


    private void exportToCsv() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Goat Diary/Vaccination Report.csv");
        FileWriter fileWriter = null;
        CSVWriter csvWriter = null;
        try {
            fileWriter = new FileWriter(file);
            csvWriter = new CSVWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<String[]> data = new ArrayList<>();
        data.add(new String[]{"Sr no.", "Date", "Vaccine", "Tag Id", "Dose"});
        int srno = 1;

        for (ReportVaccinationModel rm : rawArrayList) {

            data.add(new String[]{
                    String.valueOf(srno),
                    rm.getDate(),
                    rm.getVaccine(),
                    String.valueOf(rm.getTagId()),
                    String.valueOf(rm.getDose())});
            Log.i("CUSTOM",
                    String.valueOf(srno) + "," +
                            rm.getDate() + "," +
                            rm.getVaccine() + "," +
                            String.valueOf(rm.getTagId()) + "," +
                            String.valueOf(rm.getDose()));
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

package com.aarushsystems.goatdiary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.aarushsystems.goatdiary.BuildConfig;
import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.adapter.MasterSettingsAdapter;
import com.aarushsystems.goatdiary.helper.AppConfig;
import com.aarushsystems.goatdiary.helper.DialogDateUtil;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.helper.SessionManager;
import com.aarushsystems.goatdiary.model.AddAnimalModel;
import com.aarushsystems.goatdiary.model.GenericModel;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MasterSettingsActivity extends AppCompatActivity {

    private LinearLayout addL, editL, deleteL, add1L, add2L, animalTypeL;
    private Spinner operationSpinner, addEditSpinner, masterTablesSpinner, selectEditSpinner, delSpinner, recordSpinner, animalTypeSpinner;
    private DialogDateUtil dialogDateUtil;
    private TextView addTV, editTV, delDisplayTV, delTV, clearTV;
    private EditText addED, editED;
    private ListView addDisplayListView;
    private LocalDatabase db;
    private String tableName, delTableName;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_settings);
        Toolbar toolbar = findViewById(R.id.toolbar_MasterSettingsActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        declarations();
        loadPreRequisiteContent();
        listeners();
    }

    private void loadPreRequisiteContent() {
        add1L.setVisibility(View.GONE);
        add2L.setVisibility(View.GONE);
        addL.setVisibility(View.GONE);
        editL.setVisibility(View.GONE);
        deleteL.setVisibility(View.GONE);
        animalTypeL.setVisibility(View.GONE);
        ArrayList<String> al = db.getDataForMastersTable(LocalDatabase.TABLE_ANIMAL_TYPE);
        ArrayAdapter<String> aa = new ArrayAdapter<>(MasterSettingsActivity.this,
                R.layout.layout_text_view_black, al);
        animalTypeSpinner.setAdapter(aa);
    }

    private void declarations() {
        operationSpinner = findViewById(R.id.spinner_operation_MasterSettingsActivity);
        addEditSpinner = findViewById(R.id.spinner_add_edit_MasterSettingsActivity);
        masterTablesSpinner = findViewById(R.id.spinner_master_tables_MasterSettingsActivity);
        selectEditSpinner = findViewById(R.id.spinner_select_edit_MasterSettingsActivity);
        delSpinner = findViewById(R.id.spinner_delete_MasterSettingsActivity);
        recordSpinner = findViewById(R.id.spinner_record_MasterSettingsActivity);
        animalTypeSpinner = findViewById(R.id.spinner_animal_type_MasterSettingsActivity);

        addTV = findViewById(R.id.textView_add_MasterSettingsActivity);
        editTV = findViewById(R.id.textView_edit_MasterSettingsActivity);
        delTV = findViewById(R.id.textView_delete_MasterSettingsActivity);
        delDisplayTV = findViewById(R.id.textView_delete_display_MasterSettingsActivity);
        clearTV = findViewById(R.id.textView_clear_MasterSettingsActivity);
        animalTypeL = findViewById(R.id.layout_animal_type_MasterSettingsActivity);

        addED = findViewById(R.id.editText_add_MasterSettingsActivity);
        editED = findViewById(R.id.editText_edit_MasterSettingsActivity);

        addL = findViewById(R.id.layout_add_MasterSettingsActivity);
        editL = findViewById(R.id.layout_edit_MasterSettingsActivity);
        deleteL = findViewById(R.id.layout_delete_MasterSettingsActivity);
        add1L = findViewById(R.id.layout_add_sub_1_MasterSettingsActivity);
        add2L = findViewById(R.id.layout_add_sub_2_MasterSettingsActivity);

        addDisplayListView = findViewById(R.id.listView_add_display_MasterSettingsActivity);

        dialogDateUtil = new DialogDateUtil(MasterSettingsActivity.this);
        db = new LocalDatabase(MasterSettingsActivity.this);
        sessionManager = new SessionManager(MasterSettingsActivity.this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listeners() {
        clearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationSpinner.setSelection(0);
            }
        });

        delTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordSpinner.getSelectedItemPosition() != 0) {
                    if (delSpinner.getSelectedItemPosition() < 8) {
                        HashMap<String, String> map = db.deleteSingleRecord(Integer.parseInt(recordSpinner.getSelectedItem().toString()), delTableName);
                        if (map.get("error").equals("0")) {
                            dialogDateUtil.showMessage("Record Deleted.");
                            //delDisplayTV.setVisibility(View.GONE);
                            ArrayList<String> al = db.getAllSrNosByTable(delTableName);
                            al.add(0, "SELECT");
                            if (al.size() == 1) {
                                dialogDateUtil.showMessage("No Record Found.");
                            }
                            ArrayAdapter<String> aa = new ArrayAdapter<>(MasterSettingsActivity.this,
                                    R.layout.layout_text_view_black, al);
                            recordSpinner.setAdapter(aa);
                            delSpinner.setSelection(0);
                        } else {
                            dialogDateUtil.showMessage("Local Database Error.");
                        }
                    } else if (delSpinner.getSelectedItemPosition() == 8) {
                        if (db.deleteAnimalPermanently(recordSpinner.getSelectedItem().toString())) {
                            dialogDateUtil.showMessage("Record Deleted.");
                            //delDisplayTV.setVisibility(View.GONE);
                            ArrayList<String> al = db.getAllNonDeletedTagIds();
                            al.add(0, "SELECT");
                            if (al.size() == 1) {
                                dialogDateUtil.showMessage("No Record Found.");
                            }
                            ArrayAdapter<String> aa = new ArrayAdapter<>(MasterSettingsActivity.this,
                                    R.layout.layout_text_view_black, al);
                            recordSpinner.setAdapter(aa);
                            delSpinner.setSelection(0);
                        } else {
                            dialogDateUtil.showMessage("Local Database Error.");
                        }
                    } else if (delSpinner.getSelectedItemPosition() == 9) {
                        AddAnimalModel model = new AddAnimalModel();
                        model.setTagId(Integer.parseInt(recordSpinner.getSelectedItem().toString()));
                        model.setDeleted(0);
                        //false for reversing delete operation
                        HashMap<String, String> map = db.userDeleteAnimalDetails(model, false);
                        if (map.get("error").equals("0")) {
                            dialogDateUtil.showMessage("Record Deleted.");
                            //delDisplayTV.setVisibility(View.GONE);
                            ArrayList<String> al = db.getAllDeletedTagIds();
                            al.add(0, "SELECT");
                            if (al.size() == 1) {
                                dialogDateUtil.showMessage("No Record Found.");
                            }
                            ArrayAdapter<String> aa = new ArrayAdapter<>(MasterSettingsActivity.this,
                                    R.layout.layout_text_view_black, al);
                            recordSpinner.setAdapter(aa);
                            delSpinner.setSelection(0);
                        } else {
                            dialogDateUtil.showMessage("Local Database Error.");
                        }
                    }
                } else {
                    dialogDateUtil.showMessage("No Record Selected.");
                }
            }
        });

        delSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    recordSpinner.setSelection(0);
                    delDisplayTV.setVisibility(View.GONE);
                    //remove button too
                } else {
                    getDelTableName();
                    delDisplayTV.setVisibility(View.VISIBLE);
                    ArrayList<String> al = new ArrayList<>();
                    if (delTableName.equals(LocalDatabase.TABLE_ANIMAL_DETAILS)) {
                        if (i == 8) {
                            al = db.getAllNonDeletedTagIds();
                        } else if (i == 9) {
                            al = db.getAllDeletedTagIds();
                        }
                    } else {
                        al = db.getAllSrNosByTable(delTableName);
                    }
                    al.add(0, "SELECT");
                    if (al.size() == 1) {
                        dialogDateUtil.showMessage("No Record Found.");
                    }
                    ArrayAdapter<String> aa = new ArrayAdapter<>(MasterSettingsActivity.this,
                            R.layout.layout_text_view_black, al);
                    recordSpinner.setAdapter(aa);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    delDisplayTV.setVisibility(View.VISIBLE);
                    loadDeleteLayout();
                } else {
                    delDisplayTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addED.getText().toString().trim().isEmpty()) {
                    HashMap<String, String> map = new HashMap<>();
                    if (masterTablesSpinner.getSelectedItemPosition() != 3) {
                        map = db.addNewValueToMastersTable(addED.getText().toString().trim().toUpperCase(Locale.ENGLISH), tableName);
                    } else {
                        if (animalTypeSpinner.getSelectedItemPosition() != 0) {
                            map = db.addNewValueToBreedTable(addED.getText().toString().trim().toUpperCase(Locale.ENGLISH), animalTypeSpinner.getSelectedItemPosition());
                        } else {
                            dialogDateUtil.showMessage("Please Select Animal Type");
                        }
                    }
                    if (Objects.requireNonNull(map.get("error")).equals("0")) {
                        dialogDateUtil.showMessage("New Record Added.");
                        addED.setText("");
                        loadAddLayout();
                    } else {
                        if (Objects.requireNonNull(map.get("message")).equals("duplicate_entry")) {
                            String text = "Cannot Add this Record.\n" + addED.getText().toString().trim().toUpperCase(Locale.ENGLISH) + " already present.";
                            dialogDateUtil.showMessage(text);
                            addED.setText("");
                        } else {
                            dialogDateUtil.showMessage("Local Database Error.");
                        }
                    }
                } else {
                    dialogDateUtil.showMessage("Please Enter New Value.");
                }
            }
        });

        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editED.getText().toString().trim().isEmpty()) {
                    if (selectEditSpinner.getSelectedItemPosition() != 0) {
                        HashMap<String, String> map = new HashMap<>();
                        if (masterTablesSpinner.getSelectedItemPosition() != 3) {
                            map = db.editValueofMastersTable(
                                    editED.getText().toString().trim().toUpperCase(Locale.ENGLISH),
                                    tableName,
                                    selectEditSpinner.getSelectedItemPosition());
                        } else {
                            if (animalTypeSpinner.getSelectedItemPosition() != 0) {
                                map = db.editValueofBreedTable(
                                        editED.getText().toString().trim().toUpperCase(Locale.ENGLISH),
                                        selectEditSpinner.getSelectedItem().toString(),
                                        animalTypeSpinner.getSelectedItemPosition());
                            } else {
                                dialogDateUtil.showMessage("Please Select Animal Type");
                            }
                        }
                        if (Objects.requireNonNull(map.get("error")).equals("0")) {
                            dialogDateUtil.showMessage("Record Edited Successfully.");
                            editED.setText("");
                            loadEditLayout();
                        } else {
                            if (Objects.requireNonNull(map.get("message")).equals("duplicate_entry")) {
                                String text = "Cannot Edit this Record.\nOld and New Record Values are same.";
                                dialogDateUtil.showMessage(text);
                                editED.setText("");
                            } else {
                                dialogDateUtil.showMessage("Local Database Error.");
                            }
                        }
                    } else {
                        dialogDateUtil.showMessage("Please Select a Value.");
                    }
                } else {
                    dialogDateUtil.showMessage("Please Enter New Value.");
                }
            }
        });

        addEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (masterTablesSpinner.getSelectedItemPosition() != 0) {
                    Log.i("CUSTOM", "after selectuon");
                    if (i == 0) {
                        addL.setVisibility(View.GONE);
                        editL.setVisibility(View.GONE);
                        Log.i("CUSTOM", "Called addedit");
                    } else if (i == 1) {
                        addL.setVisibility(View.VISIBLE);
                        editL.setVisibility(View.GONE);
                        loadAddLayout();
                    } else if (i == 2) {
                        addL.setVisibility(View.GONE);
                        editL.setVisibility(View.VISIBLE);
                        loadEditLayout();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    addEditSpinner.setVisibility(View.GONE);
                } else {
                    addEditSpinner.setVisibility(View.VISIBLE);
                    addEditSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        masterTablesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    addL.setVisibility(View.GONE);
                    editL.setVisibility(View.GONE);
                    addEditSpinner.setVisibility(View.GONE);
                } else {
                    Log.i("CUSTOM", "addEditSpinner position = " + addEditSpinner.getSelectedItemPosition());
                    addEditSpinner.setSelection(0);
                    if (i != 3) {
                        addEditSpinner.setVisibility(View.VISIBLE);
                        animalTypeL.setVisibility(View.GONE);
                    } else {
                        Log.i("CUSTOM", "should work");
                        animalTypeL.setVisibility(View.VISIBLE);
                        addEditSpinner.setVisibility(View.GONE);
                        addL.setVisibility(View.GONE);
                        editL.setVisibility(View.GONE);
                        animalTypeSpinner.setSelection(0);
                    }
                    /*if (addEditSpinner.getSelectedItemPosition() == 1) {
                        addL.setVisibility(View.VISIBLE);
                        editL.setVisibility(View.GONE);
                        if (i == 3) {
                            animalTypeL.setVisibility(View.VISIBLE);
                        } else {
                            animalTypeL.setVisibility(View.GONE);
                        }
                        loadAddLayout();
                    } else {
                        addL.setVisibility(View.GONE);
                        editL.setVisibility(View.VISIBLE);
                        if (i == 3) {
                            animalTypeL.setVisibility(View.VISIBLE);
                        } else {
                            animalTypeL.setVisibility(View.GONE);
                        }
                        loadEditLayout();
                    }*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        operationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    add1L.setVisibility(View.GONE);
                    add2L.setVisibility(View.GONE);
                    addL.setVisibility(View.GONE);
                    editL.setVisibility(View.GONE);
                    deleteL.setVisibility(View.GONE);
                    animalTypeL.setVisibility(View.GONE);
                    animalTypeSpinner.setSelection(0);
                    addEditSpinner.setSelection(0);
                    masterTablesSpinner.setSelection(0);
                    delSpinner.setSelection(0);
                } else if (i == 1) {
                    add1L.setVisibility(View.VISIBLE);
                    add2L.setVisibility(View.VISIBLE);
                    animalTypeL.setVisibility(View.GONE);
                    addL.setVisibility(View.GONE);
                    editL.setVisibility(View.GONE);
                    deleteL.setVisibility(View.GONE);
                } else if (i == 2) {
                    animalTypeL.setVisibility(View.GONE);
                    add1L.setVisibility(View.GONE);
                    add2L.setVisibility(View.GONE);
                    addL.setVisibility(View.GONE);
                    editL.setVisibility(View.GONE);
                    deleteL.setVisibility(View.VISIBLE);
                    addEditSpinner.setSelection(0);
                } else if (i == 3) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    String rationale = "Please provide Storage Permission to access your data.\nThank You!";
                    Permissions.Options options = new Permissions.Options()
                            .setRationaleDialogTitle("Info")
                            .setSettingsDialogTitle("Warning");

                    Permissions.check(MasterSettingsActivity.this/*context*/, permissions, rationale, options, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            // do your task.
                            String appDir = Environment.getExternalStorageDirectory().toString() + "/Goat Diary";
                            File file = new File(appDir);
                            if (!file.exists()) {
                                if (file.mkdir()) {
                                    exportDatabaseToStorageAndEmail();
                                } else {
                                    Log.i("CUSTOM", "failed");
                                }
                            } else {
                                exportDatabaseToStorageAndEmail();
                            }
                        }

                        @Override
                        public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                            DialogDateUtil dialogDateUtil = new DialogDateUtil(MasterSettingsActivity.this);
                            dialogDateUtil.showMessage("Back-up cannot be generated.\nStorage Access needed.");
                            operationSpinner.setSelection(0);
                        }
                    });
                    add1L.setVisibility(View.GONE);
                    add2L.setVisibility(View.GONE);
                    addL.setVisibility(View.GONE);
                    editL.setVisibility(View.GONE);
                    deleteL.setVisibility(View.GONE);
                    animalTypeL.setVisibility(View.GONE);
                    animalTypeSpinner.setSelection(0);
                    addEditSpinner.setSelection(0);
                    masterTablesSpinner.setSelection(0);
                    delSpinner.setSelection(0);
                } else if (i == 4) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    String rationale = "Please provide Storage Permission to access your data.\nThank You!";
                    Permissions.Options options = new Permissions.Options()
                            .setRationaleDialogTitle("Info")
                            .setSettingsDialogTitle("Warning");

                    Permissions.check(MasterSettingsActivity.this/*context*/, permissions, rationale, options, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            // do your task.
                            String appDir = Environment.getExternalStorageDirectory().toString() + "/Goat Diary";
                            File file = new File(appDir);
                            if (!file.exists()) {
                                if (file.mkdir()) {
                                    importDatabaseFromEmail();
                                } else {
                                    Log.i("CUSTOM", "failed");
                                }
                            } else {
                                importDatabaseFromEmail();
                            }
                        }

                        @Override
                        public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                            DialogDateUtil dialogDateUtil = new DialogDateUtil(MasterSettingsActivity.this);
                            dialogDateUtil.showMessage("Data cannot be restored.\nStorage Access needed.");
                            operationSpinner.setSelection(0);
                        }
                    });
                    add1L.setVisibility(View.GONE);
                    add2L.setVisibility(View.GONE);
                    addL.setVisibility(View.GONE);
                    editL.setVisibility(View.GONE);
                    deleteL.setVisibility(View.GONE);
                    animalTypeL.setVisibility(View.GONE);
                    animalTypeSpinner.setSelection(0);
                    addEditSpinner.setSelection(0);
                    masterTablesSpinner.setSelection(0);
                    delSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addDisplayListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("CUSTOM", "'ACTION_DOWN'");
                        // Disallow ScrollView to intercept touch events.
                        //It is scrolled all the way down here
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.i("CUSTOM", "'ACTION_UP'");
                        // Allow ScrollView to intercept touch events.
                        //It is scrolled all the way up here
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

    }

    private void importDatabaseFromEmail() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    28);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("CUSTOM", "onActivityResult()");
        switch (requestCode) {
            case 28:
                Log.i("CUSTOM", "case 28");
                if (resultCode == RESULT_OK) {
                    Log.i("CUSTOM", "result OK");
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    if (uri.getLastPathSegment().contains(".db")) {
                        String path = uri.getPath();
                        Log.i("CUSTOM", "file raw path = " + path);
                        if (!path.contains(sessionManager.getKeyEmail())) {
                            dialogDateUtil.showMessage("Invalid File." +
                                    "\nPlease choose your own back-up file downloaded from Email.");
                            break;
                        }
                        if (path.contains(":")) {
                            path = path.split(":")[1];
                        }
                        if (path.contains("root_path")) {
                            path = path.replace("/root_path", "");
                        }
                        /*for (String s : Environment.getExternalStorageDirectory().list()) {
                            Log.i("CUSTOM",Environment.getExternalStorageDirectory().getPath()+" - "+s);
                        }*/
                        if (!path.startsWith("/storage")) {
                            if (path.startsWith("/")) {
                                path = Environment.getExternalStorageDirectory().getPath().concat(path);
                            } else {
                                path = Environment.getExternalStorageDirectory().getPath() + "/".concat(path);
                            }
                        }
                        File dummy = new File(path);
                        if (dummy.exists()) {
                            Log.i("CUSTOM", "dummy exists");
                        }
                        if (dummy.isFile()) {
                            Log.i("CUSTOM", "dummy isfile");
                        }
                        if (dummy.isAbsolute()) {
                            Log.i("CUSTOM", "dummy absolute");
                        }
                        Log.i("CUSTOM", "file ab path = " + dummy.getAbsolutePath());
                        InputStream myInput = null;
                        OutputStream myOutput = null;
                        try {
                            myInput = new FileInputStream(path);
                            String outFileName = "/data/data/com.aarushsystems.goatdiary/databases/" + LocalDatabase.DATABASE_NAME;
                            myOutput = new FileOutputStream(outFileName);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = myInput.read(buffer)) > 0) {
                                myOutput.write(buffer, 0, length);
                            }
                            if (!db.checkUserEmailWithRestoreEmail(sessionManager.getKeyEmail())) {
                                sessionManager.setLogin(false);
                                startActivity(new Intent(MasterSettingsActivity.this, LoginActivity.class)
                                        .putExtra("message",
                                                "Restoration Failed.\nAttempt to restore other user's data."));
                                finish();
                                Log.i("CUSTOM", "...1");
                                break;
                            } else {
                                dialogDateUtil.showMessage("Restoration Successful.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                Log.i("CUSTOM", "...2");
                                myOutput.flush();
                                myOutput.close();
                                myInput.close();
                            } catch (Exception ioe) {
                                ioe.printStackTrace();
                            }
                        }
                    } else {
                        dialogDateUtil.showMessage("Invalid File Format." +
                                "\nPlease select the file downloaded from Email." +
                                "\nFile Name : goat_diary_" + sessionManager.getKeyEmail() + ".db");
                        operationSpinner.setSelection(0);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void exportDatabaseToStorageAndEmail() {
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/Goat Diary/" + LocalDatabase.DATABASE_NAME + "_" + sessionManager.getKeyEmail() + ".db";
            InputStream myInput = null;
            OutputStream myOutput = null;
            try {
                String inFileName = "/data/data/com.aarushsystems.goatdiary/databases/" + LocalDatabase.DATABASE_NAME;
                myInput = new FileInputStream(inFileName);
                String outFileName = fileName;
                myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                //TODO:: SUUCESS CODE EHRE
                myOutput.flush();
                myOutput.close();
                myInput.close();
                try {
                    File toFile = new File(fileName);
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("message/rfc822");
                    String to[] = {sessionManager.getKeyEmail()};
                    String cc[] = {"atatti25@gmail.com"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    emailIntent.putExtra(Intent.EXTRA_CC, cc);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Goat Diary BACK-UP");
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // the attachment
                    Uri path;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        path = Uri.fromFile(toFile);
                    } else {
                        path = FileProvider.getUriForFile(MasterSettingsActivity.this, BuildConfig.APPLICATION_ID + ".provider", toFile);
                    }
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Select Email app"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("CUSTOM", "email Exception = " + e.getLocalizedMessage());
                    }
                    dialogDateUtil.showMessage("Back-up Successful at Local Storage.\nLocation : " + toFile.getPath()
                            + "\nPlease select the same file to Restore.\nThank you!");
                } catch (Exception e) {
                    Log.i("CUSTOM", "rename exported file exception = " + e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    // Close the streams
                    myOutput.flush();
                    myOutput.close();
                    myInput.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDeleteLayout() {
        String data = "";
        if (delSpinner.getSelectedItemPosition() < 8) {
            //true for srno
            data = db.getSingleRecordBySrNo(delTableName, Integer.parseInt(recordSpinner.getSelectedItem().toString()), true);
        } else if (delSpinner.getSelectedItemPosition() == 8 || delSpinner.getSelectedItemPosition() == 9) {
            //false for tagid
            data = db.getSingleRecordBySrNo(delTableName, Integer.parseInt(recordSpinner.getSelectedItem().toString()), false);
            data = data.replace("null", "NA");
            data = data.replace(": \n", ": NA\n");
            if (data.trim().lastIndexOf(":") == data.length() - 1) {
                data = data.replace(": ", ": NA");
            }
        }
        delDisplayTV.setText(data);
    }

    private void loadEditLayout() {
        getTableNameImplicitly();
        ArrayList<String> al;
        if (masterTablesSpinner.getSelectedItemPosition() == 3) {
            al = db.getDataForBreedTable(animalTypeSpinner.getSelectedItemPosition());
            al.add(0, "SELECT");
        } else {
            al = db.getDataForMastersTable(tableName);
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(MasterSettingsActivity.this, R.layout.layout_text_view_black, al);
        selectEditSpinner.setAdapter(aa);
    }

    private void loadAddLayout() {
        getTableNameImplicitly();
        ArrayList<String> al;
        ArrayList<GenericModel> genericArrayList = new ArrayList<>();
        if (masterTablesSpinner.getSelectedItemPosition() == 3) {
            al = db.getDataForBreedTable(animalTypeSpinner.getSelectedItemPosition());
            for (int i = 0; i < al.size(); i++) {
                genericArrayList.add(new GenericModel(String.valueOf(i + 1), al.get(i)));
            }
            genericArrayList.add(0, new GenericModel(String.valueOf(0), "SELECT"));
        } else {
            al = db.getDataForMastersTable(tableName);
            for (int i = 1; i < al.size(); i++) {
                genericArrayList.add(new GenericModel(String.valueOf(i), al.get(i)));
            }
        }
        Log.i("CUSTOM", "generic size - " + genericArrayList.size());
        MasterSettingsAdapter adapter = new MasterSettingsAdapter(MasterSettingsActivity.this,
                genericArrayList);
        addDisplayListView.setAdapter(adapter);
    }

    private void getTableNameImplicitly() {
        tableName = "";
        switch (masterTablesSpinner.getSelectedItemPosition()) {
            case 1:
                tableName = LocalDatabase.TABLE_ANIMAL_TYPE;
                break;
            case 2:
                tableName = LocalDatabase.TABLE_AQUISATION;
                break;
            case 3:
                tableName = LocalDatabase.TABLE_BREED;
                break;
            case 4:
                tableName = LocalDatabase.TABLE_PURPOSE;
                break;
            case 5:
                tableName = LocalDatabase.TABLE_RELEASE;
                break;
            case 6:
                tableName = LocalDatabase.TABLE_VACCINE;
                break;
            case 7:
                tableName = LocalDatabase.TABLE_EXPENSE;
                break;
            case 8:
                tableName = LocalDatabase.TABLE_INCOME;
                break;
            case 9:
                tableName = LocalDatabase.TABLE_FEED_TYPE;
                break;
        }
    }

    private void getDelTableName() {
        delTableName = "";
        switch (delSpinner.getSelectedItemPosition()) {
            case 1:
                delTableName = LocalDatabase.TABLE_ANIMAL_VACCINATION;
                break;
            case 2:
                delTableName = LocalDatabase.TABLE_ANIMAL_WEIGHTS;
                break;
            case 3:
                delTableName = LocalDatabase.TABLE_ANIMAL_BREEDING;
                break;
            case 4:
                delTableName = LocalDatabase.TABLE_ANIMAL_EXPENSE;
                break;
            case 5:
                delTableName = LocalDatabase.TABLE_ANIMAL_INCOME;
                break;
            case 6:
                delTableName = LocalDatabase.TABLE_ANIMAL_FEED;
                break;
            case 7:
                delTableName = LocalDatabase.TABLE_ANIMAL_MILK;
                break;
            case 8:
            case 9:
                delTableName = LocalDatabase.TABLE_ANIMAL_DETAILS;
                break;
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
        if (id == R.id.action_terms_and_cond) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.TERMS_AND_CONDITIONS));
                startActivity(browserIntent);
            } catch (Exception ignore) {
            }
        }
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_about_us) {
            startActivity(new Intent(MasterSettingsActivity.this, AboutUsActivity.class)
                    .putExtra("from", "master_settings_activity"));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}

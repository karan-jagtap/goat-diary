package com.aarushsystems.goatdiary.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseIntArray;

import androidx.annotation.Nullable;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.model.AddAnimalModel;
import com.aarushsystems.goatdiary.model.BreedingModel;
import com.aarushsystems.goatdiary.model.ExpenseModel;
import com.aarushsystems.goatdiary.model.FeedModel;
import com.aarushsystems.goatdiary.model.IncomeModel;
import com.aarushsystems.goatdiary.model.MilkModel;
import com.aarushsystems.goatdiary.model.UserModel;
import com.aarushsystems.goatdiary.model.VaccinationModel;
import com.aarushsystems.goatdiary.model.WeightModel;
import com.aarushsystems.goatdiary.model.reports.ReportBreedingModel;
import com.aarushsystems.goatdiary.model.reports.ReportExpenseModel;
import com.aarushsystems.goatdiary.model.reports.ReportFeedStockModel;
import com.aarushsystems.goatdiary.model.reports.ReportIncomeModel;
import com.aarushsystems.goatdiary.model.reports.ReportLiveStockModel;
import com.aarushsystems.goatdiary.model.reports.ReportMilkModel;
import com.aarushsystems.goatdiary.model.reports.ReportVaccinationModel;
import com.aarushsystems.goatdiary.model.reports.ReportWeightModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LocalDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "goat_diary";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ANIMAL_TYPE = "animal_type";
    public static final String TABLE_AQUISATION = "aquisation";
    public static final String TABLE_BREED = "breed";
    public static final String TABLE_PURPOSE = "purpose";
    public static final String TABLE_RELEASE = "custom_release";
    public static final String TABLE_VACCINE = "vaccine";
    public static final String TABLE_MONTHS = "months";
    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_INCOME = "income";
    public static final String TABLE_ACTION_MILK = "action_milk";
    public static final String TABLE_ACTION_FEED = "action_feed";
    public static final String TABLE_FEED_TYPE = "feed_type";

    private static final String TABLE_USERS = "users";
    public static final String TABLE_ANIMAL_DETAILS = "animal_details";
    public static final String TABLE_ANIMAL_VACCINATION = "animal_vaccination";
    public static final String TABLE_ANIMAL_WEIGHTS = "animal_weight";
    public static final String TABLE_ANIMAL_BREEDING = "animal_breeding";
    public static final String TABLE_ANIMAL_EXPENSE = "animal_expense";
    public static final String TABLE_ANIMAL_INCOME = "animal_income";
    public static final String TABLE_ANIMAL_MILK = "animal_milk";
    public static final String TABLE_ANIMAL_FEED = "animal_feed";

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_SR_NO = "sr_no";
    private static final String KEY_TAG_ID = "tag_id";
    private static final String KEY_ANIMAL_TYPE = "animal_type";
    private static final String KEY_AQUISATION = "aquisation";
    private static final String KEY_GENDER = "gender";
    //private static final String KEY_FEMALE_STATUS = "female_status";
    private static final String KEY_BREED = "breed";
    private static final String KEY_DATE = "add_date";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_PURPOSE = "purpose";
    private static final String KEY_MOTHER_ID = "mother_id";
    private static final String KEY_PRICE = "price";
    private static final String KEY_RELEASE = "custom_release";
    private static final String KEY_D_DATE = "del_date";
    private static final String KEY_RELEASE_PRICE = "release_price";
    private static final String KEY_RELEASE_WEIGHT = "release_weight";
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_DELETED = "deleted";
    private static final String KEY_FIELD = "field";
    private static final String KEY_VACCINE = "vaccine";
    private static final String KEY_DOSE = "dose";
    private static final String KEY_BOOSTER = "booster";
    private static final String KEY_P_DATE = "proposed_date";
    private static final String KEY_MALE_ID = "male_id";
    private static final String KEY_FEMALE_ID = "female_id";
    private static final String KEY_MATING_FLAG = "mating_flag";
    private static final String KEY_MATING_DATE = "mating_date";
    private static final String KEY_CONF_FLAG = "conf_flag";
    private static final String KEY_CONF_DATE = "conf_date";
    private static final String KEY_DELIVERY_FLAG = "delivery_flag";
    private static final String KEY_DELIVERY_DATE = "delivery_date";
    private static final String KEY_ABORTION = "abortion";
    private static final String KEY_STILLBORN = "stillborn";
    private static final String KEY_CHILD_MALE = "child_male";
    private static final String KEY_CHILD_FEMALE = "child_female";
    private static final String KEY_HEAD = "head";
    private static final String KEY_CASH_CHEQUE = "cash_cheque";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_PAID_TO = "paid_to";
    private static final String KEY_CHEQUE_NO = "cheque_no";
    private static final String KEY_RECEIVED_FROM = "received_from";
    private static final String KEY_BANK = "bank";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_ACTION = "custom_action";
    private static final String KEY_LOT = "lot";
    private static final String KEY_TIME = "milk_time";
    private static final String KEY_FEED_TYPE = "feed_type";

    private String create_animal_type_table = "CREATE TABLE " + TABLE_ANIMAL_TYPE + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_aquisation_table = "CREATE TABLE " + TABLE_AQUISATION + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_breed_table = "CREATE TABLE " + TABLE_BREED + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT, " +
            KEY_ANIMAL_TYPE + " INTEGER " +
            ");";
    private String create_purpose_table = "CREATE TABLE " + TABLE_PURPOSE + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_release_table = "CREATE TABLE " + TABLE_RELEASE + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_months_table = "CREATE TABLE " + TABLE_MONTHS + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_vaccine_table = "CREATE TABLE " + TABLE_VACCINE + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_expense_table = "CREATE TABLE " + TABLE_EXPENSE + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_income_table = "CREATE TABLE " + TABLE_INCOME + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_action_milk_table = "CREATE TABLE " + TABLE_ACTION_MILK + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_action_feed_table = "CREATE TABLE " + TABLE_ACTION_FEED + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";
    private String create_feed_type_table = "CREATE TABLE " + TABLE_FEED_TYPE + " (" +
            KEY_SR_NO + " INTEGER, " +
            KEY_FIELD + " TEXT " +
            ");";

    private String create_weights_table = "CREATE TABLE " + TABLE_ANIMAL_WEIGHTS + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_TAG_ID + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_WEIGHT + " TEXT " +
            ");";
    private String create_animal_expense_table = "CREATE TABLE " + TABLE_ANIMAL_EXPENSE + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_HEAD + " INTEGER, " +
            KEY_CASH_CHEQUE + " TEXT, " +
            KEY_DATE + " TEXT, " +
            KEY_AMOUNT + " TEXT, " +
            KEY_PAID_TO + " TEXT, " +
            KEY_CHEQUE_NO + " INTEGER, " +
            KEY_BANK + " TEXT, " +
            KEY_REMARKS + " TEXT " +
            ");";
    private String create_animal_income_table = "CREATE TABLE " + TABLE_ANIMAL_INCOME + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_HEAD + " INTEGER, " +
            KEY_CASH_CHEQUE + " TEXT, " +
            KEY_DATE + " TEXT, " +
            KEY_AMOUNT + " TEXT, " +
            KEY_RECEIVED_FROM + " TEXT, " +
            KEY_CHEQUE_NO + " INTEGER, " +
            KEY_BANK + " TEXT, " +
            KEY_REMARKS + " TEXT " +
            ");";
    private String create_vaccination_table = "CREATE TABLE " + TABLE_ANIMAL_VACCINATION + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_TAG_ID + " INTEGER, " +
            KEY_VACCINE + " INTEGER, " +
            KEY_DOSE + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_BOOSTER + " INTEGER, " +
            KEY_P_DATE + " TEXT " +
            ");";
    private String create_breeding_table = "CREATE TABLE " + TABLE_ANIMAL_BREEDING + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_MALE_ID + " INTEGER, " +
            KEY_FEMALE_ID + " INTEGER, " +
            KEY_MATING_FLAG + " TEXT, " +
            KEY_MATING_DATE + " TEXT, " +
            KEY_CONF_FLAG + " TEXT, " +
            KEY_CONF_DATE + " TEXT, " +
            KEY_DELIVERY_FLAG + " TEXT, " +
            KEY_DELIVERY_DATE + " TEXT, " +
            KEY_ABORTION + " INTEGER, " +
            KEY_STILLBORN + " INTEGER, " +
            KEY_CHILD_MALE + " INTEGER, " +
            KEY_CHILD_FEMALE + " INTEGER " +
            ");";
    private String create_milk_table = "CREATE TABLE " + TABLE_ANIMAL_MILK + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_ACTION + " INTEGER, " +
            KEY_LOT + " INTEGER, " +
            KEY_TAG_ID + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_TIME + " INTEGER, " +
            KEY_QUANTITY + " TEXT, " +
            KEY_PRICE + " TEXT " +
            ");";
    private String create_feed_table = "CREATE TABLE " + TABLE_ANIMAL_FEED + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_ACTION + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_TIME + " INTEGER, " +
            KEY_FEED_TYPE + " INTEGER, " +
            KEY_WEIGHT + " TEXT, " +
            KEY_REMARKS + " TEXT " +
            ");";

    private String create_users_table = "CREATE TABLE " + TABLE_USERS + " (" +
            KEY_NAME + " TEXT NOT NULL," +
            KEY_EMAIL + " TEXT NOT NULL," +
            KEY_PHONE + " TEXT NOT NULL," +
            KEY_CITY + " TEXT," +
            KEY_COUNTRY + " TEXT" +
            ");";

    private String create_animal_details_table = "CREATE TABLE " + TABLE_ANIMAL_DETAILS + " (" +
            KEY_SR_NO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            KEY_TAG_ID + " INTEGER, " +
            KEY_ANIMAL_TYPE + " INTEGER, " +
            KEY_AQUISATION + " INTEGER, " +
            KEY_GENDER + " TEXT, " +
            //KEY_FEMALE_STATUS + " TEXT, " +
            KEY_BREED + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_WEIGHT + " TEXT, " +
            KEY_PURPOSE + " INTEGER, " +
            KEY_MOTHER_ID + " TEXT, " +
            KEY_PRICE + " TEXT, " +
            KEY_RELEASE + " INTEGER, " +
            KEY_D_DATE + " TEXT, " +
            KEY_RELEASE_PRICE + " TEXT, " +
            KEY_RELEASE_WEIGHT + " TEXT, " +
            KEY_REMARKS + " TEXT, " +
            KEY_DELETED + " INTEGER " +
            ");";

    private Context context;

    public LocalDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_users_table);
        sqLiteDatabase.execSQL(create_animal_details_table);
        sqLiteDatabase.execSQL(create_animal_type_table);
        sqLiteDatabase.execSQL(create_aquisation_table);
        sqLiteDatabase.execSQL(create_breed_table);
        sqLiteDatabase.execSQL(create_purpose_table);
        sqLiteDatabase.execSQL(create_release_table);
        sqLiteDatabase.execSQL(create_months_table);
        sqLiteDatabase.execSQL(create_weights_table);
        sqLiteDatabase.execSQL(create_vaccine_table);
        sqLiteDatabase.execSQL(create_vaccination_table);
        sqLiteDatabase.execSQL(create_breeding_table);
        sqLiteDatabase.execSQL(create_expense_table);
        sqLiteDatabase.execSQL(create_animal_expense_table);
        sqLiteDatabase.execSQL(create_income_table);
        sqLiteDatabase.execSQL(create_animal_income_table);
        sqLiteDatabase.execSQL(create_action_milk_table);
        sqLiteDatabase.execSQL(create_milk_table);
        sqLiteDatabase.execSQL(create_action_feed_table);
        sqLiteDatabase.execSQL(create_feed_type_table);
        sqLiteDatabase.execSQL(create_feed_table);
        insertAllRecords(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    private void insertAllRecords(SQLiteDatabase db) {
        //M1 : Animal Type Table
        ContentValues cv = new ContentValues();
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_ANIMAL_TYPE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "GOAT");
        db.insert(TABLE_ANIMAL_TYPE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "SHEEP");
        db.insert(TABLE_ANIMAL_TYPE, null, cv);
        cv.clear();

        //M2 : Aquisation Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_AQUISATION, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "BY PURCHASE");
        db.insert(TABLE_AQUISATION, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "BY BIRTH");
        db.insert(TABLE_AQUISATION, null, cv);
        cv.clear();

        //M3 : Breed Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        cv.put(KEY_ANIMAL_TYPE, 0);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        // TYPE 1
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "OSMANABADI");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "SIROHI");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "JAMNAPARI");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 4);
        cv.put(KEY_FIELD, "BEETAL");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 5);
        cv.put(KEY_FIELD, "MARWARI");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 6);
        cv.put(KEY_FIELD, "BARBARI");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 7);
        cv.put(KEY_FIELD, "BOER");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 8);
        cv.put(KEY_FIELD, "MADGYAL");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 9);
        cv.put(KEY_FIELD, "TALICHERI");
        cv.put(KEY_ANIMAL_TYPE, 1);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        // TYPE 2
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "GADDI");
        cv.put(KEY_ANIMAL_TYPE, 2);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "MERINO");
        cv.put(KEY_ANIMAL_TYPE, 2);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "DORSET");
        cv.put(KEY_ANIMAL_TYPE, 2);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 4);
        cv.put(KEY_FIELD, "BARBARI");
        cv.put(KEY_ANIMAL_TYPE, 2);
        db.insert(TABLE_BREED, null, cv);
        cv.clear();

        //M4 : Purpose Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_PURPOSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "FOR BREEDING");
        db.insert(TABLE_PURPOSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "FOR SALE");
        db.insert(TABLE_PURPOSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "FOR EID");
        db.insert(TABLE_PURPOSE, null, cv);
        cv.clear();

        //M5 : Release Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_RELEASE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "BY SALE");
        db.insert(TABLE_RELEASE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "BY DEATH");
        db.insert(TABLE_RELEASE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "BY CULLING");
        db.insert(TABLE_RELEASE, null, cv);
        cv.clear();

        //M6 : Months Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "1");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "2");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "3");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 4);
        cv.put(KEY_FIELD, "6");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 5);
        cv.put(KEY_FIELD, "9");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 6);
        cv.put(KEY_FIELD, "12");
        db.insert(TABLE_MONTHS, null, cv);
        cv.clear();

        //M7 : Vaccine Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_VACCINE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "PPR");
        db.insert(TABLE_VACCINE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "ETV");
        db.insert(TABLE_VACCINE, null, cv);
        cv.clear();

        //M9 : Expense Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "SHED");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "ELECTRICITY");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "LABOUR");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 4);
        cv.put(KEY_FIELD, "TELEPHONE");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 5);
        cv.put(KEY_FIELD, "ANIMAL PURCHASE");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 6);
        cv.put(KEY_FIELD, "WATER");
        db.insert(TABLE_EXPENSE, null, cv);
        cv.clear();

        //M10 : Income Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_INCOME, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "ANIMAL SALE");
        db.insert(TABLE_INCOME, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "MILK SALE");
        db.insert(TABLE_INCOME, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "FODDER SALE");
        db.insert(TABLE_INCOME, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 4);
        cv.put(KEY_FIELD, "MANURE SALE");
        db.insert(TABLE_INCOME, null, cv);
        cv.clear();

        //M11 : Action Feed Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_ACTION_FEED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "ADD STOCK");
        db.insert(TABLE_ACTION_FEED, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "FEED ANIMALS");
        db.insert(TABLE_ACTION_FEED, null, cv);
        cv.clear();

        //M12 : Action Feed Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_FEED_TYPE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "DRY");
        db.insert(TABLE_FEED_TYPE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "GREEN");
        db.insert(TABLE_FEED_TYPE, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 3);
        cv.put(KEY_FIELD, "CONCENTRATED MIXTURE");
        db.insert(TABLE_FEED_TYPE, null, cv);
        cv.clear();

        //M13 : Action Milk Table
        cv.put(KEY_SR_NO, 0);
        cv.put(KEY_FIELD, "SELECT");
        db.insert(TABLE_ACTION_MILK, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 1);
        cv.put(KEY_FIELD, "PRODUCTION");
        db.insert(TABLE_ACTION_MILK, null, cv);
        cv.clear();
        cv.put(KEY_SR_NO, 2);
        cv.put(KEY_FIELD, "SALES");
        db.insert(TABLE_ACTION_MILK, null, cv);
        cv.clear();
    }

    public boolean userLogin(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, user.getName());
        cv.put(KEY_EMAIL, user.getEmail());
        cv.put(KEY_PHONE, user.getPhone());
        cv.put(KEY_CITY, user.getCity());
        cv.put(KEY_COUNTRY, user.getCountry());
        db.insert(TABLE_USERS, null, cv);
        db.close();
        db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE email='" + user.getEmail() + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public boolean checkUserEmailWithRestoreEmail(String restoreEmail) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE email='" + restoreEmail + "'";
        Log.i("CUSTOM", "checkUserEmailWithRestoreEmail query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 1) {
                cursor.close();
                db.close();
                Log.i("CUSTOM", "Valid User");
                return true;
            }
        }
        cursor.close();
        Log.i("CUSTOM", "InValid User");
        deleteTableUsers();
        db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_MILK);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_FEED);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_WEIGHTS);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_INCOME);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_EXPENSE);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_BREEDING);
        db.execSQL("DELETE FROM " + TABLE_ANIMAL_VACCINATION);
        db.close();
        return false;
    }

    public HashMap<String, String> userAddAnimalDetails(AddAnimalModel model) {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, String> response = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + "=" + model.getTagId();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.close();
            db.close();
            response.put("error", "1");
            response.put("message", "tag_id_present");
            return response;
        }
        cursor.close();
        db.close();
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAG_ID, model.getTagId());
        cv.put(KEY_ANIMAL_TYPE, model.getAnimalType());
        cv.put(KEY_AQUISATION, model.getAquisation());
        cv.put(KEY_GENDER, model.getGender());
        // cv.put(KEY_FEMALE_STATUS, model.getFemaleStatus());
        cv.put(KEY_BREED, model.getBreed());
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_WEIGHT, model.getWeight());
        cv.put(KEY_PURPOSE, model.getPurpose());
        cv.put(KEY_MOTHER_ID, model.getMotherId());
        cv.put(KEY_PRICE, model.getPrice());
        cv.put(KEY_RELEASE, model.getRelease());
        cv.put(KEY_DELETED, model.getDeleted());
        long status = db.insert(TABLE_ANIMAL_DETAILS, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        db = getReadableDatabase();
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            response.put("error", "0");
            response.put("message", "success");
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public ArrayList<AddAnimalModel> getAllAnimalDetailsRecords() {
        ArrayList<AddAnimalModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_DETAILS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            AddAnimalModel model = new AddAnimalModel();
            model.setSrno(cursor.getInt(0));
            model.setTagId(cursor.getInt(1));
            model.setAnimalType(cursor.getInt(2));
            model.setAquisation(cursor.getInt(3));
            model.setGender(cursor.getString(4));
            //model.setFemaleStatus(cursor.getString(5));
            model.setBreed(cursor.getInt(5));
            model.setDate(cursor.getString(6));
            model.setWeight(cursor.getString(7));
            model.setPurpose(cursor.getInt(8));
            model.setMotherId(cursor.getString(9));
            model.setPrice(cursor.getString(10));
            model.setRelease(cursor.getInt(11));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public int getNewestSrNo() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_SR_NO + ") FROM " + TABLE_ANIMAL_DETAILS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int response = cursor.getInt(0);
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        return -1;
    }

    public int getTotalAnimalsCount() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_ANIMAL_DETAILS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int response = cursor.getInt(0);
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        return 0;
    }

    public int getTotalAnimalsWeightsCount() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_ANIMAL_WEIGHTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int response = cursor.getInt(0);
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        return 0;
    }

    public ArrayList<String> getAllFemalesTagIds(int status) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "";
        Log.i("CUSTOM", "STATUS = " + status);
        if (status != -1) {
            selectQuery = "SELECT " +
                    KEY_TAG_ID +
                    " FROM " + TABLE_ANIMAL_DETAILS +
                    " WHERE " +
                    KEY_GENDER + "='F' AND " +
                    KEY_DELETED + "=0 AND " +
                    KEY_ANIMAL_TYPE + "=" + status;
        } else {
            selectQuery = "SELECT " + KEY_TAG_ID + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE gender='F' AND " + KEY_DELETED + "=0";
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            list.add(String.valueOf(cursor.getInt(0)));
            Log.i("CUSTOM", "in while");
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getAllNonPregnantFemalesTagIds() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        //TODO :: Now that female status is removed. Change query to breeding table.
        //select tag_id from animal_details
        //where
        //(gender='F'
        //AND
        //tag_id not in
        //(select female_id from animal_breeding
        //where delivery_flag = 'N'));
        String selectQuery = "SELECT " + KEY_TAG_ID +
                " FROM " + TABLE_ANIMAL_DETAILS +
                " WHERE (gender='F' AND deleted=0 AND " + KEY_TAG_ID +
                " NOT IN ( SELECT " + KEY_FEMALE_ID +
                " FROM " + TABLE_ANIMAL_BREEDING +
                " WHERE " + KEY_DELIVERY_FLAG + " = 'N'));";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            list.add(String.valueOf(cursor.getInt(0)));
            Log.i("CUSTOM", "in while");
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getAllMalesTagIds() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_TAG_ID + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE gender='M' AND " + KEY_DELETED + "=0";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            list.add(String.valueOf(cursor.getInt(0)));
            Log.i("CUSTOM", "in while");
        }
        cursor.close();
        db.close();
        return list;
    }

    public String getFieldBySrNo(int srNo, String tableName) {
        String response;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_FIELD + " FROM " + tableName + " WHERE " + KEY_SR_NO + "=" + srNo;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            response = cursor.getString(0);
        } else {
            response = null;
        }
        cursor.close();
        db.close();
        return response;
    }

    public String getBreedBySrNoAndAnimalType(int srNo, int animalType) {
        String response;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_FIELD + " FROM " + TABLE_BREED +
                " WHERE " + KEY_SR_NO + "=" + srNo + " AND " + KEY_ANIMAL_TYPE + "=" + animalType;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            response = cursor.getString(0);
        } else {
            response = null;
        }
        cursor.close();
        db.close();
        return response;
    }

    public ArrayList<String> getDataForMastersTable(String tableName) {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<String> getDataForBreedTable(int animalType) {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BREED + " WHERE " + KEY_ANIMAL_TYPE + "=" + animalType;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.i("CUSTOM", "getdatabreed query =- " + selectQuery + " cursor count = " + cursor.getCount());
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(1));
            Log.i("CUSTOM", "data = " + cursor.getInt(0) + " - " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<String> getAllNonDeletedTagIds() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_TAG_ID + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_DELETED + "=0";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            list.add(String.valueOf(cursor.getInt(0)));
            Log.i("CUSTOM", "in while");
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getAllDeletedTagIds() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_TAG_ID + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_DELETED + "=1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            list.add(String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteTableUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS);
        db.close();
    }

    public int getDeletedAnimalsCount() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_DELETED + "=1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int response = cursor.getInt(0);
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        return 0;
    }

    public int getThisMonthAnimalsCount(String thisMonth, String year) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_DATE + " FROM " + TABLE_ANIMAL_WEIGHTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        Log.i("CUSTOM", "cursor count - " + cursor.getCount());
        while (cursor.moveToNext()) {
            String[] date = cursor.getString(0).split("/");
            Log.i("CUSTOM", "this month = " + thisMonth + " date month = " + date[1]);
            if (date[1].equals(thisMonth) && date[2].equals(year)) {
                count++;
            }
        }
        cursor.close();
        db.close();
        return count;
    }

    public AddAnimalModel getDetailsForTagID(String tagId) {
        AddAnimalModel model = new AddAnimalModel();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + "=" + tagId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            model.setSrno(cursor.getInt(0));
            model.setTagId(cursor.getInt(1));
            model.setAnimalType(cursor.getInt(2));
            model.setAquisation(cursor.getInt(3));
            model.setGender(cursor.getString(4));
            //model.setFemaleStatus(cursor.getString(5));
            model.setBreed(cursor.getInt(5));
            model.setDate(cursor.getString(6));
            model.setWeight(cursor.getString(7));
            model.setPurpose(cursor.getInt(8));
            model.setMotherId(cursor.getString(9));
            model.setPrice(cursor.getString(10));
            model.setRelease(cursor.getInt(11));
            model.setDeleted(0);
            cursor.close();
            db.close();
        } else {
            model = null;
        }
        cursor.close();
        db.close();
        return model;
    }

    public ArrayList<AddAnimalModel> getAllDeletedRecords() {
        ArrayList<AddAnimalModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_DELETED + "= 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            AddAnimalModel model = new AddAnimalModel();
            model.setSrno(cursor.getInt(0));
            model.setTagId(cursor.getInt(1));
            model.setAnimalType(cursor.getInt(2));
            model.setAquisation(cursor.getInt(3));
            model.setGender(cursor.getString(4));
            //model.setFemaleStatus(cursor.getString(5));
            model.setBreed(cursor.getInt(5));
            model.setDate(cursor.getString(6));
            model.setWeight(cursor.getString(7));
            model.setPurpose(cursor.getInt(8));
            model.setMotherId(cursor.getString(9));
            model.setPrice(cursor.getString(10));
            model.setRelease(cursor.getInt(11));
            model.setdDate(cursor.getString(12));
            model.setReleasePrice(cursor.getString(13));
            model.setReleaseWeight(cursor.getString(14));
            model.setRemarks(cursor.getString(15));
            model.setDeleted(1);
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> userDeleteAnimalDetails(AddAnimalModel model, boolean flag) {
        SQLiteDatabase db = getWritableDatabase();
        HashMap<String, String> response = new HashMap<>();
        ContentValues cv = new ContentValues();
        cv.put(KEY_RELEASE, model.getRelease());
        cv.put(KEY_D_DATE, model.getdDate());
        cv.put(KEY_RELEASE_WEIGHT, model.getReleaseWeight());
        cv.put(KEY_RELEASE_PRICE, model.getReleasePrice());
        cv.put(KEY_REMARKS, model.getRemarks());
        cv.put(KEY_DELETED, model.getDeleted());
        long status = db.update(TABLE_ANIMAL_DETAILS, cv, KEY_TAG_ID + "= ?", new String[]{String.valueOf(model.getTagId())});
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + "=" + model.getTagId() + " AND " + KEY_DELETED + "=1";
        if (!flag) {
            selectQuery = "SELECT * FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + "=" + model.getTagId() + " AND " + KEY_DELETED + "=0";
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            response.put("error", "0");
            response.put("message", "success");
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public ArrayList<WeightModel> getAllWeightsRecords() {
        ArrayList<WeightModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_WEIGHTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            WeightModel model = new WeightModel();
            model.setSrno(cursor.getInt(0));
            model.setTagId(cursor.getInt(1));
            model.setDate(cursor.getString(2));
            model.setWeight(cursor.getString(3));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalWeightDetails(WeightModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAG_ID, model.getTagId());
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_WEIGHT, model.getWeight());
        long status = db.insert(TABLE_ANIMAL_WEIGHTS, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public int getVaccinesDueThisMonthAnimalsCount(String thisDay, String thisMonth, String thisYear) throws ParseException {
        SQLiteDatabase db = getReadableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date today = simpleDateFormat.parse(thisDay + "/" + thisMonth + "/" + thisYear);
        String selectQuery = "SELECT " + KEY_P_DATE + " FROM " + TABLE_ANIMAL_VACCINATION;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            Date recordDate = simpleDateFormat.parse(cursor.getString(0));
            assert recordDate != null;
            if (recordDate.after(today)) {
                count++;
            }
        }
        cursor.close();
        db.close();
        return count;
    }

    public ArrayList<VaccinationModel> getAllVaccinationsRecords() {
        ArrayList<VaccinationModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_VACCINATION;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            VaccinationModel model = new VaccinationModel();
            model.setSrno(cursor.getInt(0));
            model.setTagId(cursor.getInt(1));
            model.setVaccine(cursor.getInt(2));
            model.setDose(cursor.getInt(3));
            model.setDate(cursor.getString(4));
            model.setBooster(cursor.getInt(5));
            model.setProposedDate(cursor.getString(6));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalVaccinationDetails(VaccinationModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAG_ID, model.getTagId());
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_VACCINE, model.getVaccine());
        cv.put(KEY_BOOSTER, model.getBooster());
        cv.put(KEY_P_DATE, model.getProposedDate());
        cv.put(KEY_DOSE, model.getDose());
        long status = db.insert(TABLE_ANIMAL_VACCINATION, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public ArrayList<BreedingModel> getAllBreedingRecords() {
        ArrayList<BreedingModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_BREEDING;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            BreedingModel model = new BreedingModel();
            model.setSrno(cursor.getInt(0));
            model.setMaleId(cursor.getInt(1));
            model.setFemaleId(cursor.getInt(2));
            model.setMatingFlag(cursor.getString(3));
            model.setMatingDate(cursor.getString(4));
            model.setConfFlag(cursor.getString(5));
            model.setConfDate(cursor.getString(6));
            model.setDeliveryFlag(cursor.getString(7));
            model.setDeliveryDate(cursor.getString(8));
            model.setAbortion(cursor.getInt(9));
            model.setStillborn(cursor.getInt(10));
            model.setChildMale(cursor.getInt(11));
            model.setChildFemale(cursor.getInt(12));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalBreedingDetails(BreedingModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_MALE_ID, model.getMaleId());
        cv.put(KEY_FEMALE_ID, model.getFemaleId());
        cv.put(KEY_MATING_FLAG, model.getMatingFlag());
        cv.put(KEY_MATING_DATE, model.getMatingDate());
        cv.put(KEY_CONF_FLAG, model.getConfFlag());
        cv.put(KEY_CONF_DATE, model.getConfDate());
        cv.put(KEY_DELIVERY_FLAG, model.getDeliveryFlag());
        cv.put(KEY_DELIVERY_DATE, model.getDeliveryDate());
        cv.put(KEY_ABORTION, model.getAbortion());
        cv.put(KEY_STILLBORN, model.getStillborn());
        cv.put(KEY_CHILD_MALE, model.getChildMale());
        cv.put(KEY_CHILD_FEMALE, model.getChildFemale());
        long status = db.insert(TABLE_ANIMAL_BREEDING, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public void updateBreedingDetails(BreedingModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_MATING_FLAG, model.getMatingFlag());
        cv.put(KEY_MATING_DATE, model.getMatingDate());
        cv.put(KEY_CONF_FLAG, model.getConfFlag());
        cv.put(KEY_CONF_DATE, model.getConfDate());
        cv.put(KEY_DELIVERY_FLAG, model.getDeliveryFlag());
        cv.put(KEY_DELIVERY_DATE, model.getDeliveryDate());
        cv.put(KEY_ABORTION, model.getAbortion());
        cv.put(KEY_STILLBORN, model.getStillborn());
        cv.put(KEY_CHILD_MALE, model.getChildMale());
        cv.put(KEY_CHILD_FEMALE, model.getChildFemale());
        long status = db.update(TABLE_ANIMAL_BREEDING, cv, KEY_SR_NO + "= ?", new String[]{String.valueOf(model.getSrno())});
        Log.i("CUSTOM", "insert status " + status);
        db.close();
    }

    public String getLastUpdatedDateExpenseTable() {
        int srno = -1;
        String date = null;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_SR_NO + ") FROM " + TABLE_ANIMAL_EXPENSE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            srno = cursor.getInt(0);
        }
        cursor.close();
        String dateQuery = "SELECT " + KEY_DATE + " FROM " + TABLE_ANIMAL_EXPENSE + " WHERE " + KEY_SR_NO + "=" + srno;
        cursor = db.rawQuery(dateQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            date = cursor.getString(0);
        }
        cursor.close();
        db.close();
        if (date == null) {
            date = "";
        }
        return date;
    }

    public float getTotalExpense() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_AMOUNT + " FROM " + TABLE_ANIMAL_EXPENSE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        float amount = 0;
        while (cursor.moveToNext()) {
            amount += Float.parseFloat(cursor.getString(0));
            Log.i("CUSTOM", "amount = " + amount);
        }
        cursor.close();
        db.close();
        return amount;
    }

    public ArrayList<ExpenseModel> getAllExpenseRecords() {
        ArrayList<ExpenseModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_EXPENSE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ExpenseModel model = new ExpenseModel();
            model.setSrno(cursor.getInt(0));
            model.setHead(cursor.getInt(1));
            model.setCash_cheque(cursor.getString(2));
            model.setDate(cursor.getString(3));
            model.setAmount(cursor.getString(4));
            model.setPaidTo(cursor.getString(5));
            model.setChequeNo(cursor.getInt(6));
            model.setBank(cursor.getString(7));
            model.setRemarks(cursor.getString(8));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalExpenseDetails(ExpenseModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_HEAD, model.getHead());
        cv.put(KEY_CASH_CHEQUE, model.getCash_cheque());
        cv.put(KEY_AMOUNT, model.getAmount());
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_PAID_TO, model.getPaidTo());
        cv.put(KEY_BANK, model.getBank());
        cv.put(KEY_REMARKS, model.getRemarks());
        cv.put(KEY_CHEQUE_NO, model.getChequeNo());
        long status = db.insert(TABLE_ANIMAL_EXPENSE, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public String getLastUpdatedDateIncomeTable() {
        int srno = -1;
        String date = null;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_SR_NO + ") FROM " + TABLE_ANIMAL_INCOME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            srno = cursor.getInt(0);
        }
        cursor.close();
        String dateQuery = "SELECT " + KEY_DATE + " FROM " + TABLE_ANIMAL_INCOME + " WHERE " + KEY_SR_NO + "=" + srno;
        cursor = db.rawQuery(dateQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            date = cursor.getString(0);
        }
        cursor.close();
        db.close();
        if (date == null) {
            date = "";
        }
        return date;
    }

    public float getTotalIncome() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_AMOUNT + " FROM " + TABLE_ANIMAL_INCOME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        float amount = 0;
        while (cursor.moveToNext()) {
            amount += Float.parseFloat(cursor.getString(0));
            Log.i("CUSTOM", "amount = " + amount);
        }
        cursor.close();
        db.close();
        return amount;
    }

    public ArrayList<IncomeModel> getAllIncomeRecords() {
        ArrayList<IncomeModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_INCOME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            IncomeModel model = new IncomeModel();
            model.setSrno(cursor.getInt(0));
            model.setHead(cursor.getInt(1));
            model.setCash_cheque(cursor.getString(2));
            model.setDate(cursor.getString(3));
            model.setAmount(cursor.getString(4));
            model.setReceivedFrom(cursor.getString(5));
            model.setChequeNo(cursor.getInt(6));
            model.setBank(cursor.getString(7));
            model.setRemarks(cursor.getString(8));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalIncomeDetails(IncomeModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_HEAD, model.getHead());
        cv.put(KEY_CASH_CHEQUE, model.getCash_cheque());
        cv.put(KEY_AMOUNT, model.getAmount());
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_RECEIVED_FROM, model.getReceivedFrom());
        cv.put(KEY_BANK, model.getBank());
        cv.put(KEY_REMARKS, model.getRemarks());
        cv.put(KEY_CHEQUE_NO, model.getChequeNo());
        long status = db.insert(TABLE_ANIMAL_INCOME, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public float getTotalProduction() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_MILK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        float quantity = 0;
        while (cursor.moveToNext()) {
            if (cursor.getInt(1) == 1) {
                quantity += Float.parseFloat(cursor.getString(6));
                Log.i("CUSTOM", "amount = " + quantity);
            }
        }
        cursor.close();
        db.close();
        return quantity;
    }

    public float getTotalSales() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_MILK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        float quantity = 0;
        while (cursor.moveToNext()) {
            Log.i("CUSTOM", "getTotalSales() - while()");
            if (cursor.getInt(1) == 2) {
                quantity += Float.parseFloat(cursor.getString(6));
                Log.i("CUSTOM", "amount = " + quantity);
            }
        }
        cursor.close();
        db.close();
        return quantity;
    }

    public ArrayList<MilkModel> getAllMilkRecords() {
        Log.i("CUSTOM", "getAllMilkRecords()");
        ArrayList<MilkModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_MILK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            Log.i("CUSTOM", "getAllMilkRecords() - while()");
            MilkModel model = new MilkModel();
            model.setSrno(cursor.getInt(0));
            model.setAction(cursor.getInt(1));
            if (model.getAction() == 1) {
                model.setLot(cursor.getInt(2));
                if (model.getLot() == 2) {
                    model.setTagId(cursor.getInt(3));
                }
            } else {
                model.setPrice(cursor.getString(7));
            }
            model.setDate(cursor.getString(4));
            model.setTime(cursor.getInt(5));
            model.setQuantity(cursor.getString(6));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalMilkDetails(MilkModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (model.getAction() == 2) {
            String selectQuery = "SELECT " +
                    "SUM(" + KEY_QUANTITY + ") FROM " +
                    TABLE_ANIMAL_MILK +
                    " WHERE " +
                    KEY_DATE + " = '" + model.getDate() +
                    "' AND " +
                    KEY_ACTION + "=1" +
                    " GROUP BY " +
                    KEY_DATE;
            Log.i("CUSTOM", "milk insertion query = " + selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                Log.i("CUSTOM", "cursor > 0");
                cursor.moveToFirst();
                if (cursor.getFloat(0) < Float.parseFloat(model.getQuantity())) {
                    Log.i("CUSTOM", "weight is less");
                    response.put("error", "1");
                    response.put("message", "action_error");
                    cursor.close();
                    db.close();
                    return response;
                }
            } else {
                response.put("error", "1");
                response.put("message", "action_error");
                cursor.close();
                db.close();
                return response;
            }
        }
        cv.put(KEY_ACTION, model.getAction());
        if (model.getAction() == 1) {
            cv.put(KEY_LOT, model.getLot());
            if (model.getLot() == 2) {
                cv.put(KEY_TAG_ID, model.getTagId());
            }
        } else if (model.getAction() == 2) {
            cv.put(KEY_PRICE, model.getPrice());
        }
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_TIME, model.getTime());
        cv.put(KEY_QUANTITY, model.getQuantity());
        long status = db.insert(TABLE_ANIMAL_MILK, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public float getTotalInput() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_ACTION + " , " + KEY_WEIGHT + " FROM " + TABLE_ANIMAL_FEED;
        Cursor cursor = db.rawQuery(selectQuery, null);
        float weight = 0;
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == 1) {
                weight += Float.parseFloat(cursor.getString(1));
                Log.i("CUSTOM", "weight = " + weight);
            }
        }
        cursor.close();
        db.close();
        return weight;
    }

    public float getTotalOutput() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_ACTION + " , " + KEY_WEIGHT + " FROM " + TABLE_ANIMAL_FEED;
        Cursor cursor = db.rawQuery(selectQuery, null);
        float weight = 0;
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == 2) {
                weight += Float.parseFloat(cursor.getString(1));
                Log.i("CUSTOM", "weight = " + weight);
            }
        }
        cursor.close();
        db.close();
        return weight;
    }

    public ArrayList<FeedModel> getAllFeedRecords() {
        ArrayList<FeedModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ANIMAL_FEED;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            FeedModel model = new FeedModel();
            model.setSrno(cursor.getInt(0));
            model.setAction(cursor.getInt(1));
            model.setDate(cursor.getString(2));
            model.setTime(cursor.getInt(3));
            model.setFeedType(cursor.getInt(4));
            model.setWeight(cursor.getString(5));
            model.setRemarks(cursor.getString(6));
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, String> addAnimalFeedDetails(FeedModel model) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (model.getAction() == 2) {
            // 30/12/2019
           /* String selectQuery = "SELECT " +
                    "SUM(" + KEY_WEIGHT + ") FROM " +
                    TABLE_ANIMAL_FEED +
                    " WHERE " +
                    KEY_DATE + " = '" + model.getDate() +
                    "' AND " +
                    KEY_ACTION + "=1" +
                    " GROUP BY " +
                    KEY_DATE;*/

            //01/01/2020
            String selectQuery = "SELECT " +
                    "SUM(" + KEY_WEIGHT + "), " +
                    KEY_ACTION +
                    " FROM " +
                    TABLE_ANIMAL_FEED +
                    " GROUP BY " +
                    KEY_ACTION;
            Log.i("CUSTOM", "feed insertion query = " + selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 1) {
                Log.i("CUSTOM", "cursor > 0");
                cursor.moveToFirst();
                if (cursor.getInt(1) == 1) {
                    if (cursor.getFloat(0) < Float.parseFloat(model.getWeight())) {
                        Log.i("CUSTOM", "weight is less");
                        response.put("error", "1");
                        response.put("message", "action_error");
                        cursor.close();
                        db.close();
                        return response;
                    }
                }
            } else if (cursor.getCount() == 2) {
                cursor.moveToFirst();
                float addWeight = 0, feedWeight = 0;
                if (cursor.getInt(1) == 1) {
                    addWeight = cursor.getFloat(0);
                } else {
                    feedWeight = cursor.getFloat(0);
                }
                cursor.moveToNext();
                if (cursor.getInt(1) == 1) {
                    addWeight = cursor.getFloat(0);
                } else {
                    feedWeight = cursor.getFloat(0);
                }
                if (addWeight < feedWeight) {
                    Log.i("CUSTOM", "weight is less");
                    response.put("error", "1");
                    response.put("message", "action_error");
                    cursor.close();
                    db.close();
                    return response;
                }
            } else {
                response.put("error", "1");
                response.put("message", "action_error");
                cursor.close();
                db.close();
                return response;
            }
        }
        cv.put(KEY_ACTION, model.getAction());
        cv.put(KEY_DATE, model.getDate());
        cv.put(KEY_TIME, model.getTime());
        cv.put(KEY_FEED_TYPE, model.getFeedType());
        cv.put(KEY_WEIGHT, model.getWeight());
        cv.put(KEY_REMARKS, model.getRemarks());
        long status = db.insert(TABLE_ANIMAL_FEED, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public HashMap<String, String> addNewValueToMastersTable(String data, String tableName) {
        int srno = -1;
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE " + KEY_FIELD + "='" + data + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            if (cursor.getInt(0) > 0) {
                response.put("error", "1");
                response.put("message", "duplicate_entry");
                cursor.close();
                db.close();
                return response;
            }
            cursor.close();
        }
        selectQuery = "SELECT MAX(" + KEY_SR_NO + ") FROM " + tableName;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            srno = cursor.getInt(0) + 1;
            cursor.close();
        }
        db.close();
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SR_NO, srno);
        cv.put(KEY_FIELD, data);
        long status = db.insert(tableName, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public HashMap<String, String> addNewValueToBreedTable(String data, int animalType) {
        int srno = -1;
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_BREED +
                " WHERE " + KEY_FIELD + "='" + data + "' AND " + KEY_ANIMAL_TYPE + "=" + animalType;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            if (cursor.getInt(0) > 0) {
                response.put("error", "1");
                response.put("message", "duplicate_entry");
                cursor.close();
                db.close();
                return response;
            }
            cursor.close();
        }
        selectQuery = "SELECT COUNT(*) FROM " + TABLE_BREED +
                " WHERE " + KEY_ANIMAL_TYPE + " = " + animalType +
                " GROUP BY " + KEY_ANIMAL_TYPE;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            srno = cursor.getInt(0) + 1;
            cursor.close();
        }
        db.close();
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SR_NO, srno);
        cv.put(KEY_FIELD, data);
        cv.put(KEY_ANIMAL_TYPE, animalType);
        long status = db.insert(TABLE_BREED, null, cv);
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public HashMap<String, String> editValueofMastersTable(String data, String tableName, int srno) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE " + KEY_FIELD + "='" + data + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            if (cursor.getInt(0) > 0) {
                response.put("error", "1");
                response.put("message", "duplicate_entry");
                cursor.close();
                db.close();
                return response;
            }
            cursor.close();
            db.close();
        }
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_FIELD, data);
        long status = db.update(tableName, cv, KEY_SR_NO + "= ?", new String[]{String.valueOf(srno)});
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;
    }

    public HashMap<String, String> editValueofBreedTable(String data, String selectedItem, int animalType) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_BREED +
                " WHERE " + KEY_FIELD + "='" + data + "' AND " + KEY_ANIMAL_TYPE + "=" + animalType;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            if (cursor.getInt(0) > 0) {
                response.put("error", "1");
                response.put("message", "duplicate_entry");
                cursor.close();
                db.close();
                return response;
            }
            cursor.close();
            db.close();
        }
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_FIELD, data);
        long status = db.update(TABLE_BREED, cv, KEY_FIELD + "= ? AND " + KEY_ANIMAL_TYPE + " = ?",
                new String[]{selectedItem, String.valueOf(animalType)});
        Log.i("CUSTOM", "insert status " + status);
        db.close();
        if (status >= 0) {
            response.put("error", "0");
            response.put("message", "success");
            db.close();
            return response;
        }
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;

    }

    public ArrayList<String> getAllSrNosByTable(String delTableName) {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_SR_NO + " FROM " + delTableName;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            Log.i("CUSTOM", "getAllMilkRecords() - while()");
            arrayList.add(String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public String getSingleRecordBySrNo(String tableName, int srno, boolean flag) {
        String data = "";
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "";
        if (flag) {
            selectQuery = "SELECT * FROM " + tableName + " WHERE " + KEY_SR_NO + "=" + srno;
        } else {
            selectQuery = "SELECT * FROM " + tableName + " WHERE " + KEY_TAG_ID + "=" + srno;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            switch (tableName) {
                case TABLE_ANIMAL_DETAILS: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.tag_id) + " : " + cursor.getInt(1) + "\n" +
                            context.getString(R.string.animal_type) + " : " + getFieldBySrNo(cursor.getInt(2), TABLE_ANIMAL_TYPE) + "\n" +
                            context.getString(R.string.aquisation) + " : " + getFieldBySrNo(cursor.getInt(3), TABLE_AQUISATION) + "\n" +
                            context.getString(R.string.gender) + " : " + cursor.getString(4) + "\n" +
                            context.getString(R.string.breed) + " : " + getBreedBySrNoAndAnimalType(cursor.getInt(5), cursor.getInt(2)) + "\n" +
                            context.getString(R.string.add) + " Date : " + cursor.getString(6) + "\n" +
                            context.getString(R.string.weight) + " : " + cursor.getString(7) + "\n" +
                            context.getString(R.string.purpose) + " : " + getFieldBySrNo(cursor.getInt(8), TABLE_PURPOSE) + "\n" +
                            (cursor.getString(9) != null ? (context.getString(R.string.mother_id) + " : " + cursor.getString(9) + "\n") : "") +
                            context.getString(R.string.price) + " : " + cursor.getString(10) + "\n" +
                            context.getString(R.string.release) + " : " + getFieldBySrNo(cursor.getInt(11), TABLE_RELEASE) + "\n" +
                            context.getString(R.string.delete) + " Date : " + cursor.getString(12) + "\n" +
                            "Release " + context.getString(R.string.price) + " : " + cursor.getString(13) + "\n" +
                            "Release " + context.getString(R.string.weight) + " : " + cursor.getString(14) + "\n" +
                            context.getString(R.string.remarks) + " : " + cursor.getString(15);
                    return data;
                }
                case TABLE_ANIMAL_EXPENSE: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.action) + " : " + getFieldBySrNo(cursor.getInt(1), TABLE_EXPENSE) + "\n" +
                            context.getString(R.string.payment_mode) + " : " + cursor.getString(2) + "\n" +
                            ((cursor.getString(2).equals("cheque"))
                                    ?
                                    (context.getString(R.string.cheque_no) + " : " + cursor.getString(6) + "\n" +
                                            context.getString(R.string.bank) + " : " + cursor.getString(7) + "\n")
                                    :
                                    ""
                            ) +
                            context.getString(R.string.date) + " : " + cursor.getString(3) + "\n" +
                            context.getString(R.string.amount) + " : Rs. " + cursor.getString(4) + "\n" +
                            context.getString(R.string.paid_to) + " : " + cursor.getString(5) + "\n" +
                            context.getString(R.string.remarks) + " : " + ((cursor.getString(8) != null) ? cursor.getString(8) : "NA");
                    cursor.close();
                    db.close();
                    return data;
                }
                case TABLE_ANIMAL_INCOME: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.action) + " : " + getFieldBySrNo(cursor.getInt(1), TABLE_INCOME) + "\n" +
                            context.getString(R.string.payment_mode) + " : " + cursor.getString(2) + "\n" +
                            ((cursor.getString(2).equals("cheque"))
                                    ?
                                    (context.getString(R.string.cheque_no) + " : " + cursor.getString(6) + "\n" +
                                            context.getString(R.string.bank) + " : " + cursor.getString(7) + "\n")
                                    :
                                    ""
                            ) +
                            context.getString(R.string.date) + " : " + cursor.getString(3) + "\n" +
                            context.getString(R.string.amount) + " : Rs. " + cursor.getString(4) + "\n" +
                            context.getString(R.string.paid_to) + " : " + cursor.getString(5) + "\n" +
                            context.getString(R.string.remarks) + " : " + ((cursor.getString(8) != null) ? cursor.getString(8) : "NA");
                    cursor.close();
                    db.close();
                    return data;
                }
                case TABLE_ANIMAL_WEIGHTS: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.tag_id) + " : " + cursor.getInt(1) + "\n" +
                            context.getString(R.string.date) + " : " + cursor.getString(2) + "\n" +
                            context.getString(R.string.weight) + " : " + cursor.getString(3) + " kg.";
                    cursor.close();
                    db.close();
                    return data;
                }
                case TABLE_ANIMAL_FEED: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.action) + " : " + getFieldBySrNo(cursor.getInt(1), TABLE_ACTION_FEED) + "\n" +
                            context.getString(R.string.date) + " : " + cursor.getString(2) + "\n" +
                            context.getString(R.string.time) + " : " +
                            ((cursor.getInt(3) != 0)
                                    ?
                                    cursor.getInt(3)
                                    :
                                    "NA") + "\n" +
                            context.getString(R.string.feed_type) + " : " + getFieldBySrNo(cursor.getInt(4), TABLE_FEED_TYPE) + "\n" +
                            context.getString(R.string.weight) + " : " + cursor.getString(5) + " kg.\n" +
                            context.getString(R.string.remarks) + " : " + cursor.getString(6)
                    ;
                    cursor.close();
                    db.close();
                    return data;
                }
                case TABLE_ANIMAL_MILK: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.action) + " : " + getFieldBySrNo(cursor.getInt(1), TABLE_ACTION_MILK) + "\n" +
                            ((cursor.getInt(1) == 1)
                                    ?
                                    ((cursor.getInt(2) == 2)
                                            ?
                                            (context.getString(R.string.lot) + " : Individual\n" +
                                                    context.getString(R.string.tag_id) + " : " + cursor.getInt(3) + "\n")
                                            :
                                            context.getString(R.string.lot) + " : Farm\n"
                                    )
                                    :
                                    context.getString(R.string.price) + " : Rs. " + cursor.getString(7) + "\n"
                            ) +
                            context.getString(R.string.date) + " : " + cursor.getString(4) + "\n" +
                            context.getString(R.string.time) + " : " +
                            ((cursor.getInt(5) != 0)
                                    ?
                                    cursor.getInt(5)
                                    :
                                    "NA") + "\n" +
                            context.getString(R.string.quantity) + " : " + cursor.getString(6) + " litres";
                    cursor.close();
                    db.close();
                    return data;
                }
                case TABLE_ANIMAL_VACCINATION: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.tag_id) + " : " + cursor.getInt(1) + "\n" +
                            context.getString(R.string.vaccine) + " : " + getFieldBySrNo(cursor.getInt(2), TABLE_VACCINE) + "\n" +
                            context.getString(R.string.dose) + " : " + cursor.getInt(3) + " ml.\n" +
                            context.getString(R.string.date) + " : " + cursor.getString(4) + "\n" +
                            context.getString(R.string.booster) + " : " + getFieldBySrNo(cursor.getInt(5), TABLE_VACCINE) + "\n" +
                            context.getString(R.string.proposed_date) + " : " + cursor.getString(6);
                    cursor.close();
                    db.close();
                    return data;
                }
                case TABLE_ANIMAL_BREEDING: {
                    data = context.getString(R.string.record_no) + " : " + cursor.getInt(0) + "\n" +
                            context.getString(R.string.male_id) + " : " +
                            ((cursor.getInt(1) != 0)
                                    ?
                                    cursor.getInt(1)
                                    :
                                    context.getString(R.string.unknown)) + "\n" +
                            context.getString(R.string.female_id) + " : " + cursor.getInt(2) + "\n" +
                            context.getString(R.string.mating) + " " + context.getString(R.string.date) + " : " +
                            ((cursor.getString(4) != null)
                                    ?
                                    cursor.getString(4)
                                    :
                                    "NA") + "\n" +
                            context.getString(R.string.confirmation) + " " + context.getString(R.string.date) + " : " +
                            ((cursor.getString(6) != null)
                                    ?
                                    cursor.getString(6)
                                    :
                                    "NA") + "\n" +
                            context.getString(R.string.delivery) + " " + context.getString(R.string.date) + " : " +
                            ((cursor.getString(8) != null)
                                    ?
                                    cursor.getString(8)
                                    :
                                    "NA") + "\n" +
                            context.getString(R.string.abortion) + " : " + cursor.getInt(9) + "\n" +
                            context.getString(R.string.stillborn) + " : " + cursor.getInt(10) + "\n" +
                            context.getString(R.string.male_child) + " : " + cursor.getInt(11) + "\n" +
                            context.getString(R.string.female_child) + " : " + cursor.getInt(12);
                    cursor.close();
                    db.close();
                    return data;
                }
            }
        }
        cursor.close();
        db.close();
        return data;
    }

    public HashMap<String, String> deleteSingleRecord(int srno, String delTableName) {
        HashMap<String, String> response = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        long status = db.delete(delTableName, KEY_SR_NO + "= ?", new String[]{String.valueOf(srno)});
        db.close();
        db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + delTableName + " WHERE " + KEY_SR_NO + "=" + srno;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (status == 1) {
            response.put("error", "0");
            response.put("message", "success");
            cursor.close();
            db.close();
            return response;
        }
        cursor.close();
        db.close();
        response.put("error", "1");
        response.put("message", "failure");
        return response;

    }

    public ArrayList<ReportLiveStockModel> getReportLiveStock(String fromDate, String toDate) {
        ArrayList<ReportLiveStockModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + ", " +
                TABLE_BREED + "." + KEY_FIELD + ", " +
                TABLE_AQUISATION + "." + KEY_FIELD + " as 'action', " +
                TABLE_AQUISATION + "." + KEY_FIELD + ", " +
                TABLE_RELEASE + "." + KEY_FIELD + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_GENDER + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_DATE +
                " FROM " +
                TABLE_ANIMAL_DETAILS + ", " +
                TABLE_RELEASE + ", " +
                TABLE_ANIMAL_TYPE + ", " +
                TABLE_BREED + ", " +
                TABLE_AQUISATION +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_ANIMAL_TYPE + "." + KEY_SR_NO + " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + TABLE_BREED + "." + KEY_SR_NO + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_BREED + "." + KEY_ANIMAL_TYPE +
                ")" +
                " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_AQUISATION + " = " + TABLE_AQUISATION + "." + KEY_SR_NO + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_RELEASE + " = " + TABLE_RELEASE + "." + KEY_SR_NO +
                " UNION ALL " +
                "SELECT " +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + ", " +
                TABLE_BREED + "." + KEY_FIELD + ", " +
                TABLE_RELEASE + "." + KEY_FIELD + " as 'action', " +
                TABLE_AQUISATION + "." + KEY_FIELD + ", " +
                TABLE_RELEASE + "." + KEY_FIELD + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_GENDER + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_DATE +
                " FROM " +
                TABLE_ANIMAL_DETAILS + ", " +
                TABLE_RELEASE + ", " +
                TABLE_ANIMAL_TYPE + ", " +
                TABLE_BREED + ", " +
                TABLE_AQUISATION +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_ANIMAL_TYPE + "." + KEY_SR_NO + " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + TABLE_BREED + "." + KEY_SR_NO + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_BREED + "." + KEY_ANIMAL_TYPE +
                ")" +
                " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_AQUISATION + " = " + TABLE_AQUISATION + "." + KEY_SR_NO + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_RELEASE + " = " + TABLE_RELEASE + "." + KEY_SR_NO +
                " ORDER BY " +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + " ASC," +
                TABLE_BREED + "." + KEY_FIELD + " ASC " +
                ";";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportLiveStockModel model = new ReportLiveStockModel();
            String modelDate = "";
            model.setAnimalType(cursor.getString(0));
            model.setBreed(cursor.getString(1));
            model.setAction(cursor.getString(2));
            model.setAquisation(cursor.getString(3));
            model.setRelease(cursor.getString(4));
            model.setGender(cursor.getString(5));
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                modelDate = cursor.getString(6);
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(modelDate, fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(modelDate, toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + model.getAnimalType() +
                    " breed = " + model.getBreed() +
                    " action = " + model.getAction() +
                    " aquisation = " + model.getAquisation() +
                    " release = " + model.getRelease() +
                    " gender = " + model.getGender());
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportWeightModel> getReportWeight(int animalType, int breed) {
        ArrayList<ReportWeightModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_WEIGHTS + ".*, " +
                TABLE_ANIMAL_DETAILS + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_WEIGHT +
                " FROM " +
                TABLE_ANIMAL_DETAILS + ", " +
                TABLE_ANIMAL_WEIGHTS +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_TAG_ID + " = " + TABLE_ANIMAL_WEIGHTS + "." + KEY_TAG_ID +
                " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + animalType +
                " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + breed +
                ")" +
                " ORDER BY " + KEY_TAG_ID +
                ";";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportWeightModel model = new ReportWeightModel();
            model.setTagId(cursor.getInt(1));
            model.setDate(cursor.getString(2));
            model.setWeight(cursor.getFloat(3));
            model.setFirstDate(cursor.getString(4));
            model.setFirstWeight(cursor.getFloat(5));
            model.setLastDate("");
            arrayList.add(model);
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal_type = " + animalType +
                    " breed = " + breed +
                    " tag_id = " + model.getTagId() +
                    " first_date = " + model.getFirstDate() +
                    " first_weight = " + model.getFirstWeight() +
                    " weight = " + model.getWeight() +
                    " date = " + model.getDate());
        }
        cursor.close();

        selectQuery = "SELECT " + KEY_TAG_ID + "," +
                KEY_DATE + ", " +
                KEY_WEIGHT + " " +
                " FROM " + TABLE_ANIMAL_DETAILS +
                " WHERE " +
                KEY_ANIMAL_TYPE + " = " + animalType + " AND " + KEY_BREED + " = " + breed + " AND " +
                KEY_TAG_ID + " NOT IN ( SELECT " +
                KEY_TAG_ID + " FROM " + TABLE_ANIMAL_WEIGHTS
                + ")" +
                ";";
        cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ReportWeightModel model = new ReportWeightModel();
            model.setTagId(cursor.getInt(0));
            model.setFirstDate(cursor.getString(1));
            model.setFirstWeight(cursor.getFloat(2));
            model.setLastWeight(0);
            model.setLastDate("NA");
            arrayList.add(model);
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal_type = " + animalType +
                    " breed = " + breed +
                    " tag_id = " + model.getTagId() +
                    " first_date = " + model.getFirstDate() +
                    " first_weight = " + model.getFirstWeight() +
                    " weight = " + model.getWeight() +
                    " date = " + model.getDate() +
                    " last_date = " + model.getLastDate());
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportVaccinationModel> getReportVaccination(String fromDate, String toDate, int animalType, int breed) {
        ArrayList<ReportVaccinationModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_DATE + ", " +
                TABLE_VACCINE + "." + KEY_FIELD + ", " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_TAG_ID + ", " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_DOSE + " " +
                " FROM " +
                TABLE_ANIMAL_DETAILS + ", " +
                TABLE_VACCINE + ", " +
                TABLE_ANIMAL_VACCINATION +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_TAG_ID + " = " + TABLE_ANIMAL_VACCINATION + "." + KEY_TAG_ID + " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + breed + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + animalType +
                ")" +
                " AND " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_VACCINE + " = " + TABLE_VACCINE + "." + KEY_SR_NO +
                ";";
        Log.i("CUSTOM", "vaccination select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        while (cursor.moveToNext()) {
            ReportVaccinationModel model = new ReportVaccinationModel();
            model.setDate(cursor.getString(0));
            model.setVaccine(cursor.getString(1));
            model.setTagId(cursor.getInt(2));
            model.setDose(cursor.getInt(3));
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + model.getAnimalType() +
                    " breed = " + model.getBreed() +
                    " date = " + model.getDate() +
                    " vaccine = " + model.getVaccine() +
                    " tag_id = " + model.getTagId() +
                    " dose = " + model.getDose());
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportBreedingModel> getReportBreeding(String fromDate, String toDate, int animalType, int breed) {
        ArrayList<ReportBreedingModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT DISTINCT " +
                TABLE_ANIMAL_BREEDING + ".*" +
                " FROM " +
                TABLE_ANIMAL_DETAILS + ", " +
                TABLE_ANIMAL_BREEDING +
                " WHERE " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_TAG_ID + " = " + TABLE_ANIMAL_BREEDING + "." + KEY_MALE_ID +
                " OR " +
                TABLE_ANIMAL_DETAILS + "." + KEY_TAG_ID + " = " + TABLE_ANIMAL_BREEDING + "." + KEY_FEMALE_ID +
                ")" +
                " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + breed + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + animalType +
                ")" +
                ";";
        Log.i("CUSTOM", "breeding select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        while (cursor.moveToNext()) {
            ReportBreedingModel model = new ReportBreedingModel();
            model.setMaleId(cursor.getInt(1));
            model.setFemaleId(cursor.getInt(2));
            model.setMatingDate(cursor.getString(4));
            model.setConfDate(cursor.getString(6));
            model.setDelDate(cursor.getString(8));
            model.setAbortion(cursor.getInt(9));
            model.setStillBorn(cursor.getInt(10));
            model.setMaleChild(cursor.getInt(11));
            model.setFemaleChild(cursor.getInt(12));
            boolean anyOne = false;
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (model.getMatingDate() != null) {
                        if (!dialogDateUtil.isProposedDateBeforeDate(model.getMatingDate(), fromDate) &&
                                !dialogDateUtil.isProposedDateAfterDate(model.getMatingDate(), toDate)) {
                            anyOne = true;
                        }
                    }
                    if (model.getConfDate() != null) {
                        if (!dialogDateUtil.isProposedDateBeforeDate(model.getConfDate(), fromDate) &&
                                !dialogDateUtil.isProposedDateAfterDate(model.getConfDate(), toDate)) {
                            anyOne = true;
                        }
                    }
                    if (model.getDelDate() != null) {
                        if (!dialogDateUtil.isProposedDateBeforeDate(model.getDelDate(), fromDate) &&
                                !dialogDateUtil.isProposedDateAfterDate(model.getDelDate(), toDate)) {
                            anyOne = true;
                        }
                    }
                    if (anyOne) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + animalType +
                    " breed = " + breed +
                    " mating_date = " + model.getMatingDate() +
                    " conf_date = " + model.getMatingDate() +
                    " del_date = " + model.getMatingDate() +
                    " abortion = " + model.getAbortion() +
                    " stillborn = " + model.getStillBorn() +
                    " male_child = " + model.getMaleChild() +
                    " female_child = " + model.getFemaleChild()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportFeedStockModel> getReportFeedStock(String fromDate, String toDate, int itemType) {
        ArrayList<ReportFeedStockModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        /*String selectQuery = "SELECT " +
                TABLE_ANIMAL_FEED + "." + KEY_ACTION + ", " +
                TABLE_ANIMAL_FEED + "." + KEY_DATE + ", " +
                "SUM(" + TABLE_ANIMAL_FEED + "." + KEY_WEIGHT + ")" +
                " FROM " +
                TABLE_ANIMAL_FEED +
                " WHERE " +
                TABLE_ANIMAL_FEED + "." + KEY_FEED_TYPE + " = " + itemType +
                " GROUP BY " +
                TABLE_ANIMAL_FEED + "." + KEY_ACTION + ", " +
                TABLE_ANIMAL_FEED + "." + KEY_DATE +
                ";";*/
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_FEED + "." + KEY_ACTION + ", " +
                TABLE_ANIMAL_FEED + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_FEED + "." + KEY_WEIGHT +
                " FROM " +
                TABLE_ANIMAL_FEED +
                " WHERE " +
                TABLE_ANIMAL_FEED + "." + KEY_FEED_TYPE + " = " + itemType +
                ";";
        Log.i("CUSTOM", "feed stock select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
        while (cursor.moveToNext()) {
            ReportFeedStockModel model = new ReportFeedStockModel();
            model.setAction(cursor.getInt(0));
            model.setDate(cursor.getString(1));
            model.setQuantity(cursor.getFloat(2));
            if (model.getAction() == 2) {
                model.setQuantity(-cursor.getFloat(2));
            } else if (model.getAction() == 1) {
                model.setQuantity(cursor.getFloat(2));
            }
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " item = " + itemType +
                    " action = " + model.getAction() +
                    " date = " + model.getDate() +
                    " quantity = " + model.getQuantity() +
                    " total_quantity = " + model.getTotalQuantity()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportMilkModel> getReportMilk(String fromDate, String toDate) {
        ArrayList<ReportMilkModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        /*String selectQuery = "SELECT " +
                TABLE_ANIMAL_MILK + "." + KEY_ACTION + ", " +
                TABLE_ANIMAL_MILK + "." + KEY_DATE + ", " +
                "SUM(" + TABLE_ANIMAL_MILK + "." + KEY_QUANTITY + ")" +
                " FROM " +
                TABLE_ANIMAL_MILK +
                " GROUP BY " +
                TABLE_ANIMAL_MILK + "." + KEY_ACTION + ", " +
                TABLE_ANIMAL_MILK + "." + KEY_DATE +
                ";";*/
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_MILK + "." + KEY_ACTION + ", " +
                TABLE_ANIMAL_MILK + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_MILK + "." + KEY_QUANTITY +
                " FROM " +
                TABLE_ANIMAL_MILK +
                ";";
        Log.i("CUSTOM", "feed stock select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
        while (cursor.moveToNext()) {
            ReportMilkModel model = new ReportMilkModel();
            model.setAction(cursor.getInt(0));
            model.setDate(cursor.getString(1));
            if (model.getAction() == 2) {
                model.setQuantity(-cursor.getFloat(2));
            } else if (model.getAction() == 1) {
                model.setQuantity(cursor.getFloat(2));
            }
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " action = " + model.getAction() +
                    " date = " + model.getDate() +
                    " quantity = " + model.getQuantity() +
                    " total_quantity = " + model.getTotalQuantity()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportExpenseModel> getReportDetailExpense(String fromDate, String toDate, int itemType) {
        ArrayList<ReportExpenseModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                KEY_DATE + "," +
                KEY_CASH_CHEQUE + "," +
                KEY_AMOUNT + "," +
                KEY_PAID_TO +
                " FROM " +
                TABLE_ANIMAL_EXPENSE +
                " WHERE " +
                KEY_HEAD + " = " + itemType +
                ";";
        Log.i("CUSTOM", "expense detail select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        while (cursor.moveToNext()) {
            ReportExpenseModel model = new ReportExpenseModel();
            model.setDate(cursor.getString(0));
            model.setCashCheque(cursor.getString(1));
            model.setAmount(cursor.getFloat(2));
            model.setPaidTo(cursor.getString(3));
            model.setItemType(itemType);
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " head = " + model.getItemType() +
                    " date = " + model.getDate() +
                    " amount = " + model.getAmount() +
                    " paid to = " + model.getPaidTo() +
                    " cash_cheque = " + model.getCashCheque()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportExpenseModel> getReportSummaryExpense(String fromDate, String toDate) {
        ArrayList<ReportExpenseModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_EXPENSE + "." + KEY_FIELD + "," +
                " SUM(" + TABLE_ANIMAL_EXPENSE + "." + KEY_AMOUNT + "), " +
                TABLE_ANIMAL_EXPENSE + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_EXPENSE + "." + KEY_HEAD +
                " FROM " +
                TABLE_ANIMAL_EXPENSE + ", " +
                TABLE_EXPENSE +
                " WHERE " +
                TABLE_ANIMAL_EXPENSE + "." + KEY_HEAD + " = " + TABLE_EXPENSE + "." + KEY_SR_NO +
                " GROUP BY " +
                TABLE_ANIMAL_EXPENSE + "." + KEY_HEAD +
                " ORDER BY " +
                TABLE_EXPENSE + "." + KEY_FIELD +
                ";";
        Log.i("CUSTOM", "expense summary select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        while (cursor.moveToNext()) {
            ReportExpenseModel model = new ReportExpenseModel();
            model.setHead(cursor.getString(0));
            model.setAmount(cursor.getFloat(1));
            model.setDate(cursor.getString(2));
            model.setItemType(cursor.getInt(3));
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " head = " + model.getHead() +
                    " amount = " + model.getAmount()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportIncomeModel> getReportDetailIncome(String fromDate, String toDate, int itemType) {
        ArrayList<ReportIncomeModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                KEY_DATE + "," +
                KEY_CASH_CHEQUE + "," +
                KEY_AMOUNT + "," +
                KEY_RECEIVED_FROM +
                " FROM " +
                TABLE_ANIMAL_INCOME +
                " WHERE " +
                KEY_HEAD + " = " + itemType +
                ";";
        Log.i("CUSTOM", "income detail select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        while (cursor.moveToNext()) {
            ReportIncomeModel model = new ReportIncomeModel();
            model.setDate(cursor.getString(0));
            model.setCashCheque(cursor.getString(1));
            model.setAmount(cursor.getFloat(2));
            model.setReceivedFrom(cursor.getString(3));
            model.setItemType(itemType);
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        Log.i("CUSTOM", "date in between");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date not in between");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " head = " + model.getItemType() +
                    " date = " + model.getDate() +
                    " amount = " + model.getAmount() +
                    " received_from = " + model.getReceivedFrom() +
                    " cash_cheque = " + model.getCashCheque()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportIncomeModel> getReportSummaryIncome(String fromDate, String toDate) {
        ArrayList<ReportIncomeModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_INCOME + "." + KEY_FIELD + "," +
                " SUM(" + TABLE_ANIMAL_INCOME + "." + KEY_AMOUNT + "), " +
                TABLE_ANIMAL_INCOME + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_INCOME + "." + KEY_HEAD +
                " FROM " +
                TABLE_ANIMAL_INCOME + ", " +
                TABLE_INCOME +
                " WHERE " +
                TABLE_ANIMAL_INCOME + "." + KEY_HEAD + " = " + TABLE_INCOME + "." + KEY_SR_NO +
                " GROUP BY " +
                TABLE_ANIMAL_INCOME + "." + KEY_HEAD +
                " ORDER BY " +
                TABLE_INCOME + "." + KEY_FIELD +
                ";";
        Log.i("CUSTOM", "expense summary select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            Log.i("CUSTOM", "loading without Filter");
        } else {
            Log.i("CUSTOM", "loading with Filter");
        }
        while (cursor.moveToNext()) {
            ReportIncomeModel model = new ReportIncomeModel();
            model.setHead(cursor.getString(0));
            model.setAmount(cursor.getFloat(1));
            model.setDate(cursor.getString(2));
            model.setItemType(cursor.getInt(3));
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (!dialogDateUtil.isProposedDateBeforeDate(model.getDate(), fromDate) &&
                            !dialogDateUtil.isProposedDateAfterDate(model.getDate(), toDate)) {
                        arrayList.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                arrayList.add(model);
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " head = " + model.getHead() +
                    " amount = " + model.getAmount()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public SparseIntArray getReportSubVaccination(String fromDate, String toDate, int animalType, int breed) {
        SparseIntArray sparseIntArray = new SparseIntArray();
        DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
        SQLiteDatabase db = getReadableDatabase();

        //Total Number of Animals in Farm
        String selectQuery = "SELECT " + KEY_DATE + " FROM " + TABLE_ANIMAL_DETAILS +
                " WHERE " + KEY_ANIMAL_TYPE + " = " + animalType + " AND " + KEY_BREED + " = " + breed;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            int count = 0;
            while (cursor.moveToNext()) {
                try {
                    if (fromDate.isEmpty() && toDate.isEmpty()) {
                        count++;
                    } else {
                        if (!dialogDateUtil.isProposedDateBeforeDate(cursor.getString(0), fromDate) &&
                                !dialogDateUtil.isProposedDateAfterDate(cursor.getString(0), toDate)) {
                            count++;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            sparseIntArray.put(1, count);
            Log.i("CUSTOM", "total animals = " + count);
        }
        cursor.close();

        //Total Vaccinations done till Date
        selectQuery = "SELECT " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_DATE +
                " FROM "
                + TABLE_ANIMAL_VACCINATION + ", " +
                TABLE_ANIMAL_DETAILS +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_TAG_ID + " = " + TABLE_ANIMAL_VACCINATION + "." + KEY_TAG_ID + " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + breed + " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + animalType +
                ")" +
                ";";
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            int count = 0;
            while (cursor.moveToNext()) {
                try {
                    if (fromDate.isEmpty() && toDate.isEmpty()) {
                        count++;
                    } else {
                        if (!dialogDateUtil.isProposedDateBeforeDate(cursor.getString(0), fromDate) &&
                                !dialogDateUtil.isProposedDateAfterDate(cursor.getString(0), toDate)) {
                            count++;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            sparseIntArray.put(2, count);
            Log.i("CUSTOM", "total vaccinations = " + count);
        }
        cursor.close();
        db.close();
        return sparseIntArray;
    }

    public ArrayList<Float> getReportMenuValues() {
        ArrayList<Float> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        //Livestock
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_ANIMAL_DETAILS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrayList.add(cursor.getFloat(0));
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        //Weight
        /*selectQuery = "SELECT SUM(" + KEY_WEIGHT + ") FROM " + TABLE_ANIMAL_WEIGHTS;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrayList.add(cursor.getFloat(0));
        } else {
            arrayList.add(0f);
        }
        cursor.close();*/
        selectQuery = "SELECT " +
                KEY_WEIGHT + ", " +
                KEY_TAG_ID +
                " FROM " +
                TABLE_ANIMAL_WEIGHTS +
                " GROUP BY " +
                KEY_TAG_ID
        ;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            float lastWeightTotal = 0;
            while (cursor.moveToNext()) {
                lastWeightTotal += cursor.getFloat(0);
            }
            cursor.close();
            selectQuery = "SELECT " +
                    KEY_WEIGHT +
                    " FROM " +
                    TABLE_ANIMAL_DETAILS +
                    " WHERE " +
                    KEY_TAG_ID +
                    " NOT IN " +
                    "(" +
                    " SELECT DISTINCT " +
                    KEY_TAG_ID +
                    " FROM " +
                    TABLE_ANIMAL_WEIGHTS +
                    ")";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    lastWeightTotal += cursor.getFloat(0);
                }
            }
            arrayList.add(lastWeightTotal);
        } else {
            arrayList.add(0f);
        }

        //Vaccination
        selectQuery = "SELECT " + KEY_DATE + " FROM " + TABLE_ANIMAL_VACCINATION;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            float count = 0;
            DialogDateUtil du = new DialogDateUtil(context);
            while (cursor.moveToNext()) {
                try {
                    if (du.isProposedDateBeforeDate(cursor.getString(0), du.getTodaysDate())) {
                        count++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //arrayList.add(cursor.getFloat(0));
            }
            arrayList.add(count);
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        //Breeding
        selectQuery = "SELECT SUM(" + KEY_CHILD_MALE + " + " + KEY_CHILD_FEMALE + ") FROM " + TABLE_ANIMAL_BREEDING;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrayList.add(cursor.getFloat(0));
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        //Feed Stock
        selectQuery = "SELECT SUM(" + KEY_WEIGHT + ")," +
                KEY_ACTION +
                " FROM " + TABLE_ANIMAL_FEED +
                " GROUP BY " + KEY_ACTION;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            float add = 0, sub = 0;
            if (cursor.getInt(1) == 1) {
                add = cursor.getFloat(0);
            } else {
                sub = cursor.getFloat(0);
            }
            arrayList.add(add - sub);
        } else if (cursor.getCount() == 2) {
            cursor.moveToFirst();
            float add = 0, sub = 0;
            add = cursor.getFloat(0);
            cursor.moveToNext();
            sub = cursor.getFloat(0);
            arrayList.add(add - sub);
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        //Milk
        selectQuery = "SELECT " +
                "SUM(" + KEY_QUANTITY + "), " +
                KEY_ACTION +
                " FROM " + TABLE_ANIMAL_MILK +
                " GROUP BY " + KEY_ACTION;
        cursor = db.rawQuery(selectQuery, null);
        Log.i("CUSTOM", "milk menu query = " + selectQuery + "\ncursor count = " + cursor.getCount());
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            float add = 0, sub = 0;
            if (cursor.getInt(1) == 1) {
                add = cursor.getFloat(0);
            } else {
                sub = cursor.getFloat(0);
            }
            arrayList.add(add - sub);
        } else if (cursor.getCount() == 2) {
            cursor.moveToFirst();
            float add = 0, sub = 0;
            add = cursor.getFloat(0);
            cursor.moveToNext();
            sub = cursor.getFloat(0);
            arrayList.add(add - sub);
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        //Expense
        selectQuery = "SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_ANIMAL_EXPENSE;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrayList.add(cursor.getFloat(0));
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        //Income
        selectQuery = "SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_ANIMAL_INCOME;
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrayList.add(cursor.getFloat(0));
        } else {
            arrayList.add(0f);
        }
        cursor.close();

        db.close();
        return arrayList;
    }

    public ArrayList<ReportIncomeModel> getReportDailyIncome(String selectedDate) {
        ArrayList<ReportIncomeModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_INCOME + "." + KEY_DATE + "," +
                TABLE_ANIMAL_INCOME + "." + KEY_CASH_CHEQUE + "," +
                TABLE_ANIMAL_INCOME + "." + KEY_AMOUNT + "," +
                TABLE_ANIMAL_INCOME + "." + KEY_RECEIVED_FROM + ", " +
                TABLE_INCOME + "." + KEY_FIELD +
                " FROM " +
                TABLE_INCOME + ", " +
                TABLE_ANIMAL_INCOME +
                " WHERE " +
                TABLE_ANIMAL_INCOME + "." + KEY_HEAD + " = " + TABLE_INCOME + "." + KEY_SR_NO +
                ";";
        Log.i("CUSTOM", "income daily select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportIncomeModel model = new ReportIncomeModel();
            model.setDate(cursor.getString(0));
            model.setCashCheque(cursor.getString(1));
            model.setAmount(cursor.getFloat(2));
            model.setReceivedFrom(cursor.getString(3));
            model.setHead(cursor.getString(4));
            if (!selectedDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (dialogDateUtil.isDateEqual(model.getDate(), selectedDate)) {
                        Log.i("CUSTOM", "date is equal");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date is not equal");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " head = " + model.getItemType() +
                    " date = " + model.getDate() +
                    " amount = " + model.getAmount() +
                    " received_from = " + model.getReceivedFrom() +
                    " cash_cheque = " + model.getCashCheque()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportExpenseModel> getReportDailyExpense(String selectedDate) {
        ArrayList<ReportExpenseModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_EXPENSE + "." + KEY_DATE + "," +
                TABLE_ANIMAL_EXPENSE + "." + KEY_CASH_CHEQUE + "," +
                TABLE_ANIMAL_EXPENSE + "." + KEY_AMOUNT + "," +
                TABLE_ANIMAL_EXPENSE + "." + KEY_PAID_TO + ", " +
                TABLE_EXPENSE + "." + KEY_FIELD +
                " FROM " +
                TABLE_EXPENSE + ", " +
                TABLE_ANIMAL_EXPENSE +
                " WHERE " +
                TABLE_ANIMAL_EXPENSE + "." + KEY_HEAD + " = " + TABLE_EXPENSE + "." + KEY_SR_NO +
                ";";
        Log.i("CUSTOM", "expense daily select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportExpenseModel model = new ReportExpenseModel();
            model.setDate(cursor.getString(0));
            model.setCashCheque(cursor.getString(1));
            model.setAmount(cursor.getFloat(2));
            model.setPaidTo(cursor.getString(3));
            model.setHead(cursor.getString(4));
            if (!selectedDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (dialogDateUtil.isDateEqual(model.getDate(), selectedDate)) {
                        Log.i("CUSTOM", "date is equal");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date is not equal");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " head = " + model.getItemType() +
                    " date = " + model.getDate() +
                    " amount = " + model.getAmount() +
                    " received_from = " + model.getPaidTo() +
                    " cash_cheque = " + model.getCashCheque()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportLiveStockModel> getReportDailyAddSummary(String selectedDate) {
        ArrayList<ReportLiveStockModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_DETAILS + "." + KEY_DATE + "," +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + "," +
                TABLE_BREED + "." + KEY_FIELD + "," +
                TABLE_AQUISATION + "." + KEY_FIELD +
                " FROM " +
                TABLE_ANIMAL_TYPE + ", " +
                TABLE_BREED + ", " +
                TABLE_AQUISATION + ", " +
                TABLE_ANIMAL_DETAILS +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_ANIMAL_TYPE + "." + KEY_SR_NO +
                " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + TABLE_BREED + "." + KEY_SR_NO +
                " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_BREED + "." + KEY_ANIMAL_TYPE +
                ") AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_AQUISATION + " = " + TABLE_AQUISATION + "." + KEY_SR_NO +
                " ORDER BY " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_AQUISATION +
                ";";
        Log.i("CUSTOM", "add summary daily select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportLiveStockModel model = new ReportLiveStockModel();
            model.setDate(cursor.getString(0));
            model.setAnimalType(cursor.getString(1));
            model.setBreed(cursor.getString(2));
            model.setAction(cursor.getString(3));
            if (!selectedDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (dialogDateUtil.isDateEqual(model.getDate(), selectedDate)) {
                        Log.i("CUSTOM", "date is equal");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date is not equal");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + model.getAnimalType() +
                    " breed = " + model.getBreed() +
                    " action = " + model.getAction() +
                    " date = " + model.getDate()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportLiveStockModel> getReportDailyDeleteSummary(String selectedDate) {
        ArrayList<ReportLiveStockModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " +
                TABLE_ANIMAL_DETAILS + "." + KEY_DATE + "," +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + "," +
                TABLE_BREED + "." + KEY_FIELD + "," +
                TABLE_RELEASE + "." + KEY_FIELD +
                " FROM " +
                TABLE_ANIMAL_TYPE + ", " +
                TABLE_BREED + ", " +
                TABLE_RELEASE + ", " +
                TABLE_ANIMAL_DETAILS +
                " WHERE " +
                TABLE_ANIMAL_DETAILS + "." + KEY_DELETED + " = 1 AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_ANIMAL_TYPE + "." + KEY_SR_NO +
                " AND " +
                "(" +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + " = " + TABLE_BREED + "." + KEY_SR_NO +
                " AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + " = " + TABLE_BREED + "." + KEY_ANIMAL_TYPE +
                ") AND " +
                TABLE_ANIMAL_DETAILS + "." + KEY_RELEASE + " = " + TABLE_RELEASE + "." + KEY_SR_NO +
                " ORDER BY " +
                TABLE_ANIMAL_DETAILS + "." + KEY_ANIMAL_TYPE + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_BREED + ", " +
                TABLE_ANIMAL_DETAILS + "." + KEY_RELEASE +
                ";";
        Log.i("CUSTOM", "del summary daily select query = " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportLiveStockModel model = new ReportLiveStockModel();
            model.setDate(cursor.getString(0));
            model.setAnimalType(cursor.getString(1));
            model.setBreed(cursor.getString(2));
            model.setAction(cursor.getString(3));
            if (!selectedDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (dialogDateUtil.isDateEqual(model.getDate(), selectedDate)) {
                        Log.i("CUSTOM", "date is equal");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date is not equal");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + model.getAnimalType() +
                    " breed = " + model.getBreed() +
                    " action = " + model.getAction() +
                    " date = " + model.getDate()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportWeightModel> getReportDailyWeightDone(String selectedDate) {
        ArrayList<ReportWeightModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT DISTINCT " +
                TABLE_ANIMAL_WEIGHTS + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_WEIGHTS + "." + KEY_TAG_ID + ", " +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + ", " +
                TABLE_BREED + "." + KEY_FIELD + ", " +
                TABLE_ANIMAL_WEIGHTS + "." + KEY_WEIGHT + " FROM " +
                TABLE_ANIMAL_TYPE + ", " +
                TABLE_BREED + ", " +
                TABLE_ANIMAL_WEIGHTS + " WHERE " +
                TABLE_ANIMAL_TYPE + "." + KEY_SR_NO + " = " +
                "(SELECT " + KEY_ANIMAL_TYPE + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + " = " + TABLE_ANIMAL_WEIGHTS + "." + KEY_TAG_ID + ")" +
                " AND " +
                TABLE_BREED + "." + KEY_SR_NO + " = " +
                "(SELECT " + KEY_BREED + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + " = " + TABLE_ANIMAL_WEIGHTS + "." + KEY_TAG_ID + ")" +
                " AND " +
                TABLE_BREED + "." + KEY_ANIMAL_TYPE + " = " + TABLE_ANIMAL_TYPE + "." + KEY_SR_NO +
                ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportWeightModel model = new ReportWeightModel();
            model.setDate(cursor.getString(0));
            model.setTagId(cursor.getInt(1));
            model.setAnimalType(cursor.getString(2));
            model.setBreed(cursor.getString(3));
            model.setWeight(cursor.getFloat(4));
            if (!selectedDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (dialogDateUtil.isDateEqual(model.getDate(), selectedDate)) {
                        Log.i("CUSTOM", "date is equal");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date is not equal");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + model.getAnimalType() +
                    " breed = " + model.getBreed() +
                    " weight = " + model.getWeight() +
                    " date = " + model.getDate()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<ReportVaccinationModel> getReportDailyVaccinationDone(String selectedDate) {
        ArrayList<ReportVaccinationModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT DISTINCT " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_DATE + ", " +
                TABLE_ANIMAL_VACCINATION + "." + KEY_TAG_ID + ", " +
                TABLE_ANIMAL_TYPE + "." + KEY_FIELD + ", " +
                TABLE_BREED + "." + KEY_FIELD + ", " +
                TABLE_VACCINE + "." + KEY_FIELD + " FROM " +
                TABLE_ANIMAL_TYPE + ", " +
                TABLE_BREED + ", " +
                TABLE_VACCINE + ", " +
                TABLE_ANIMAL_VACCINATION + " WHERE " +
                TABLE_ANIMAL_TYPE + "." + KEY_SR_NO + " = " +
                "(SELECT " + KEY_ANIMAL_TYPE + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + " = " + TABLE_ANIMAL_VACCINATION + "." + KEY_TAG_ID + ")" +
                " AND " +
                TABLE_BREED + "." + KEY_SR_NO + " = " +
                "(SELECT " + KEY_BREED + " FROM " + TABLE_ANIMAL_DETAILS + " WHERE " + KEY_TAG_ID + " = " + TABLE_ANIMAL_VACCINATION + "." + KEY_TAG_ID + ")" +
                " AND " +
                TABLE_BREED + "." + KEY_ANIMAL_TYPE + " = " + TABLE_ANIMAL_TYPE + "." + KEY_SR_NO +
                " AND " +
                TABLE_VACCINE + "." + KEY_SR_NO + " = " + TABLE_ANIMAL_VACCINATION + "." + KEY_VACCINE +
                ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            ReportVaccinationModel model = new ReportVaccinationModel();
            model.setDate(cursor.getString(0));
            model.setTagId(cursor.getInt(1));
            model.setAnimalTypeString(cursor.getString(2));
            model.setBreedString(cursor.getString(3));
            model.setVaccine(cursor.getString(4));
            if (!selectedDate.isEmpty()) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(context);
                try {
                    if (dialogDateUtil.isDateEqual(model.getDate(), selectedDate)) {
                        Log.i("CUSTOM", "date is equal");
                        arrayList.add(model);
                    } else {
                        Log.i("CUSTOM", "date is not equal");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i("CUSTOM", "Record " + (++count) +
                    " animal type = " + model.getAnimalType() +
                    " breed = " + model.getBreed() +
                    " weight = " + model.getVaccine() +
                    " date = " + model.getDate()
            );
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public HashMap<String, Float> getReportDailyFeedStockAndMilk(String selectedDate) {
        HashMap<String, Float> hashMap = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQueryFeed = "SELECT " +
                KEY_DATE + ", " +
                KEY_ACTION + ", " +
                KEY_WEIGHT +
                " FROM " +
                TABLE_ANIMAL_FEED;
        Cursor cursor = db.rawQuery(selectQueryFeed, null);
        float input = 0, output = 0;
        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            if (date.equals(selectedDate)) {
                int action = cursor.getInt(1);
                if (action == 1) {
                    input += cursor.getFloat(2);
                } else if (action == 2) {
                    output += cursor.getFloat(2);
                }
            }
        }
        hashMap.put("feed_input", input);
        hashMap.put("feed_output", output);
        cursor.close();


        selectQueryFeed = "SELECT " +
                KEY_DATE + ", " +
                KEY_ACTION + ", " +
                KEY_QUANTITY +
                " FROM " +
                TABLE_ANIMAL_MILK;
        cursor = db.rawQuery(selectQueryFeed, null);
        input = 0;
        output = 0;
        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            if (date.equals(selectedDate)) {
                int action = cursor.getInt(1);
                if (action == 1) {
                    input += cursor.getFloat(2);
                } else if (action == 2) {
                    output += cursor.getFloat(2);
                }
            }
        }
        hashMap.put("milk_input", input);
        hashMap.put("milk_output", output);
        cursor.close();
        db.close();

        return hashMap;
    }

    public boolean deleteAnimalPermanently(String tagId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ANIMAL_DETAILS, KEY_TAG_ID + "= ?", new String[]{tagId});
        db.delete(TABLE_ANIMAL_BREEDING, KEY_MALE_ID + "= ? OR " + KEY_FEMALE_ID + "= ?", new String[]{tagId, tagId});
        db.delete(TABLE_ANIMAL_MILK, KEY_TAG_ID + "= ?", new String[]{tagId});
        db.delete(TABLE_ANIMAL_VACCINATION, KEY_TAG_ID + "= ?", new String[]{tagId});
        db.delete(TABLE_ANIMAL_WEIGHTS, KEY_TAG_ID + "= ?", new String[]{tagId});
        db.close();
        return true;
    }

}

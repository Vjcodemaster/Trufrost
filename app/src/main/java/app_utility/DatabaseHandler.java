package app_utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.DownloadManager.COLUMN_ID;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KIOSK_DB";

    private static final String TABLE_MAIN_PRODUCTS = "TABLE_MAIN_PRODUCTS";

    private static final String TABLE_INDIVIDUAL_PRODUCTS = "TABLE_INDIVIDUAL_PRODUCTS";
    //DataBaseHelper table name
    /*private static final String TABLE_PERMANENT = "TABLE_PERMANENT";

    //DataBaseHelper table name (2nd table for all tab)
    private static final String TABLE_TEMPORARY = "TABLE_TEMPORARY";

    private static final String TABLE_EMPLOYEE = "TABLE_EMPLOYEE";*/

    // BEL_DATA Table Columns names
    /*private static final String KEY_ID = "_id";
    private static final String KEY_STALL_ID = "stall_id";
    private static final String KEY_EMP_ID = "emp_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TIME = "time";

    private static final String KEY_SCAN_ID = "scan_id";*/

    private static final String KEY_MAIN_PRODUCT_ID = "KEY_MAIN_PRODUCT_ID";

    private static final String KEY_ODOO_PRODUCT_ID = "KEY_ODOO_PRODUCT_ID";

    private static final String KEY_SUB_PRODUCT_ID = "KEY_SUB_PRODUCT_ID";

    private static final String KEY_SUB_PRODUCT_NAME = "KEY_SUB_PRODUCT_NAME";

    private static final String KEY_ID = "_id";

    private static final String KEY_MAIN_PRODUCT_NAMES = "KEY_MAIN_PRODUCT_NAMES";

    private static final String KEY_MAIN_PRODUCT_DESCRIPTION = "KEY_MAIN_PRODUCT_DESCRIPTION";

    private static final String KEY_MAIN_PRODUCT_IMAGES_PATH = "KEY_MAIN_PRODUCT_IMAGES_PATH";

    private static final String KEY_SUB_PRODUCT_CATEGORY_NAMES = "KEY_SUB_PRODUCT_CATEGORY_NAMES ";

    private static final String KEY_PRODUCT_CATEGORY_DESCRIPTION = "KEY_PRODUCT_CATEGORY_DESCRIPTION";

    private static final String KEY_PRODUCT_CATEGORY_IMAGES_PATH = "KEY_PRODUCT_CATEGORY_IMAGES_PATH";

    private static final String KEY_INDIVIDUAL_PRODUCT_NAMES = "KEY_INDIVIDUAL_PRODUCT_NAMES";
    
    private static final String KEY_INDIVIDUAL_PRODUCT_DESCRIPTION = "KEY_INDIVIDUAL_PRODUCT_DESCRIPTION";

    private static final String KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER = "KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER";

    private static final String KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE = "KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE";

    private static final String KEY_INDIVIDUAL_PRODUCT_ADDRESS = "KEY_INDIVIDUAL_PRODUCT_ADDRESS";

    private static final String KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH = "KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH";

    private static final String KEY_INDIVIDUAL_PRODUCT_VARIANT_NAMES = "KEY_INDIVIDUAL_PRODUCT_VARIANT_NAMES";

    private static final String KEY_INDIVIDUAL_PRODUCT_VARIANT_IMAGES = "KEY_INDIVIDUAL_PRODUCT_VARIANT_IMAGES";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    /*
    leaving gap between "CREATE TABLE" & TABLE_RECENT gives error watch out!
    Follow the below format
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_RECENT_TABLE = "CREATE TABLE " + TABLE_RECENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_TAGID + " TEXT, "
                + KEY_NAME + " TEXT, "
                + KEY_DESIGNATION + " TEXT, "
                + KEY_NUMBER + " TEXT, "
                + KEY_EMAILID + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_TIME + " TEXT, "
                + KEY_LAST_SEEN_TIME + " TEXT, "
                + KEY_MAJOR + " TEXT, "
                + KEY_MINOR + " TEXT, "
                + KEY_UUID + " TEXT, "
                + KEY_RSSI + " TEXT)";*/

        /*String CREATE_TABLE_EMPLOYEE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_EMP_ID + " TEXT, "
                + KEY_SCAN_ID + " TEXT)";*/


        String CREATE_ALL_MAIN_PRODUCTS = "CREATE TABLE " + TABLE_MAIN_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_MAIN_PRODUCT_NAMES + " TEXT, "
                + KEY_MAIN_PRODUCT_DESCRIPTION + " TEXT, "
                + KEY_SUB_PRODUCT_CATEGORY_NAMES + " TEXT)";

        /*String CREATE_ALL_MAIN_PRODUCTS = "CREATE TABLE " + TABLE_MAIN_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_MAIN_PRODUCT_NAMES + " TEXT, "
                + KEY_MAIN_PRODUCT_IMAGES_PATH + " TEXT, "
                + KEY_MAIN_PRODUCT_DESCRIPTION + " TEXT, "
                + KEY_PRODUCT_CATEGORY_NAMES + " TEXT, "
                + KEY_PRODUCT_CATEGORY_DESCRIPTION + " TEXT, "
                + KEY_PRODUCT_CATEGORY_IMAGES_PATH + " TEXT)";*/

        String CREATE_INDIVIDUAL_PRODUCTS = "CREATE TABLE " + TABLE_INDIVIDUAL_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_MAIN_PRODUCT_ID + " INTEGER, "
                + KEY_ODOO_PRODUCT_ID + " INTEGER, "
                + KEY_MAIN_PRODUCT_NAMES + " TEXT, "
                + KEY_SUB_PRODUCT_CATEGORY_NAMES + " TEXT, "
                + KEY_INDIVIDUAL_PRODUCT_NAMES + " TEXT, "
                + KEY_INDIVIDUAL_PRODUCT_DESCRIPTION + " TEXT, "
                + KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER + " TEXT, "
                + KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE + " TEXT, "
                + KEY_INDIVIDUAL_PRODUCT_ADDRESS + " TEXT, "
                + KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH + " TEXT)";
                //+ KEY_INDIVIDUAL_PRODUCT_VARIANT_NAMES + " TEXT, "
                //+ KEY_INDIVIDUAL_PRODUCT_VARIANT_IMAGES + " TEXT)";

        /*String CREATE_TABLE_TEMPORARY = "CREATE TABLE " + TABLE_TEMPORARY + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_STALL_ID + " TEXT, "
                + KEY_EMP_ID + " TEXT, "
                + KEY_SCAN_ID + " TEXT, "
                + KEY_AMOUNT + " TEXT, "
                + KEY_TIME + " TEXT)";*/

        //db.execSQL(CREATE_TABLE_EMPLOYEE);
        //db.execSQL(CREATE_TABLE_TEMPORARY);
        db.execSQL(CREATE_ALL_MAIN_PRODUCTS);
        db.execSQL(CREATE_INDIVIDUAL_PRODUCTS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_PRODUCTS);

        // Create tables again
        onCreate(db);
    }

    public void addDataToMainProducts(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_ID, dataBaseHelper.getID()); // Contact Name
        values.put(KEY_MAIN_PRODUCT_NAMES, dataBaseHelper.get_main_product_names()); // Contact Phone
        //values.put(KEY_MAIN_PRODUCT_IMAGES_PATH, dataBaseHelper.get_main_product_images_path());
        values.put(KEY_MAIN_PRODUCT_DESCRIPTION, dataBaseHelper.get_individual_product_description());
        values.put(KEY_SUB_PRODUCT_CATEGORY_NAMES, dataBaseHelper.get_product_category_names());
        //values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, dataBaseHelper.get_product_category_description());
        //values.put(KEY_PRODUCT_CATEGORY_IMAGES_PATH, dataBaseHelper.get_main_product_images_path());

        // Inserting Row
        //db.insert(TABLE_RECENT, null, values);
        db.insert(TABLE_MAIN_PRODUCTS, null, values);

        db.close(); // Closing database connection
    }

    // Adding new data
    public void addDataToIndividualProducts(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, dataBaseHelper.getID()); // Contact Name
        values.put(KEY_MAIN_PRODUCT_ID, dataBaseHelper.get_main_product_id());
        values.put(KEY_ODOO_PRODUCT_ID, dataBaseHelper.get_odoo_product_id());
        values.put(KEY_MAIN_PRODUCT_NAMES, dataBaseHelper.get_main_product_names());
        values.put(KEY_SUB_PRODUCT_CATEGORY_NAMES, dataBaseHelper.get_product_category_names()); // Contact Phone
        values.put(KEY_INDIVIDUAL_PRODUCT_NAMES, dataBaseHelper.get_individual_product_names());
        values.put(KEY_INDIVIDUAL_PRODUCT_DESCRIPTION, dataBaseHelper.get_individual_product_description());
        values.put(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER, dataBaseHelper.get_individual_product_tech_specs_header());
        values.put(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE, dataBaseHelper.get_individual_product_tech_specs_value());
        values.put(KEY_INDIVIDUAL_PRODUCT_ADDRESS, dataBaseHelper.get_individual_product_address());
        values.put(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH, dataBaseHelper.get_individual_product_images_path());
        //values.put(KEY_INDIVIDUAL_PRODUCT_VARIANT_NAMES, dataBaseHelper.get_individual_product_variant_names());
        //values.put(KEY_INDIVIDUAL_PRODUCT_VARIANT_IMAGES, dataBaseHelper.get_individual_product_variant_images_path());

        // Inserting Row
        //db.insert(TABLE_RECENT, null, values);
        db.insert(TABLE_INDIVIDUAL_PRODUCTS, null, values);
        //db.insert(TABLE_PERMANENT, null, values);

        db.close(); // Closing database connection
    }

    public int lastID(){
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{COLUMN_ID,
        },null, null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        cursor.close();
        return res;
    }

    public int lastIDOfMainProducts(){
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_MAIN_PRODUCTS, new String[]{COLUMN_ID,
        },null, null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        cursor.close();
        return res;
    }

    public List<DataBaseHelper> getAllMainProducts() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MAIN_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                //dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(1));
                dataBaseHelper.set_main_product_description(cursor.getString(2));
                dataBaseHelper.set_product_category_names(cursor.getString(3));
                //dataBaseHelper.set_individual_product_description(cursor.getString(5));
                //dataBaseHelper.set_individual_product_images_path(cursor.getString(6));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    public int getIdForStringTablePermanent(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_MAIN_PRODUCTS, new String[]{COLUMN_ID,
                }, KEY_MAIN_PRODUCT_NAMES + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }

    public int getIdForStringTableTemporary(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{COLUMN_ID,
                }, KEY_SUB_PRODUCT_ID + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }

    public List<DataBaseHelper> getRowData(String sSubName) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MAIN_PRODUCTS + " WHERE " + " " +KEY_MAIN_PRODUCT_NAMES+ " = " + sSubName;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            //do {
            DataBaseHelper dataBaseHelper = new DataBaseHelper();

            dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
            dataBaseHelper.set_product_category_names(cursor.getString(3));
            // Adding data to list
            dataBaseHelperList.add(dataBaseHelper);
            //} while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    public String getDescriptionFromProductName(String sProduct) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_DESCRIPTION,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProduct}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_DESCRIPTION));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public String getProductTechSpecHeading(String sProductName) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProductName}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public String getProductTechSpecValue(String sProductName) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProductName}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public String getImagePathFromProducts(String sProduct) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH,
                }, KEY_INDIVIDUAL_PRODUCT_NAMES + "=?",
                new String[]{sProduct}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public String getImagePathFromProducts(int sID) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(sID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public int getRecordsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public List<String> getProductNamesOnly() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<String> alProductNames = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_INDIVIDUAL_PRODUCT_NAMES +" FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                //dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                /*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*/
                dataBaseHelper.set_individual_product_names(cursor.getString(0));
                /*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*/
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                alProductNames.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alProductNames;
    }

    public List<Integer> getProductDBIDOnly() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<Integer> alProductDBID = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_ID + "," + KEY_INDIVIDUAL_PRODUCT_NAMES +" FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                /*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*/
                //dataBaseHelper.set_individual_product_names(cursor.getString(3));
                /*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*/
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                //String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_id());
                int id = dataBaseHelperList.get(cursor.getPosition()).get_id();
                alProductDBID.add(id);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alProductDBID;
    }

    public List<String> getTechSpecsHeading(String sKey) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<String> alTechSpecs = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER +" FROM " + TABLE_INDIVIDUAL_PRODUCTS + " WHERE "
                + KEY_INDIVIDUAL_PRODUCT_NAMES + "=" + sKey;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                //dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                /*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*/
                dataBaseHelper.set_individual_product_tech_specs_header(cursor.getString(0));
                /*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*/
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                alTechSpecs.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alTechSpecs;
    }

    public List<String> getTechSpecsValue() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<String> alTechSpecs = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE +" FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                //dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                /*dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));*/
                dataBaseHelper.set_individual_product_tech_specs_value(cursor.getString(0));
                /*dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_address(cursor.getString(6));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(7));*/
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                alTechSpecs.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return alTechSpecs;
    }

    public List<DataBaseHelper> getAllProductsData1() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_odoo_product_id(cursor.getInt(2));
                dataBaseHelper.set_main_product_names(cursor.getString(3));
                dataBaseHelper.set_product_category_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_names(cursor.getString(5));
                dataBaseHelper.set_individual_product_description(cursor.getString(6));
                dataBaseHelper.set_individual_product_tech_specs_header(cursor.getString(7));
                dataBaseHelper.set_individual_product_tech_specs_value(cursor.getString(8));
                dataBaseHelper.set_individual_product_address(cursor.getString(9));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(10));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }


    public List<DataBaseHelper> getDataForSearch() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_ID + "," + KEY_SUB_PRODUCT_CATEGORY_NAMES + "," + KEY_INDIVIDUAL_PRODUCT_NAMES +" FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_product_category_names(cursor.getString(1));
                dataBaseHelper.set_individual_product_names(cursor.getString(2));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }
        /*if(sName==null){
        return "";
    }*//*
            cursor.close();
        return sName;
}


    /*
    this will return only the product data of specific KEY_MAIN_PRODUCT_ID
    so we have to pass the id of main product id to get back only those products under main product
     */
    public List<DataBaseHelper> getAllProductsData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS + " " + "WHERE " +KEY_MAIN_PRODUCT_ID + "="+ 1 ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));
                dataBaseHelper.set_product_category_names(cursor.getString(3));
                dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_tech_specs_header(cursor.getString(6));
                dataBaseHelper.set_individual_product_tech_specs_value(cursor.getString(7));
                dataBaseHelper.set_individual_product_address(cursor.getString(8));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(9));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    public String getProductTechSpecHeading(int sID) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(sID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public String getProductTechSpecValue(int sID) {
        Cursor cursor;
        String sName;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_INDIVIDUAL_PRODUCTS, new String[]{KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(sID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }

    public int updateImagePathIndividualProducts(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_INDIVIDUAL_PRODUCT_IMAGES_PATH, dataBaseHelper.get_individual_product_images_path());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_INDIVIDUAL_PRODUCTS, values, "_id" + " = " + KEY_ID, null);
        //*//**//*ContentValues data=new ContentValues();
        //data.put("Field1","bob");
        //DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }

    public int update(DataBaseHelper dataBaseHelper, int KEY_ODOO_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_HEADER, dataBaseHelper.get_individual_product_tech_specs_header());
        values.put(KEY_INDIVIDUAL_PRODUCT_TECH_SPECS_VALUE, dataBaseHelper.get_individual_product_tech_specs_value());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_INDIVIDUAL_PRODUCTS, values, "KEY_ODOO_PRODUCT_ID" + " = " + KEY_ODOO_ID, null);
        //*//**//*ContentValues data=new ContentValues();
        //data.put("Field1","bob");
        //DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }

    public List<Integer> getProductsOdooID() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        ArrayList<Integer> alODooID = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;
        String selectQuery = "SELECT " + KEY_ID + "," + KEY_ODOO_PRODUCT_ID + "," + KEY_INDIVIDUAL_PRODUCT_ADDRESS +" FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_odoo_product_id(cursor.getInt(1));
                dataBaseHelper.set_individual_product_address(cursor.getString(2));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                //String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                int id = dataBaseHelperList.get(cursor.getPosition()).get_odoo_product_id();
                alODooID.add(id);
            } while (cursor.moveToNext());
        }


        // return recent list
        return alODooID;
    }
    /*public List<DataBaseHelper> getAllProductsData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INDIVIDUAL_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_main_product_id(cursor.getInt(1));
                dataBaseHelper.set_main_product_names(cursor.getString(2));
                dataBaseHelper.set_product_category_names(cursor.getString(3));
                dataBaseHelper.set_individual_product_names(cursor.getString(4));
                dataBaseHelper.set_individual_product_description(cursor.getString(5));
                dataBaseHelper.set_individual_product_images_path(cursor.getString(6));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/

   /* //gets Name of index to check whether to perform update task in recyclerview or not
    public String getNameFromTablePermanent(int ID) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_PERMANENT, new String[]{KEY_EMP_ID,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_EMP_ID));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;

    }

    //gets Name of index to check whether to perform update task in recyclerview or not
    public String getNameFromTableTemporary(int ID) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_TEMPORARY, new String[]{KEY_EMP_ID,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_EMP_ID));
        } else {
            sName = "";
        }
        *//*if(sName==null){
            return "";
        }*//*
        cursor.close();
        return sName;
    }


    // Getting data
    public List<DataBaseHelper> getRowData(int ID) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMANENT + " WHERE " + " _id " + " = " + ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            //do {
            DataBaseHelper dataBaseHelper = new DataBaseHelper();

            dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
            dataBaseHelper.set_stall_name(cursor.getString(1));
            dataBaseHelper.set_emp_id(cursor.getString(2));
            dataBaseHelper.set_amount(cursor.getString(3));
            dataBaseHelper.set_time(cursor.getString(4));
            // Adding data to list
            dataBaseHelperList.add(dataBaseHelper);
            //} while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    // Getting data
    public List<DataBaseHelper> getAllTemporaryData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEMPORARY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_stall_name(cursor.getString(1));
                dataBaseHelper.set_emp_id(cursor.getString(2));
                dataBaseHelper.set_scanned_id(cursor.getString(3));
                dataBaseHelper.set_amount(cursor.getString(4));
                dataBaseHelper.set_time(cursor.getString(5));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    public List<DataBaseHelper> getAllEmployeeData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_emp_id(cursor.getString(1));
                dataBaseHelper.set_scanned_id(cursor.getString(2));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    // Getting data
    *//*public List<DataBaseHelper> getAllPermanentData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMANENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_stall_name(cursor.getString(1));
                dataBaseHelper.set_emp_id(cursor.getString(2));
                dataBaseHelper.set_amount(cursor.getString(3));
                dataBaseHelper.set_time(cursor.getString(4));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*//*

    public List<DataBaseHelper> getAllPermanentData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMANENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_stall_name(cursor.getString(1));
                dataBaseHelper.set_emp_id(cursor.getString(2));
                dataBaseHelper.set_scanned_id(cursor.getString(3));
                dataBaseHelper.set_amount(cursor.getString(4));
                dataBaseHelper.set_time(cursor.getString(5));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }



    // Updating single data
    public int updateMultipleDataList(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";


        ContentValues values = new ContentValues();

        values.put(KEY_STALL_ID, dataBaseHelper.get_stall_name());
        values.put(KEY_EMP_ID, dataBaseHelper.get_emp_id()); // Contact Phone
        values.put(KEY_AMOUNT, dataBaseHelper.get_amount());
        values.put(KEY_TIME, dataBaseHelper.get_time());
        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_TEMPORARY, values, "_id" + " = " + KEY_ID, null);
        *//*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*//*
    }

    // Updating single data in all tab
    public int updateMultipleDataListAllTab(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        *//*values.put(KEY_NAME, dataBaseHelper.getName());
        values.put(KEY_NUMBER, dataBaseHelper.getName());
        values.put(KEY_EMAILID, dataBaseHelper.getName());
        values.put(KEY_DESIGNATION, dataBaseHelper.getName());*//*

        values.put(KEY_STALL_ID, dataBaseHelper.get_stall_name());
        values.put(KEY_EMP_ID, dataBaseHelper.get_emp_id()); // Contact Phone
        values.put(KEY_AMOUNT, dataBaseHelper.get_amount());
        values.put(KEY_TIME, dataBaseHelper.get_time());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_PERMANENT, values, "_id" + " = " + KEY_ID, null);
        *//*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*//*
    }

    // Updating single data
    *//*public int updateSingleDataList(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_LAST_SEEN_TIME, dataBaseHelper.getLastSeen());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_TEMPORARY, values, "_id" + " = " + KEY_ID, null);
        *//**//*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }*//*

    // Updating single data in all tab
    *//*public int updateSingleDataListAllTab(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_LAST_SEEN_TIME, dataBaseHelper.getLastSeen());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_PERMANENT, values, "_id" + " = " + KEY_ID, null);
        *//**//*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }*//*

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    // Deleting single data
    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_RECENT, KEY_ID + " = ?", new String[] { String.valueOf(recent.getID()) });
        db.delete(TABLE_TEMPORARY, KEY_ID + " = " + id, null);
        db.close();
    }

    public void deleteEmployeeData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_RECENT, KEY_ID + " = ?", new String[] { String.valueOf(recent.getID()) });
        db.delete(TABLE_EMPLOYEE, KEY_ID + " = " + id, null);
        db.close();
    }

    // Getting recent Count
    public int getRecordsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_PERMANENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }*/

    /*public boolean CheckIsDataAlreadyInDBorNot(String TableName,String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_ALL + " where " + dbfield + "="
                + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount<=0){
            return false;
        }
        return true;
    }*/
}

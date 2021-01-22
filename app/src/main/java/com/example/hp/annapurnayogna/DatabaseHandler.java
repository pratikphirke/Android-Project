package com.example.hp.annapurnayogna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by HP on 14-03-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    static SQLiteDatabase db;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "users3.db";

    static Context this_context;
    //  private static String tableName;


    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this_context = context;
        //
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String usersTable = "CREATE TABLE loginDetails (iD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, userName TEXT, password TEXT, rememberMe BOOLEAN ,email TEXT, phone TEXT, address TEXT, dob TEXT, city TEXT)";

        String usersTable = "CREATE TABLE 'Register_user'(uiD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, 'userName' TEXT, 'phone' TEXT, 'address' TEXT)";
        String CREATE_TABLE_FOOD = "CREATE TABLE 'Register_food'(fiD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, 'fName' TEXT, 'date' TEXT, 'price' INTEGER,'qty' INTEGER,'amount' INTEGER)";
        String CREATE_TABLE_FOOD_amt = "CREATE TABLE 'Register_food_amt'(famtiD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, 'date' TEXT, 'total_qty' INTEGER, 'total_price' INTEGER, 'total_amt' INTEGER)";
        String MasterItemTable = "CREATE TABLE 'Master_Item'(itemiD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, 'itemName' TEXT, 'rateItem' INTEGER )";

        try {
            Toast.makeText(this_context, "DB Created Successfully ", Toast.LENGTH_SHORT).show();
            db.execSQL(usersTable);
            Toast.makeText(this_context, "Table create Register_user", Toast.LENGTH_SHORT).show();

            db.execSQL(CREATE_TABLE_FOOD);
            Toast.makeText(this_context, "CREATE_TABLE_FOOD", Toast.LENGTH_SHORT).show();

            db.execSQL(CREATE_TABLE_FOOD_amt);
            Toast.makeText(this_context, "CREATE_TABLE_FOOD_amt", Toast.LENGTH_SHORT).show();

            db.execSQL(MasterItemTable);
            Toast.makeText(this_context, "CREATE_MasterItemTable", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            Toast.makeText(this_context, "Issues in creating db " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

  /*  public static void createTable(String tableName, String col1, String col2, String col3) {

        try {
            // #1 create writable instance of the database file
            db = new DatabaseHandler(this_context).getWritableDatabase();

            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " + col1 + " TEXT , " + col2 + " TEXT ," + col3 + " TEXT )";

            db.execSQL(query);
            Toast.makeText(this_context, "Table create", Toast.LENGTH_LONG).show();
            // #3 execute query and update user for successful insert operation

        } catch (Exception e) {
            Toast.makeText(this_context, "Issues  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }*/


/***inasert for DayChart in Registrer_food**/
   public static  void insertDayChart(String fName, String date, int price,int qty,int amt) {

        db = new DatabaseHandler(this_context).getWritableDatabase();
        try {
            String insertQuery = "insert into Register_food (fName,date,price,qty,amount) values('"+fName+"','"+date+"',"+price+","+qty+","+amt+")";

            db.execSQL(insertQuery);
        } catch (Exception e1) {

            Toast.makeText(this_context, e1.getMessage(), Toast.LENGTH_LONG).show();

        }

    }


/****Update Query :
 update table Master_Item set rateItem=rate where itemName=iname;
 */

    public boolean updateData(String iname, int rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rateItem",rate);

        db.update("Master_Item", contentValues, "itemName = ?",new String[] { iname });
        return true;
    }


    public static Cursor getUserCount(String tableName) {
        int count = 0;
        Cursor c1;
        db = new DatabaseHandler(this_context).getWritableDatabase();

        String SelectValues = "SELECT * FROM  '" + tableName + "'";
        c1 = db.rawQuery(SelectValues, null);
        //count=c1.getCount();
        return c1;
    }

    public static Cursor getTotalQty()
    {
        int totalQty=0;
        Cursor c1;
        db=new DatabaseHandler(this_context).getWritableDatabase();

        String qry="SELECT fName, SUM(qty) FROM Register_food GROUP BY fName";
        c1= db.rawQuery(qry,null);
        return c1;
    }

    public static String getTQty(String fname, String month)
    {
       String ans="";
        Cursor c1;
        db=new DatabaseHandler(this_context).getWritableDatabase();

        String qry="SELECT SUM(qty),Sum(amount) FROM Register_food WHERE fName= '"+fname+"' and date like '%/"+month+"/%'" ;
        Toast.makeText(this_context, ""+qry, Toast.LENGTH_SHORT).show();
        c1= db.rawQuery(qry,null);
        c1.moveToFirst();
        ans=c1.getString(0)+":"+c1.getString(1);
        return ans;
    }


    public static Cursor getDates(String month) {
        int count = 0;
        Cursor c1;
        db = new DatabaseHandler(this_context).getWritableDatabase();

        String SelectValues = "select distinct date from Register_food where date like '%/"+month+"/%'";
        c1 = db.rawQuery(SelectValues, null);
        //count=c1.getCount();
        return c1;
    }

    //select fName,sum(qty) from Register_food where date like '2/5/2018' group by fName

    public static Cursor getDayDetail(String Date) {
        int count = 0;
        Cursor c1;
        db = new DatabaseHandler(this_context).getWritableDatabase();

        String SelectValues = "select fName,sum(qty),sum(amount) from Register_food where date='"+Date+"' group by fName";
        c1 = db.rawQuery(SelectValues, null);
        //count=c1.getCount();
        return c1;
    }

}

package com.example.hp.annapurnayogna;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MasterRateActivity extends AppCompatActivity {


    TextView poli, bhaji, rice, daal, other, item, rate, unit;
    EditText et_poli, et_bhaji, et_rice, et_daal, et_other;
    TextView tvrate_poli, tvrate_bhaji, tvrate_rice, tvrate_daal, tvrate_other;
    Button madd, mclear, mcreatefile, mupdate;
    DatabaseHandler dbh;
    SQLiteDatabase db;
    Cursor c1 = null;
    ArrayList<String> columns = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<>();

    int itemcount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_rate);

       dbh = new DatabaseHandler(getApplicationContext());

        poli = (TextView) findViewById(R.id.tv_poli);
        bhaji = (TextView) findViewById(R.id.tv_bhaji);
        rice = (TextView) findViewById(R.id.tv_rice);
        daal = (TextView) findViewById(R.id.tv_daal);
        //oth--er=(TextView)findViewById(R.id.tv_other);
        item = (TextView) findViewById(R.id.tv_item);
        rate = (TextView) findViewById(R.id.tv_rate);
        unit = (TextView) findViewById(R.id.tv_unit);


        et_poli = (EditText) findViewById(R.id.et_poli);
        et_bhaji = (EditText) findViewById(R.id.et_bhaji);
        et_rice = (EditText) findViewById(R.id.et_rice);
        et_daal = (EditText) findViewById(R.id.et_daal);
        //et_other=(EditText)findViewById(R.id.et_other);

        tvrate_poli = (TextView) findViewById(R.id.tvrate_poli);
        tvrate_bhaji = (TextView) findViewById(R.id.tvrate_bhaji);
        tvrate_rice = (TextView) findViewById(R.id.tvrate_rice);
        tvrate_daal = (TextView) findViewById(R.id.tvrate_daal);
        //tvrate_other=(TextView)findViewById(R.id.tvrate_other);


        madd = (Button) findViewById(R.id.btn_add);
        mclear = (Button) findViewById(R.id.btn_clr);
        //mcreatefile = (Button) findViewById(R.id.btn_createfile);
        mupdate = (Button) findViewById(R.id.btn_update);


        Cursor c1 = DatabaseHandler.getUserCount("Master_Item");
        c1.moveToFirst();

        itemcount=c1.getCount();

        if(itemcount>0)
        {
            Toast.makeText(getApplicationContext(), "Master Entry Exists ", Toast.LENGTH_SHORT).show();
            madd.setEnabled(false);
            mupdate.setEnabled(true);
            int pri[]=new int[4];
        for(int i=0;i<itemcount;i++) {
            //String iname = c1.getString(1);
             pri[i]= c1.getInt(2);
            c1.moveToNext();
            Toast.makeText(getApplicationContext(), "Total Record : " + itemcount , Toast.LENGTH_SHORT).show();
        }

                et_poli.setText(""+pri[0]);
                et_bhaji.setText(""+pri[1]);
                et_rice.setText(""+pri[2]);
                et_daal.setText(""+pri[3]);


        }
        else {
            Toast.makeText(getApplicationContext(), "Master Entry not Exists ", Toast.LENGTH_SHORT).show();
            madd.setEnabled(true);
            mupdate.setEnabled(false);
        }


        mupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    db = new DatabaseHandler(MasterRateActivity.this).getWritableDatabase();
                    int pr_poli = Integer.parseInt(et_poli.getText().toString());
                    int pr_bhaji = Integer.parseInt(et_bhaji.getText().toString());
                    int pr_rice = Integer.parseInt(et_rice.getText().toString());
                    int pr_daal = Integer.parseInt(et_daal.getText().toString());

                   boolean b1= dbh.updateData("Poli", pr_poli);
                    boolean b2=dbh.updateData("Bhaji", pr_bhaji);
                    boolean b3= dbh.updateData("Rice", pr_rice);
                    boolean b4= dbh.updateData("Daal", pr_daal);

                    if (b1 && b2 && b3 && b4) {
                        Toast.makeText(getApplicationContext(), "Value updated......!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Exception "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        madd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                db = new DatabaseHandler(MasterRateActivity.this).getWritableDatabase();
                Cursor c1 = DatabaseHandler.getUserCount("Master_Item");
                c1.moveToFirst();

                int pr_poli = Integer.parseInt(et_poli.getText().toString());
                int pr_bhaji = Integer.parseInt(et_bhaji.getText().toString());
                int pr_rice = Integer.parseInt(et_rice.getText().toString());
                int pr_daal = Integer.parseInt(et_daal.getText().toString());
                //int pr_poli = Integer.parseInt(et_poli.getText().toString());

                db = new DatabaseHandler(MasterRateActivity.this).getWritableDatabase();


                InsertData("Poli", pr_poli);

                InsertData("Bhaji", pr_bhaji);

                InsertData("Rice", pr_rice);

                InsertData("Daal", pr_daal);

                Toast.makeText(getApplicationContext(), "Value added......!!", Toast.LENGTH_SHORT).show();

                /*// Cursor c1 = DatabaseHandler.getUserCount("Master_Item");
                c1.moveToFirst();
                int count = c1.getCount();
                for (int i = 0; i < count; i++) {
                    String iname = c1.getString(1);
                    String pri = c1.getString(2);
                    c1.moveToNext();*/


                Toast.makeText(getApplicationContext(), "Total Record : " + c1.getCount(), Toast.LENGTH_SHORT).show();

                madd.setEnabled(false);
                mupdate.setEnabled(true);
              /*  db = new DatabaseHandler(MasterRateActivity.this).getWritableDatabase();
                Cursor c2 = DatabaseHandler.getUserCount("Master_Item");

                int countrecord= c2.getCount();
                if(countrecord<0)
                {
                    et_poli.setEnabled(true);
                    et_bhaji.setEnabled(true);
                    et_rice.setEnabled(true);
                    et_daal.setEnabled(true);
                    madd.setEnabled(true);
                }
                else
                {
                    et_poli.setEnabled(false);
                    et_bhaji.setEnabled(false);
                    et_rice.setEnabled(false);
                    et_daal.setEnabled(false);
                    madd.setEnabled(false);
                }*/

        }
    });

        mclear.setOnClickListener(new View.OnClickListener()
    {

        @Override
        public void onClick (View v){
        et_poli.setText("");
        et_bhaji.setText("");
        et_rice.setText("");
        et_daal.setText("");
    }
    });
/**create rate card excel**/
     /*   mcreatefile.setOnClickListener(new View.OnClickListener()

    {

        @Override
        public void onClick (View v){
        //New Workbook
        Workbook wb = new HSSFWorkbook();
        File file = null;
        Cell c = null;

        //ArrayList<String> column_names = new ArrayList<String>();
        //ArrayList<String[]> values = new ArrayList<String[]>();
        try {
            Cursor c1 = DatabaseHandler.getUserCount("Master_Item");

            c1.moveToFirst();

            int count = c1.getColumnCount();

            int index = 1;
            do {

                if (index < count) {
                    columns.add(c1.getColumnName(index));
                    index++;
                    Toast.makeText(getApplicationContext(), "Total Record column count : " + count, Toast.LENGTH_SHORT).show();
                }

            } while (c1.moveToNext());


            int cnt = 1;
            if (c1.moveToFirst()) {
                do {
                    values.add("" + c1.getString(1) + " " + c1.getString(2));
                    //values.add(cnt, "" + c1.getString(1) + " " + c1.getString(2));
                    cnt++;
                } while (c1.moveToNext());
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "SQL EXception " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        CellStyle cs2 = wb.createCellStyle();
        cs2.setFillForegroundColor(HSSFColor.ROSE.index);
        cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Rate Card");

        sheet1.setColumnWidth(0, (15 * 500));
        // Cell c = null;
        Cell c3 = null;
        Row row = sheet1.createRow(1);
        // c = row.createCell(0);
        // c.setCellValue("");
        // c.setCellStyle(cs);



        int i = 1;
        //add column names to the firt row of excel
        row = sheet1.createRow(i++);
        for (int a = 0; a < columns.size(); a++) {
            c = row.createCell(a);
            // c.setCellValue(cursor.getString(a));
            c.setCellValue(columns.get(a));
            c.setCellStyle(cs);
        }


        for (int j = 0; j < values.size(); j++) {
            //String rowValues = values.get(j);
            Row row4 = sheet1.createRow(i++);
            for (int k = 0; k <1 ; k++) {
                {
                    String item=values.get(j);
                    String arr[]=item.split(" ");
                    c = row4.createCell(k);
                    c.setCellValue(arr[0]);
                    c.setCellStyle(cs);

                    c = row4.createCell(k+1);
                    c.setCellValue(arr[1]);
                    c.setCellStyle(cs);
                }
            }

        }


        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/Annapurna/");
        dir.mkdir();

        file = new File(dir, "MasterItemReport.xls");
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Toast.makeText(getApplicationContext(), "File Created Successfully...!!" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            wb.close();
            os.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "File Creation Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
        }

    }
    });
*/
}
    /*Cursor c1 = DatabaseHandler.getUserCount("Master_Item");

        c1.moveToFirst();

    int count= c1.getCount();
        for(int i=0;i<count;i++) {
            String iname = c1.getString(1);
            String pri = c1.getString(2);
            c1.moveToNext();
            Toast.makeText(getApplicationContext(), "Total Record : " + count + "Item: "+iname+"  Price: "+pri, Toast.LENGTH_SHORT).show();
        }*/


//Inserting Data into database - Like INSERT INTO QUERY.
    public void InsertData(String itemName, int price) {
        ContentValues values = new ContentValues();
        values.put("itemName", itemName);
        values.put("rateItem", price);
        long id = db.insert("Master_Item", null, values);

    }

}

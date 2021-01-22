package com.example.hp.annapurnayogna;

import android.Manifest;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayChartActivity extends AppCompatActivity {

    TextView day, date;
    TextView poli, bhaji, rice, daal, other, total;
    EditText poli_qty,bhaji_qty,rice_qty,daal_qty;
    TextView poli_price, poli_amt;
    TextView bhaji_price, bhaji_amt;
    TextView rice_price, rice_amt;
    TextView daal_price, daal_amt;
    TextView other_qty;
    TextView total_qty, total_price, total_amt;
    Button submit, clear, create, file, add, addother;
    DatabaseHandler dbh;
    SQLiteDatabase db;
    Cursor c1 = null;
    LinearLayout ll = null;
    int cnt = 0,count=0;

    ArrayList<String> columns = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<>();
    ArrayList<String> Dates = new ArrayList<>();

    EditText etqty;
    EditText etprice;
    EditText etamt;

    List<EditText> allEdqty = new ArrayList<EditText>();
    List<EditText> allEdprice = new ArrayList<EditText>();
    List<EditText> allEdamt = new ArrayList<EditText>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_chart);

        dbh = new DatabaseHandler(getApplicationContext());

        db = new DatabaseHandler(DayChartActivity.this).getWritableDatabase();

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d = new Date();


        day = (TextView) findViewById(R.id.day);
        date = (TextView) findViewById(R.id.date);

        day.setText("Day: " + sdf1.format(d));
        date.setText("Date: " + sdf.format(calender.getTime()));

        poli = (TextView) findViewById(R.id.poli);
        bhaji = (TextView) findViewById(R.id.bhaji);
        rice = (TextView) findViewById(R.id.rice);
        daal = (TextView) findViewById(R.id.dal);
        //other = (TextView) findViewById(R.id.other);
        total = (TextView) findViewById(R.id.total);

        poli_amt = (TextView) findViewById(R.id.poli_amt);
        poli_qty = (EditText) findViewById(R.id.poli_qty);
        poli_price = (TextView) findViewById(R.id.poli_price);

        bhaji_amt = (TextView) findViewById(R.id.bhaji_amt);
        bhaji_qty = (EditText) findViewById(R.id.bhaji_qty);
        bhaji_price = (TextView) findViewById(R.id.bhaji_price);

        rice_qty = (EditText) findViewById(R.id.rice_qty);
        rice_amt = (TextView) findViewById(R.id.rice_amt);
        rice_price = (TextView) findViewById(R.id.rice_price);

        daal_qty = (EditText) findViewById(R.id.daal_qty);
        daal_amt = (TextView) findViewById(R.id.daal_amt);
        daal_price = (TextView) findViewById(R.id.daal_price);


       // other_qty = (EditText) findViewById(R.id.other_qty);

        total_amt = (TextView) findViewById(R.id.total_amt);
        total_qty = (TextView) findViewById(R.id.total_qty);
        total_price = (TextView) findViewById(R.id.total_price);


        //create = (Button) findViewById(R.id.btn_create);
        submit = (Button) findViewById(R.id.btn_submit);
        clear = (Button) findViewById(R.id.btn_clr);
        file = (Button) findViewById(R.id.btn_file);
        add = (Button) findViewById(R.id.btn_add5);
        //addother = (Button) findViewById(R.id.btn_addother);


        Cursor c1 = DatabaseHandler.getUserCount("Master_Item");
        c1.moveToFirst();

        int itemcount = c1.getCount();

        if (itemcount > 0) {
            int pri[] = new int[4];
            for (int i = 0; i < itemcount; i++) {
                //String iname = c1.getString(1);
                pri[i] = c1.getInt(2);
                c1.moveToNext();
                //Toast.makeText(getApplicationContext(), "Total Record : " + itemcount , Toast.LENGTH_SHORT).show();
            }

            poli_price.setText("" + pri[0]);
            bhaji_price.setText("" + pri[1]);
            rice_price.setText("" + pri[2]);
            daal_price.setText("" + pri[3]);

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    int tqty = 0;//Integer.parseInt(total_qty.getText().toString());
                    int tprice = 0;// Integer.parseInt(total_price.getText().toString());
                    int tamt = 0;// Integer.parseInt(total_amt.getText().toString());

                    if (poli_qty.getText().length() == 0 || bhaji_qty.getText().length() == 0 || rice_qty.getText().length() == 0 || daal_qty.getText().length() == 0) {
                        Toast.makeText(DayChartActivity.this, "Please add all quantity..!!", Toast.LENGTH_LONG).show();
                        total_qty.setText("0");
                        total_price.setText("0");
                        total_amt.setText("0");
                    } else {
                        tqty = tqty + Integer.parseInt(poli_qty.getText().toString()) + Integer.parseInt(bhaji_qty.getText().toString()) + Integer.parseInt(rice_qty.getText().toString()) + Integer.parseInt(daal_qty.getText().toString());
                        tprice = tprice + Integer.parseInt(poli_price.getText().toString()) + Integer.parseInt(bhaji_price.getText().toString()) + Integer.parseInt(rice_price.getText().toString()) + Integer.parseInt(daal_price.getText().toString());
                        tamt = tamt + Integer.parseInt(poli_amt.getText().toString()) + Integer.parseInt(bhaji_amt.getText().toString()) + Integer.parseInt(rice_amt.getText().toString()) + Integer.parseInt(daal_amt.getText().toString());

                        total_qty.setText("" + tqty);
                        total_price.setText("" + tprice);
                        total_amt.setText("" + tamt);
                    }
                }
                catch(Exception e1)
                    {
                        Toast.makeText(DayChartActivity.this, "Exception : " + e1.getMessage(), Toast.LENGTH_LONG).show();
                    }

/*
               String fn = String.valueOf(Integer.parseInt(poli_qty.getText().toString()));
                String  dte=date.getText().toString();
                String  pri=String.valueOf(Integer.parseInt(poli_price.getText().toString()));
                String  amt=String.valueOf(Integer.parseInt(poli_amt.getText().toString()));

                dbh.insertDayChart(fn, dte, pri, amt);
                //String rec = getCount;
                Toast.makeText(DayChartActivity.this, "add", Toast.LENGTH_LONG).show();
         */
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = new DatabaseHandler(DayChartActivity.this).getWritableDatabase();
                Cursor c1 = DatabaseHandler.getUserCount("Master_Item");
                c1.moveToFirst();
                if( poli_price.getText().length()==0 || bhaji_price.getText().length()==0 || rice_price.getText().length()==0 || daal_price.getText().length()==0||
                        poli_qty.getText().length()==0 || bhaji_qty.getText().length()==0 || rice_qty.getText().length()==0 || daal_qty.getText().length()==0 ||
                        poli_amt.getText().length()==0 || bhaji_amt.getText().length()==0 || rice_amt.getText().length()==0 || daal_amt.getText().length()==0) {
                    Toast.makeText(getApplicationContext(), "Please Fill all details properly...!!", Toast.LENGTH_SHORT).show();

                }
                else {
                    int pr_poli = Integer.parseInt(poli_price.getText().toString());
                    int pr_bhaji = Integer.parseInt(bhaji_price.getText().toString());
                    int pr_rice = Integer.parseInt(rice_price.getText().toString());
                    int pr_daal = Integer.parseInt(daal_price.getText().toString());
                    //int pr_poli = Integer.parseInt(et_poli.getText().toString());

                    int qty_poli = Integer.parseInt(poli_qty.getText().toString());
                    int qty_bhaji = Integer.parseInt(bhaji_qty.getText().toString());
                    int qty_rice = Integer.parseInt(rice_qty.getText().toString());
                    int qty_daal = Integer.parseInt(daal_qty.getText().toString());

                    int amt_poli = Integer.parseInt(poli_amt.getText().toString());
                    int amt_bhaji = Integer.parseInt(bhaji_amt.getText().toString());
                    int amt_rice = Integer.parseInt(rice_amt.getText().toString());
                    int amt_daal = Integer.parseInt(daal_amt.getText().toString());


                    db = new DatabaseHandler(DayChartActivity.this).getWritableDatabase();
                    Cursor c23 = DatabaseHandler.getUserCount("Register_food");

                    c23.moveToLast();

                    String dt = date.getText().toString();
                    String arr[] = dt.split(" ");
                    String arr2[] = arr[1].split("/");
                    //Toast.makeText(getApplicationContext(), "Today's Date: "+arr2[0]+"  From Database : "+c23.getString(2), Toast.LENGTH_SHORT).show();
                    String dbdate[] = c23.getString(2).split("/");
                    if (arr2[0].equals(dbdate[0])) {
                        Toast.makeText(getApplicationContext(), "Today's Record is already added..!!", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseHandler.insertDayChart("Poli", arr[1], pr_poli, qty_poli, amt_poli);
                        DatabaseHandler.insertDayChart("Bhaji", arr[1], pr_bhaji, qty_bhaji, amt_bhaji);
                        DatabaseHandler.insertDayChart("Rice", arr[1], pr_rice, qty_rice, amt_rice);
                        DatabaseHandler.insertDayChart("Daal", arr[1], pr_daal, qty_daal, amt_daal);

                        Toast.makeText(getApplicationContext(), "Value added to database......!!", Toast.LENGTH_SHORT).show();

                        Cursor c2 = DatabaseHandler.getUserCount("Register_food");

                        c2.moveToFirst();

                        int count = c2.getCount();
                        for (int i = 0; i < count; i++) {
                            String iname = c2.getString(1);
                            String Date = c2.getString(2);
                            String pri = c2.getString(3);
                            String qty = c2.getString(4);
                            String amt = c2.getString(5);
                            c2.moveToNext();
                            //  Toast.makeText(getApplicationContext(), "Total Record : " + count + "Item: " + iname + " Date: "+Date+"  Price: " + pri+" Qty :"+qty+" Amt "+amt, Toast.LENGTH_SHORT).show();
                        }
                    }
                    poli_qty.setText("");
                    bhaji_qty.setText("");
                    rice_qty.setText("");
                    daal_qty.setText("");

                    poli_amt.setText("");
                    bhaji_amt.setText("");
                    rice_amt.setText("");
                    daal_amt.setText("");

                    total_qty.setText("");
                    total_price.setText("");
                    total_amt.setText("");
                }
                /****for create notification*/
                /*
                Intent intent = new Intent(getBaseContext(),DayChartActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(DayChartActivity.this, 0, intent,
                          PendingIntent.FLAG_UPDATE_CURRENT);
                //set rinton for notification
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(DayChartActivity.this);

                mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                mBuilder.setContentTitle("new record added");
                mBuilder.setContentText("new record fro todays westage");
                mBuilder.addAction(R.mipmap.ic_launcher,"OPEN APP AGIN",contentIntent);
                mBuilder.setSound(uri);


                Notification nf = mBuilder.build();
                NotificationManager nm= (NotificationManager) DayChartActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(0,nf);*/
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poli_qty.setText("");
                bhaji_qty.setText("");
                rice_qty.setText("");
                daal_qty.setText("");

                poli_amt.setText("");
                bhaji_amt.setText("");
                rice_amt.setText("");
                daal_amt.setText("");

                total_qty.setText("");
                total_price.setText("");
                total_amt.setText("");
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "file buttn", Toast.LENGTH_SHORT).show();

                //New Workbook
                Workbook wb = new HSSFWorkbook();
                File file = null;
                Cell c = null;

                //ArrayList<String> column_names = new ArrayList<String>();
                //ArrayList<String[]> values = new ArrayList<String[]>();
                try {
                    Cursor c1 = DatabaseHandler.getUserCount("Register_food");

                    c1.moveToFirst();

                     count = c1.getCount();
                    /*int index = 1;
                    do {

                        if (index < count) {
                            columns.add(c1.getColumnName(index));
                            index++;
                            Toast.makeText(getApplicationContext(), "Total Record column count : " + count, Toast.LENGTH_SHORT).show();
                        }

                    } while (c1.moveToNext());   */

                    int cnt = 1;

                    columns.add("Date ");
                    columns.add("Poli");
                    columns.add("Bhaji");
                    columns.add("Rice");
                    columns.add("Daal");

                    if (c1.moveToFirst()) {
                        do {
                            Dates.add("" + c1.getString(2));
                            values.add("" +c1.getString(4));
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
                sheet1 = wb.createSheet("Daily Report");

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

                row = sheet1.createRow(i++);
                for (int a = 0; a < columns.size(); a++) {
                    c = row.createCell(a);
                    // c.setCellValue(cursor.getString(a));
                    c.setCellValue(columns.get(a));
                    c.setCellStyle(cs);
                }

               //Row row4 = sheet1.createRow(i++);
               int cnt=0,cnt1=0;
                for (int j = 0; j < count/4; j++) {
                    //String rowValues = values.get(j);

                    Row row4 = sheet1.createRow(i++);
                for(int k=0;k<1;k++,cnt=cnt+4) {
                   // String item = values.get(k);
                    //String arr[]=item.split(" ");

                    c = row4.createCell(k);
                    c.setCellValue(Dates.get(cnt));
                    c.setCellStyle(cs);
                }


                //    Row row4 = sheet1.createRow(i++);
                    for(int k=1;k<5;k++,cnt1++) {
                        String item = values.get(cnt1);
                        //String arr[]=item.split(" ");

                        c = row4.createCell(k);
                        c.setCellValue(item);
                        c.setCellStyle(cs);
                    }
                }



                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/Annapurna/");
                    dir.mkdir();

                file = new File(dir, "DailyWasteReport01.xls");
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    wb.write(os);
                    Toast.makeText(getApplicationContext(), "File Created Successfully...!!" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    wb.close();
                    os.close();
                } catch (IOException e) {

                  //  ActivityCompat.requestPermissions(DayChartActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Toast.makeText(getApplicationContext(), "File Creation Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                }

            }
        });

        poli_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int qty = (Integer.parseInt(poli_qty.getText().toString()));
                    int price = (Integer.parseInt(poli_price.getText().toString()));
                    int amt = qty * price;

                    poli_amt.setText("" + amt);
                }catch (Exception e){}
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bhaji_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             try {
                 int qty = (Integer.parseInt(bhaji_qty.getText().toString()));
                 int price = (Integer.parseInt(bhaji_price.getText().toString()));
                 int amt = qty * price;

                 bhaji_amt.setText("" + amt);
             }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rice_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    int qty = (Integer.parseInt(rice_qty.getText().toString()));
                    int price = (Integer.parseInt(rice_price.getText().toString()));
                    int amt = qty * price;

                    rice_amt.setText("" + amt);
                }catch (Exception e){}
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        daal_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    int qty = (Integer.parseInt(daal_qty.getText().toString()));
                    int price = (Integer.parseInt(daal_price.getText().toString()));
                    int amt = qty * price;

                    daal_amt.setText("" + amt);
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






     /*   create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = (Integer.parseInt(other_qty.getText().toString()));
                Toast.makeText(getApplicationContext(), " Count : " + cnt, Toast.LENGTH_SHORT).show();

                ll = (LinearLayout) findViewById(R.id.LinearLayout_other);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setId(R.id.LinearLayout_other);



                  /*  for (int i = 0; i < cnt; i++) {

                        ed = new EditText(Activity2.this);
                        allEds.add(ed);
                        ed.setBackgroundResource(R.color.blackOpacity);
                        ed.setId(id);
                        ed.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.WRAP_CONTENT));
                        linear.addView(ed);
                    }*/
/*

                for (int i = 0; i < cnt; i++) {
                    TextView tv = new TextView(DayChartActivity.this);
                    tv.setText("Item Name");
                    //tv.setHint("Item Name ");

                    TextView tv1 = new TextView(DayChartActivity.this);
                    tv1.setText("Qty");
                    etqty = new EditText(DayChartActivity.this);
                    allEdqty.add(etqty);
                   // etqty.setHint("Qty");
                    //etqty.setText("" + 0);
                    etqty.setInputType(InputType.TYPE_CLASS_NUMBER);

                    // etqty.setInputType();

                    TextView tv2 = new TextView(DayChartActivity.this);
                    tv2.setText("Price");
                    etprice = new EditText(DayChartActivity.this);
                    etprice.setHint("price");
                    allEdprice.add(etprice);
                    //etprice.setText("" + 0);
                    etprice.setInputType(InputType.TYPE_CLASS_NUMBER);

                    TextView tv3 = new TextView(DayChartActivity.this);
                    tv3.setText("Amount");
                    etamt = new EditText(DayChartActivity.this);
                    etamt.setHint("amount");
                    allEdamt.add(etamt);
                    //etamt.setText("" + 0);
                    etamt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    //etamt.setEnabled(false);


                    etprice.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            try {
                                int qty = (Integer.parseInt(etqty.getText().toString()));
                                int price = (Integer.parseInt(etprice.getText().toString()));
                                int amt = qty * price;

                                etamt.setText("" + amt);
                            }catch (Exception e){}
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    ll.addView(tv);
                    ll.addView(etqty);
                    ll.addView(etprice);
                    ll.addView(etamt);

                }*
            }
        });*/
  /*    addother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tqty = 0;
                int tprices = 0;
                int tamt = 0;

                tqty = Integer.parseInt(total_qty.getText().toString());
                tprices = Integer.parseInt(total_price.getText().toString());
                tamt = Integer.parseInt(total_amt.getText().toString());


                int[] qtys = new int[allEdqty.size()];
                int[] prices = new int[allEdprice.size()];
                int[] amts = new int[allEdamt.size()];

                for (int i = 0; i < allEdqty.size(); i++) {
                    qtys[i] = Integer.parseInt(allEdqty.get(i).getText().toString());
                    prices[i] = Integer.parseInt(allEdprice.get(i).getText().toString());
                    amts[i] = Integer.parseInt(allEdamt.get(i).getText().toString());

                }
                for (int i = 0; i < qtys.length; i++) {
                    tqty = tqty + qtys[i];
                    tprices = tprices + prices[i];
                    tamt = tamt + amts[i];
                }


                total_qty.setText("" + tqty);
                total_price.setText("" + tprices);
                total_amt.setText("" + tamt);
            }
        });
*/
        // Toast.makeText(getApplicationContext(), "Date: "+sdf.format(calender.getTime())+" \n Day: "+sdf1.format(d), Toast.LENGTH_SHORT).show();
//  Toast.makeText(getApplicationContext(), "Date: "+dt1.getDayOfMonth()+"/ "+ dt1.getMonth()+"/ "+ dt1.getYear(), Toast.LENGTH_SHORT).show();

    }

}

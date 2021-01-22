package com.example.hp.annapurnayogna;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class ViewTotalActivity extends AppCompatActivity {

    TextView tv;
    Button btnCreateTotal, btnShare;
    DatabaseHandler dbh;
    SQLiteDatabase db;
    Cursor c1 = null;
    ArrayList<String> columns = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<>();
    ArrayList<String> amts = new ArrayList<>();
    ArrayList<String> Dates = new ArrayList<>();

    Calendar calender = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
    Date d = new Date();
    File file = null;
    //String month="",CurDate="";
    //int preMonth=0;

    /**pie chart**/
    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    int preMonth=0;
    String month="",CurDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_total);


        /**piechart**/
        final Calendar calender = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d = new Date();
        CurDate=sdf.format(calender.getTime());
        String darr[]=CurDate.split("/");
        month=darr[1];
        preMonth=Integer.parseInt(month)-1;
        //Toast.makeText(getApplicationContext(), "Month"+month+ "Previous Month :"+preMonth, Toast.LENGTH_SHORT).show();



        pieChart = (PieChart) findViewById(R.id.chart1);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(3000);

        /**total**/
        tv = (TextView) findViewById(R.id.tv_totalview);
        btnCreateTotal = (Button) findViewById(R.id.btn_createtotal);
        btnShare = (Button) findViewById(R.id.btn_viewpiechart);

        dbh = new DatabaseHandler(getApplicationContext());

        db = new DatabaseHandler(ViewTotalActivity.this).getWritableDatabase();


        /*
  `      Cursor c2 = DatabaseHandler.getTotalQty();

        c2.moveToFirst();

        int count = c2.getCount();
        for (int i = 0; i < count; i++) {

            String fname = c2.getString(0);
            String qty = c2.getString(1);

            c2.moveToNext();
            Toast.makeText(getApplicationContext(), "Total Record : " + count + "Item: " + fname + " Qty :" + qty, Toast.LENGTH_SHORT).show();
        }
*/
        String poliTQty = DatabaseHandler.getTQty("Poli",""+preMonth);
        String BhajiTQty = DatabaseHandler.getTQty("Bhaji",""+preMonth);
        String RiceTQty = DatabaseHandler.getTQty("Rice",""+preMonth);
        String DaalTQty = DatabaseHandler.getTQty("Daal",""+preMonth);
        Cursor c1 = DatabaseHandler.getUserCount("Register_user");

        c1.moveToFirst();

        final String uname=c1.getString(1);

        tv.setText("Date :" + sdf.format(calender.getTime()) + " \n Day : " + sdf1.format(d) + " \n Person Name: " +uname + "\n Total \t Poli \t Bhaji \t Rice \t Daal \n Total \t " + poliTQty + "     \t      " + BhajiTQty + "      \t     " + RiceTQty + "     \t     " + DaalTQty + "\n");

        final String Date=sdf.format(calender.getTime());
        String dateArr[]=Date.split("/");
        if(dateArr[0].equals("01") || dateArr[0].equals("02") || dateArr[0].equals("03") || dateArr[0].equals("04") || dateArr[0].equals("05"))
        {
            btnCreateTotal.setEnabled(true);
        }
        else
            btnCreateTotal.setEnabled(false);

        btnCreateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //New Workbook
                Workbook wb = new HSSFWorkbook();
                //File file = null;
                Cell c = null;


                //columns.add("Person Name :"+uname);
                columns.add("Items");
                columns.add("Poli");
                columns.add("Bhaji");
                columns.add("Rice");
                columns.add("Daal");

                //tqty:tamt
                String poliTQty = DatabaseHandler.getTQty("Poli",""+preMonth);
                String BhajiTQty = DatabaseHandler.getTQty("Bhaji",""+preMonth);
                String RiceTQty = DatabaseHandler.getTQty("Rice",""+preMonth);
                String DaalTQty = DatabaseHandler.getTQty("Daal",""+preMonth);

                String arr1[]=poliTQty.split(":");
                String arr2[]=BhajiTQty.split(":");
                String arr3[]=RiceTQty.split(":");
                String arr4[]=DaalTQty.split(":");


                values.add("Total Qty");
                values.add("" + arr1[0]);
                values.add("" + arr2[0]);
                values.add("" + arr3[0]);
                values.add("" + arr4[0]);

                amts.add("Total Amount");
                amts.add(""+arr1[1]);
                amts.add(""+arr2[1]);
                amts.add(""+arr3[1]);
                amts.add(""+arr4[1]);


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
                Row rowname = sheet1.createRow(1);
                c = rowname.createCell(0);
                c.setCellValue("Person Name :"+uname);
                c.setCellStyle(cs);

                Row row = sheet1.createRow(2);
                c = row.createCell(0);
                c.setCellValue("Date : " + CurDate);
                c.setCellStyle(cs);


                int i = 3;
                //add column names to the firt row of excel
                row = sheet1.createRow(i++);

                for (int a = 0; a < columns.size(); a++) {
                    c = row.createCell(a);
                    // c.setCellValue(cursor.getString(a));
                    c.setCellValue(columns.get(a));
                    c.setCellStyle(cs);
                }

                row = sheet1.createRow(i++);

                for (int a = 0; a < values.size(); a++) {
                    c = row.createCell(a);
                    // c.setCellValue(cursor.getString(a));
                    c.setCellValue(values.get(a));
                    c.setCellStyle(cs);
                }

                row = sheet1.createRow(i++);

                for (int a = 0; a < amts.size(); a++) {
                    c = row.createCell(a);
                    // c.setCellValue(cursor.getString(a));
                    c.setCellValue(amts.get(a));
                    c.setCellStyle(cs);
                }

                  /*  Calendar c2 = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        c2 = Calendar.getInstance();
                    }
                    SimpleDateFormat sdf2 = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        sdf2 = new SimpleDateFormat("ddMMMyyyy_HH_mm_a");
                    }
                    String strDate2 = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        strDate2 = sdf2.format(c2.getTime());
                    }*/

                File sdcard = Environment.getExternalStorageDirectory();//getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/Annapurna/");
                dir.mkdir();

                file = new File(dir, "TotalQtyWasteReport01.xls");
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
                // create intent for opening this file using suitable application, add data and type to intent
                // create intent with action view
                final Intent share = new Intent(Intent.ACTION_SEND);
                //share.setDataAndType(Uri.fromFile( file ), "*/*");
                    //share.setType("application/vnd.ms-excel");
                  //  share.setDataAndType(Uri.fromFile(file),"application/ms-excel");

                share.setType("application/vnd.ms-excel");
                    share.putExtra("android.intent.extra.STREAM",Uri.fromFile(file));
              //  share.putExtra("android.intent.extra.TEXT");
                // create chooser intent with previously created intent
                //Intent appselecter = Intent.createChooser(share, "Select application");

                startActivity(Intent.createChooser(share,"Share my File"));


                // create pendingIntent to open chooser intent and open file in suitable app
                //PendingIntent pendingIntent = PendingIntent.getActivity(ViewTotalActivity.this, 0, appselecter, PendingIntent.FLAG_UPDATE_CURRENT);


                /***
                 *
                 * final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                 shareIntent.setType("image/jpg");
                 final File photoFile = new File(getFilesDir(), "foo.jpg");
                 shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
                 startActivity(Intent.createChooser(shareIntent, "Share image using"));
                 *
                 */

              /*  try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }*/

            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewTotalActivity.this,PieChartActivity.class);

                Bundle b=new Bundle();
                b.putInt("preMonth",preMonth);
                intent.putExtra("Bundle",b);
                startActivity(intent)
                ;
            }
        });
    }


    public void AddValuesToPIEENTRY(){

        String poliTQty = DatabaseHandler.getTQty("Poli",""+preMonth);
        String BhajiTQty = DatabaseHandler.getTQty("Bhaji",""+preMonth);
        String RiceTQty = DatabaseHandler.getTQty("Rice",""+preMonth);
        String DaalTQty = DatabaseHandler.getTQty("Daal",""+preMonth);

        String arr1[]=poliTQty.split(":");
        String arr2[]=BhajiTQty.split(":");
        String arr3[]=RiceTQty.split(":");
        String arr4[]=DaalTQty.split(":");


        entries.add(new BarEntry(Integer.parseInt(arr1[1]), 0));
        entries.add(new BarEntry(Integer.parseInt(arr2[1]), 1));
        entries.add(new BarEntry(Integer.parseInt(arr3[1]), 2));
        entries.add(new BarEntry(Integer.parseInt(arr4[1]), 3));
        //entries.add(new BarEntry(7f, 4));
        //entries.add(new BarEntry(3f, 5));
    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("Poli");
        PieEntryLabels.add("Bhaji");
        PieEntryLabels.add("Rice");
        PieEntryLabels.add("Daal");
        //PieEntryLabels.add("May");
        //PieEntryLabels.add("June");

    }


}

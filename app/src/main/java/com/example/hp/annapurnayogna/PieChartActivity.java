package com.example.hp.annapurnayogna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PieChartActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_pie_chart);

//        Bundle b=getIntent().getExtras();
  //      preMonth=b.getInt("preMonth");

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

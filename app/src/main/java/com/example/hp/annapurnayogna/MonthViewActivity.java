package com.example.hp.annapurnayogna;

import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MonthViewActivity extends AppCompatActivity {

    Spinner sp;
    ListView lView;
    String[] Month = { "Jan", "Feb", "March", "April", "May","June","July","Aug","Sept","Oct","Nov","Dec" };
    ArrayList DateArray = new ArrayList<String>();
    int mon;
    String SelectedDate="";
    ArrayList<String> fNames = new ArrayList<String>();
    ArrayList<String> qtys= new ArrayList<>();
    ArrayList<String> amts = new ArrayList<>();

    ArrayAdapter adapter;

     Dialog dialog;

    TextView txt,txPoli,txBhaji,txRice,txDaal;

    public MonthViewActivity() {
        dialog = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);

            sp=(Spinner) findViewById(R.id.spinner1);

        /***
         *
         * ArrayAdapter adapter = new ArrayAdapter<String>(this,
         R.layout.activity_listview, mobileArray);

         ListView listView = (ListView) findViewById(R.id.mobile_list);
         listView.setAdapter(adapter);

         */
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Month);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp.setAdapter(aa);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            DateArray.clear();
              //  Toast.makeText(getApplicationContext(), "Selected Month :"+position, Toast.LENGTH_SHORT).show();
                String selectedMonth=""+position;
                mon=Integer.parseInt(selectedMonth)+1;
              //  Toast.makeText(getApplicationContext(), "Selected Month :"+mon, Toast.LENGTH_SHORT).show();


                Cursor c=DatabaseHandler.getDates(""+mon);
                c.moveToFirst();

                int itemcount = c.getCount();


//                Toast.makeText(getApplicationContext(), "Total Record : " + itemcount , Toast.LENGTH_SHORT).show();

                if (itemcount > 0) {
                    for (int i = 0; i < itemcount; i++) {
                        DateArray.add(c.getString(0));
                        c.moveToNext();
                        //Toast.makeText(getApplicationContext(), "Total Record : " + itemcount , Toast.LENGTH_SHORT).show();

                    }
                }

                adapter = new ArrayAdapter<String>(MonthViewActivity.this
                        ,R.layout.activity_listview, DateArray);



                dialog = new Dialog(MonthViewActivity.this);

                //setting custom layout to dialog
                dialog.setContentView(R.layout.dilog_view_day);
                dialog.setTitle("Day Waste Details");

                //adding text dynamically
                txt = (TextView) dialog.findViewById(R.id.tv_date);

                txPoli = (TextView) dialog.findViewById(R.id.tv_poli);
                //txPoli.setText(""+fNames.get(0)+" :"+qtys.get(0)+" :"+amts.get(0)+" Rs/-");

                txBhaji = (TextView) dialog.findViewById(R.id.tv_bhaji);
                //txBhaji.setText(""+fNames.get(1)+" :"+qtys.get(1)+" :"+amts.get(1)+" Rs/-");

                txRice = (TextView) dialog.findViewById(R.id.tv_rice);
                //txRice.setText(""+fNames.get(2)+" :"+qtys.get(2)+" :"+amts.get(2)+" Rs/-");

                txDaal = (TextView) dialog.findViewById(R.id.tv_daal);
                //txDaal.setText(""+fNames.get(3)+" :"+qtys.get(3)+" :"+amts.get(3)+" Rs/-");


                //adding button click event
                final Button dismissButton = (Button) dialog.findViewById(R.id.btn_ok);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        dialog.dismiss();
                    }
                });
                //dialog.show();



                lView =(ListView)findViewById(R.id.lView1);
                lView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


/*
                if(lView.getAdapter() == null){ //Adapter not set yet.
                    lView.setAdapter(adapter);
                }
                else{ //Already has an adapter
                    lView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lView.invalidateViews();
                    lView.refreshDrawableState();
                }
*/
                lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SelectedDate   = (String) lView.getItemAtPosition(position);
                        fNames.clear();
                        qtys.clear();
                        amts.clear();
                        // Show Alert
                        //Toast.makeText(getApplicationContext(),"Position :"+position+"  ListItem : " +SelectedDate , Toast.LENGTH_LONG.show();

                        Cursor curr=DatabaseHandler.getDayDetail(SelectedDate);
                        curr.moveToFirst();

                      //  Toast.makeText(getApplicationContext(),"Total Records:"+curr.getCount(), Toast.LENGTH_LONG).show();

                        do {
                                  fNames.add(curr.getString(0));
                                  qtys.add(curr.getString(1));
                                  amts.add(curr.getString(2));

                                  //  Toast.makeText(getApplicationContext(),"Fname :"+curr.getString(0)+"  Qty : " +curr.getString(1) +" "+curr.getString(2), Toast.LENGTH_LONG).show();


                          //  curr.moveToNext();
                        } while (curr.moveToNext());

                        txt.setText("Date :"+SelectedDate);

                        txPoli.setText(""+fNames.get(0)+" :"+qtys.get(0)+" :"+amts.get(0)+" Rs/-");

                        txBhaji.setText(""+fNames.get(1)+" :"+qtys.get(1)+" :"+amts.get(1)+" Rs/-");

                        txRice.setText(""+fNames.get(2)+" :"+qtys.get(2)+" :"+amts.get(2)+" Rs/-");

                        txDaal.setText(""+fNames.get(3)+" :"+qtys.get(3)+" :"+amts.get(3)+" Rs/-");

                       dialog.show();
                    }
                });
            }

                @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

}


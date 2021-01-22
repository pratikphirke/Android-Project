package com.example.hp.annapurnayogna;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Databasehelper myDB;
    DatabaseHandler mydb;
    SQLiteDatabase db;
    public static TextView tv, tv1, tv2, tv3, notice;
    int count = 0;
    Button btn_total, btn_dayView;

    /**
     * sliderview
     **/
    ViewPager viewPager;
    int images[] = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4};
    MyCustomPagerAdapter myCustomPagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        viewPager = (ViewPager) findViewById(R.id.viewPager);

        myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 3000);

        /***Notification**/

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.SECOND, 10);
        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        /* Repeating on every 20 minutes interval */
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //      1000 * 60 * 10, broadcast);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);


        // myDB = new Databasehelper(MainActivity.this);
        //myDB.open();

        btn_total = (Button) findViewById(R.id.btn_total);
        btn_dayView = (Button) findViewById(R.id.btn_dailyreport);
        mydb = new DatabaseHandler(MainActivity.this);
        db = new DatabaseHandler(MainActivity.this).getWritableDatabase();
        //  db = new Databasehelper(MainActivity.this).getWritableDatabase();
        btn_total.setEnabled(false);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        tv = (TextView) findViewById(R.id.textView);
        tv1 = (TextView) findViewById(R.id.name);
        tv2 = (TextView) findViewById(R.id.phoneno);
        tv3 = (TextView) findViewById(R.id.address);
        notice = (TextView) findViewById(R.id.send_notice);

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");


        String Date = sdf.format(calender.getTime());
        String dateArr[] = Date.split("/");
        if (dateArr[0].equals("01") || dateArr[0].equals("02") || dateArr[0].equals("03") || dateArr[0].equals("04") || dateArr[0].equals("05")) {
            notice.setText("Please Send Your Monthly Excel File...!!");
            btn_total.setEnabled(true);
        } else
            notice.setText("*");
       // btn_total.setEnabled(false);



        /*Calendar calender =Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1=new SimpleDateFormat("EEEE");
        Date d=new Date();

        Toast.makeText(getApplicationContext(), "Date: "+sdf.format(calender.getTime())+" \n Day: "+sdf1.format(d), Toast.LENGTH_SHORT).show();
//  Toast.makeText(getApplicationContext(), "Date: "+dt1.getDayOfMonth()+"/ "+ dt1.getMonth()+"/ "+ dt1.getYear(), Toast.LENGTH_SHORT).show();
*/
        File f = new File("/data/data/com.example.hp.annapurnayogna/databases/users3.db");

        if (f.exists()) {

            //Toast.makeText(getApplicationContext(), "Database exists users3", Toast.LENGTH_SHORT).show();

        } else {

            //Toast.makeText(getApplicationContext(), "Database not exist", Toast.LENGTH_SHORT).show();
        }

        Cursor c1 = DatabaseHandler.getUserCount("Register_user");

        c1.moveToFirst();

        count = c1.getCount();

        if (count > 0) {
            Toast.makeText(getApplicationContext(), "User Exists ", Toast.LENGTH_SHORT).show();

            String uname = c1.getString(1);
            String phno = c1.getString(2);
            String add = c1.getString(3);

            Toast.makeText(getApplicationContext(), "Uname: " + uname + " Phno: " + phno + " Address: " + add, Toast.LENGTH_SHORT).show();

            //tv1.setTextColor();
            tv1.setText("नाव :" + uname);
            tv2.setText("मो.नंबर :" + phno);
            tv3.setText("पत्ता. :" + add);

        } else {
            Toast.makeText(getApplicationContext(), "User Not exists ", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(i);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, DayChartActivity.class);
                startActivity(i);

                Snackbar.make(view, "Add Daily Information", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btn_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent totalIntent = new Intent(MainActivity.this, ViewTotalActivity.class);
                startActivity(totalIntent);
            }

        });

        btn_dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent totalIntent = new Intent(MainActivity.this, MonthViewActivity.class);
                startActivity(totalIntent);

            }
        });

/*        final Calendar calender = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("TT:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String CurDate=sdf.format(calender.getTime());

          Toast.makeText(getApplicationContext(), "Time :"+CurDate, Toast.LENGTH_SHORT).show();
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
-*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Drower-- ADD MAster rate of food Item
        if (id == R.id.nav_ratecard) {
            Intent i = new Intent(MainActivity.this, MasterRateActivity.class);
            startActivity(i);


        }

        // Drower ABout US -Annapurna yojana help developed by.
        else if (id == R.id.nav_Aboutus) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("About Us..");
            builder.setMessage("Developed by:Mr.Pratik Firke." +
                    "K.K Acs & cs collage, Nashik-3 ");
            builder.setIcon(R.drawable.ic_user);
            builder.setView(R.layout.aboutus_alaertdilog);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                 //   finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);//Intent.ACTION_SEND);
            //shareIntent.setData(Uri.parse("text/plain"));
            shareIntent.setType("text/plain");
            //shareIntent.setType("application/vnd.");
            //shareIntent.putExtra("android.intent.extra.STREAM",Uri.fromFile(null));
            String sharetext ="www.googleplay.com" +
                    "    SHARE ANNAPURNA YOJNA APP";
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Annapurna yojena");
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharetext);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }else if (id == R.id.nav_rate) {
            // Drower - rate us
        Intent i = new Intent(MainActivity.this, RateViewActivity.class);
        startActivity(i);

        /*else if (id == R.id.nav_rate) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Rate application - Notification");
            builder.setMessage(" application is not on play store");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent ratei = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=www.googlepay.com"));
                    startActivity(ratei);
                    dialogInterface.dismiss();

                 //   finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }*/
        }else if (id == R.id.nav_close) {

            Toast.makeText(getApplicationContext(), "closed application", Toast.LENGTH_SHORT).show();
            finish();
            moveTaskToBack(true);

        } else if (id == R.id.nav_feedback) {

            Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
            feedbackIntent.setType("text/email");
            //feedbackIntent.setData(Uri.parse("message/html"));
            feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"phirkepratik84@gmail.com"});
            feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "feedback from *--Annapurna yojena");
            feedbackIntent.putExtra(Intent.EXTRA_TEXT,"Dear....");
            startActivity(Intent.createChooser(feedbackIntent, "SEND Feedback.."));

        } else if (id == R.id.nav_contact) {
            Intent callIntent = new Intent(Intent.ACTION_SEND);
            //Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("mobile:"+"8485861797"));
            callIntent.putExtra(Intent.EXTRA_TEXT,"Dear....");
            callIntent.setType("text/plain");
            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;*/
           // }
            startActivity(callIntent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast.makeText(getApplicationContext(), " for FUTURE USED....!!!", Toast.LENGTH_SHORT).show();
  /*          Intent intent =new Intent(MainActivity.this,UserUpdateActivity.class);
            startActivity(intent);
*/

            return true;
        }
       /* else if (id == R.id.action_additem)
        {
            Intent intent =new Intent(MainActivity.this,MasterRateActivity.class);
            startActivity(intent);
          return true;
        }
        else if (id == R.id.action_About_us)
        {
           // Intent intent =new Intent(MainActivity.this,UpdateRateActivity.class);
            //startActivity(intent);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() >= 3) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
            });
        }
    }
}

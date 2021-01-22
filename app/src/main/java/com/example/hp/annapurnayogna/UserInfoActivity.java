package com.example.hp.annapurnayogna;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInfoActivity extends AppCompatActivity {

    EditText id, name, email, phone, addrs, dob, city, pass, rpass;
    Button btn_save;

  //  SQLiteOpenHelper sql = new SQLiteOpenHelper(this);
    DatabaseHandler dbh;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        addrs = (EditText) findViewById(R.id.addrs);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db = new DatabaseHandler(UserInfoActivity.this).getWritableDatabase();
                //db = dbh.getWritableDatabase();
                String userName = name.getText().toString();
                String ph = phone.getText().toString();
                String addr = addrs.getText().toString();

                InsertData(userName, ph, addr);

                //Alert dialog after clicking the Register Account
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("Information");
                builder.setMessage("Your Account is Successfully Created.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Finishing the dialog and removing Activity from stack.
                        /*Cursor c1 = DatabaseHandler.getUserCount("Register_user");

                        c1.moveToFirst();

                        String uname=c1.getString(1);
                        String phno=c1.getString(2);
                        String add=c1.getString(3);

                        MainActivity.tv1.setText("Name :"+uname);
                        MainActivity.tv2.setText("Phone No :"+phno);
                        MainActivity.tv3.setText("Address :"+add);
*/
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }


    //Inserting Data into database - Like INSERT INTO QUERY.
    public void InsertData(String userName, String phone, String address) {
        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("phone", phone);
        values.put("address", address);
        long id = db.insert("Register_user", null, values);
    }
}

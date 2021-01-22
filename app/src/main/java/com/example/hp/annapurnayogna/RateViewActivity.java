package com.example.hp.annapurnayogna;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateViewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView tvRateCount,tvRateMessage;
    private float ratedvalue;
    private Button ratesubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateview);

        ratingBar = (RatingBar)findViewById(R.id.ratingbar);
        tvRateCount = (TextView)findViewById(R.id.tvRateCount);
        tvRateMessage = (TextView)findViewById(R.id.tvRateMessage);
        ratesubmit = (Button)findViewById(R.id.ratesubmit);

     ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
         @Override
         public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
             ratedvalue = ratingBar.getRating();
             tvRateCount.setText("तुमचा प्रमाणे रेटिंग :"+ratedvalue+"/5.");
             if (ratedvalue<1){
                 tvRateMessage.setText(" धन्यवाद...");
             }else if (ratedvalue<2){
                 tvRateMessage.setText(" धन्यवाद..");
             }else if (ratedvalue<3){
                 tvRateMessage.setText(" धन्यवाद..");
             }else if (ratedvalue<4){
                 tvRateMessage.setText(" धन्यवाद..");
             }else if (ratedvalue==5) {
                 tvRateMessage.setText(" धन्यवाद..!");
             }
        }
     });
        ratesubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ratei = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=www.googlepay.com"));
                startActivity(ratei);
            }
        });


    }

}

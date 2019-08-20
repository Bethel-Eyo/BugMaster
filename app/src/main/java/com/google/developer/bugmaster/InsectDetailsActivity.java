package com.google.developer.bugmaster;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.developer.bugmaster.data.Insect;

import java.io.IOException;
import java.io.InputStream;

public class InsectDetailsActivity extends AppCompatActivity {

    Insect insect;
    TextView name, sciName, classification;
    ImageView imageAsset;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Implement layout and display insect details
        setContentView(R.layout.activity_details);

        name = (TextView) findViewById(R.id.detail_bug_name);
        sciName = (TextView) findViewById(R.id.detail_sci_name);
        classification = (TextView) findViewById(R.id.detail_class);
        imageAsset = (ImageView) findViewById(R.id.detail_image);
        ratingBar = (RatingBar) findViewById(R.id.dteail_rating);

        setUpViews();
    }

    public void setUpViews(){
        Intent intent = getIntent();

        String friendlyName = intent.getExtras().getString("name");
        String scientificName = intent.getExtras().getString("sciName");
        String classy = intent.getExtras().getString("classification");
        String mImageAsset = intent.getExtras().getString("imageAsset");
        int dangerLevel = intent.getExtras().getInt("wildLevel");
        name.setText(friendlyName);
        sciName.setText(scientificName);
        classification.setText(classy);
        InputStream imageStream = null;
        try {
            // get input stream
            imageStream  = getAssets().open(mImageAsset);
            // load image as Drawable
            Drawable drawable= Drawable.createFromStream(imageStream, null);
            // set image to ImageView
            imageAsset.setImageDrawable(drawable);
        }
        catch(IOException ex) {
            return;
        }
        ratingBar.setNumStars(10);
        ratingBar.setRating(dangerLevel);
    }
}

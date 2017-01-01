package com.lopic.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle;
    TextView mOverview;
    TextView mRelease_Date;
    TextView mVote_AVG;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.original_title);
        mImageView = (ImageView) findViewById(R.id.poster);
        mOverview = (TextView) findViewById(R.id.overview);
        mRelease_Date = (TextView) findViewById(R.id.release_date);
        mVote_AVG = (TextView) findViewById(R.id.vote_avg);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                String[] movie = intentThatStartedThisActivity.getStringArrayExtra("movie");
                mTitle.setText(movie[0]);
                Picasso.with(getApplicationContext()).load(movie[1]).into(mImageView);
                mOverview.setText(movie[2]);
                mVote_AVG.setText(movie[3] + " / 10");
                mRelease_Date.setText(movie[4]);
            }
        }
    }
}

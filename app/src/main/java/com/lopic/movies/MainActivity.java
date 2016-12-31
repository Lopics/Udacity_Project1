package com.lopic.movies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.lopic.movies.utilities.NetworkUtils;
import com.lopic.movies.utilities.OpenWeatherJsonUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView gridview;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gridview);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadMovieData();
    }
    private void loadMovieData(){
        showWeatherDataView();
        new FetchMovieData().execute();
    }
    private void showWeatherDataView(){
        gridview.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }
    private void showErrorMessage() {
        gridview.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            boolean options = getPreference();
            URL weatherRequestUrl = NetworkUtils.buildUrl(options);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                List<Movie> simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Movie> weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                gridview.setAdapter(new ImageAdapter(MainActivity.this, weatherData));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
                        intentToStartDetailActivity.putExtra("movie", weatherData.get(position).getArray());
                        startActivity(intentToStartDetailActivity);
                    }
                });
            }else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }
    private void popup(){
        CharSequence colors[] = new CharSequence[] {"Most Popular", "Top Rated"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        setPreference(false);
                        gridview.setAdapter(null);
                        loadMovieData();
                        break;
                    case 1:
                        setPreference(true);
                        gridview.setAdapter(null);
                        loadMovieData();
                        break;
                }

            }
        });
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                popup();
                return true;
            case  R.id.action_refresh:
                gridview.setAdapter(null);
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setPreference(boolean b){
        SharedPreferences sharedPref = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("SortBy", b);
        editor.apply();
    }

    private boolean getPreference(){
        SharedPreferences sharedPref = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);

        return sharedPref.getBoolean("SortBy",true);
    }
}


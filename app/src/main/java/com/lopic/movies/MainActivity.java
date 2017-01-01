package com.lopic.movies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lopic.movies.utilities.NetworkUtils;
import com.lopic.movies.utilities.OpenJsonUtils;

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
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        gridview = (GridView) findViewById(R.id.gridview);
        loadMovieData();
    }

    private void loadMovieData() {
        showDataView();
        new FetchMovieData().execute();
    }

    private void showDataView() {
        gridview.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        gridview.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieData extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {

            boolean options = getPreference();
            URL RequestUrl = NetworkUtils.buildUrl(options);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(RequestUrl);

                List<Movie> simpleJsonData = OpenJsonUtils
                        .getSimpleStringsFromJson(jsonResponse);

                return simpleJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> results) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            imageDisplay(results);
        }
    }

    private void imageDisplay(final List<Movie> results) {
        if (results != null) {
            gridview.setAdapter(new ImageAdapter(MainActivity.this, results));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
                    intentToStartDetailActivity.putExtra("movie", results.get(position).getArray());
                    startActivity(intentToStartDetailActivity);
                }
            });
        } else {
            showErrorMessage();
        }
    }

    private void popup() {
        CharSequence colors[] = new CharSequence[]{"Most Popular", "Top Rated"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setPreference(false);
                } else if (which == 1) {
                    setPreference(true);
                }
                loadMovieData();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                popup();
                return true;
            case R.id.action_refresh:
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setPreference(boolean b) {
        SharedPreferences sharedPref = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("SortBy", b);
        editor.apply();
    }

    private boolean getPreference() {
        SharedPreferences sharedPref = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);

        return sharedPref.getBoolean("SortBy", true);
    }
}


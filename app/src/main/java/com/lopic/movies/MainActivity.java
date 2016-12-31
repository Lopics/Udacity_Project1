package com.lopic.movies;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.lopic.movies.Data.Preferences;
import com.lopic.movies.utilities.NetworkUtils;
import com.lopic.movies.utilities.OpenWeatherJsonUtils;

import java.net.URL;
public class MainActivity extends AppCompatActivity {

    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gridview);
        Preferences.setPreferredFilter(true);
        loadMovieData();
    }
    private void loadMovieData(){
        new FetchMovieData().execute();
    }

    public class FetchMovieData extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            boolean options = Preferences.getPreferredFilter();
            URL weatherRequestUrl = NetworkUtils.buildUrl(options);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            if (weatherData != null) {
                gridview.setAdapter(new ImageAdapter(MainActivity.this, weatherData));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Toast.makeText(MainActivity.this, "" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
                        Preferences.setPreferredFilter(false);
                        break;
                    case 1:
                        Preferences.setPreferredFilter(true);
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
                gridview.setAdapter(null);
                loadMovieData();
                return true;
            case  R.id.action_refresh:
                gridview.setAdapter(null);
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}


package com.lopic.movies.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import static android.R.attr.description;

public final class OpenWeatherJsonUtils {


    public static String[] getSimpleWeatherStringsFromJson(String JsonStr)
            throws JSONException {

        final String OWM_LIST = "results";

        final String OWM_POSTER = "poster_path";
        final String OWM_MESSAGE_CODE = "status_code";

        /* String array to hold each day's weather String */
        String[] parsedData = null;

        JSONObject Json = new JSONObject(JsonStr);

        /* Is there an error? */
        if (Json.has(OWM_MESSAGE_CODE)) {
            int errorCode = Json.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray weatherArray = Json.getJSONArray(OWM_LIST);

        parsedData = new String[weatherArray.length()];

        for (int i = 0; i < weatherArray.length(); i++) {
            String Poster;
            JSONObject dayForecast = weatherArray.getJSONObject(i);
            Poster = dayForecast.getString(OWM_POSTER);

            parsedData[i] = "http://image.tmdb.org/t/p/w185" + Poster;
        }

        return parsedData;
    }

    public static ContentValues[] getFullWeatherDataFromJson(Context context, String JsonStr) {

        return null;
    }
}
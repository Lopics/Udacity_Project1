package com.lopic.movies.utilities;
import com.lopic.movies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public final class OpenJsonUtils {


    public static List<Movie> getSimpleStringsFromJson(String JsonStr)
            throws JSONException {

        final String OWM_LIST = "results";
        final String OWM_OT = "original_title";
        final String OWM_OV = "overview";
        final String OWM_VA = "vote_average";
        final String OWM_RD = "release_date";

        final String OWM_POSTER = "poster_path";
        final String OWM_MESSAGE_CODE = "status_code";

        List<Movie> movies = new ArrayList<Movie>();

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

        JSONArray Array = Json.getJSONArray(OWM_LIST);



        for (int i = 0; i < Array.length(); i++) {
            String original_title;
            String poster;
            String overview;
            String vote_average;
            String release_date;
            JSONObject data_movie = Array.getJSONObject(i);
            original_title = data_movie.getString(OWM_OT);
            poster = "http://image.tmdb.org/t/p/w185" + data_movie.getString(OWM_POSTER);
            overview = data_movie.getString(OWM_OV);
            vote_average = data_movie.getString(OWM_VA);
            release_date = data_movie.getString(OWM_RD);
            movies.add(new Movie(original_title,poster,overview,vote_average,release_date));
        }

        return movies;
    }

}
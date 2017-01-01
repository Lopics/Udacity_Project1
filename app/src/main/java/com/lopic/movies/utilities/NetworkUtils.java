package com.lopic.movies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String RATED_BASE_URL =
            "https://api.themoviedb.org/3/movie/top_rated";
    private static final String POPULAR_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular";
    private static String BASE_URL;
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_PARAM = "api_key";

    public static URL buildUrl(boolean URL) {
        if(URL == true){
            BASE_URL = RATED_BASE_URL;
        }else {
            BASE_URL = POPULAR_BASE_URL;
        }
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_PARAM,API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
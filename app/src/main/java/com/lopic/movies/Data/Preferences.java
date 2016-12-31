package com.lopic.movies.Data;


public class Preferences {
    private static boolean PREF_FILTER;
    public static boolean getPreferredFilter() {
        return PREF_FILTER;
    }
    public static void setPreferredFilter(boolean f) {
        PREF_FILTER = f;
    }
}

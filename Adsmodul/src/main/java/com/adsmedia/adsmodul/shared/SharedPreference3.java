package com.adsmedia.adsmodul.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.adsmedia.adsmodul.model.DataGlobal3;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SharedPreference3 {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "DataGlobal3_Favorite";

    public SharedPreference3() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<DataGlobal3> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, DataGlobal3 product) {
        List<DataGlobal3> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, DataGlobal3 product) {
        ArrayList<DataGlobal3> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<DataGlobal3> getFavorites(Context context) {
        SharedPreferences settings;
        List<DataGlobal3> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            DataGlobal3[] favoriteItems = gson.fromJson(jsonFavorites,
                    DataGlobal3[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<DataGlobal3>) favorites;
    }
}

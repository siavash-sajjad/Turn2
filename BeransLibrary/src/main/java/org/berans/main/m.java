package org.berans.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;

public class m extends Application {
    public static Activity activity;
    public static Context context;
    public static AssetManager assets;
    public static LayoutInflater inflater;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    public static Typeface tfAref;
    public static Typeface tfTitr;
    public static Typeface tfMitra;
    public static Typeface tfYekan;
    public static Typeface tfTunisia;
    public static Typeface tfIranSans;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        assets = getAssets();
        inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        tfTitr = Typeface.createFromAsset(assets, "fonts/titr.ttf");
        tfAref = Typeface.createFromAsset(assets, "fonts/aref.ttf");
        tfMitra = Typeface.createFromAsset(assets, "fonts/mitra.ttf");
        tfYekan = Typeface.createFromAsset(assets, "fonts/yekan.ttf");
        tfIranSans = Typeface.createFromAsset(assets, "fonts/iransans.ttf");
    }

}

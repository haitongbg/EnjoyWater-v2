package com.enjoywater.activiy;

import android.support.multidex.MultiDexApplication;

import com.enjoywater.model.Location.City;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.utils.Utils;

import java.util.ArrayList;

public class MyApplication extends MultiDexApplication {
    private MainService mainService;
    private static MyApplication mInstance;
    private ArrayList<City> mCities;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mCities = Utils.getCities(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MainService getMainService() {
        if (mainService == null) mainService = MainService.Factory.create();
        return mainService;
    }

    public ArrayList<City> getCities() {
        if (mCities == null) mCities = Utils.getCities(this);
        return mCities;
    }
}

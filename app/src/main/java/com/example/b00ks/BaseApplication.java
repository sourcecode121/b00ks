package com.example.b00ks;

import android.app.Application;

import com.example.b00ks.di.ApplicationComponent;
import com.example.b00ks.di.ApplicationModule;
import com.example.b00ks.di.DaggerApplicationComponent;

/**
 * Created by Anand on 12/11/2016.
 */

public class BaseApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}

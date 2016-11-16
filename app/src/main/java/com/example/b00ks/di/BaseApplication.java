package com.example.b00ks.di;

import android.app.Application;

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

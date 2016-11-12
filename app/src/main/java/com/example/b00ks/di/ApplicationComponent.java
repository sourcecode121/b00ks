package com.example.b00ks.di;

import com.example.b00ks.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anand on 12/11/2016.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(MainActivity target);
}

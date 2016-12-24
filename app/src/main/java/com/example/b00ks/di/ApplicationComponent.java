package com.example.b00ks.di;

import com.example.b00ks.view.fragments.AuthorInfo;
import com.example.b00ks.view.fragments.FindBooks;
import com.example.b00ks.view.fragments.RecentReviews;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anand on 12/11/2016.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(RecentReviews target);
    void inject(FindBooks target);
    void inject(AuthorInfo target);
}

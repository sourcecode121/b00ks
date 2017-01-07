package com.example.insight.di;

import com.example.insight.view.BookDetailsActivity;
import com.example.insight.view.fragments.AuthorInfo;
import com.example.insight.view.fragments.FindBooks;
import com.example.insight.view.fragments.RecentReviews;

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
    void inject(BookDetailsActivity target);
}

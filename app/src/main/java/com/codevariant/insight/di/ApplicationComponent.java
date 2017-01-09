package com.codevariant.insight.di;

import com.codevariant.insight.view.BookDetailsActivity;
import com.codevariant.insight.view.fragments.AuthorInfo;
import com.codevariant.insight.view.fragments.FindBooks;
import com.codevariant.insight.view.fragments.RecentReviews;

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

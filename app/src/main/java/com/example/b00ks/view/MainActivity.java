package com.example.b00ks.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.b00ks.R;
import com.example.b00ks.view.fragments.AuthorInfo;
import com.example.b00ks.view.fragments.FindBooks;
import com.example.b00ks.view.fragments.RecentReviews;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.b00ks.util.Utility.hideKeyboard;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setUpNavigationDrawer();

        if (savedInstanceState == null) {
            RecentReviews recentReviews =  new RecentReviews();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, recentReviews)
                    .commit();
        }
    }

    private void setUpNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                }
                else {
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.nav_recent_reviews:
                            RecentReviews recentReviews = new RecentReviews();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, recentReviews)
                                    .commit();
                            break;
                        case R.id.nav_find_books:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FindBooks findBooks = new FindBooks();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, findBooks)
                                            .commit();
                                }
                            }, 1000);
                            break;
                        case R.id.nav_author_info:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AuthorInfo authorInfo = new AuthorInfo();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, authorInfo)
                                            .commit();
                                }
                            }, 1000);
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard(this);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

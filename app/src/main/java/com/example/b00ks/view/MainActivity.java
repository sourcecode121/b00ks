package com.example.b00ks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.api.BookService;
import com.example.b00ks.di.BaseApplication;
import com.example.b00ks.model.recentReview.RecentReviewResponse;
import com.example.b00ks.view.recycler.OnItemClickListener;
import com.example.b00ks.view.recycler.ReviewsAdapter;

import org.parceler.Parcels;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    @Inject
    @Named("key") String key;
    @Inject
    BookService bookService;

    @BindView(R.id.books_recycler_view)
    RecyclerView booksRecyclerView;
    @BindView(R.id.error_layout)
    View errorLayout;
    @BindView(R.id.error_text_view)
    TextView errorTextView;
    @BindView(R.id.activity_main)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    public static final String DETAILS = "details";

    private Subscription subscription;
    private RecentReviewResponse recentReviewResponse;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setUpNavigationDrawer();

        connect();
    }

    private void setUpNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_find_books:
                        item.setChecked(true);
                        startActivity(new Intent(MainActivity.this, FindBooksActivity.class));
                }
                return true;
            }
        });
    }

    @OnClick(R.id.retry_button)
    protected void retry() {
        connect();
    }

    private void connect() {
        subscription = bookService.getRecentReviews(key)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<RecentReviewResponse>() {
                                    @Override
                                    public void onCompleted() {
                                        errorLayout.setVisibility(View.GONE);
                                        booksRecyclerView.setVisibility(View.VISIBLE);

                                        booksRecyclerView.setHasFixedSize(true);
                                        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                                        booksRecyclerView.setLayoutManager(linearLayoutManager);
                                        booksRecyclerView.setAdapter(reviewsAdapter);

                                        reviewsAdapter.setClickListener(MainActivity.this);

                                        subscription.unsubscribe();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        errorTextView.setText(R.string.connection_issues);
                                        booksRecyclerView.setVisibility(View.GONE);
                                        errorLayout.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onNext(RecentReviewResponse recentReviewResponse) {
                                        MainActivity.this.recentReviewResponse = recentReviewResponse;
                                        reviewsAdapter = new ReviewsAdapter(MainActivity.this, recentReviewResponse);
                                    }
                                });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, recentReviewResponse.getReviews().get(position).getUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DETAILS, Parcels.wrap(recentReviewResponse.getReviews().get(position)));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

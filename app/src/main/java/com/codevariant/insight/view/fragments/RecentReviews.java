package com.codevariant.insight.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codevariant.insight.R;
import com.codevariant.insight.api.BookService;
import com.codevariant.insight.di.BaseApplication;
import com.codevariant.insight.model.recentReview.RecentReviewResponse;
import com.codevariant.insight.view.DetailsActivity;
import com.codevariant.insight.view.recycler.OnItemClickListener;
import com.codevariant.insight.view.recycler.ReviewsAdapter;

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

/**
 * Created by Anand on 19/12/2016.
 */

public class RecentReviews extends Fragment implements OnItemClickListener {

    @Inject
    @Named("key") String key;
    @Inject
    BookService bookService;

    @BindView(R.id.books_recycler_view)
    RecyclerView booksRecyclerView;
    @BindView(R.id.error_layout)
    View errorLayout;
    @BindView(R.id.progress_layout)
    View progressLayout;

    public static final String DETAILS = "details";
    private static final String RECYCLER_STATE = "recycler_state";
    private static final String RECYCLER_STATE_RESPONSE = "recycler_state_response";
    private static final String SCREEN_STATE = "screen_state";

    private Context context;
    private Subscription subscription;
    private RecentReviewResponse recentReviewResponse = null;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsAdapter reviewsAdapter;
    private Parcelable recyclerState;
    private int[] screenState;
    private MenuItem menuRefresh = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent_reviews, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.recent_reviews);

        booksRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(context);
        booksRecyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null && (screenState = savedInstanceState.getIntArray(SCREEN_STATE)) != null) {

            if (screenState[0] == View.VISIBLE) {
                connect();
            }
            else {
                booksRecyclerView.setVisibility(screenState[1]);
                errorLayout.setVisibility(screenState[2]);

                if (screenState[1] == View.VISIBLE) {
                    recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE);
                    recentReviewResponse = Parcels.unwrap(savedInstanceState.getParcelable(RECYCLER_STATE_RESPONSE));

                    reviewsAdapter = new ReviewsAdapter(context, recentReviewResponse);
                    booksRecyclerView.setAdapter(reviewsAdapter);
                    reviewsAdapter.setClickListener(RecentReviews.this);
                    booksRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
                }
            }
        }
        else {
            connect();
        }
    }

    @OnClick(R.id.retry_button)
    protected void retry() {
        connect();
    }

    private void connect() {
        errorLayout.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        subscription = bookService.getRecentReviews(key)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecentReviewResponse>() {
                    @Override
                    public void onCompleted() {
                        progressLayout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.GONE);
                        booksRecyclerView.setVisibility(View.VISIBLE);
                        if (menuRefresh != null) {
                            menuRefresh.getIcon().setTint(Color.WHITE);
                            menuRefresh.setEnabled(true);
                        }

                        booksRecyclerView.setAdapter(reviewsAdapter);
                        reviewsAdapter.setClickListener(RecentReviews.this);

                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressLayout.setVisibility(View.GONE);
                        booksRecyclerView.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(RecentReviewResponse recentReviewResponse) {
                        RecentReviews.this.recentReviewResponse = recentReviewResponse;
                        reviewsAdapter = new ReviewsAdapter(context, recentReviewResponse);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DETAILS, Parcels.wrap(recentReviewResponse.getReviews().get(position)));
        startActivity(intent);
    }

    public void refreshRecycler() {
        if (recentReviewResponse != null) {
            recentReviewResponse.getReviews().clear();
        }
        booksRecyclerView.removeAllViews();
        linearLayoutManager = new LinearLayoutManager(context);
        booksRecyclerView.setLayoutManager(linearLayoutManager);
        connect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                menuRefresh = item;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    item.getIcon().setTint(getResources().getColor(R.color.colorAccent, null));
                }
                else {
                    //noinspection deprecation
                    item.getIcon().setTint(getResources().getColor(R.color.colorAccent));
                }
                item.setEnabled(false);
                refreshRecycler();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        recyclerState = booksRecyclerView.getLayoutManager().onSaveInstanceState();
        screenState = new int[] {
                progressLayout.getVisibility(),
                booksRecyclerView.getVisibility(),
                errorLayout.getVisibility()
        };

        outState.putParcelable(RECYCLER_STATE, recyclerState);
        outState.putParcelable(RECYCLER_STATE_RESPONSE, Parcels.wrap(recentReviewResponse));
        outState.putIntArray(SCREEN_STATE, screenState);
        super.onSaveInstanceState(outState);
    }
}

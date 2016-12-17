package com.example.b00ks.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00ks.R;
import com.example.b00ks.api.BookService;
import com.example.b00ks.di.BaseApplication;
import com.example.b00ks.model.findBook.FindBookResponse;
import com.example.b00ks.model.findBook.SearchResult;
import com.example.b00ks.model.findBook.Work;
import com.example.b00ks.model.recentReview.RecentReviewResponse;
import com.example.b00ks.model.recentReview.User;
import com.example.b00ks.view.recycler.FindBooksAdapter;
import com.example.b00ks.view.recycler.OnItemClickListener;
import com.example.b00ks.view.recycler.OnLoadMoreListener;
import com.example.b00ks.view.recycler.RecyclerScrollListener;
import com.example.b00ks.view.recycler.ReviewsAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.b00ks.R.string.find;
import static com.example.b00ks.util.Constants.PAGE;
import static com.example.b00ks.util.Constants.SEARCH_FIELD;
import static com.example.b00ks.util.Utility.hideKeyboard;

/**
 * Created by Anand on 09/12/2016.
 */

public class FindBooksActivity extends AppCompatActivity
        implements OnItemClickListener, OnLoadMoreListener {

    @Inject
    @Named("key") String key;
    @Inject
    BookService bookService;

    @BindView(R.id.find_recycler_view)
    RecyclerView findRecyclerView;
    @BindView(R.id.error_layout)
    View errorLayout;
    @BindView(R.id.error_text_view)
    TextView errorTextView;
    @BindView(R.id.find_container)
    View findContainer;
    @BindView(R.id.find_edit_text)
    TextInputEditText findEditText;
    @BindView(R.id.progress_layout)
    View progressLayout;

    public static final String BOOK_DETAILS = "book_details";
    private static final String RECYCLER_STATE = "recycler_state";
    private static final String RECYCLER_STATE_LIST = "recycler_state_list";
    private static final String RECYCLER_STATE_PAGE = "recycler_state_page";
    private static final String RECYCLER_STATE_FIND_TEXT = "recycler_state_find_text";
    private static int page = PAGE;

    private Subscription subscription;
    private List<Work> results;
    private LinearLayoutManager linearLayoutManager;
    private FindBooksAdapter findBooksAdapter;
    private RecyclerScrollListener scrollListener;
    private Parcelable recyclerState;
    private SearchResult searchResult;
    private String findText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        searchResult = new SearchResult();
        results = new ArrayList<>();

        findRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(FindBooksActivity.this);
        scrollListener = new RecyclerScrollListener(linearLayoutManager, FindBooksActivity.this);
        findRecyclerView.setLayoutManager(linearLayoutManager);
        findRecyclerView.addOnScrollListener(scrollListener);

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(RECYCLER_STATE_PAGE);
            findText = savedInstanceState.getString(RECYCLER_STATE_FIND_TEXT);
            recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE);
            searchResult = Parcels.unwrap(savedInstanceState.getParcelable(RECYCLER_STATE_LIST));
            results = searchResult.getResults();

            findBooksAdapter = new FindBooksAdapter(FindBooksActivity.this, results);
            findBooksAdapter.setClickListener(FindBooksActivity.this);
            findRecyclerView.setAdapter(findBooksAdapter);
            findRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
        }
    }

    @OnClick(R.id.find_button)
    protected void findButtonClick() {
        if (findEditText.getText() == null || findEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Search box is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            hideKeyboard(this);
            results.clear();
            findRecyclerView.removeAllViews();
            findText = findEditText.getText().toString().trim();
            page = PAGE;
            showProgressLayout();
            connect(page);
        }
    }

    @OnClick(R.id.retry_button)
    protected void retry() {
        showProgressLayout();
        connect(page);
    }

    private void connect(int page) {
        subscription = bookService.findBooks(findText, String.valueOf(page), key, SEARCH_FIELD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindBookResponse>() {
                    @Override
                    public void onCompleted() {
                        progressLayout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.GONE);
                        findContainer.setVisibility(View.VISIBLE);
                        findRecyclerView.setVisibility(View.VISIBLE);
                        findRecyclerView.requestFocus();

                        if (!findBooksAdapter.loading()) {
                            findRecyclerView.setAdapter(findBooksAdapter);
                        }
                        else {
                            findBooksAdapter.setLoaded();
                        }

                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        errorTextView.setText(R.string.connection_issues);
                        progressLayout.setVisibility(View.GONE);
                        findRecyclerView.setVisibility(View.GONE);
                        findContainer.setVisibility(View.VISIBLE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(FindBookResponse findBookResponse) {
                        if (results.isEmpty()) {
                            results = findBookResponse.getSearch().getResults();
                            findBooksAdapter = new FindBooksAdapter(FindBooksActivity.this, results);
                            findBooksAdapter.setClickListener(FindBooksActivity.this);
                        }
                        else {
                            findBooksAdapter.addResults(findBookResponse.getSearch().getResults());
                        }
                    }
                });
    }

    private void showProgressLayout() {
        errorLayout.setVisibility(View.GONE);
        findContainer.setVisibility(View.GONE);
        findRecyclerView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra(BOOK_DETAILS, Parcels.wrap(results.get(position)));
        startActivity(intent);
    }

    @Override
    public void loadMore() {
        if (!findBooksAdapter.loading()) {
            findBooksAdapter.showProgress();

            results.add(null);
            findBooksAdapter.notifyItemInserted(results.size() - 1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    page++;
                    connect(page);
                }
            }, 2000);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        recyclerState = findRecyclerView.getLayoutManager().onSaveInstanceState();
        searchResult.setResults(results);
        outState.putParcelable(RECYCLER_STATE, recyclerState);
        outState.putParcelable(RECYCLER_STATE_LIST, Parcels.wrap(searchResult));
        outState.putInt(RECYCLER_STATE_PAGE, page);
        outState.putString(RECYCLER_STATE_FIND_TEXT, findText);
        super.onSaveInstanceState(outState);
    }
}

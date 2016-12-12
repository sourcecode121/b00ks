package com.example.b00ks.view;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.b00ks.model.findBook.Work;
import com.example.b00ks.model.recentReview.RecentReviewResponse;
import com.example.b00ks.model.recentReview.User;
import com.example.b00ks.view.recycler.FindBooksAdapter;
import com.example.b00ks.view.recycler.OnItemClickListener;
import com.example.b00ks.view.recycler.OnLoadMoreListener;
import com.example.b00ks.view.recycler.RecyclerScrollListener;
import com.example.b00ks.view.recycler.ReviewsAdapter;

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

    private static int page = PAGE;

    private Subscription subscription;
    private List<Work> results;
    private LinearLayoutManager linearLayoutManager;
    private FindBooksAdapter findBooksAdapter;
    private RecyclerScrollListener scrollListener;
    private String findText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        results = new ArrayList<>();

        findRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(FindBooksActivity.this);
        scrollListener = new RecyclerScrollListener(linearLayoutManager, FindBooksActivity.this);
        findRecyclerView.setLayoutManager(linearLayoutManager);
        findRecyclerView.addOnScrollListener(scrollListener);
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
            connect(page);
        }
    }

    @OnClick(R.id.retry_button)
    protected void retry() {
        connect(page);
    }

    private void connect(int page) {
        subscription = bookService.findBooks(findText, String.valueOf(page), key, SEARCH_FIELD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindBookResponse>() {
                    @Override
                    public void onCompleted() {
                        errorLayout.setVisibility(View.GONE);
                        findContainer.setVisibility(View.VISIBLE);
                        findRecyclerView.setVisibility(View.VISIBLE);

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
                        findContainer.setVisibility(View.GONE);
                        findRecyclerView.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {

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
}

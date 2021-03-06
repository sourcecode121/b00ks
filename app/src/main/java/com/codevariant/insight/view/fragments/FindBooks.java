package com.codevariant.insight.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.codevariant.insight.R;
import com.codevariant.insight.api.BookService;
import com.codevariant.insight.di.BaseApplication;
import com.codevariant.insight.model.findBook.FindBookResponse;
import com.codevariant.insight.model.findBook.SearchResult;
import com.codevariant.insight.model.findBook.Work;
import com.codevariant.insight.view.BookDetailsActivity;
import com.codevariant.insight.view.recycler.FindBooksAdapter;
import com.codevariant.insight.view.recycler.OnItemClickListener;
import com.codevariant.insight.view.recycler.OnLoadMoreListener;
import com.codevariant.insight.view.recycler.RecyclerScrollListener;

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

import static com.codevariant.insight.util.Constants.PAGE;
import static com.codevariant.insight.util.Constants.SEARCH_FIELD;
import static com.codevariant.insight.util.Utility.hideKeyboard;
import static com.codevariant.insight.util.Utility.showKeyboard;

/**
 * Created by Anand on 20/12/2016.
 */

public class FindBooks extends Fragment
        implements OnItemClickListener, OnLoadMoreListener {

    @Inject
    @Named("key") String key;
    @Inject
    BookService bookService;

    @BindView(R.id.find_recycler_view)
    RecyclerView findRecyclerView;
    @BindView(R.id.error_layout)
    View errorLayout;
    @BindView(R.id.find_container)
    View findContainer;
    @BindView(R.id.find_edit_text)
    TextInputEditText findEditText;
    @BindView(R.id.progress_layout)
    View progressLayout;
    @BindView(R.id.default_layout)
    View defaultLayout;

    public static final String TAG = FindBooks.class.getSimpleName();

    public static final String BOOK_DETAILS = "book_details";
    private static final String RECYCLER_STATE = "recycler_state";
    private static final String RECYCLER_STATE_LIST = "recycler_state_list";
    private static final String RECYCLER_STATE_PAGE = "recycler_state_page";
    private static final String RECYCLER_STATE_FIND_TEXT = "recycler_state_find_text";
    private static final String IS_LISTENER_ATTACHED = "is_listener_attached";
    private static final String SCREEN_STATE = "screen_state";
    private static int page = PAGE;

    private Context context;
    private Subscription subscription;
    private List<Work> results;
    private LinearLayoutManager linearLayoutManager;
    private FindBooksAdapter findBooksAdapter;
    private RecyclerScrollListener scrollListener;
    private Parcelable recyclerState;
    private SearchResult searchResult;
    private String findText;
    private int[] screenState;
    private boolean isListenerAttached = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_books, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.find_books);

        searchResult = new SearchResult();
        results = new ArrayList<>();

        findEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    findButtonClick();
                }
                return true;
            }
        });

        findRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(context);
        scrollListener = new RecyclerScrollListener(linearLayoutManager, FindBooks.this);
        findRecyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null && (screenState = savedInstanceState.getIntArray(SCREEN_STATE)) != null) {

            findText = savedInstanceState.getString(RECYCLER_STATE_FIND_TEXT);

            if (screenState[2] == View.VISIBLE) {
                page = PAGE;
                showProgressLayout();
                connect(page);
            }
            else {
                findContainer.setVisibility(screenState[0]);
                defaultLayout.setVisibility(screenState[1]);
                findRecyclerView.setVisibility(screenState[3]);
                errorLayout.setVisibility(screenState[4]);

                page = savedInstanceState.getInt(RECYCLER_STATE_PAGE);
                recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE);
                isListenerAttached = savedInstanceState.getBoolean(IS_LISTENER_ATTACHED);
                searchResult = Parcels.unwrap(savedInstanceState.getParcelable(RECYCLER_STATE_LIST));
                results = searchResult.getResults();

                findBooksAdapter = new FindBooksAdapter(context, results);
                findBooksAdapter.setClickListener(FindBooks.this);
                findRecyclerView.setAdapter(findBooksAdapter);
                findRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
                if (isListenerAttached) {
                    findRecyclerView.addOnScrollListener(scrollListener);
                }
            }
        }
        else {
            findEditText.post(new Runnable() {
                @Override
                public void run() {
                    findEditText.requestFocus();
                }
            });
            showKeyboard(getActivity());
        }
    }

    @OnClick(R.id.find_button)
    protected void findButtonClick() {
        if (findEditText.getText() == null || findEditText.getText().toString().trim().equals("")) {
            Toast.makeText(context, "Search box is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            hideKeyboard(getActivity());
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
        if (!isListenerAttached) {
            findRecyclerView.addOnScrollListener(scrollListener);
            isListenerAttached = true;
        }
        subscription = bookService.findBooks(findText, String.valueOf(page), key, SEARCH_FIELD)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindBookResponse>() {
                    @Override
                    public void onCompleted() {
                        // Screen state
                        progressLayout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.GONE);
                        findContainer.setVisibility(View.VISIBLE);
                        if (!results.isEmpty()) {
                            defaultLayout.setVisibility(View.GONE);
                            findRecyclerView.setVisibility(View.VISIBLE);
                            findRecyclerView.requestFocus();
                        }
                        else {
                            findRecyclerView.setVisibility(View.GONE);
                            defaultLayout.setVisibility(View.VISIBLE);
                        }

                        // Loading state
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
                        defaultLayout.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.GONE);
                        findRecyclerView.setVisibility(View.GONE);
                        findContainer.setVisibility(View.VISIBLE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(FindBookResponse findBookResponse) {
                        if (findBookResponse.getSearch().getResults().size() < 20) {
                            findRecyclerView.clearOnScrollListeners();
                            isListenerAttached = false;
                        }
                        if (results.isEmpty()) {
                            results = findBookResponse.getSearch().getResults();
                            findBooksAdapter = new FindBooksAdapter(context, results);
                            findBooksAdapter.setClickListener(FindBooks.this);
                        }
                        else {
                            findBooksAdapter.addResults(findBookResponse.getSearch().getResults());
                        }
                    }
                });
    }

    private void showProgressLayout() {
        defaultLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        findContainer.setVisibility(View.GONE);
        findRecyclerView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(context, BookDetailsActivity.class);
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
    public void onSaveInstanceState(Bundle outState) {
        recyclerState = findRecyclerView.getLayoutManager().onSaveInstanceState();
        searchResult.setResults(results);
        screenState = new int[] {
                findContainer.getVisibility(),
                defaultLayout.getVisibility(),
                progressLayout.getVisibility(),
                findRecyclerView.getVisibility(),
                errorLayout.getVisibility()
        };

        outState.putParcelable(RECYCLER_STATE, recyclerState);
        outState.putParcelable(RECYCLER_STATE_LIST, Parcels.wrap(searchResult));
        outState.putInt(RECYCLER_STATE_PAGE, page);
        outState.putString(RECYCLER_STATE_FIND_TEXT, findText);
        outState.putBoolean(IS_LISTENER_ATTACHED, isListenerAttached);
        outState.putIntArray(SCREEN_STATE, screenState);
        super.onSaveInstanceState(outState);
    }
}

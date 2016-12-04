package com.example.b00ks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00ks.R;
import com.example.b00ks.api.BookService;
import com.example.b00ks.di.BaseApplication;
import com.example.b00ks.model.Response;
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

    public static final String DETAILS = "details";

    private Subscription subscription;
    private Response response;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        connect();
    }

    @OnClick(R.id.retry_button)
    protected void retry() {
        connect();
    }

    private void connect() {
        subscription = bookService.getRecentReviews(key)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Response>() {
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
                                    public void onNext(Response response) {
                                        MainActivity.this.response = response;
                                        reviewsAdapter = new ReviewsAdapter(MainActivity.this, response);
                                    }
                                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, response.getReviews().get(position).getUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DETAILS, Parcels.wrap(response.getReviews().get(position)));
        startActivity(intent);
    }
}

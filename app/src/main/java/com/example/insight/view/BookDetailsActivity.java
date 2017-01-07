package com.example.insight.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.insight.R;
import com.example.insight.api.BookService;
import com.example.insight.di.BaseApplication;
import com.example.insight.model.bookInfo.Book;
import com.example.insight.model.bookInfo.BookInfoResponse;
import com.example.insight.model.findBook.Work;
import com.example.insight.view.fragments.FindBooks;
import com.squareup.picasso.Picasso;

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

import static com.example.insight.util.Constants.MONTHS;
import static com.example.insight.util.Utility.applyHtmlTags;
import static com.example.insight.util.Utility.removeHtmlTags;

/**
 * Created by Anand on 17/12/2016.
 */

public class BookDetailsActivity extends AppCompatActivity {

    @Inject
    @Named("key") String key;
    @Inject
    BookService bookService;

    @BindView(R.id.error_layout)
    View errorLayout;
    @BindView(R.id.progress_layout)
    View progressLayout;
    @BindView(R.id.book_details_container)
    View bookDetailsContainer;
    @BindView(R.id.book_details_description)
    TextView description;
    @BindView(R.id.details_title)
    TextView title;
    @BindView(R.id.details_name)
    TextView author;
    @BindView(R.id.book_details_publication_date)
    TextView publicationDate;
    @BindView(R.id.book_details_average_rating)
    TextView averageRating;
    @BindView(R.id.book_details_ratings_count)
    TextView ratingsCount;
    @BindView(R.id.book_details_reviews_count)
    TextView reviewsCount;
    @BindView(R.id.book_details_link)
    TextView link;
    @BindView(R.id.details_image)
    ImageView detailsImage;

    private static final String BOOK_INFO = "book_info";
    private static final String SCREEN_STATE = "screen_state";

    private Subscription subscription;
    private Work work;
    private Resources resources;
    private String bookId;
    private Book book = null;
    private int[] screenState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        setTitle(R.string.find_books);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        work = Parcels.unwrap(getIntent().getParcelableExtra(FindBooks.BOOK_DETAILS));
        bookId = work.getBestBook().getId();
        resources = getResources();

        if (savedInstanceState != null && (screenState = savedInstanceState.getIntArray(SCREEN_STATE)) != null) {

            if (screenState[0] == View.VISIBLE) {
                connect();
            }
            else {
                bookDetailsContainer.setVisibility(screenState[1]);
                errorLayout.setVisibility(screenState[2]);

                if (screenState[1] == View.VISIBLE) {
                    book = Parcels.unwrap(savedInstanceState.getParcelable(BOOK_INFO));
                    showBookInfo();
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
        bookDetailsContainer.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        subscription = bookService.getBookInfo(bookId, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookInfoResponse>() {
                    @Override
                    public void onCompleted() {
                        progressLayout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.GONE);
                        bookDetailsContainer.setVisibility(View.VISIBLE);

                        showBookInfo();

                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressLayout.setVisibility(View.GONE);
                        bookDetailsContainer.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(BookInfoResponse bookInfoResponse) {
                        book = bookInfoResponse.getBook();
                    }
                });
    }

    private void showBookInfo() {
        publicationDate.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);

        String month;
        String date;

        Picasso.with(this)
                .load(work.getBestBook().getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .resize(220, 300)
                .into(detailsImage);

        title.setText(work.getBestBook().getTitle());
        author.setText(work.getBestBook().getAuthor().getName());

        if (work.getPublicationYear() == null || work.getPublicationYear().trim().equals("")) {
            publicationDate.setVisibility(View.GONE);
        }
        else {
            if (work.getPublicationMonth() == null || work.getPublicationMonth().trim().equals("")) {
                publicationDate.setText(applyHtmlTags(resources.getString(R.string.book_publication_date,
                        work.getPublicationYear())));
            }
            else {
                month = MONTHS[Integer.parseInt(work.getPublicationMonth()) - 1];
                if (work.getPublicationDay() == null || work.getPublicationDay().trim().equals("")) {
                    date = month + ", " + work.getPublicationYear();
                    publicationDate.setText(applyHtmlTags(resources.getString(R.string.book_publication_date, date)));
                }
                else {
                    date = month + " " + work.getPublicationDay() + ", " + work.getPublicationYear();
                    publicationDate.setText(applyHtmlTags(resources.getString(R.string.book_publication_date, date)));
                }
            }
        }

        if (book != null) {
            if (book.getDescription() == null || book.getDescription().trim().equals("")) {
                description.setVisibility(View.GONE);
            }
            else {
                String str = "\n" + removeHtmlTags(book.getDescription().trim());
                description.setText(applyHtmlTags(resources.getString(R.string.book_description)));
                description.append(str);
            }
        }

        averageRating.setText(applyHtmlTags(resources.getString(R.string.book_average_rating, work.getAverageRating())));
        ratingsCount.setText(applyHtmlTags(resources.getString(R.string.book_ratings_count, work.getRatingsCount())));
        reviewsCount.setText(applyHtmlTags(resources.getString(R.string.book_reviews_count, work.getReviewsCount())));
        link.setMovementMethod(LinkMovementMethod.getInstance());
        String strLink = resources.getString(R.string.web_link, book.getLink());
        link.setText(applyHtmlTags(strLink));
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        screenState = new int[] {
                progressLayout.getVisibility(),
                bookDetailsContainer.getVisibility(),
                errorLayout.getVisibility()
        };

        outState.putParcelable(BOOK_INFO, Parcels.wrap(book));
        outState.putIntArray(SCREEN_STATE, screenState);
        super.onSaveInstanceState(outState);
    }
}

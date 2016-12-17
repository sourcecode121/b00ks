package com.example.b00ks.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.findBook.Work;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anand on 17/12/2016.
 */

public class BookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.book_details_title)
    TextView title;
    @BindView(R.id.book_details_author)
    TextView author;
    @BindView(R.id.book_details_publication_date)
    TextView publicationDate;
    @BindView(R.id.book_details_average_rating)
    TextView averageRating;
    @BindView(R.id.book_details_ratings_count)
    TextView ratingsCount;
    @BindView(R.id.book_details_reviews_count)
    TextView reviewsCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        ButterKnife.bind(this);

        Work work = Parcels.unwrap(getIntent().getParcelableExtra(FindBooksActivity.BOOK_DETAILS));

        Resources resources = getResources();

        title.setText(work.getBestBook().getTitle());
        author.setText(resources.getString(R.string.author_name, work.getBestBook().getAuthor().getName()));
        averageRating.setText(resources.getString(R.string.book_average_rating, work.getAverageRating()));
        ratingsCount.setText(resources.getString(R.string.book_ratings_count, work.getRatingsCount()));
        reviewsCount.setText(resources.getString(R.string.book_reviews_count, work.getReviewsCount()));
    }
}

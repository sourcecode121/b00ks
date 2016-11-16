package com.example.b00ks.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.Author;
import com.example.b00ks.model.Review;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anand on 15/11/2016.
 */

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.review_details_display_name) TextView displayName;
    @BindView(R.id.review_details_rating) TextView reviewRating;
    @BindView(R.id.review_details_body) TextView body;
    @BindView(R.id.book_details_title) TextView title;
    @BindView(R.id.book_details_num_pages) TextView numPages;
    @BindView(R.id.book_details_format) TextView format;
    @BindView(R.id.book_details_publisher) TextView publisher;
    @BindView(R.id.book_details_publication_year) TextView publicationYear;
    @BindView(R.id.book_details_average_rating) TextView bookAverageRating;
    @BindView(R.id.book_details_ratings_count) TextView bookRatingsCount;
    @BindView(R.id.author_details_name) TextView authorName;
    @BindView(R.id.author_details_average_rating) TextView authorAverageRating;
    @BindView(R.id.author_details_ratings_count) TextView authorRatingsCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Review review = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.DETAILS));

        displayName.setText(review.getUser().getDisplayName());
        reviewRating.setText(review.getRating());

        String reviewBody = review.getBody();
        if (reviewBody == null) body.setVisibility(View.GONE);
        else body.setText(reviewBody.trim());

        title.setText(review.getBook().getTitle());

        String bookPages = review.getBook().getNumPages();
        if (bookPages == null) numPages.setVisibility(View.GONE);
        else numPages.setText(bookPages);

        String bookFormat = review.getBook().getFormat();
        if (bookFormat == null) format.setVisibility(View.GONE);
        else format.setText(bookFormat);

        String bookPublisher = review.getBook().getPublisher();
        if (bookPublisher == null) publisher.setVisibility(View.GONE);
        else publisher.setText(bookPublisher);

        String bookYear = review.getBook().getPublicationYear();
        if (bookYear == null) publicationYear.setVisibility(View.GONE);
        else publicationYear.setText(bookYear);

        bookAverageRating.setText(review.getBook().getAverageRating());
        bookRatingsCount.setText(review.getBook().getRatingsCount());

        List<Author> authors = review.getBook().getAuthors();
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                authorName.append(String.format("%s ", author.getName()));
                authorAverageRating.append(String.format("%s ", author.getAverageRating()));
                authorRatingsCount.append(String.format("%s ", author.getRatingsCount()));
            }
        }
    }
}

package com.example.b00ks.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.recentReview.Author;
import com.example.b00ks.model.recentReview.Review;
import com.example.b00ks.util.Utility;

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

        Resources resources = getResources();

        displayName.setText(resources.getString(R.string.user_display_name, review.getUser().getDisplayName()));
        reviewRating.setText(resources.getString(R.string.user_rating, review.getRating()));

        String reviewBody = review.getBody();
        if (reviewBody == null) body.setVisibility(View.GONE);
        else body.setText(Utility.removeHtmlTags(resources.getString(R.string.user_review, reviewBody.trim())));

        title.setText(resources.getString(R.string.book_title, review.getBook().getTitle()));

        String bookPages = review.getBook().getNumPages();
        if (bookPages == null) numPages.setVisibility(View.GONE);
        else numPages.setText(resources.getString(R.string.num_pages, bookPages));

        String bookFormat = review.getBook().getFormat();
        if (bookFormat == null) format.setVisibility(View.GONE);
        else format.setText(resources.getString(R.string.format, bookFormat));

        String bookPublisher = review.getBook().getPublisher();
        if (bookPublisher == null) publisher.setVisibility(View.GONE);
        else publisher.setText(resources.getString(R.string.publisher, bookPublisher));

        String bookYear = review.getBook().getPublicationYear();
        if (bookYear == null) publicationYear.setVisibility(View.GONE);
        else publicationYear.setText(resources.getString(R.string.publication_year, bookYear));

        bookAverageRating.setText(resources.getString(R.string.book_average_rating, review.getBook().getAverageRating()));
        bookRatingsCount.setText(resources.getString(R.string.book_ratings_count, review.getBook().getRatingsCount()));

        List<Author> authors = review.getBook().getAuthors();
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                authorName.append(resources.getString(R.string.author_name, author.getName()));
                authorAverageRating.append(resources.getString(R.string.author_average_rating, author.getAverageRating()));
                authorRatingsCount.append(resources.getString(R.string.author_ratings_count, author.getRatingsCount()));
            }
        }
    }
}

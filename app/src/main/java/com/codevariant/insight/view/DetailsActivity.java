package com.codevariant.insight.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevariant.insight.R;
import com.codevariant.insight.model.recentReview.Author;
import com.codevariant.insight.model.recentReview.Review;
import com.codevariant.insight.view.fragments.RecentReviews;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codevariant.insight.util.Utility.applyHtmlTags;
import static com.codevariant.insight.util.Utility.removeHtmlTags;

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
    @BindView(R.id.review_details_link) TextView link;
    @BindView(R.id.details_image) ImageView detailsImage;
    @BindView(R.id.details_title) TextView detailsTitle;
    @BindView(R.id.details_name) TextView detailsName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(R.string.recent_reviews);

        ButterKnife.bind(this);

        Review review = Parcels.unwrap(getIntent().getParcelableExtra(RecentReviews.DETAILS));

        Resources resources = getResources();

        Picasso.with(this)
                .load(review.getBook().getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .resize(220, 300)
                .into(detailsImage);
        detailsTitle.setText(review.getBook().getTitle());

        displayName.setText(applyHtmlTags(resources.getString(R.string.user_display_name, review.getUser().getDisplayName())));
        reviewRating.setText(applyHtmlTags(resources.getString(R.string.user_rating, review.getRating())));

        String reviewBody = review.getBody();
        if (reviewBody == null) body.setVisibility(View.GONE);
        else body.setText(applyHtmlTags(resources.getString(R.string.user_review, removeHtmlTags(reviewBody.trim()))));

        title.setText(applyHtmlTags(resources.getString(R.string.book_title, review.getBook().getTitle())));

        String bookPages = review.getBook().getNumPages();
        if (bookPages == null) numPages.setVisibility(View.GONE);
        else numPages.setText(applyHtmlTags(resources.getString(R.string.num_pages, bookPages)));

        String bookFormat = review.getBook().getFormat();
        if (bookFormat == null) format.setVisibility(View.GONE);
        else format.setText(applyHtmlTags(resources.getString(R.string.format, bookFormat)));

        String bookPublisher = review.getBook().getPublisher();
        if (bookPublisher == null) publisher.setVisibility(View.GONE);
        else publisher.setText(applyHtmlTags(resources.getString(R.string.publisher, bookPublisher)));

        String bookYear = review.getBook().getPublicationYear();
        if (bookYear == null) publicationYear.setVisibility(View.GONE);
        else publicationYear.setText(applyHtmlTags(resources.getString(R.string.publication_year, bookYear)));

        bookAverageRating.setText(applyHtmlTags(resources.getString(R.string.book_average_rating,
                review.getBook().getAverageRating())));
        bookRatingsCount.setText(applyHtmlTags(resources.getString(R.string.book_ratings_count,
                review.getBook().getRatingsCount())));

        List<Author> authors = review.getBook().getAuthors();
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                authorName.append(applyHtmlTags(resources.getString(R.string.author_name, author.getName())));
                authorAverageRating.append(applyHtmlTags(resources.getString(R.string.author_average_rating,
                        author.getAverageRating())));
                authorRatingsCount.append(applyHtmlTags(resources.getString(R.string.author_ratings_count,
                        author.getRatingsCount())));

                detailsName.append(resources.getString(R.string.details_name, author.getName()));
            }
        }

        link.setMovementMethod(LinkMovementMethod.getInstance());
        String strLink = resources.getString(R.string.web_link, review.getLink());
        link.setText(applyHtmlTags(strLink));
    }
}

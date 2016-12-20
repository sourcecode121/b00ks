package com.example.b00ks.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.findBook.Work;
import com.example.b00ks.view.fragments.FindBooks;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.b00ks.util.Constants.MONTHS;

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

        Work work = Parcels.unwrap(getIntent().getParcelableExtra(FindBooks.BOOK_DETAILS));

        Resources resources = getResources();

        String month;
        String date;

        title.setText(work.getBestBook().getTitle());
        author.setText(resources.getString(R.string.author_name, work.getBestBook().getAuthor().getName()));

        if (work.getPublicationYear() == null || work.getPublicationYear().trim().equals("")) {
            publicationDate.setVisibility(View.GONE);
        }
        else {
            if (work.getPublicationMonth() == null || work.getPublicationMonth().trim().equals("")) {
                publicationDate.setText(resources.getString(R.string.book_publication_date, work.getPublicationYear()));
            }
            else {
                month = MONTHS[Integer.parseInt(work.getPublicationMonth()) - 1];
                if (work.getPublicationDay() == null || work.getPublicationDay().trim().equals("")) {
                    date = month + ", " + work.getPublicationYear();
                    publicationDate.setText(resources.getString(R.string.book_publication_date, date));
                }
                else {
                    date = month + " " + work.getPublicationDay() + ", " + work.getPublicationYear();
                    publicationDate.setText(resources.getString(R.string.book_publication_date, date));
                }
            }
        }

        averageRating.setText(resources.getString(R.string.book_average_rating, work.getAverageRating()));
        ratingsCount.setText(resources.getString(R.string.book_ratings_count, work.getRatingsCount()));
        reviewsCount.setText(resources.getString(R.string.book_reviews_count, work.getReviewsCount()));
    }
}

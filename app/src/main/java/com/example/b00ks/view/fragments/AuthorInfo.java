package com.example.b00ks.view.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00ks.R;
import com.example.b00ks.api.BookService;
import com.example.b00ks.di.BaseApplication;
import com.example.b00ks.model.findAuthor.Author;
import com.example.b00ks.model.findAuthor.FindAuthorResponse;
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

import static com.example.b00ks.util.Utility.applyHtmlTags;
import static com.example.b00ks.util.Utility.hideKeyboard;
import static com.example.b00ks.util.Utility.removeHtmlTags;
import static com.example.b00ks.util.Utility.showKeyboard;

/**
 * Created by Anand on 24/12/2016.
 */

public class AuthorInfo extends Fragment {

    @Inject
    @Named("key") String key;
    @Inject
    BookService bookService;

    @BindView(R.id.author_info_container) View authorInfoContainer;
    @BindView(R.id.error_layout) View errorLayout;
    @BindView(R.id.progress_layout) View progressLayout;
    @BindView(R.id.default_layout) View defaultLayout;
    @BindView(R.id.find_container) View findContainer;
    @BindView(R.id.find_edit_text) TextInputEditText findEditText;

    @BindView(R.id.details_name) TextView name;
    @BindView(R.id.author_info_about) TextView about;
    @BindView(R.id.author_info_works_count) TextView worksCount;
    @BindView(R.id.author_info_gender) TextView gender;
    @BindView(R.id.author_info_hometown) TextView hometown;
    @BindView(R.id.author_info_born_at) TextView bornAt;
    @BindView(R.id.author_info_died_at) TextView diedAt;
    @BindView(R.id.author_info_link) TextView link;
    @BindView(R.id.details_image) ImageView detailsImage;
    @BindView(R.id.details_title) TextView detailsTitle;

    private static final String AUTHOR = "author";
    private static final String FIND_TEXT = "find_text";
    private static final String SCREEN_STATE = "screen_state";

    private Context context;
    private Subscription subscription;
    private String findText;
    private String authorId = null;
    private Author author = null;
    private Resources resources;
    private int[] screenState;

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
        View v = inflater.inflate(R.layout.fragment_author_info, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.author_info);

        name.setVisibility(View.GONE);

        resources = getResources();

        findEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    findButtonClick();
                }
                return true;
            }
        });

        if (savedInstanceState != null && (screenState = savedInstanceState.getIntArray(SCREEN_STATE)) != null) {

            findText = savedInstanceState.getString(FIND_TEXT);

            if (screenState[2] == View.VISIBLE) {
                showProgressLayout();
                connect();
            }
            else {
                author = Parcels.unwrap(savedInstanceState.getParcelable(AUTHOR));
                if (author != null) {
                    showAuthorInfo();
                }

                findContainer.setVisibility(screenState[0]);
                defaultLayout.setVisibility(screenState[1]);
                authorInfoContainer.setVisibility(screenState[3]);
                errorLayout.setVisibility(screenState[4]);
            }
        }
        else {
            findEditText.requestFocus();
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
            findText = findEditText.getText().toString().trim();
            showProgressLayout();
            connect();
        }
    }

    @OnClick(R.id.retry_button)
    protected void retry() {
        showProgressLayout();
        connect();
    }

    private void connect() {
        subscription = bookService.findAuthor(findText, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindAuthorResponse>() {
                    @Override
                    public void onCompleted() {

                        subscription.unsubscribe();
                        // Screen state
                        if (authorId == null || authorId.equals("")) {
                            showDefaultLayout();
                        }
                        else {
                            getInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorLayout();
                    }

                    @Override
                    public void onNext(FindAuthorResponse findAuthorResponse) {
                        authorId = findAuthorResponse.getAuthor().getId();
                    }
                });
    }

    private void getInfo() {
        subscription = bookService.getAuthorInfo(authorId, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindAuthorResponse>() {
                    @Override
                    public void onCompleted() {
                        if (author == null) {
                            showDefaultLayout();
                        }
                        else {
                            authorInfoContainer.scrollTo(0, 0);
                            resetViewVisibility();
                            showAuthorInfo();

                            // Screen state
                            progressLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                            findContainer.setVisibility(View.VISIBLE);
                            defaultLayout.setVisibility(View.GONE);
                            authorInfoContainer.setVisibility(View.VISIBLE);
                        }

                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorLayout();
                    }

                    @Override
                    public void onNext(FindAuthorResponse findAuthorResponse) {
                        author = findAuthorResponse.getAuthor();
                    }
                });
    }

    private void resetViewVisibility() {
        about.setVisibility(View.VISIBLE);
        worksCount.setVisibility(View.VISIBLE);
        gender.setVisibility(View.VISIBLE);
        hometown.setVisibility(View.VISIBLE);
        bornAt.setVisibility(View.VISIBLE);
        diedAt.setVisibility(View.VISIBLE);
    }

    private void showAuthorInfo() {

        Picasso.with(context)
                .load(author.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(detailsImage);
        detailsTitle.setText(author.getName());

        if (author.getAbout() == null || author.getAbout().trim().equals("")) {
            about.setVisibility(View.GONE);
        }
        else {
            String str = "\n" + removeHtmlTags(author.getAbout().trim());
            about.setText(applyHtmlTags(resources.getString(R.string.author_info_about)));
            about.append(str);
        }

        if (author.getWorksCount() == null || author.getWorksCount().trim().equals("")) worksCount.setVisibility(View.GONE);
        else worksCount.setText(applyHtmlTags(resources.getString(R.string.author_info_works_count, author.getWorksCount())));

        if (author.getGender() == null || author.getGender().trim().equals("")) gender.setVisibility(View.GONE);
        else gender.setText(applyHtmlTags(resources.getString(R.string.author_info_gender, author.getGender())));

        if (author.getHometown() == null || author.getHometown().trim().equals("")) hometown.setVisibility(View.GONE);
        else hometown.setText(applyHtmlTags(resources.getString(R.string.author_info_hometown, author.getHometown())));

        if (author.getBornAt() == null || author.getBornAt().trim().equals("")) bornAt.setVisibility(View.GONE);
        else bornAt.setText(applyHtmlTags(resources.getString(R.string.author_info_born_at, author.getBornAt())));

        if (author.getDiedAt() == null || author.getDiedAt().trim().equals("")) diedAt.setVisibility(View.GONE);
        else diedAt.setText(applyHtmlTags(resources.getString(R.string.author_info_died_at, author.getDiedAt())));

        link.setMovementMethod(LinkMovementMethod.getInstance());
        String strLink = resources.getString(R.string.web_link, author.getLink());
        link.setText(applyHtmlTags(strLink));
    }

    private void showDefaultLayout() {
        progressLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        findContainer.setVisibility(View.VISIBLE);
        authorInfoContainer.setVisibility(View.GONE);
        defaultLayout.setVisibility(View.VISIBLE);
    }

    private void showErrorLayout() {
        defaultLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.GONE);
        authorInfoContainer.setVisibility(View.GONE);
        findContainer.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void showProgressLayout() {
        defaultLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        findContainer.setVisibility(View.GONE);
        authorInfoContainer.setVisibility(View.GONE);
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
    public void onSaveInstanceState(Bundle outState) {
        screenState = new int[] {
                findContainer.getVisibility(),
                defaultLayout.getVisibility(),
                progressLayout.getVisibility(),
                authorInfoContainer.getVisibility(),
                errorLayout.getVisibility()
        };

        outState.putParcelable(AUTHOR, Parcels.wrap(author));
        outState.putString(FIND_TEXT, findText);
        outState.putIntArray(SCREEN_STATE, screenState);
        super.onSaveInstanceState(outState);
    }
}

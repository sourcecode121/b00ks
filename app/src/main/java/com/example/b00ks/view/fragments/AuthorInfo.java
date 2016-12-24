package com.example.b00ks.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00ks.R;
import com.example.b00ks.api.BookService;
import com.example.b00ks.di.BaseApplication;
import com.example.b00ks.model.findAuthor.Author;
import com.example.b00ks.model.findAuthor.FindAuthorResponse;
import com.example.b00ks.model.findBook.FindBookResponse;
import com.example.b00ks.view.recycler.FindBooksAdapter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.b00ks.util.Constants.PAGE;
import static com.example.b00ks.util.Constants.SEARCH_FIELD;
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

    @BindView(R.id.author_info_name) TextView name;
    @BindView(R.id.author_info_about) TextView about;
    @BindView(R.id.author_info_works_count) TextView worksCount;
    @BindView(R.id.author_info_gender) TextView gender;
    @BindView(R.id.author_info_hometown) TextView hometown;
    @BindView(R.id.author_info_born_at) TextView bornAt;
    @BindView(R.id.author_info_died_at) TextView diedAt;

    private Context context;
    private Subscription subscription;
    private String findText;
    private String authorId = null;
    private Author author = null;

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

        findEditText.requestFocus();
        showKeyboard(getActivity());
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
                            showAuthorInfo();

                            // Screen state
                            progressLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                            findContainer.setVisibility(View.VISIBLE);
                            defaultLayout.setVisibility(View.GONE);
                            authorInfoContainer.setVisibility(View.VISIBLE);
                            authorInfoContainer.requestFocus();
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

    private void showAuthorInfo() {
        name.setText(author.getName());
        about.setText(removeHtmlTags(author.getAbout()));
        worksCount.setText(author.getWorksCount());
        gender.setText(author.getGender());
        hometown.setText(author.getHometown());
        bornAt.setText(author.getBornAt());
        diedAt.setText(author.getDiedAt());
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
}

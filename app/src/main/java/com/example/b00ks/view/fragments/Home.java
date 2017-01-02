package com.example.b00ks.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b00ks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anand on 02/01/2017.
 */

public class Home extends Fragment {

    @BindView(R.id.home_find_books)
    CardView findBooks;
    @BindView(R.id.home_author_info)
    CardView authorInfo;
    @BindView(R.id.home_recent_reviews)
    CardView recentReviews;

    private NavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.app_name);

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        findBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem(R.id.nav_find_books);
                FindBooks findBooks = new FindBooks();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, findBooks)
                        .commit();
            }
        });

        authorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem(R.id.nav_author_info);
                AuthorInfo authorInfo = new AuthorInfo();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, authorInfo)
                        .commit();
            }
        });

        recentReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem(R.id.nav_recent_reviews);
                RecentReviews recentReviews = new RecentReviews();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, recentReviews)
                        .commit();
            }
        });
    }
}

package com.example.b00ks.view.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Anand on 10/12/2016.
 */

public class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    private OnLoadMoreListener loadMoreListener;
    private LinearLayoutManager linearLayoutManager;

    public RecyclerScrollListener(LinearLayoutManager linearLayoutManager,
                                  OnLoadMoreListener loadMoreListener) {
        this.linearLayoutManager = linearLayoutManager;
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = linearLayoutManager.getItemCount();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int visibleThreshold = 5;

        if ((lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            loadMoreListener.loadMore();
        }
    }
}

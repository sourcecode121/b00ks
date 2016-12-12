package com.example.b00ks.view.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.findBook.FindBookResponse;
import com.example.b00ks.model.findBook.Work;
import com.example.b00ks.model.recentReview.Author;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anand on 09/12/2016.
 */

public class FindBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM_CONTENT = 1;
    private static final int VIEW_ITEM_LOAD = 2;

    private Context context;
    private OnItemClickListener clickListener;
    private boolean isLoading = false;
    private List<Work> results;

    public FindBooksAdapter(Context context, List<Work> results) {
        this.context = context;
        this.results = results;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void showProgress() {
        isLoading = true;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public boolean loading() {
        return isLoading;
    }

    @Override
    public int getItemViewType(int position) {
        return results.get(position) == null ? VIEW_ITEM_LOAD : VIEW_ITEM_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_ITEM_CONTENT) {
             v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_recycler_item, parent, false);
            return new ViewHolder(v);
        }
        else if (viewType == VIEW_ITEM_LOAD) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_load_more, parent, false);
            return new LoadViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) vh;
            holder.user.setVisibility(View.GONE);
            holder.title.setText(results.get(position).getBestBook().getTitle());
            holder.author.setText(results.get(position).getBestBook().getAuthor().getName());

            Picasso.with(context)
                    .load(results.get(position).getBestBook().getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .resize(220, 300)
                    .into(holder.bookImage);
        }
        else if (vh instanceof LoadViewHolder){
            LoadViewHolder holder = (LoadViewHolder) vh;
            holder.loadProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.user) TextView user;
        @BindView(R.id.book_title) TextView title;
        @BindView(R.id.book_author) TextView author;
        @BindView(R.id.book_small_image) ImageView bookImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public class LoadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.load_more_progress_bar)
        ProgressBar loadProgressBar;

        public LoadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addResults(List<Work> newResults) {
        results.remove(results.size() - 1);
        notifyItemRemoved(results.size());
        if (newResults != null) {
            int s = results.size();
            results.addAll(s, newResults);
            notifyDataSetChanged();
        }
    }
}

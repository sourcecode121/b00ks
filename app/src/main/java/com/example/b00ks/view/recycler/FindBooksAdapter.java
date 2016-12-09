package com.example.b00ks.view.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.findBook.FindBookResponse;
import com.example.b00ks.model.recentReview.Author;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anand on 09/12/2016.
 */

public class FindBooksAdapter extends RecyclerView.Adapter<FindBooksAdapter.ViewHolder> {

    private Context context;
    private FindBookResponse findBookResponse;
    private OnItemClickListener clickListener;

    public FindBooksAdapter(Context context, FindBookResponse findBookResponse) {
        this.context = context;
        this.findBookResponse = findBookResponse;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user.setVisibility(View.GONE);
        holder.title.setText(findBookResponse.getSearch().getResults().get(position).getBestBook().getTitle());
        holder.author.setText(findBookResponse.getSearch().getResults().get(position).getBestBook().getAuthor().getName());

        Picasso.with(context)
                .load(findBookResponse.getSearch().getResults().get(position).getBestBook().getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .resize(220, 300)
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return findBookResponse.getSearch().getResults().size();
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
}

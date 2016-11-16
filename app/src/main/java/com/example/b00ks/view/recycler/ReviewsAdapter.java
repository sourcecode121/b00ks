package com.example.b00ks.view.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b00ks.R;
import com.example.b00ks.model.Author;
import com.example.b00ks.model.Book;
import com.example.b00ks.model.Response;
import com.example.b00ks.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anand on 12/11/2016.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private Response response;
    private OnItemClickListener clickListener;

    public ReviewsAdapter(Context context, Response response) {
        this.context = context;
        this.response = response;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user.setText(response.getReviews().get(position).getUser().getDisplayName());
        holder.title.setText(response.getReviews().get(position).getBook().getTitle());

        String authorNames = "";
        List<Author> authors = response.getReviews().get(position).getBook().getAuthors();
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                authorNames = authorNames + author.getName() + " ";
            }
        }
        holder.author.setText(String.format("by %s", authorNames));

        Picasso.with(context)
                .load(response.getReviews().get(position).getBook().getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .resize(220, 300)
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return response.getReviews().size();
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

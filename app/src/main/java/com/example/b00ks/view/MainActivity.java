package com.example.b00ks.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.b00ks.R;
import com.example.b00ks.di.BaseApplication;
import com.example.b00ks.model.Book;
import com.example.b00ks.view.recycler.BooksAdapter;
import com.example.b00ks.view.recycler.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.books_recycler_view)
    RecyclerView booksRecyclerView;

    private List<Book> books;
    private LinearLayoutManager linearLayoutManager;
    private BooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((BaseApplication) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        books = new ArrayList<>();
        books.add(new Book("Book 1"));
        books.add(new Book("Book 2"));
        books.add(new Book("Book 3"));

        booksRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setLayoutManager(linearLayoutManager);
        booksAdapter = new BooksAdapter(books);
        booksRecyclerView.setAdapter(booksAdapter);

        booksAdapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, books.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}

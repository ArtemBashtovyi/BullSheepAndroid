package com.moshenskyi.bullsheepandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {


    private RecyclerView bookListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Books");
        bookListRv = findViewById(R.id.bookListRv);

        bookListRv.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        initRv();
    }


    private void initRv() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("peter_rabbit","Peter Rabbit", 10));
        books.add(new Book("monkey","Things to do", 80));
        books.add(new Book("cogheart","Cogheart", 0));
        books.add(new Book("some","Histories Demes Gans", 84));
        books.add(new Book("bear","Money Something", 20));

        bookListRv.setAdapter(new BookAdapter(this, books, () -> {
            startActivity(new Intent(AudioListActivity.this, AudioActivity.class));
        }));
    }



    class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
        private Context context;
        private List<Book> bookList;
        private OnItemClickListener clickListener;

        public BookAdapter(Context context, List<Book> bookList, OnItemClickListener clickListener) {
            this.context = context;
            this.bookList = bookList;
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(context, R.layout.book_item,null);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {

            bookViewHolder.progress.setProgress(bookList.get(i).progress);
            if (bookList.get(i).title != null) {
                bookViewHolder.title.setText(bookList.get(i).title);
            }
            bookViewHolder.progressTv.setText(bookList.get(i).progress + "/100");
            if (bookList.get(i).photoLink != null) {
                Glide.with(context)
                        .load(context
                                .getResources()
                                .getIdentifier(bookList.get(i).photoLink, "drawable", context.getPackageName()))
                        .into(bookViewHolder.photoIv);
            }

            bookViewHolder.constraintLayout.setOnClickListener(v -> {
                clickListener.onItemClick();
            });
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        class BookViewHolder extends RecyclerView.ViewHolder {

            private ConstraintLayout constraintLayout;
            private TextView title;
            private ImageView photoIv;
            private ProgressBar progress;
            private TextView progressTv;

            public BookViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout = itemView.findViewById(R.id.bookItemParent);
                title = itemView.findViewById(R.id.bookItemTitle);
                photoIv = itemView.findViewById(R.id.bookImage);
                progress = itemView.findViewById(R.id.bookItemProgressBar);
                progressTv = itemView.findViewById(R.id.bookItemProgressTv);

            }
        }

    }
}

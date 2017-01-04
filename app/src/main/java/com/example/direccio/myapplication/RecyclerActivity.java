package com.example.direccio.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class RecyclerActivity extends MainActivity {
    private BookData bookData;

    public static final int THIS_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        setTitle("Books sort by category");
        checkMenuItem(2);

        bookData = new BookData(this);
        bookData.open();
        List<Book> values = bookData.getAllBooks();

       // Book book = new Book();
        //book = bookData.createBook("Ring Men", "Rowling");
        //values.add(book);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //els numero de items del recycler es dinamic per aixo fico false
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(values));
    }

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
        checkMenuItem(THIS_ACTIVITY);
    }

}

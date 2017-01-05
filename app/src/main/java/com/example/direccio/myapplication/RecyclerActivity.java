package com.example.direccio.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends MainActivity implements SearchView.OnQueryTextListener {
    private BookData bookData;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    List<Book> values = new ArrayList<>();
    //Toolbar toolbar;
    public static final int THIS_ACTIVITY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Books sort by category");
        checkMenuItem(2);

        bookData = new BookData(this);
        bookData.open();
        values = bookData.getAllBooks();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //els numero de items del recycler es dinamic per aixo fico false
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(values);
        recyclerView.setAdapter(adapter);
        //registerForContextMenu(recyclerView);
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchview = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchview.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Book> books = new ArrayList<>();
        for(Book book : values) {
            String author = book.getAuthor().toLowerCase();
            if(author.contains(newText))
                books.add(book);
        }

        adapter.setFilter(books);
        return true;
    }

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
        checkMenuItem(THIS_ACTIVITY);
    }
}

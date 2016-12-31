package com.example.direccio.myapplication;


import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.R.id.list;


public class Principal extends MainActivity {
    private BookData bookData;
    private ListActivity listActivity;
    private ListView listView;
    public static final int THIS_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ll_titol);
        setTitle("Books sort by title");
        checkMenuItem(1);
        bookData = new BookData(this);
        //ens permet llegir a la database
        bookData.open();
        listActivity = new ListActivity();
        /*//this.listActivity = listActivity;
        //View emptyView = findViewById(R.id.empty);
        //listView = ((ListView)findViewById(com.android.internal.R.id.list));
        if(listActivity.getListView()) {
            listActivity = new ListActivity();
            List<Book> values = bookData.getAllBooks();
            ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
            listActivity.setListAdapter(adapter);
        }
       // if (emptyView != null) {
         //  listView.setEmptyView(emptyView);
        //}*/

    }

    // Basic method to add pseudo-random list of books so that
    // you have an example of insertion and deletion

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) listActivity.getListAdapter();
        Book book;
        switch (view.getId()) {
            case R.id.add:
                String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
                int nextInt = new Random().nextInt(4);
                // save the new book to the database
                book = bookData.createBook(newBook[nextInt*2], newBook[nextInt*2 + 1]);

                // After I get the book data, I add it to the adapter
                adapter.add(book);
                break;
            case R.id.delete:
                if (listActivity.getListAdapter().getCount() > 0) {
                    book = (Book) listActivity.getListAdapter().getItem(0);
                    bookData.deleteBook(book);
                    adapter.remove(book);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
        checkMenuItem(THIS_ACTIVITY);
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

}
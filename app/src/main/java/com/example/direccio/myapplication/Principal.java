package com.example.direccio.myapplication;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.R.id.list;


public class Principal extends MainActivity {
    private BookData bookData;
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

      /*  bookData.createBook("Miguel Strogoff", "Jules Verne");
        bookData.createBook("Ulysses", "James Joyce");
        bookData.createBook("Don Quijote", "Miguel de Cervantes");
        bookData.createBook("Metamorphosis", "Kafka");*/

        List<Book> values = bookData.getAllBooks();

       // System.out.println("AQUIIIIIIIIIIIII"+values);
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        listView = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,values);
        //adapter.addAll(values);
        adapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book lhs, Book rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }


    // Basic method to add pseudo-random list of books so that
    // you have an example of insertion and deletion

    // Will be called via the onClick attribute
    // of the buttons in main.xml
   /* public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) listView.getAdapter();
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
                if (listView.getAdapter().getCount() > 0) {
                    book = (Book) listView.getAdapter().getItem(0);
                    bookData.deleteBook(book);
                    adapter.remove(book);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }*/
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
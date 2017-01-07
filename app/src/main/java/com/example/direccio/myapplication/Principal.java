package com.example.direccio.myapplication;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


public class Principal extends MainActivity implements SearchView.OnQueryTextListener{
    private BookData bookData;
    private ListView listView;
    public static final int THIS_ACTIVITY = 1;
    private static int pos;
    private List<Book> values;
    private List<Book> list;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ll_titol);
        setTitle("Principal");
        checkMenuItem(1);
        bookData = new BookData(this);
        //ens permet llegir a la database
        bookData.open();

       /*
        bookData.createBook("Miguel Strogoff", "Jules Verne", "Santillana",2003,"Fantasy","Very Good");
        bookData.createBook("Ulysses", "James Joyce","Planeta",1922,"Fiction","Bad");
        bookData.createBook("Don Quijote", "Miguel de Cervantes","Juventud",1615,"Adventure","Ordinary");
        bookData.createBook("Metamorphosis", "Kafka", "Franz Kafka",1915,"Story","Good");*/

        values = bookData.getAllBooks_title();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        listView = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,values);

        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                showPopupMenu(view);
            }
        });
        list = values;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    public void filter(List<Book> books) {
        list = new ArrayList<>();
        list.addAll(books);
        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        if(list.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "This author does not exist", Toast.LENGTH_SHORT);
            toast.show();
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        filter(books);
        return true;
    }

    public void showPopupMenu(final View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item){
                switch(item.getItemId()) {
                    case R.id.btn_canviar:
                        Intent intent = new Intent(getApplicationContext(), PersonalEvaluation.class);
                        intent.putExtra("titulo_libro",list.get(pos).getTitle());
                        intent.putExtra("id_libro",list.get(pos).getId());
                        intent.putExtra("activity_anterior", "principal");
                        startActivity(intent);
                        return true;
                    case R.id.btn_eliminar:
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage(Html.fromHtml("Do you really want to delete " + "<b>"+ "\"" + list.get(pos).getTitle()+ "\"" + "</b>" + "?"));
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), Html.fromHtml("<b>"+ "\"" + list.get(pos).getTitle()+ "\"" + "</b>" + " has been deleted"), Toast.LENGTH_SHORT).show();
                                bookData = new BookData(v.getContext());
                                bookData.open();
                                bookData.deleteBook(bookData.getBook(list.get(pos).getId()));
                                Intent intent = new Intent(v.getContext(), Principal.class);
                                v.getContext().startActivity(intent);
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), "Elimination canceled", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    default: return false;
                }
            }
        });
    }


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
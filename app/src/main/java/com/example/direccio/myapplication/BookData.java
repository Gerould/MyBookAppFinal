package com.example.direccio.myapplication;

/**
 * BookData
 * Created by pr_idi on 10/11/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BookData {

    // Database fields
    private SQLiteDatabase database;

    // Helper to manipulate table
    private MySQLiteHelper dbHelper;

    // Here we only select Title and Author, must select the appropriate columns
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_AUTHOR, MySQLiteHelper.COLUMN_PUBLISHER, MySQLiteHelper.COLUMN_YEAR,
            MySQLiteHelper.COLUMN_CATEGORY, MySQLiteHelper.COLUMN_PERSONAL_EVALUATION};

    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Book createBook(String title, String author, String publisher, int year, String category, String personal_evaluation) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + author);

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, personal_evaluation);

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null,
                values);

        // Main activity calls this procedure to create a new book
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example)
        // to feed the view.

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);

        // Do not forget to close the cursor
        cursor.close();

        // Return the book
        return newBook;
    }

    public void deleteBook(Book book) {
        long id = book.getId();
        System.out.println("Book deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Book> getAllBooks_title() {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, MySQLiteHelper.COLUMN_TITLE+" ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            //deleteBook(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, MySQLiteHelper.COLUMN_CATEGORY+" ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }

    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        book.setPublisher(cursor.getString(3));
        book.setYear(cursor.getInt(4));
        book.setCategory(cursor.getString(5));
        book.setPersonal_evaluation(cursor.getString(6));

        return book;
    }

    public List<Book> getBooks(String author) {
        List<Book> books = new ArrayList<>();

        String[] where = {author};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, "author=?", where, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }

    public Book getBook(long id) {
        boolean notfound = true;
        //Book libro = new Book();

        String[] where = {id+""};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, "_id=?", where, null, null, null);

        cursor.moveToFirst();
        //while (!cursor.isAfterLast() && notfound) {
            Book book = cursorToBook(cursor);
           /* if (book.getId() == id) {
                libro = book;
                notfound = false;
            }*/
           // cursor.moveToNext();
        //}
        // make sure to close the cursor
        cursor.close();
        return book;
    }

    public int update_personalEv(Book b, String personal_evaluation) {
        b.setPersonal_evaluation(personal_evaluation);
        String [] where = {b.getId()+""};
        ContentValues values = new ContentValues();
        values.put("personal_evaluation", personal_evaluation);
        int n = database.update(MySQLiteHelper.TABLE_BOOKS, values,"_id=?", where);
        return n;
    }
}
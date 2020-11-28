package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

// Can view all the details of a book
public class BookInfo extends AppCompatActivity implements Serializable {
    private Book book;
    private String uid;
    private BookDB bookDB;
    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        this.book = (Book) getIntent().getSerializableExtra("BOOK");
        this.uid = (String) getIntent().getSerializableExtra("UID");

        bookDB = new BookDB();
        userDB = new UserDB();

        // connecting the variables to the TextView components of the layout
        TextView title = (TextView) findViewById(R.id.book_title);
        final EditText bookDesc = (EditText) findViewById(R.id.description);
        TextView status = (TextView) findViewById(R.id.status);

        // updating the variables to the corresponding values
        title.setText(book.getTitle());
        bookDesc.setText(book.getDescription());
        status.setText(book.getStatus());

        // Update the book description if it has changed
        final Button update_description_button = (Button) findViewById(R.id.update_description);
        update_description_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookDesc.getText().toString().length() == 0) {
                    bookDesc.setError("Must have a description");
                } else {
                    book.setDescription(bookDesc.getText().toString());
                    bookDB.addBook(book);
                }
            }
        });

        // Delete a book
        final Button delete_button = (Button) findViewById(R.id.delete_book);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookDB.deleteBook(book.getISBN());
                userDB.deleteBookFromUser(uid, book.getISBN());
                Intent intent = new Intent(BookInfo.this, MainActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });

        // View requests button for when the user is the owner of the book
        // and has received atleast one request
        final Button request_button = (Button) findViewById(R.id.view_requests);
        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookInfo.this, View_Request.class));
            }
        });
//        final Button scan_button = (Button) findViewById(R.id.scan);
//        final Button location_button = (Button) findViewById(R.id.location);
//        if(status.getText() != "requested"){
//            request_button.setVisibility(Button.GONE);
//        }
//        else{
//            request_button.setVisibility(Button.VISIBLE);
//        }
//        if (status.getText() == "borrowed"){
//            scan_button.setVisibility(Button.VISIBLE);
//            location_button.setVisibility(Button.VISIBLE);
//        }
//        else {
//            scan_button.setVisibility(Button.GONE);
//            location_button.setVisibility(Button.GONE);
//        }
    }
}


package com.bookmart.bookmartpk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookInfo extends AppCompatActivity {
    private ImageView book_img, bg_img;
    private TextView category, pages_num, author_name, author_detail, book_detail,bookname;
    private LinearLayout read;
    private String PDF;
    String image;
    String authorName;
    String bookName;
    String bookDetail;
    String bookPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        setTitle("Book Info");

        book_img = findViewById(R.id.book_img);
        category = findViewById(R.id.category);
        pages_num = findViewById(R.id.pages);
        author_name = findViewById(R.id.author_name);
        book_detail = findViewById(R.id.book_detail);
        read = findViewById(R.id.read);
        bg_img = findViewById(R.id.image1);
        bookname = findViewById(R.id.book_name);


        image = getIntent().getStringExtra("BOOK_IMG");
        authorName = getIntent().getStringExtra("AUTHORNAME");
        bookDetail = getIntent().getStringExtra("BOOK_DETAIL");
        bookName = getIntent().getStringExtra("BOOK_NAME");
        bookPages = getIntent().getStringExtra("BOOK_PAGES");
        PDF = getIntent().getStringExtra("BOOK_PDF");

        category.setText(MainPage.CATEGORY_NAME);
        author_name.setText(authorName);
        book_detail.setText(bookDetail);
        bookname.setText(bookName);
        pages_num.setText(bookPages);


        Glide.with(BookInfo.this)
                .load(image).into(book_img);
        Glide.with(BookInfo.this)
                .load(image).into(bg_img);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BookInfo.this, PdfView.class);
                intent1.putExtra("BOOK_NAME", bookName );
                intent1.putExtra("PDF", PDF);
                startActivity(intent1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.favourite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favBTn:
                addToFav();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addToFav(){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference doorbookRef = FirebaseFirestore.getInstance().collection("Favourite List").document("List").collection(user_id);
        String AuthorName = authorName;
        String BookName = bookName;
        String BookImage = image;
        String BookPdf = PDF;
        String pages = bookPages;
        String Description = bookDetail;
        doorbookRef.add(new Book(AuthorName, BookImage, BookName, BookPdf, Description, pages));
        Toast.makeText(BookInfo.this,"Added to Favourite List.",Toast.LENGTH_SHORT).show();
    }

}

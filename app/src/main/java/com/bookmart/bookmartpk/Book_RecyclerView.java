package com.bookmart.bookmartpk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Book_RecyclerView extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BookAdapter adapter;
    private RecyclerView.LayoutManager manager;
    public static String bookPdf, bookname, bookauthor, bookimage, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recycler_view);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        String str = getIntent().getStringExtra("CATEGORY_NAME");
        setTitle(MainPage.CATEGORY_NAME);



        String str1 = getIntent().getStringExtra("CAT_NAME");
        Query dquery = db.collection(str1).orderBy("BookName", Query.Direction.ASCENDING);

        Log.d("this",str1);

        FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>().setQuery(dquery, Book.class).build();
        adapter = new BookAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListerner(new BookAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Book book = documentSnapshot.toObject(Book.class);
                bookPdf = book.getBookPdf();
                bookname = book.getBookName();
                bookauthor = book.getAuthorName();
                bookimage = book.getBookImage();
                description = book.getDescription();
                Intent intent1 = new Intent(Book_RecyclerView.this, BookInfo.class);
                intent1.putExtra("BOOK_NAME", book.getBookName() );
                intent1.putExtra("BOOK_PDF", book.getBookPdf());
                intent1.putExtra("AUTHORNAME", book.getAuthorName());
                intent1.putExtra("BOOK_IMG", book.getBookImage());
                intent1.putExtra("BOOK_DETAIL", book.getDescription());
                intent1.putExtra("BOOK_PAGES", book.getPages());
                startActivity(intent1);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

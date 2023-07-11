package com.bookmart.bookmartpk;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainPage extends Fragment {

    public static String CATEGORY_NAME;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CategoryAdapter adapter, adapter1;
    private CategoryBookAdapter adapter2, adapter3;
    private RecyclerView.LayoutManager manager;
    private LinearLayout more_btn1, more_btn2;

    RecyclerView ExamplerecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_page, container1, false);
    }

    @Override
    public void onViewCreated(final View view1, Bundle savedInstanceState) {
        getActivity().setTitle("Bookmart Online Library");

        view1.findViewById(R.id.listview);
        ExamplerecyclerView = view1.findViewById(R.id.recyclerview_cat5);

        Query dquery = db.collection("Category_English").orderBy("Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>().setQuery(dquery, Category.class).build();
        adapter = new CategoryAdapter(options);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview_cat1);
        manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        Query query1 = db.collection("Category_Urdu").orderBy("Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Category> options1 = new FirestoreRecyclerOptions.Builder<Category>().setQuery(query1, Category.class).build();
        adapter1 = new CategoryAdapter(options1);
        RecyclerView recyclerView1 = getActivity().findViewById(R.id.recyclerview_cat2);
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView1.setLayoutManager(manager1);
        recyclerView1.setAdapter(adapter1);

        Query query2 = db.collection("Novels & Storybooks");

        FirestoreRecyclerOptions<Book> options2 = new FirestoreRecyclerOptions.Builder<Book>().setQuery(query2, Book.class).build();
        adapter3 = new CategoryBookAdapter(options2);
        RecyclerView recyclerView2 = getActivity().findViewById(R.id.recyclerview_cat3);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView2.setLayoutManager(manager2);
        recyclerView2.setAdapter(adapter3);

        Query query3 = db.collection("ناول و افسانے");

        FirestoreRecyclerOptions<Book> options3 = new FirestoreRecyclerOptions.Builder<Book>().setQuery(query3, Book.class).build();
        adapter2 = new CategoryBookAdapter(options3);
        RecyclerView recyclerView3 = getActivity().findViewById(R.id.recyclerview_cat4);
        LinearLayoutManager manager3 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView3.setLayoutManager(manager3);
        recyclerView3.setAdapter(adapter2);


        adapter.setOnItemClickListerner(new CategoryAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Category category = documentSnapshot.toObject(Category.class);

                Intent intent1 = new Intent(getContext(), Book_RecyclerView.class);
                CATEGORY_NAME = category.getName();
                intent1.putExtra("CAT_NAME", category.getName());
                startActivity(intent1);
            }
        });
        adapter1.setOnItemClickListerner(new CategoryAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Category category = documentSnapshot.toObject(Category.class);

                Intent intent1 = new Intent(getContext(), Book_RecyclerView.class);
                CATEGORY_NAME = category.getName();
                intent1.putExtra("CAT_NAME", category.getName());
                startActivity(intent1);
            }
        });

        adapter3.setOnItemClickListerner(new CategoryBookAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Book book = documentSnapshot.toObject(Book.class);

                Intent intent1 = new Intent(getContext(), BookInfo.class);

                CATEGORY_NAME = "Novels & StoryBooks";
                intent1.putExtra("CATEGORY_NAME", "Novels & StoryBooks");
                intent1.putExtra("BOOK_NAME", book.getBookName());
                intent1.putExtra("BOOK_IMG", book.getBookImage());
                intent1.putExtra("AUTHORNAME", book.getAuthorName());
                intent1.putExtra("BOOK_DETAIL", book.getDescription());
                intent1.putExtra("BOOK_PAGES", book.getPages());
                intent1.putExtra("BOOK_PDF", book.getBookPdf());
                startActivity(intent1);
            }
        });

        more_btn1 = view1.findViewById(R.id.More_btn1);
        more_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(), Book_RecyclerView.class);
                CATEGORY_NAME = "Novels & Storybooks";
                intent1.putExtra("CAT_NAME", "Novels & Storybooks");
                startActivity(intent1);
            }
        });

        adapter2.setOnItemClickListerner(new CategoryBookAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Book book = documentSnapshot.toObject(Book.class);

                Intent intent1 = new Intent(getContext(), BookInfo.class);
                CATEGORY_NAME = "ناول و افسانے";
                intent1.putExtra("CATEGORY_NAME", "ناول و افسانے");
                intent1.putExtra("BOOK_NAME", book.getBookName());
                intent1.putExtra("BOOK_IMG", book.getBookImage());
                intent1.putExtra("AUTHORNAME", book.getAuthorName());
                intent1.putExtra("BOOK_DETAIL", book.getDescription());
                intent1.putExtra("BOOK_PAGES", book.getPages());
                intent1.putExtra("BOOK_PDF", book.getBookPdf());
                startActivity(intent1);
            }
        });

        more_btn2 = view1.findViewById(R.id.More_btn2);
        more_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(), Book_RecyclerView.class);
                CATEGORY_NAME = "ناول و افسانے";
                intent1.putExtra("CAT_NAME", "ناول و افسانے");
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter1.startListening();
        adapter2.startListening();
        adapter3.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter1.stopListening();
        adapter2.stopListening();
        adapter3.stopListening();
    }


}

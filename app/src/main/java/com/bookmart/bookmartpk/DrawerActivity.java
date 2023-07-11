package com.bookmart.bookmartpk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String author, image, pdf, pages, description, bookname;
    ArrayList<String> list;
    private ListView listView;
    MaterialSearchView materialSearchView;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        listView = findViewById(R.id.listview);
        frameLayout = findViewById(R.id.content_frame);

        materialSearchView = findViewById(R.id.searchView);

        list = new ArrayList<>();
        if (materialSearchView != null) {
            materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (s.isEmpty()) {
                        listView.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                    } else {
                        listView.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        if (list == null) {
                            Toast.makeText(DrawerActivity.this, "Please turn on your internet connection.", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(DrawerActivity.this, "Search feature will be updated soon...", Toast.LENGTH_SHORT).show();

                        }
                    }
                    return true;
                }
            });
        }

        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new MainPage()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        LinearLayout headerlayout = header.findViewById(R.id.headerlayout);
        headerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawerActivity.this, Profile.class);
                startActivity(intent);
            }
        });
        TextView email = header.findViewById(R.id.txt_ownerEmail);
        final TextView name = header.findViewById(R.id.txt_OwnerName);
        final ImageView img = header.findViewById(R.id.profile_image);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(user.getEmail());
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Ownerbook").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String image = task.getResult().getString("OwnerImage");
                                String firstName = task.getResult().getString("FirstName");
                                String lastName = task.getResult().getString("LastName");
                                String password = task.getResult().getString("ConfirmPassword");
                                name.setText(firstName + " " + lastName);
                                Glide.with(DrawerActivity.this)
                                        .load(image).into(img);
                            }
                        }
                    }
                });


        Query query2 = FirebaseFirestore.getInstance().collection("Novels & Storybooks");
        query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Book book = document.toObject(Book.class);
                        bookname = document.getString("BookName");
                        author = book.getAuthorName();
                        pdf = book.getBookPdf();
                        description = book.getDescription();
                        image = book.getBookImage();
                        pages = book.getPages();
                        list.add(bookname);
                    }
                    ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<>(DrawerActivity.this, R.layout.display_data, list);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new MainPage()).commit();
                break;
            case R.id.nav_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new Favorites()).commit();
                break;
            case R.id.nav_request:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new Request()).commit();
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bookmart App Download Link: ");
                String app_url = " Bookmart App Share Link will be updated soon... ";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.nav_privacy:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new About()).commit();
                break;
            case R.id.nav_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new Logout()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }


    private void search(final String str) {
        final ArrayList<String> myList = new ArrayList<>();
        if (!list.isEmpty()) {
            for (String object : list) {
                if (object.toLowerCase().contains(str.toLowerCase())) {
                    myList.add(object);
                }
                ArrayAdapter<String> arrayAdapter1 =
                        new ArrayAdapter<>(this, R.layout.display_data, myList);
                listView.setAdapter(arrayAdapter1);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent1 = new Intent(DrawerActivity.this, BookInfo.class);
                        intent1.putExtra("BOOK_NAME", bookname);
                        intent1.putExtra("BOOK_PDF", pdf);
                        intent1.putExtra("AUTHORNAME", author);
                        intent1.putExtra("BOOK_IMG", image);
                        intent1.putExtra("BOOK_DETAIL", description);
                        intent1.putExtra("BOOK_PAGES", pages);
                        startActivity(intent1);
                    }
                });
            }
        } else {
            Toast.makeText(DrawerActivity.this, "Please turn on your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen() && drawer.isDrawerOpen(GravityCompat.START)) {
            materialSearchView.closeSearch();
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

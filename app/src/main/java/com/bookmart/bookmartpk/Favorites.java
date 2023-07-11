package com.bookmart.bookmartpk;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Favorites extends Fragment {
    private FavAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private RelativeLayout hideFav;
    private RecyclerView fav_recyclerView;
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Query query = FirebaseFirestore.getInstance().collection("Favourite List").document("List").collection(user_id).orderBy("bookName", Query.Direction.ASCENDING);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorites_activity, container1, false);
    }

    @Override
    public void onViewCreated(final View view1, Bundle savedInstanceState) {
        getActivity().setTitle("Favorites");

        hideFav = view1.findViewById(R.id.hide_fav);
        if (adapter!=null){
            hideFav.setVisibility(View.GONE);
        }else {
            hideFav.setVisibility(View.VISIBLE);
        }

            FirestoreRecyclerOptions<FavBook> options = new FirestoreRecyclerOptions.Builder<FavBook>().setQuery(query, FavBook.class).build();
        adapter = new FavAdapter(options);
        fav_recyclerView = getActivity().findViewById(R.id.fav_recyclerview);
        manager = new LinearLayoutManager(getActivity());
        fav_recyclerView.setLayoutManager(manager);
        fav_recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder1, @NonNull RecyclerView.ViewHolder target1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder1, int direction) {
                adapter.deleteItem(viewHolder1.getAdapterPosition());
                Toast.makeText(getContext(), "Book removed from favourite list.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(fav_recyclerView);

    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }
}

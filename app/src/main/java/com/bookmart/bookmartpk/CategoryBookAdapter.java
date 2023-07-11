package com.bookmart.bookmartpk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryBookAdapter extends FirestoreRecyclerAdapter<Book, CategoryBookAdapter.ViewHolder2> {
    private OnItemClickListerner listerner;

    private Context context;
    private FirestoreRecyclerOptions<Book> mUploads;

    public CategoryBookAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
        mUploads = options;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder2 holder, int position, @NonNull Book model) {
        model = getSnapshots().get(position);
        holder.nameTextView.setText(model.getBookName());
        Glide.with(holder.image.getContext())
                .load(model.getBookImage())
                .into(holder.image);
        holder.BOOKImage = model.getBookImage();
        holder.BOOKPdf = model.getBookPdf();
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dview = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_cardview, parent, false);
        return new ViewHolder2(dview);
    }

    @Override
    public int getItemCount() {
        return mUploads.getSnapshots().size();
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView authorTextView;
        ImageView image;
        TextView description;
        TextView pdf;
        private String BOOKImage, BOOKPdf;

        public ViewHolder2(@NonNull final View ditemView) {
            super(ditemView);

            context = ditemView.getContext();
            nameTextView = ditemView.findViewById(R.id.textview_bookname);
            image = ditemView.findViewById(R.id.bookcover);
            authorTextView = ditemView.findViewById(R.id.textview_bookauthor);
            pdf = ditemView.findViewById(R.id.textview_bookpdf);

            ditemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listerner != null) {
                        listerner.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });


        }
    }

    public interface OnItemClickListerner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListerner(OnItemClickListerner listerner) {
        this.listerner = listerner;
    }
}

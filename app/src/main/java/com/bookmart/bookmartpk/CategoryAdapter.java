package com.bookmart.bookmartpk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.ViewHolder1> {

    private OnItemClickListerner listerner;

    private Context context;

    private FirestoreRecyclerOptions<Category> Uploads;
    public static String NAME;

    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);
        Uploads = options;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder1 holder, int position, @NonNull Category model) {
        holder.name.setText(model.getName());
        Glide.with(holder.image.getContext())
                .load(model.getImage())
                .into(holder.image);
    }

    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cardview, parent, false);
        return new ViewHolder1(view);
    }

    @Override
    public int getItemCount() {
        return Uploads.getSnapshots().size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder1(@NonNull final View citemView) {
            super(citemView);

            context = citemView.getContext();
            name = citemView.findViewById(R.id.cat_name);
            image = citemView.findViewById(R.id.cat_image);

            citemView.setOnClickListener(new View.OnClickListener() {
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

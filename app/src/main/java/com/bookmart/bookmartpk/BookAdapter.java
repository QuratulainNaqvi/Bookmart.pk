package com.bookmart.bookmartpk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.ViewHolder> {
    private OnItemClickListerner listerner;

    private Context context;


    private FirestoreRecyclerOptions<Book> mUploads;

    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
        mUploads = options;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Book model) {
        model = getSnapshots().get(position);
        holder.nameTextView.setText(model.getBookName());
        holder.authorTextView.setText(model.getAuthorName());
        Glide.with(holder.image.getContext())
                .load(model.getBookImage())
                .into(holder.image);
        holder.description.setText(model.getDescription());
        holder.BOOKImage = model.getBookImage();
        holder.BOOKPdf = model.getBookPdf();
        holder.pdf.setText(model.getBookPdf());
        holder.imageViewFavorite.setImageResource(model.getIsFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        holder.Pages = model.getPages();
        holder.BookName = model.getBookName();
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dview = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview, parent, false);
        return new ViewHolder(dview);
    }

    @Override
    public int getItemCount() {
        return mUploads.getSnapshots().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView authorTextView;
        ImageView image;
        ImageView imageViewFavorite;
        TextView description;
        TextView pdf;
        Button read;
        private String BOOKImage, BOOKPdf,Pages, BookName;

        public ViewHolder(@NonNull final View ditemView) {
            super(ditemView);

            context = ditemView.getContext();
            nameTextView = ditemView.findViewById(R.id.book_name);
            authorTextView = ditemView.findViewById(R.id.book_author);
            image = ditemView.findViewById(R.id.book_image);
            imageViewFavorite = ditemView.findViewById(R.id.fav);
            description = ditemView.findViewById(R.id.book_descrition);
            pdf = ditemView.findViewById(R.id.book_pdf);
            read = ditemView.findViewById(R.id.book_read);

            ditemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listerner != null) {
                        listerner.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(ditemView.getContext(), PdfView.class);
                    intent1.putExtra("BOOK_NAME", BookName );
                    intent1.putExtra("PDF", BOOKPdf);
                    ditemView.getContext().startActivity(intent1);
                }
            });
            imageViewFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    CollectionReference doorbookRef = FirebaseFirestore.getInstance().collection("Favourite List").document("List").collection(user_id);
                    String AuthorName = authorTextView.getText().toString();
                    String BookName = nameTextView.getText().toString();
                    String BookImage = BOOKImage;
                    String BookPdf = BOOKPdf;
                    String Description = description.getText().toString();
                    doorbookRef.add(new Book(AuthorName, BookImage, BookName, BookPdf, Description, Pages));
                    Toast.makeText(ditemView.getContext(),"Added to Favourite List.",Toast.LENGTH_SHORT).show();
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

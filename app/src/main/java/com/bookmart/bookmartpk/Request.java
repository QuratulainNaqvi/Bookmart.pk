package com.bookmart.bookmartpk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Request extends Fragment {
    private EditText title, category, author_name;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.request_activity, container1, false);
    }

    @Override
    public void onViewCreated(final View view1, Bundle savedInstanceState) {
        getActivity().setTitle("Request Book");

        author_name = view1.findViewById(R.id.request_writer1);
        title = view1.findViewById(R.id.request_Title1);
        category = view1.findViewById(R.id.request_subject2);
        submit = view1.findViewById(R.id.request_submit_btn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (author_name.getText().toString().trim().equals("") && title.getText().toString().trim().equals("") && category.getText().toString().trim().equals("")) {
                    submit.setEnabled(false);
                    Toast.makeText(getContext(), "Please fill all the fields properly. Fields shouldn't be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    submit.setEnabled(true);
                    String AuthorName = author_name.getText().toString();
                    String BookName = title.getText().toString();
                    String Category = category.getText().toString();
                    data(AuthorName, BookName, Category);
                    Toast.makeText(getContext(), "Your request for a Book has been received.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void data(String uAuthorName, String uBookName, String uCategory) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("AuthorName", uAuthorName);
        userData.put("BookName", uBookName);
        userData.put("Category", uCategory);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("REQUESTBOOK").document(user_id).collection("Requests").add(userData);
    }
}

package com.bookmart.bookmartpk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText fName, lName, email, pswd, c_pswd;
    private String firstName, lastName, email_str, pswd_str, cpswd_str;
    private ProgressBar progressBar;
    private LinearLayout presentUser;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private LinearLayout linearLayout;
    private Button signup_btn;
    private final int PICK_IMAGE_REQUEST = 1;
    private ImageView userImg;
    private Uri UploadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fName = findViewById(R.id.signup_firstName);
        lName = findViewById(R.id.signup_lastName);
        email = findViewById(R.id.signup_email);
        pswd = findViewById(R.id.signup_password);
        c_pswd = findViewById(R.id.signup_cpassword);
        progressBar = findViewById(R.id.signup_progressBar);
        presentUser = findViewById(R.id.signup_presentUser);
        userImg = findViewById(R.id.signup_img);
        linearLayout = findViewById(R.id.upload_pic);
        signup_btn = findViewById(R.id.signup_btn);

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        presentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pswdValidate() || !Validate() || !validateImage()) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                signup_btn.setEnabled(true);
                String uEmail = email.getText().toString();
                String uPassword = pswd.getText().toString();
                auth.createUserWithEmailAndPassword(uEmail, uPassword)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    createUser();
                                    startActivity(new Intent(SignUpActivity.this, MainPage.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean validateImage(){
        if (UploadImg == null){
            Toast.makeText(SignUpActivity.this, "Please Select an Image..." ,
                    Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            UploadImg = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), UploadImg);
                userImg.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void createUser(){
        //create user
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference ownerref = storageReference.child("Ownerbook/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+ ".jpg");
            ownerref.putFile(UploadImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    String uFirstName = fName.getText().toString();
                    String uLastName = lName.getText().toString();
                    String cPassword = c_pswd.getText().toString();
                    String sdownload_url = String.valueOf(downloadUrl);
                    ownerData(uFirstName,uLastName,cPassword,sdownload_url);
                }
            });
        }
    private void ownerData(String uFirstName, String uLastName, String cPassword,String sdownload_url){
        Map<String, Object> userData = new HashMap<>();
        userData.put("FirstName",uFirstName);
        userData.put("LastName",uLastName);
        userData.put("ConfirmPassword",cPassword);
        userData.put("OwnerImage",sdownload_url);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Ownerbook").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(userData);
    }

    private boolean Validate(){
        email_str = email.getText().toString();
        pswd_str = pswd.getText().toString();
        cpswd_str = c_pswd.getText().toString();
        firstName = fName.getText().toString();
        lastName = lName.getText().toString();
        if (email_str.isEmpty() || pswd_str.isEmpty() || cpswd_str.isEmpty() || firstName.isEmpty() || lastName.isEmpty()){
            email.setError("Fields can't be empty");
            pswd.setError("Fields can't be empty");
            c_pswd.setError("Fields can't be empty");
            fName.setError("Fields can't be empty");
            lName.setError("Fields can't be empty");
            return false;
        }else {
            email.setError(null);
            pswd.setError(null);
            c_pswd.setError(null);
            fName.setError(null);
            lName.setError(null);
            return true;
        }
    }

    private boolean pswdValidate(){
        pswd_str = pswd.getText().toString();
        cpswd_str = c_pswd.getText().toString();
        if (!pswd_str.equals(cpswd_str)){
            pswd.setError("Doesn't match");
            c_pswd.setError("Doesn't match");
            return false;
        }else {
            pswd.setError(null);
            c_pswd.setError(null);
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}

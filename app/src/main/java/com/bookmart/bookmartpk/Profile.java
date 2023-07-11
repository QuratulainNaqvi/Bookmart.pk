package com.bookmart.bookmartpk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    Uri OwnerUri;
    private Button saveUser;
    private TextInputEditText changeEmail;
    private ProgressBar progressBar;
    private TextInputEditText changePassword;
    private FirebaseAuth.AuthStateListener authListener;
    private TextInputEditText profilefName, profilelName;
    private TextInputEditText profilePassword;
    private ImageView OwnerUserImage;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    StorageReference ref = FirebaseStorage.getInstance().getReference().child("Ownerbook/").child(user_id + ".jpg");


    DocumentReference Ownerbook = FirebaseFirestore.getInstance().collection("Ownerbook").document(user_id);

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String pswd_update;

    // Creating URI.
    Uri OwnerUserUri;

    private final int PICK_IMAGE_REQUEST = 78;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        setTitle("Account Settings");

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        saveUser = findViewById(R.id.saveaccount);
        changeEmail = findViewById(R.id.edprofile_email);
        profilefName = findViewById(R.id.edprofile_fname);
        profilelName = findViewById(R.id.edprofile_lname);
        profilePassword = findViewById(R.id.edprofile_password);
        progressBar = findViewById(R.id.profile_progressBar);
        OwnerUserImage = findViewById(R.id.edprofile_img);

        OwnerUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    Intent intent = new Intent(Profile.this, LoginActivity.class);
                    startActivity(intent);
                }
                if (user != null) {
                    // Name, email address, and profile photo Url
                    changeEmail.setText(user.getEmail());
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
                                            profilefName.setText(firstName);
                                            profilelName.setText(lastName);
                                            profilePassword.setText(password);
                                            Glide.with(Profile.this)
                                                    .load(image).into(OwnerUserImage);
                                        }
                                    }
                                }
                            });
                }
            }
        };

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                pswd_update = profilePassword.getText().toString();
                Ownerbook.update("ConfirmPassword", pswd_update);
                user.updatePassword(pswd_update);

                String fName_update = profilefName.getText().toString();
                Ownerbook.update("FirstName", fName_update);

                String lName_update = profilelName.getText().toString();
                Ownerbook.update("LastName", lName_update);

                Toast.makeText(Profile.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_door_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_dnote:
                deleteuser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            OwnerUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), OwnerUri);
                OwnerUserImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveNote() {
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (OwnerUri != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("Ownerbook/" + System.currentTimeMillis() + "." + GetFileExtension(OwnerUri)).child(user_id + ".jpg");
            ref.putFile(OwnerUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    final String Img_update = String.valueOf(downloadUrl);
                    Ownerbook.update("OwnerImage", Img_update);
                }
            });
        }
    }

    public void deleteuser() {
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ref.delete();
                                Ownerbook.delete();
                                Toast.makeText(Profile.this, "Your profile is deleted:( Create an account now!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Profile.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Profile.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }
}

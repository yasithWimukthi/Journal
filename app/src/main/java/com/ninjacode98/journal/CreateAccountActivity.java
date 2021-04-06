package com.ninjacode98.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.JournalApi;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "Create Account Activity ";
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;
    private ProgressBar progressBar;
    private Button createAccountButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createAccountButton = findViewById(R.id.create_account_button);
        emailEditText = findViewById(R.id.email_account);
        passwordEditText = findViewById(R.id.password_account);
        usernameEditText = findViewById(R.id.username);
        progressBar = findViewById(R.id.create_account_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    // already logged in
                }else{

                }
            }
        };

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailEditText.getText().toString())
                    && !TextUtils.isEmpty(passwordEditText.getText().toString())
                    && !TextUtils.isEmpty(usernameEditText.getText().toString())){

                    createUserEmailAccount(
                            emailEditText.getText().toString().trim(),
                            passwordEditText.getText().toString().trim(),
                            usernameEditText.getText().toString().trim()
                    );
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Empty Fields Not Allowed!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createUserEmailAccount(String email,String password,String username){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserId = currentUser.getUid();

                                //create a user map to create a user in the User collection
                                Map<String, String> userObject = new HashMap<String, String>();
                                userObject.put("userId",currentUserId);
                                userObject.put("username",username);

                                // save to firestore DB
                                collectionReference.add(userObject)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(Objects.requireNonNull(task.getResult()).exists()){

                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String name = task.getResult().getString("username");

                                                                    //Global API
                                                                    JournalApi journalApi = new JournalApi();
                                                                    journalApi.setUsername(name);
                                                                    journalApi.setUserId(currentUserId);

                                                                    Intent intent = new Intent(CreateAccountActivity.this,PostJournalActivity.class);
                                                                    intent.putExtra("username",name);
                                                                    intent.putExtra("userId",currentUserId);
                                                                    startActivity(intent);

                                                                }else{
                                                                    progressBar.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @SuppressLint("LongLogTag")
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                            }
                                        });
                            }else{

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        }else{

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
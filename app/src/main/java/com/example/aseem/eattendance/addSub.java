package com.example.aseem.eattendance;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class addSub extends AppCompatActivity {

    private Button mAddButton;
    private EditText mSubCode;


    private FirebaseDatabase mFirebaseDatabase;//entry point for database connectivity
    private DatabaseReference mTeachers;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);

        mAddButton = (Button) findViewById(R.id.addButton);
        mSubCode = (EditText) findViewById(R.id.subjectCode);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTeachers = mFirebaseDatabase.getReference().child("teachers");

        mFirebaseAuth = FirebaseAuth.getInstance();

//
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                 user = firebaseAuth.getCurrentUser();
//                if (user != null && user.getDisplayName() != null){
//                    //user is signed in
//                    //Toast.makeText(MainActivity.this, "You are now signed in!!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    //user is signed out
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
//                                    .build(),
//                            1);
//                }
//            }
//        };

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                user = firebaseAuth.getCurrentUser();
//            }
//        };
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = mSubCode.getText().toString();
                String name = getIntent().getStringExtra("name");
                String uid = getIntent().getStringExtra("uid");
                TeacherDetails teacher = new TeacherDetails(name,uid,code);
                mTeachers.setValue(teacher);
                addSub.this.finish();

            }
        });
    }
}

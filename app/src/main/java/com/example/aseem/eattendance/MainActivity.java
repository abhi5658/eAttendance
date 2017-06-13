package com.example.aseem.eattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";



    private FirebaseDatabase mFirebaseDatabase;//entry point for database connectivity
    private DatabaseReference mStudents;//refers just to the messages portion of database
    private DatabaseReference mTeachers;
    private ChildEventListener mChildEventListener;//attached to the messages node of database and listens to it only

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mUsername;
    private String mUid;

    private TextView mWelcomeText;
    private Button mSendButton;
    private EditText mValueBox;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWelcomeText = (TextView) findViewById(R.id.welcomeText);
        mSendButton = (Button)findViewById(R.id.sendButton);
        mValueBox = (EditText)findViewById(R.id.valueBox);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStudents = mFirebaseDatabase.getReference().child("students");
        mTeachers = mFirebaseDatabase.getReference().child("teachers");


        mFirebaseAuth = FirebaseAuth.getInstance();

        intent = new Intent(MainActivity.this, addSub.class);

        //firebase login
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.getDisplayName() != null){
                    //user is signed in
                    onSignedInInialize(user);
                    //Toast.makeText(MainActivity.this, "You are now signed in!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //user is signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = mValueBox.getText().toString();
                if(value!=null){
                    mStudents.push().setValue(value);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled !!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                //sign out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
//        detachDatabaseReadListener();
//        mMessageAdapter.clear();
    }

    private void onSignedInInialize(FirebaseUser user){
        mUsername = user.getDisplayName();
        if (mUsername!=null) {
            mWelcomeText.setText("Welcome " + mUsername);
        }
        else{
            mWelcomeText.setText("Welcome new user!!");
        }
        mUid = user.getUid();
        TeacherDetails newTeacher = new TeacherDetails(mUsername,mUid,"");
        mTeachers.push().setValue(newTeacher);
        intent.putExtra("uid",mUid);
        intent.putExtra("name",mUsername);
        //attachDatabaseReadListener();
    }

    private void onSignedOutCleanUp(){
        mUsername = ANONYMOUS;
        mWelcomeText.setText("Welcome "+mUsername);

//        mMessageAdapter.clear();
    }
    public void addSubject(View view){
        startActivity(intent);
    }
}

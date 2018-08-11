package com.example.rashidsaddique.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Employe_login extends AppCompatActivity {
    private Button btn_employe_reg,btn_employe_login;
    private AutoCompleteTextView employe_pass_login,employe_email_login;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener firebaseauthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe_login);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    Intent intent= new Intent(Employe_login.this,  EmployeeMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }
            }
        };

        employe_email_login = (AutoCompleteTextView) findViewById(R.id.employe_email_login);
        employe_pass_login = (AutoCompleteTextView) findViewById(R.id.employe_pass_login);

        btn_employe_login = (Button) findViewById(R.id.btn_emploe_login);
        btn_employe_reg = (Button) findViewById(R.id.btn_employe_reg);
        btn_employe_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = employe_email_login.getText().toString();
                final String pass = employe_pass_login.getText().toString();
                firebaseauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Employe_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(Employe_login.this,"Employe Signup Error", Toast.LENGTH_SHORT).show();
                        }else {
                            String user_id = firebaseauth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });
        btn_employe_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = employe_email_login.getText().toString();
                final String pass = employe_pass_login.getText().toString();
                firebaseauth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Employe_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(Employe_login.this,"Employe login Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        btn_employe_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Employe_login.this, signup_employ.class);
                startActivity(intent);
            }

        });

    }
    private String userType1;
    private String userType2;
    public void CheckUserType(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference customerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Employees");
        customerDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())){
                    userType1 = "Employees";
                    userType2 = "Customers";
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseauth.addAuthStateListener(firebaseauthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseauth.removeAuthStateListener(firebaseauthListener);
    }
}

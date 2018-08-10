package com.example.rashidsaddique.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;


public class Signup_page extends AppCompatActivity {


    //private AutoCompleteTextView username;
    private AutoCompleteTextView userEmail;
    //private AutoCompleteTextView user_number;
    private AutoCompleteTextView userPassword;
    //private AutoCompleteTextView userConfirmPassword;
    private Button btn_signup;
   // private ProgressDialog progressDialog;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener firebaseauthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        firebaseauth = FirebaseAuth.getInstance();
//        firebaseauthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user!=null){
//                    Intent intent= new Intent(Signup_page.this,  MapActivity.class);
//                    startActivity(intent);
//                    finish();
//                    return;
//
//                }
//            }
//        };

        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPassword = (AutoCompleteTextView) findViewById(R.id.userPassword);

        btn_signup = (Button) findViewById(R.id.btn_signup);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = userEmail.getText().toString();
                final String pass = userPassword.getText().toString();
                firebaseauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(Signup_page.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup_page.this, "Customers SignUp Error", Toast.LENGTH_SHORT).show();
                        } else {
                            String user_id = firebaseauth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });

    }
//        @Override
//        protected void onStart() {
//            super.onStart();
//            firebaseauth.addAuthStateListener(firebaseauthListener);
//        }
//
//        @Override
//        protected void onStop() {
//            super.onStop();
//            firebaseauth.removeAuthStateListener(firebaseauthListener);
//        }





//        btn_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String email = userEmail.getText().toString();
//                final String pass = userPassword.getText().toString();
//                firebaseauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Signup_page.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(Signup_page.this, "Customer Signup Error", Toast.LENGTH_SHORT).show();
//                        } else {
//                            String user_id = firebaseauth.getCurrentUser().getUid();
//                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
//                            current_user_db.setValue(true);
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseauth.addAuthStateListener(firebaseauthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        firebaseauth.removeAuthStateListener(firebaseauthListener);
    }
//}
   //        //username = (AutoCompleteTextView) findViewById(R.id.username);
//        btn_signup = (Button) findViewById(R.id.btn_signup);
//        firebaseAuth = firebaseAuth.getInstance();
//        firebaseauthlistner = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null) {
//                    Intent intent = new Intent(Signup_page.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
//
//
//            }
//
//        };
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference mref = database.getReference("FYP");
//        progressDialog = new ProgressDialog(this);
//
//
//        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
//        //user_number = (AutoCompleteTextView) findViewById(R.id.user_number);
//        user_Password = (AutoCompleteTextView) findViewById(R.id.userPassword);
//        //userConfirmPassword = (AutoCompleteTextView) findViewById(R.id.userConfirmPassword);
//
//        btn_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //    String user_name = username.getText().toString().trim();
//                String email = userEmail.getText().toString().trim();
//                String password = user_Password.getText().toString().trim();
//                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Signup_page.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()){
//                            Toast.makeText(Signup_page.this, "Sign in error",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
////                mref.setValue(user_name);
//
//            }
//        });
//
//    }
//
//
//
//            @Override
//            public void onClick(View view) {
//                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//
//
//            }

//    private void registerUser() {
//
//
//        //String number = user_number.getText().toString().trim();
//
//        //String confirmPassword = userConfirmPassword.getText().toString().trim();
//        if (TextUtils.isEmpty(user_name)) {
//            Toast.makeText(this, "Please Enter Yore name", Toast.LENGTH_SHORT).show();
//            return;
//
//        }
//        if (TextUtils.isEmpty(email)) {
//            Toast.makeText(this, "Please Enter Yore Email", Toast.LENGTH_SHORT).show();
//            return;
//        }
////        if (TextUtils.isEmpty(number)) {
////            Toast.makeText(this,"Please Enter Your Number" , Toast.LENGTH_SHORT).show();
////            return;
////        }
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Please Enter Yore Password", Toast.LENGTH_SHORT).show();
//            return;
//
//        }
//        if (TextUtils.isEmpty(confirmPassword)) {
//            Toast.makeText(this,"Please Confirm Your Password" , Toast.LENGTH_SHORT).show();
//            return;
//
//        }
//        if (TextUtils.isEmpty(confirmPassword)) {
//
//        }
//        progressDialog.setMessage("User Registration");
//        progressDialog.show();
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(Signup_page.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(Signup_page.this, "Could not Register", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }


//    public void onClick(View view) {
//        if (view == btn_signup) {
//            registerUser();
//        }
//    }


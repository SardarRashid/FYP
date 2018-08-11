package com.example.rashidsaddique.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class MainActivity extends AppCompatActivity {
    private Button btn_customer_login,btn_customer_Reg,employe_customer_btn;
    private AutoCompleteTextView userPasswordlogin,userEmaillogin;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener firebaseauthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    Intent intent= new Intent(MainActivity.this,  CustomerMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }
            }
        };

        userEmaillogin= (AutoCompleteTextView) findViewById(R.id.userEmaillogin);
        userPasswordlogin= (AutoCompleteTextView) findViewById(R.id.userPasswordlogin);
employe_customer_btn = (Button) findViewById(R.id.employe_btn);
        btn_customer_login = (Button) findViewById(R.id.btn_login);
        btn_customer_Reg = (Button) findViewById(R.id.btn_Reg);
        btn_customer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmaillogin.getText().toString();
                final String pass = userPasswordlogin.getText().toString();
                firebaseauth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Customer login Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        employe_customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Employe_login.class);
                startActivity(intent);
            }

        });
        btn_customer_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(MainActivity.this, Signup_page.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
    private String userType1;
    private String userType2;
    public void CheckUserType(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference customerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        customerDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())){
                    userType1 = "Customers";
                    userType2 = "Employees";
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


//        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmaillogin);
//        userpassword =(AutoCompleteTextView) findViewById(R.id.userPasswordlogin);
//       loginbutton = (Button) findViewById(R.id.btn_login);
//       regbutton = (Button) findViewById(R.id.btn_Reg);
//       employebutton = (Button) findViewById(R.id.employe_btn);
//        firebaseAuthlistner = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null)
//                {
//                    Intent intent = new Intent(MainActivity.this, customerdash.class);
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
//
//
//            }
//
//        };
//        loginbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = userEmail.getText().toString().trim();
//                        String password = userpassword.getText().toString().trim();
//                        firebaseauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                    if (!task.isSuccessful()){
//                                        Toast.makeText(MainActivity.this, "Sign in error",Toast.LENGTH_SHORT).show();
//                                    }
//
//                            }
//                        });
//            }
//        });
//login_btn.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//
//        Intent logbtn= new Intent(MainActivity.this, Mr_Fix_it.class);
//        startActivity(logbtn);
//    }
////});
//        loginbutton.setOnClickListener(new View.OnClickListener() {
//          @Override
//            public void onClick(View view) {
//
//                Intent intentloignbutton= new Intent(MainActivity.this, customerdash.class);
//                startActivity(intentloignbutton);
//              finish();
//              return;
//            }
//        });





    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseauth.addAuthStateListener(firebaseAuthlistner);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        firebaseauth.removeAuthStateListener(firebaseAuthlistner);
//    }
//}

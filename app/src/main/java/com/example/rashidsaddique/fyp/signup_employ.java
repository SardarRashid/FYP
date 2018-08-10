package com.example.rashidsaddique.fyp;

import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_employ extends AppCompatActivity {
    private AutoCompleteTextView employe_email_signup;
    private AutoCompleteTextView employe_pass_signup;
    private Button btn_signup_employe;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener firebaseauthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_employ);
        firebaseauth = FirebaseAuth.getInstance();
        employe_email_signup = (AutoCompleteTextView)findViewById(R.id.employe_email_signup);
        employe_pass_signup =(AutoCompleteTextView)findViewById(R.id.employe_pass_signup);
        btn_signup_employe = (Button)findViewById(R.id.btn_signup_employe);

        btn_signup_employe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = employe_email_signup.getText().toString();
                final String pass = employe_pass_signup.getText().toString();
                firebaseauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(signup_employ.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(signup_employ.this, "Employees SignUp Error", Toast.LENGTH_SHORT).show();
                        } else {
                            String user_id = firebaseauth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });
    }
}

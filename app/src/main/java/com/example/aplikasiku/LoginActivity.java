package com.example.aplikasiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewDaftar;
    private Button btnMasuk;
    private EditText editTextEmail, editTextPass;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewDaftar = findViewById(R.id.tvDaftar);
        btnMasuk = findViewById(R.id.btnLogin);
        editTextEmail = findViewById(R.id.etEmailLogin);
        editTextPass = findViewById(R.id.etPasswordLogin);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() != null){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        if (dataSnapshot.child("Users").hasChild(user.getUid())){
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
//                        else{
//                            finish();
//                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        textViewDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Tolong masukkan email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPass.setError("Tolong masukkan password");
            editTextPass.requestFocus();
            return;
        }

        progressDialog.setMessage("Masuukkk...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        if (snapshot.child("Users").hasChild(user.getUid())) {
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        }
//                                        else {
//                                            finish();
//                                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else {
                            //failed
                            Toast.makeText(LoginActivity.this, "Invalid Email / Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
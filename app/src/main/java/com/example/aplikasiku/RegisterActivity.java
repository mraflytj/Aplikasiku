package com.example.aplikasiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {

    private TextView textViewMasuk;
    private Button btnDaftar;
    private EditText editTextEmail, editTextPass, editTextName, editTextHp, editTextConfirm;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewMasuk = findViewById(R.id.tvMasuk);
        btnDaftar = findViewById(R.id.btnRegister);
        editTextName = findViewById(R.id.etNameRegister);
        editTextEmail = findViewById(R.id.etEmailRegister);
        editTextHp = findViewById(R.id.etHpRegister);
        editTextPass = findViewById(R.id.etPasswordRegister);
        editTextConfirm = findViewById(R.id.etConfirmRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        textViewMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    if (dataSnapshot.child("Users").hasChild(user.getUid())){
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });
    }

    private void userRegister() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String phone = editTextHp.getText().toString().trim();
        final String password = editTextPass.getText().toString().trim();
        String confirm_password = editTextConfirm.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Tolong masukkan nama");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Tolong masukkan email");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Tolong masukkan email yang valid");
            editTextEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextHp.setError("Tolong masukkan no.telpon");
            editTextHp.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPass.setError("Tolong masukkan password");
            editTextPass.requestFocus();
            return;
        }

        if (confirm_password.isEmpty()) {
            editTextConfirm.setError("Tolong masukkan konfirmasi password");
            editTextConfirm.requestFocus();
            return;
        }

        if (!confirm_password.equals(password)) {
            editTextConfirm.setError("Password tidak sesuai");
            editTextConfirm.requestFocus();
            return;
        }

        progressDialog.setMessage("Mendaftaarrr...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //user registered successfully
                            UserInformation userInformation = new UserInformation(name,email,phone,password);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            databaseReference.child("Users").child(user.getUid()).setValue(userInformation);
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            //not registered
                            Toast.makeText(RegisterActivity.this, "Daftar gagal!! Coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        UserInformation userInformation = new UserInformation(name,email,phone,password);
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user == null) {
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//        else {
//            databaseReference.child("Users").child(user.getUid()).setValue(userInformation);
//            finish();
//            startActivity(new Intent(this, HomeActivity.class));
//        }
    }
}
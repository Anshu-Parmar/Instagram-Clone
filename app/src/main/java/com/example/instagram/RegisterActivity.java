package com.example.instagram;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.tiles.material.Button;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, name, email,password,repassword;
    private View register;
    private TextView loginUser;
    private DatabaseReference mRootRef;
    public DBHelper db;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.re_Password);
        register = findViewById(R.id.btnReg);
        loginUser = findViewById(R.id.loginUser);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = new DBHelper();
        pd = new ProgressDialog(this);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUser = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPass = password.getText().toString();
                String txtRepass = repassword.getText().toString();

                if (txtUser.equals("") || txtPass.equals("") || txtRepass.equals("") || txtEmail.equals("") || txtName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter all fields ", Toast.LENGTH_SHORT).show();
                } else if (txtPass.equals(txtRepass)) {
                    if (db.validateUsername(txtUser) && db.validateUsername(txtName)) {
                        username.setError(null);
                        name.setError(null);
                            if (db.validateEmail(txtEmail)){
                                email.setError(null);
                                if (db.validatePassword(txtPass)){
                                    password.setError(null);
                                    registerUser(txtUser, txtName, txtEmail, txtPass);
                                }else {
                                    password.setError("Password too weak!!!");
                                }
                            }else{
                                email.setError("Enter a valid email!!!");
                            }
                    }else {
                        if (!db.validateUsername(txtUser)) username.setError("Username is not Valid");
                        if (!db.validateUsername(txtName)) name.setError("Name is not Valid");
                    }
                }else{
                    repassword.setError("Does not match!!!");
                }
            }

        });
    }
    private void registerUser(final String userName, final String name, final String email, String password){

        pd.setMessage("Please Wait!");
        pd.setTitle("Registering...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    pd.dismiss();
//                    Log.d(TAG, "onComplete: data stored");
//
//                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(RegisterActivity.this, "Verify your Email ID!!!", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                finish();
//                            }else{
//                                pd.dismiss();
//                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }else{
//                    pd.dismiss();
//                    Toast.makeText(RegisterActivity.this, "UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("userName", userName);
                map.put("id", mAuth.getCurrentUser().getUid());
                  map.put("bio", "");
                  map.put("imageurl", "default");

                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(RegisterActivity.this, "Verify your Email ID!!!", Toast.LENGTH_SHORT).show();

                        mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e(TAG, "onComplete: onComplete Successfully executed.");
                                if (task.isSuccessful()){
                                    pd.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Update the profile.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }else{
                                    pd.dismiss();
                                    Toast.makeText(RegisterActivity.this, "UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

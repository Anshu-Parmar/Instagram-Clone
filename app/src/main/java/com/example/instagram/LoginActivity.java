package com.example.instagram;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button login;
    private TextView registerUser;
    public DBHelper db;
    ProgressDialog pd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btnLogin);
        registerUser = findViewById(R.id.RegisterUser);
        db = new DBHelper();
        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString();
                String txtPass = password.getText().toString();

                if (txtPass.equals("") || txtEmail.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter all fields ", Toast.LENGTH_SHORT).show();
                } else {
                        loginUser(txtEmail, txtPass);
                    }
            }
        });
    }

    private void loginUser(final String email, String password) {

        pd.setMessage("Please Wait!");
        pd.setTitle("Registering...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (mAuth.getCurrentUser().isEmailVerified()){
                        pd.dismiss();
                        Log.d(TAG, "onComplete: data stored");
                        Toast.makeText(LoginActivity.this, "Update the profile.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }else{
                        pd.dismiss();
                        Log.e(TAG, "onComplete: Email cannot be verified" );
                        Toast.makeText(LoginActivity.this, "Verify Email-Id...", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    pd.dismiss();
                    Log.e(TAG, "onComplete: Email cannot be verified" );
                    Toast.makeText(LoginActivity.this, "Verify Email-Id...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
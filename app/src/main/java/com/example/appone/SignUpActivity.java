package com.example.appone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    //DECLARE OBJECTS
    private FirebaseAuth mAuth;
    private EditText emailView, passwordView;
    Button signUp;
    TextView LogIntText;
    ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();    //HIDE ACTION BAR

        //INITIALIZE OBJECTS
        mAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.emailCreateInput);
        passwordView = findViewById(R.id.passwordCreateInput);
        signUp = findViewById(R.id.SignUp);
        LogIntText = findViewById(R.id.LogInText);
        progBar = findViewById(R.id.progressbar);

        //SET OnClickListeners
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(emailView.getText().toString().trim(), passwordView.getText().toString().trim());
            }
        });

        LogIntText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

    }

    //IF NO USER IS LOGGED IN


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }
    }

    //REGISTRATION METHOD
    void registerUser(String email, String password){

        if(email.isEmpty()){
            emailView.setError("Field cannot be empty");
            emailView.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailView.setError("Please enter a valid email address");
            emailView.requestFocus();
            return;
        }

        if(password.length()<8){
            passwordView.setError("Password should be at least 8 characters long");
            passwordView.requestFocus();
            return;
        }

        progBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "User Registration Successful", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, ProfileSetUpActivity.class));
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){    //IF EMAIL ALREADY EXISTS
                        Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}

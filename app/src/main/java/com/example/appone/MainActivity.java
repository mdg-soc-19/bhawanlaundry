package com.example.appone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    protected Button buttonL,buttonS;
    private EditText emailView, passwordView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView signUp;
    ProgressBar progBar;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        buttonL = findViewById(R.id.LAUNDRYMAN_ENTER_BUTTON);
        buttonS = findViewById(R.id.STUDENT_ENTER_BUTTON);
        emailView = findViewById(R.id.LOGIN_EMAIL_ENTER);
        passwordView = findViewById(R.id.LOGIN_PASSWORD_ENTER);
        signUp = findViewById(R.id.SIGN_UP_TEXT);
        progBar = findViewById(R.id.PROGRESS_CIRCULAR_MAIN);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //Intent use to login
                    startActivity(new Intent(MainActivity.this, StudentMainActivity.class));
                }
            }
        };
       // mAuth.addAuthStateListener(mAuthListener);

        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String emailText = emailView.getText().toString();
                String passwordText = passwordView.getText().toString();

               validate(emailText, passwordText);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void validate(String e, String p){

        if(e.isEmpty()){
            emailView.setError("Field cannot be empty");
            emailView.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            emailView.setError("Please enter a valid email address");
            emailView.requestFocus();
            return;
        }

        if(p.isEmpty()){
            passwordView.setError("Password is required");
            passwordView.requestFocus();
            return;
        }

        progBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    finish();
                    Intent i = new Intent(MainActivity.this, StudentMainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}

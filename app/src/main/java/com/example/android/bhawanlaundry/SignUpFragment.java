package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignUpFragment extends Fragment {

    EditText emailSignUp, passwordSignUp;
    TextView loginText;
    Button signUp;
    signUpCI SIGNUP_CI_INTERFACE;
    String email, password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailSignUp = view.findViewById(R.id.EMAIL_CREATE_INPUT);
        passwordSignUp = view.findViewById(R.id.PASSWORD_CREATE_INPUT);
        signUp = view.findViewById(R.id.SIGN_UP_BUTTON);
        loginText = view.findViewById(R.id.LOG_IN_TEXT);
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_SIGN_UP);
        mAuth = FirebaseAuth.getInstance();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SIGNUP_CI_INTERFACE.openLoginScreen();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailSignUp.onEditorAction(EditorInfo.IME_ACTION_DONE);
                passwordSignUp.onEditorAction(EditorInfo.IME_ACTION_DONE);
                email = emailSignUp.getText().toString().trim();
                password=passwordSignUp.getText().toString().trim();
                registerUser(email,password);
            }
        });


        return view;
    }

    public interface signUpCI{

        void openLoginScreen();
        void openProfileSetUp();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;

        SIGNUP_CI_INTERFACE = (signUpCI) activity;
    }

    public void registerUser(String e, String p){
        if(e.isEmpty()){
            emailSignUp.setError("Field cannot be empty");
            emailSignUp.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            emailSignUp.setError("Enter a valid email address");
            emailSignUp.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordSignUp.setError("Field cannot be empty");
            passwordSignUp.requestFocus();
            return;
        }

        if(password.length()<8){
            passwordSignUp.setError("Password should be at least 8 characters long");
            passwordSignUp.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getContext(), "User Successfully Registered", Toast.LENGTH_LONG).show();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            SIGNUP_CI_INTERFACE.openProfileSetUp();

                        }else {
                            //Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                if(e instanceof FirebaseAuthUserCollisionException){
                    emailSignUp.setError("Account already exists with this email");
                    emailSignUp.requestFocus();
                } else {
                   // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

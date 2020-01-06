package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    EditText emailInputLogin, passwordInputLogin;
    Button buttonL, buttonS;
    TextView signUpText, forgotPasswordText;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String email, password;

    MainActivity mainActivity;
    loginCI LOGIN_CI_INTERFACE;
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //AUTH STATE LISTENER

           mAuth = FirebaseAuth.getInstance();

           mAuthStateListener = new FirebaseAuth.AuthStateListener() {
               @Override
               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                   if(firebaseAuth.getCurrentUser() != null){
                    LOGIN_CI_INTERFACE.openRoomSelectFragment();
/*


                    Toast.makeText(getContext(), firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    return;
*/

                   }
               }
           };

           mAuth.addAuthStateListener(mAuthStateListener);


           //INSTANTIATING VIEWS
        emailInputLogin = view.findViewById(R.id.LOGIN_EMAIL_ENTER);
        passwordInputLogin = view.findViewById(R.id.LOGIN_PASSWORD_ENTER);
        buttonL = view.findViewById(R.id.LAUNDRYMAN_ENTER_BUTTON);
        buttonS = view.findViewById(R.id.STUDENT_ENTER_BUTTON);
        signUpText = view.findViewById(R.id.SIGN_UP_TEXT);
        forgotPasswordText = view.findViewById(R.id.FORGOT_PASSWORD_TEXT);
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_LOGIN);


        mainActivity = new MainActivity();

        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailInputLogin.getText().toString().trim();
                password = passwordInputLogin.getText().toString().trim();
                validate(email, password);

            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGIN_CI_INTERFACE.openSignUp();
            }
        });

        return view;
    }

    void validate(String e, String p){

        if(e.isEmpty()){
            emailInputLogin.setError("Field cannot be empty");
            emailInputLogin.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            emailInputLogin.setError("Please enter a valid email address");
            emailInputLogin.requestFocus();
            return;
        }

        if(p.isEmpty()){
            passwordInputLogin.setError("Password is required");
            passwordInputLogin.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    LOGIN_CI_INTERFACE.openRoomSelectFragment();

                }else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface loginCI{
          void openSignUp();
          void openRoomSelectFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        LOGIN_CI_INTERFACE = (loginCI) activity;

    }


}

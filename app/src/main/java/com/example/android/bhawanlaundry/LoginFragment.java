package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {

    EditText emailInputLogin, passwordInputLogin;
    Button loginButton;
    TextView signUpText, forgotPasswordText;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String email, password;

    MainActivity mainActivity;
    loginCI LOGIN_CI_INTERFACE;

    FirebaseFirestore ff;

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ff = FirebaseFirestore.getInstance();


        //AUTH STATE LISTENER

           mAuth = FirebaseAuth.getInstance();

           mAuthStateListener = new FirebaseAuth.AuthStateListener() {
               @Override
               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   if(firebaseAuth.getCurrentUser() != null){
                       if(user.getDisplayName() == null || user.getDisplayName().isEmpty()){
                           LOGIN_CI_INTERFACE.openProfileSetUp();
                       } else {
                           DocumentReference dRef = ff.collection("students").document(user.getDisplayName());
                           progressBar.setVisibility(View.VISIBLE);
                           dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                               @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {
                                   progressBar.setVisibility(View.GONE);
                                   StudentUser studentUser = documentSnapshot.toObject(StudentUser.class);

                                   String room = studentUser.getRoom();

                                   if(room.trim().equals("laundryman")){
                                       LOGIN_CI_INTERFACE.openLaundrymanRoomSelectFragment();
                                   } else{
                                       LOGIN_CI_INTERFACE.openRoomSelectFragment();
                                   }
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   progressBar.setVisibility(View.GONE);
                                   Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });

                       }

                   }
               }
           };

           mAuth.addAuthStateListener(mAuthStateListener);


           //INSTANTIATING VIEWS
        emailInputLogin = view.findViewById(R.id.LOGIN_EMAIL_ENTER);
        passwordInputLogin = view.findViewById(R.id.LOGIN_PASSWORD_ENTER);
        loginButton = view.findViewById(R.id.LOGIN_BUTTON);
        signUpText = view.findViewById(R.id.SIGN_UP_TEXT);
        forgotPasswordText = view.findViewById(R.id.FORGOT_PASSWORD_TEXT);
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_LOGIN);


        mainActivity = new MainActivity();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.lockDrawer();
                emailInputLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);
                passwordInputLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);
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

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){

                        DocumentReference dRef = ff.collection("students").document(user.getDisplayName());
                        progressBar.setVisibility(View.VISIBLE);
                        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                progressBar.setVisibility(View.GONE);
                                StudentUser studentUser = documentSnapshot.toObject(StudentUser.class);

                                String room = studentUser.getRoom();

                                if(room.trim().equals("laundryman")){
                                    LOGIN_CI_INTERFACE.openLaundrymanRoomSelectFragment();
                                } else {
                                    LOGIN_CI_INTERFACE.openRoomSelectFragment();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }




                }else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface loginCI{
          void openSignUp();
          void openRoomSelectFragment();
          void openProfileSetUp();
          void openLaundrymanRoomSelectFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        LOGIN_CI_INTERFACE = (loginCI) activity;

    }


}

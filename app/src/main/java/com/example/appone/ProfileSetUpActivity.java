package com.example.appone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSetUpActivity extends AppCompatActivity {


    EditText eName, eRoom;
    Button bUpdate;
    FirebaseFirestore ff;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);
        eName = findViewById(R.id.NAMEINPUT);
        eRoom = findViewById(R.id.ROOMNUMBER);
        bUpdate = findViewById(R.id.UPDATEINFO);
        ff = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                eRoom.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String name = eName.getText().toString();
                String room = eRoom.getText().toString();
                if(name.isEmpty()){
                    eName.setError("Field cannot be empty");
                    eName.requestFocus();
                    return;
                }

                if(room.isEmpty()){
                    eRoom.setError("Field cannot be empty");
                    eRoom.requestFocus();
                    return;
                }

                updateInfo(name, room);
            }
        });


    }

    void updateInfo(String name, String room){
        String key;

        StudentUser sUser = new StudentUser(name, room);

        DocumentReference dRef = ff.collection("students").document();
        key = dRef.getId();
        dRef.set(sUser).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Toast.makeText(ProfileSetUpActivity.this, "Information updated", Toast.LENGTH_LONG);
           }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(key).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                       Toast.makeText(ProfileSetUpActivity.this, "Display name updated", Toast.LENGTH_LONG );
                        startActivity(new Intent(ProfileSetUpActivity.this, StudentMainActivity.class));
                    }
                }
            });
        }
    }

}

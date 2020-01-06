package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ProfileSetUpFragment extends Fragment {

    EditText nameInput, roomInput;
    String name, room;
    Button updateButton;
    FirebaseFirestore ff;
    FirebaseAuth mAuth;
    profileSetUpCI PROFILE_SET_UP_INTERFACE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_set_up, container, false);

        nameInput = view.findViewById(R.id.NAME_INPUT);
        roomInput = view.findViewById(R.id.ROOM_NUMBER_INPUT);
        updateButton = view.findViewById(R.id.UPDATE_INFO_BUTTON);

        ff = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
                roomInput.onEditorAction(EditorInfo.IME_ACTION_DONE);

                name = nameInput.getText().toString().trim();
                room = roomInput.getText().toString().trim();

                updateInfo(name, room);
            }
        });
        return view;
    }

    private void updateInfo(String name, String room) {
        if(name.isEmpty()){
            nameInput.setError("Field cannot be empty");
            nameInput.requestFocus();
            return;
        }

        if(room.isEmpty()){
            roomInput.setError("Field cannot be empty");
            roomInput.requestFocus();
            return;
        }

        StudentUser studentUser = new StudentUser(name, room);

        DocumentReference dRef = ff.collection("students").document();
        String key = dRef.getId();

        dRef.set(studentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               //Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_LONG).show();

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
                       // Toast.makeText(getContext(), "Display name updated", Toast.LENGTH_LONG );
                        PROFILE_SET_UP_INTERFACE.openRoomSelectFragment();
                    }
                }
            });
        }




    }

    public interface profileSetUpCI{

        void openRoomSelectFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        PROFILE_SET_UP_INTERFACE = (profileSetUpCI) activity;

    }

}

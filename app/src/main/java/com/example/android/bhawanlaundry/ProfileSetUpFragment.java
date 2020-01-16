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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileSetUpFragment extends Fragment {

    EditText nameInput, roomInput;
    String name, room;
    Button updateButton;
    FirebaseFirestore ff;
    FirebaseAuth mAuth;
    profileSetUpCI PROFILE_SET_UP_INTERFACE;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_set_up, container, false);

        nameInput = view.findViewById(R.id.NAME_INPUT);
        roomInput = view.findViewById(R.id.ROOM_NUMBER_INPUT);
        updateButton = view.findViewById(R.id.UPDATE_INFO_BUTTON);
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_PROFILE_SET_UP);

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

        final FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(key).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                       // Toast.makeText(getContext(), "Display name updated", Toast.LENGTH_LONG );
                        DocumentReference dRef = ff.collection("students").document(user.getDisplayName());
                        progressBar.setVisibility(View.VISIBLE);
                        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                progressBar.setVisibility(View.GONE);
                                StudentUser studentUser = documentSnapshot.toObject(StudentUser.class);

                                String room = studentUser.getRoom();

                                if(room.trim().equals("laundryman")){
                                    PROFILE_SET_UP_INTERFACE.openLaundrymanRoomSelectFragment();
                                } else {
                                    PROFILE_SET_UP_INTERFACE.openRoomSelectFragment();
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
            });
        }




    }

    public interface profileSetUpCI{
        void openLaundrymanRoomSelectFragment();
        void openRoomSelectFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        PROFILE_SET_UP_INTERFACE = (profileSetUpCI) activity;

    }

}

package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class RoomSelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    TextView nameText, roomText, laundrymanInAx, laundrymanInB, QueueInAx, QueueInB;
    Button buttonAx, buttonB, signOut;
    FirebaseAuth mAuth;
    StudentUser studentUser;
    static String name, room;
    roomSelectCI ROOM_SELECT_INTERFACE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_select, container, false);

        nameText = view.findViewById(R.id.HELLO_STUDENT);
        roomText = view.findViewById(R.id.ROOM_TEXT);
        buttonAx = view.findViewById(R.id.ENTER_Ax_ROOM_BUTTON);
        buttonB = view.findViewById(R.id.ENTER_B_ROOM_BUTTON);
        laundrymanInAx = view.findViewById(R.id.Ax_LAUNDRYMAN_IN);
        laundrymanInB = view.findViewById(R.id.B_LAUNDRYMAN_IN);
        QueueInAx = view.findViewById(R.id.Ax_QUEUE_LENGTH);
        QueueInB = view.findViewById(R.id.B_QUEUE_LENGTH);
        signOut = view.findViewById(R.id.SIGN_OUT_BUTTON);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user ==  null){
            ROOM_SELECT_INTERFACE.openLoginScreen();
            return view;
        }

        if(user.getDisplayName() == null || user.getDisplayName().isEmpty()){
            ROOM_SELECT_INTERFACE.openProfileSetUp();
            return view;
        }

        DocumentReference dRef = FirebaseFirestore.getInstance().collection("students").document(user.getDisplayName());


        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                studentUser = documentSnapshot.toObject(StudentUser.class);

                name = studentUser.getName();
                room = studentUser.getRoom();
                nameText.setText("Hello " + name + "!");
                roomText.setText("Room " + room);
            }
        });

        buttonAx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ROOM_SELECT_INTERFACE.openLaundryRoom(false, name, room);
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ROOM_SELECT_INTERFACE.openLaundryRoom(true, name, room);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    ROOM_SELECT_INTERFACE.openLoginScreen();
                }
            }
        });


        return view;
    }


    public interface roomSelectCI{
        void openLaundryRoom(boolean isB, String n, String r);
        void openLoginScreen();
        void openProfileSetUp();

    }

       @Override
    public void onAttach(Context context) {
        super.onAttach(context);
           Activity activity = (Activity) context;
           ROOM_SELECT_INTERFACE = (roomSelectCI) activity;
    }

}

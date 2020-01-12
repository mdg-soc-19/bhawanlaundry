package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class LaundrymanRoomSelect extends Fragment {

    laundrymanRoomSelectCI LAUNDRYMAN_ROOM_SELECT_INTERFACE;
    Button laundryman_enter_Ax, laundryman_enter_B;
    FirebaseFirestore ff;
    DocumentReference dRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_laundryman_room_select, container, false);

        laundryman_enter_Ax = view.findViewById(R.id.LAUNDRYMAN_ENTER_Ax_BUTTON);
        laundryman_enter_B = view.findViewById(R.id.LAUNDRYMAN_ENTER_B_BUTTON);

        ff = FirebaseFirestore.getInstance();



        laundryman_enter_Ax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRef = ff.document("laundryrooms/Ax");

                dRef.update("laundrymanPresent", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LAUNDRYMAN_ROOM_SELECT_INTERFACE.openLaundrymanRoom(false);
                    }
                });

            }
        });

        laundryman_enter_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRef = ff.document("laundryrooms/B");

                dRef.update("laundrymanPresent", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LAUNDRYMAN_ROOM_SELECT_INTERFACE.openLaundrymanRoom(true);
                    }
                });

            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        LAUNDRYMAN_ROOM_SELECT_INTERFACE = (laundrymanRoomSelectCI) activity;
    }

    public interface laundrymanRoomSelectCI {
            void openLaundrymanRoom(boolean isB);

    }
}

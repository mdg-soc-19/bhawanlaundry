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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Queue;


public class RoomSelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    TextView nameText, roomText, laundrymanInAx, laundrymanInB, QueueInAx, QueueInB;
    Button buttonAx, buttonB, signOut;
    FirebaseAuth mAuth;
    FirebaseFirestore ff;
    StudentUser studentUser;
    static String name, room;
    static boolean AxLaundrymanIn, BLaundrymanIn;
    roomSelectCI ROOM_SELECT_INTERFACE;
    ProgressBar progressBar;
    static int numBucketsAx, numBucketsB;
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
        progressBar = view.findViewById(R.id.PROGRESS_CIRCULAR_STUDENT_ROOM_SELECT);

        ff = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user ==  null){
            ROOM_SELECT_INTERFACE.openLoginScreen();
            return view;
        }



        if(user.getDisplayName() == null || user.getDisplayName().isEmpty()){
            ROOM_SELECT_INTERFACE.openProfileSetUp();
            return view;
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
                        ROOM_SELECT_INTERFACE.openLaundrymanRoomSelectFragment();
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

        //GET NAME AND ROOM NUMBER OF STUDENT

        DocumentReference dRef = ff.collection("students").document(user.getDisplayName());
        progressBar.setVisibility(View.VISIBLE);
        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                studentUser = documentSnapshot.toObject(StudentUser.class);

                name = studentUser.getName();
                room = studentUser.getRoom();
                nameText.setText("Hello " + name + "!");
                roomText.setText("Room " + room);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //LAUNDRY ROOM STATUS IN BLOCK Ax

        dRef = ff.collection("laundryrooms").document("Ax");
        progressBar.setVisibility(View.VISIBLE);
        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                AxLaundrymanIn = (Boolean) documentSnapshot.get("laundrymanPresent");
                numBucketsAx = ((Long) documentSnapshot.get("numBuckets")).intValue();

                if(AxLaundrymanIn){
                    laundrymanInAx.setText("OPEN");
                } else {
                    laundrymanInAx.setText("CLOSED");
                }

                QueueInAx.setText("" + numBucketsAx);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //LAUNDRY ROOM STATUS IN BLOCK B

        dRef = ff.collection("laundryrooms").document("B");
        progressBar.setVisibility(View.VISIBLE);
        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                BLaundrymanIn = (Boolean) documentSnapshot.get("laundrymanPresent");
                numBucketsB = ((Long) documentSnapshot.get("numBuckets")).intValue();

                if(BLaundrymanIn){
                    laundrymanInB.setText("OPEN");
                } else {
                    laundrymanInB.setText("CLOSED");
                }

                QueueInB.setText("" + numBucketsB);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        buttonAx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference dRef = FirebaseFirestore.getInstance().document("laundryrooms/Ax");
                progressBar.setVisibility(View.VISIBLE);
                dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        Boolean in = (Boolean) documentSnapshot.get("laundrymanPresent");

                        if(in){
                            ROOM_SELECT_INTERFACE.openLaundryRoom(false, name, room, numBucketsAx);
                        } else  {
                            Toast.makeText(getContext(), "Laundry room is closed", Toast.LENGTH_SHORT).show();
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
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DocumentReference dRef = FirebaseFirestore.getInstance().document("laundryrooms/B");
                progressBar.setVisibility(View.VISIBLE);
                dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        Boolean in = (Boolean) documentSnapshot.get("laundrymanPresent");

                        if(in){
                            ROOM_SELECT_INTERFACE.openLaundryRoom(true, name, room, numBucketsB);
                        } else  {
                            Toast.makeText(getContext(), "Laundry room is closed", Toast.LENGTH_SHORT).show();
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
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    MainActivity.lockDrawer();
                    ROOM_SELECT_INTERFACE.openLoginScreen();
                }
            }
        });


        return view;
    }


    public interface roomSelectCI{
        void openLaundryRoom(boolean isB, String n, String r, int numBuckets);
        void openLoginScreen();
        void openProfileSetUp();
        void openLaundrymanRoomSelectFragment();

    }

       @Override
    public void onAttach(Context context) {
        super.onAttach(context);
           Activity activity = (Activity) context;
           ROOM_SELECT_INTERFACE = (roomSelectCI) activity;
    }

}

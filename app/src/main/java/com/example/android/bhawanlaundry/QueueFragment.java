package com.example.android.bhawanlaundry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.FirstPartyScopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class QueueFragment extends Fragment{

    FirebaseFirestore ff = FirebaseFirestore.getInstance();
    CollectionReference cRef;
    static String laundryRoom;
    RecyclerView recyclerView;
    static QueueAdapter queueAdapter;

    List<Task> queueList = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue, container, false);
        laundryRoom = getArguments().getString("laundry_room").trim();

        ((MainActivity) getActivity()).setActionBarTitle(laundryRoom  +" Finished" );

        swipeRefreshLayout = view.findViewById(R.id.SWIPE_REFRESH_QUEUE);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateQueueList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        recyclerView = view.findViewById(R.id.QUEUE_RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        queueAdapter = new QueueAdapter(queueList, getContext());
        //requestsAdapter = new RequestsAdapter(updateRequestsList(), getContext());
        recyclerView.setAdapter(queueAdapter);

        updateQueueList();


        return view;
    }

    public void updateQueueList() {

        cRef = ff.collection("laundryrooms").document(laundryRoom).collection("taskQueue");

        cRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    if(!task.getResult().isEmpty()) {
                       queueList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Task t = documentSnapshot.toObject(Task.class);
                            queueList.add(t);
                        }
                    } else{
                        Toast.makeText(getContext(), "No buckets finished", Toast.LENGTH_SHORT).show();
                    }

                    queueAdapter.notifyDataSetChanged();
                }
            }
        });


    }




}

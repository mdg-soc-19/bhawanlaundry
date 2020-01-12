package com.example.android.bhawanlaundry;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class RequestsFragment extends Fragment{

    Task task;
    FirebaseFirestore ff = FirebaseFirestore.getInstance();
    CollectionReference cRef;
    static String laundryRoom;
    RecyclerView recyclerView;
    static RequestsAdapter requestsAdapter;

    List<Task> taskList = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        laundryRoom = getArguments().getString("laundry_room").trim();
        swipeRefreshLayout = view.findViewById(R.id.SWIPE_REFRESH_REQUESTS);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateRequestsList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        recyclerView = view.findViewById(R.id.REQUESTS_RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestsAdapter = new RequestsAdapter(taskList, getContext());
        //requestsAdapter = new RequestsAdapter(updateRequestsList(), getContext());
        recyclerView.setAdapter(requestsAdapter);

        updateRequestsList();


        return view;
    }

    public void updateRequestsList() {

        cRef = ff.collection("laundryrooms").document(laundryRoom).collection("tasks");

        cRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    taskList.clear();
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Task t = documentSnapshot.toObject(Task.class);
                        taskList.add(t);
                    }


                    requestsAdapter.notifyDataSetChanged();
                }
            }
        });

       /*Task t = new Task("Shreyas", "AF-028", 5, "Blue");
       taskList.add(t);

       t = new Task("Jitesh", "AF-029", 5, "Blue");
       taskList.add(t);

        t = new Task("Dinku", "AF-033", 56, "Pink");
        taskList.add(t);*/

        requestsAdapter.notifyDataSetChanged();
       return;
    }



}

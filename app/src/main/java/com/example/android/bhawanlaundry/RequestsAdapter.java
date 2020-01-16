package com.example.android.bhawanlaundry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsHolder> {

    Context context;
    List<Task> tasks;

    FirebaseFirestore ff;
    DocumentReference dRef;

    public RequestsAdapter (List<Task> t, Context c){
        this.context = c;
        this.tasks = t;
    }

    @NonNull
    @Override
    public RequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_request, null); //to inflate our cardiew


        return new RequestsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestsHolder holder, int position) {
        holder.studentNameTextView.setText(tasks.get(position).getNameOfStudent());
        holder.studentRoomTextView.setText(tasks.get(position).getRoomOfStudent());
        holder.bucketColourTextView.setText(tasks.get(position).getBucketColour());
        holder.numClothesTextView.setText("" + tasks.get(position).getNumberOfClothes());

        ff = FirebaseFirestore.getInstance();

        holder.wash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   // Toast.makeText(v.getContext(),"TEST", Toast.LENGTH_SHORT).show();
                    Task tassk = new Task(holder.studentNameTextView.getText().toString().trim(),
                                         holder.studentRoomTextView.getText().toString().trim(),
                                         Integer.parseInt(holder.numClothesTextView.getText().toString().trim()),
                                         holder.bucketColourTextView.getText().toString().trim() );

                    dRef = ff.collection("laundryrooms").document(RequestsFragment.laundryRoom).collection("taskQueue").document();
                    dRef.set(tassk).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if(task.isSuccessful()){
                                final DocumentReference dd = ff.collection("laundryrooms").document(RequestsFragment.laundryRoom);

                                dd.collection("tasks").whereEqualTo("nameOfStudent", holder.studentNameTextView.getText().toString().trim()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                    String key = documentSnapshot.getId();
                                                    DocumentReference df = dd.collection("tasks").document(key);
                                                    df.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            dd.update("numBuckets", FieldValue.increment(-1));

                                                            RequestsFragment rf = new RequestsFragment();
                                                            rf.updateRequestsList();
                                                        }
                                                    });
                                                }

                                            }
                                        }
                                    });
                            }
                        }
                    });

            }
        });



    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}

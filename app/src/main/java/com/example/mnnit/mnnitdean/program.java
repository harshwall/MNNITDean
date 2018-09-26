package com.example.mnnit.mnnitdean;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class program extends RecyclerView.Adapter{
    private String[] data;
    public DatabaseReference rootReference;
    private ArrayList<user> arrayList=new ArrayList<user>();
    public program(ArrayList<user> arrayList){
        this.arrayList=arrayList;


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.listitem,viewGroup,false);
        rootReference=FirebaseDatabase.getInstance().getReference();
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final user prin=arrayList.get(i);
        final holder newHolder=(holder)viewHolder;
        newHolder.t1.setText(prin.name);
        newHolder.t2.setText(prin.regno);
        newHolder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootReference.child("Profile:").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {

                            if(snapshot.child("regno").getValue().toString().equals(prin.regno))
                            {
                                rootReference.child("Profile:/"+snapshot.getKey().toString()).child("flag").setValue("1");
                                return;
                            }





                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView t1,t2;
        Button b1,b2;
        public holder(@NonNull View itemView) {
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.regno);
            t2=(TextView)itemView.findViewById(R.id.name);
            b1=(Button)itemView.findViewById(R.id.accept);
            b2=(Button)itemView.findViewById(R.id.reject);

        }
    }

}

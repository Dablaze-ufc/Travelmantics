package com.dablaze.travelmanticsblaze;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase fFirebaseDatabase;
    private DatabaseReference fDatabaseReference;
    private ChildEventListener fChildEventListener;
    public RecyclerViewAdapter (){

        FirebaseUtil.openFbReference("travel deals");
        fFirebaseDatabase = FirebaseUtil.sFirebaseDatabase;
        fDatabaseReference = FirebaseUtil.sDatabaseReference;
        deals = FirebaseUtil.sDeals;
        fChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ",travelDeal.getTittle());
                travelDeal.setid(dataSnapshot.getKey());
                deals.add(travelDeal);
                notifyItemInserted(deals.size() -1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        fDatabaseReference.addChildEventListener(fChildEventListener);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_layout,  parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView fTextTittle;
        TextView fTextPrice;
        TextView fTextDes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fTextTittle = itemView.findViewById(R.id.text_tittle5);
            fTextPrice = itemView.findViewById(R.id.text_price2);
            fTextDes = itemView.findViewById(R.id.text_description2);
            itemView.setOnClickListener(this);
        }
        public void bind (TravelDeal deal){
            fTextTittle.setText(deal.getTittle());
            fTextDes.setText(deal.getDescription());
            fTextPrice.setText(deal.getPrice());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("click", String.valueOf(position));
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(v.getContext(),DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            v.getContext().startActivity(intent);
        }
    }
}

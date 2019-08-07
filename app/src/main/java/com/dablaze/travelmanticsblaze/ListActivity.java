package com.dablaze.travelmanticsblaze;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        MenuItem insetMenu = menu.findItem(R.id.new_deal);
        if (FirebaseUtil.isAdmin  ){
            insetMenu.setVisible(true);
        }else {
            insetMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_deal:
                Intent intent = new Intent(this, DealActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUtil.attachListner();
                            }
                        });
                FirebaseUtil.detachListner();
                return true;


        }
        return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("travel deals", this);
        RecyclerView recyclerView = findViewById(R.id.recycler_deals);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseUtil.attachListner();
          if (FirebaseUtil.sFirebaseAuth.getUid() != null) {
            FirebaseUtil.checkAdmin(FirebaseUtil.sFirebaseAuth.getUid());
            showMenu();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListner();
        if (FirebaseUtil.sFirebaseAuth.getUid() != null) {
            FirebaseUtil.checkAdmin(FirebaseUtil.sFirebaseAuth.getUid());
            showMenu();
        }

    }

    public void showMenu() {
        invalidateOptionsMenu();
    }





    boolean twice = false;
    @Override
    public void onBackPressed(){
    if (twice == true) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);}
        twice = true;
        Toast.makeText(this,"Press BACK again to EXIT",Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;

            }
        },3000);


    }

}




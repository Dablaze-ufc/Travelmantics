package com.dablaze.travelmanticsblaze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase fFirebaseDatabase;
    private DatabaseReference fDatabaseReference;
    EditText txtTittle;
    EditText txtPrice;
    EditText txtDescription;
    TravelDeal fDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        fFirebaseDatabase = FirebaseUtil.sFirebaseDatabase;
        fDatabaseReference = FirebaseUtil.sDatabaseReference;
        txtTittle = findViewById(R.id.text_tittle);
        txtDescription = findViewById(R.id.text_des);
        txtPrice = findViewById(R.id.text_price);
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null){
            deal = new TravelDeal();
        } this.fDeal = deal;
        txtTittle.setText(deal.getTittle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save:
                saveDeal();
                Toast.makeText(this,"Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.menu_delete:
                deleteDeal();
                Toast.makeText(this,"Deal Deleted", Toast.LENGTH_LONG).show();
                backToList();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    private void clean() {
        txtTittle.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtTittle.requestFocus();

    }

    private void saveDeal() {
        fDeal.setTittle(txtTittle.getText().toString());
        fDeal.setPrice(txtPrice.getText().toString());
        fDeal.setDescription(txtDescription.getText().toString());
        if (fDeal.getId() == null){
        fDatabaseReference.push().setValue(fDeal);}
        else {
            fDatabaseReference.child(fDeal.getId()).setValue(fDeal);
        }
    }

    private void deleteDeal (){
        if (fDeal == null){
            Toast.makeText(this,"Please save deal before deleting", Toast.LENGTH_LONG).show();
            return;

        }
        fDatabaseReference.child(fDeal.getId()).removeValue();
    }
    private void backToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        if (FirebaseUtil.isAdmin ){
            menu.findItem(R.id.menu_delete).setVisible(true);
            menu.findItem(R.id.menu_save).setVisible(true);
            enaleEditText(true);
        } else {
            menu.findItem(R.id.menu_delete).setVisible(false);
            menu.findItem(R.id.menu_save).setVisible(false);
            enaleEditText(false);
        }
        return true;
    }
    private void enaleEditText(boolean isEnabled){
        txtTittle.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
    }
}

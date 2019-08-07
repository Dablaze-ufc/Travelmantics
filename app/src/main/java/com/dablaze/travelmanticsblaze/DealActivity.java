package com.dablaze.travelmanticsblaze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.Resource;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase fFirebaseDatabase;
    private static final int PICTURE_RESULT = 56;
    private DatabaseReference fDatabaseReference;
    EditText txtTittle;
    EditText txtPrice;
    EditText txtDescription;
    TravelDeal fDeal;
    ImageView fImageView;
    private Button fButton;
    private String fUri;
    private ProgressBar fProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        fFirebaseDatabase = FirebaseUtil.sFirebaseDatabase;
        fDatabaseReference = FirebaseUtil.sDatabaseReference;
        txtTittle = findViewById(R.id.text_tittle);
        txtDescription = findViewById(R.id.text_des);
        txtPrice = findViewById(R.id.text_price);
        fImageView = findViewById(R.id.imageDeals);
        fProgressBar = findViewById(R.id.progressBar_image);
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null){
            deal = new TravelDeal();
        } this.fDeal = deal;
        txtTittle.setText(deal.getTittle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());
        fButton = findViewById(R.id.button_upload);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent,"Insert picture"),PICTURE_RESULT);
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save:
                saveDeal();
                Toast.makeText(this,"Deal Saved", Toast.LENGTH_LONG).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri imageUri =  data.getData();
            final StorageReference reference = FirebaseUtil.sStorageReference.child(imageUri.getLastPathSegment());
            fProgressBar.setVisibility(fProgressBar.VISIBLE);
            fButton.setEnabled(false);
            UploadTask uploadTask = reference.putFile(imageUri);
            Task <Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        fProgressBar.setVisibility(fProgressBar.GONE);
                        fButton.setEnabled(true);
                        Uri downloadUri = task.getResult();
                        fUri = downloadUri.toString();
                        fDeal.setImageUrl(fUri);
                        String picName = task.getResult().getLastPathSegment();
                        fDeal.setImageName(picName);
                        showImage(fUri);

                    }
                }
            });
        }
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
//        fDeal.setTittle(txtTittle.getText().toString());
//        fDeal.setPrice(txtPrice.getText().toString());
//        fDeal.setDescription(txtDescription.getText().toString());
        if (TextUtils.isEmpty(txtTittle.getText()) && TextUtils.isEmpty(txtDescription.getText())&& TextUtils.isEmpty(txtPrice.getText())){
            Toast.makeText(this,"Please save deal before deleting", Toast.LENGTH_LONG).show();
            return;

        }
        fDatabaseReference.child(fDeal.getId()).removeValue();
        if(this.fDeal.getImageUrl() != null && !this.fDeal.getImageUrl().isEmpty()){
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(this.fDeal.getImageUrl());
                    ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("delete image","image deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("delete image",
                            "something went wrong");

                }
            });

        }
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
            fButton.setEnabled(true);

        } else {
            menu.findItem(R.id.menu_delete).setVisible(false);
            menu.findItem(R.id.menu_save).setVisible(false);
            enaleEditText(false);
            fButton.setEnabled(false);
        }
        return true;
    }
    private void showImage( String uri){
        if(uri != null && uri.isEmpty() == false);
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Picasso.get()
                .load(uri)
                .resize(width,width*2/3)
                .centerCrop()
                .into(fImageView);

    }
    private void enaleEditText(boolean isEnabled){
        txtTittle.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
    }
}

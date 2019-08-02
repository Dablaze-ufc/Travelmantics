package com.dablaze.travelmanticsblaze;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase sFirebaseDatabase;
    public static DatabaseReference sDatabaseReference;
    public static ArrayList<TravelDeal> sDeals;
    private static FirebaseUtil sFirebaseUtil;

    private FirebaseUtil(){}

    public static void openFbReference (String ref) {
        if (sFirebaseUtil == null) {
            sFirebaseUtil = new FirebaseUtil();
            sFirebaseDatabase = FirebaseDatabase.getInstance();

        }
        sDeals = new ArrayList<TravelDeal>();
        sDatabaseReference = sFirebaseDatabase.getReference().child(ref);
    }
}

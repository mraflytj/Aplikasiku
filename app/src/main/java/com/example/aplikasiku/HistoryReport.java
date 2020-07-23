package com.example.aplikasiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HistoryReport extends AppCompatActivity {

    private ListView listView;
    ImageView back;

    ImageInformation childValue;

    //Firebase variables
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    //Firebase items
    ArrayList<String> itemData;
    ArrayList<String> itemGps;
    ArrayList<String> itemTime;
    ArrayList<String> itemStatus;

    //Dialog Items
    ArrayList<String> dialogDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_report);

        listView = findViewById(R.id.reportList);

        //initializing pothole details array list
        itemData = new ArrayList<String>();
        itemGps = new ArrayList<String>();
        itemTime = new ArrayList<String>();
        itemStatus = new ArrayList<String>();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        //firebase data retrieval
        databaseReference.child("").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    childValue = child.getValue(ImageInformation.class);
                    assert childValue != null;
                    itemGps.add(childValue.GPS_Coordinates);
                    itemStatus.add(String.valueOf(childValue.Status));
                    itemTime.add(childValue.Timestamp);
                }
                createListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createListView();

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                startActivity(new Intent(TrackReportActivity.this, HomeActivity.class));
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogDetails = new ArrayList<String>();
                dialogDetails.add(itemTime.get(position));
                dialogDetails.add(itemGps.get(position));
                dialogDetails.add(itemStatus.get(position));
                openDialog(dialogDetails);
            }
        });
    }

    public void openDialog(ArrayList<String> dialogDetails) {
        PotholeReportDialog potholeReportDialog = new PotholeReportDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("key", dialogDetails);
        potholeReportDialog.setArguments(bundle);
        potholeReportDialog.show(getSupportFragmentManager(), "Show Report");
    }

    private void createListView(){
        ReportStatusAdapter myAdapter = new ReportStatusAdapter(this, itemTime, itemGps, itemStatus);
        listView.setAdapter(myAdapter);
    }
}

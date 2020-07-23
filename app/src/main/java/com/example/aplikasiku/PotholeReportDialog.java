package com.example.aplikasiku;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class PotholeReportDialog extends AppCompatDialogFragment {
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvGPS;
//    private TextView tvArea;
    private TextView tvStatus;
    private Button btnConfirm;
    private Button btnYes;
    private Button btnNo;

    ArrayList<String> dialogDetails;

    //data variables
    String date_Time;
    String gpsVal;
//    String areaVal;
    String statusVal;

    //Firebase variables
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        dialogDetails = new ArrayList<String>();

        Bundle bundle = this.getArguments();
        assert bundle != null;
        dialogDetails = bundle.getStringArrayList("key");

        date_Time = dialogDetails.get(0);
        gpsVal = dialogDetails.get(1);
//        areaVal = dialogDetails.get(2);
        statusVal = dialogDetails.get(2);

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.pothole_report_dialog, null);

        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);
        tvGPS = view.findViewById(R.id.tvGPS);
//        tvArea = view.findViewById(R.id.tvArea);
        tvStatus = view.findViewById(R.id.tvStatus);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        btnConfirm.setVisibility(View.GONE);
        btnYes.setVisibility(View.GONE);
        btnNo.setVisibility(View.GONE);

        tvDate.setText(date_Time);
        tvTime.setText(date_Time);
        tvGPS.setText(gpsVal);
//        tvArea.setText(areaVal);
        tvStatus.setText(statusVal);

        builder.setView(view)
                .setTitle("Report Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        if (Integer.parseInt(statusVal) == 80){
            btnConfirm.setVisibility(View.VISIBLE);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnYes.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.VISIBLE);
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("").child(firebaseUser.getUid()).child(date_Time).child("Status").setValue(100)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Status Updated !!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return builder.create();
    }
}

package com.example.bloodbank4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bloodbank4.R;
import com.example.bloodbank4.adapter.CustomAdapter;
import com.example.bloodbank4.container.Donor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private EditText editText;
    private TextView resultText;
    private List<Donor> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        
        initComponent();
        String bgp=getIntent().getStringExtra("bloodgp");
        resultText.setText("Showing Results For "+bgp);

        databaseReference= FirebaseDatabase.getInstance().getReference("Donors")
        .child(bgp);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for(DataSnapshot ds:dataSnapshot.getChildren()){
                     Donor donor=ds.getValue(Donor.class);
                     editText.setText(donor.getName()+" "+editText.getText());
                     list.add(donor);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(new CustomAdapter(getApplicationContext(),list));

    }

    private void initComponent() {
        editText = findViewById(R.id.editText);
        resultText = findViewById(R.id.result_text_view);
        recyclerView = findViewById(R.id.result_recycle_view);
        list=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

    }
}

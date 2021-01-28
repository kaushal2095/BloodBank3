package com.example.bloodbank4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank4.R;
import com.example.bloodbank4.container.Donor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DonateActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText phoneInput;
    private Spinner bgps;
    private TextView locationInput;
    private Button donateButton;
    private ProgressDialog  p,progressDialog;
    private CheckBox usemyloc;

    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat,loongi;
    private int PERMISSION_CODE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        initComponent();

        setupBloodGroupSpinner();

        checkBox();

        insertInDonorDatabase();

    }

    private void insertInDonorDatabase() {
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameInput.getText().toString();
                String phone=phoneInput.getText().toString();
                String bloodgrp=bgps.getSelectedItem().toString();
                String location=locationInput.getText().toString();
                if(!name.isEmpty()&&!phone.isEmpty()&&!bloodgrp.isEmpty()&& !location.isEmpty()){
                    progressDialog=new ProgressDialog(DonateActivity.this);
                    progressDialog.setMessage("Submitting...");
                    progressDialog.show();
                    storeInDatabase();
                }else{
                    Toast.makeText(DonateActivity.this, "Please Enter Complete Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeInDatabase() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Donors");
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String bloodgp = bgps.getSelectedItem().toString();
        String location = locationInput.getText().toString();
        String latitude=String.valueOf(lat);
        String longitude=String.valueOf(loongi);
        Donor donor=new Donor(name,phone,bloodgp,latitude,longitude);
        databaseReference.child(bloodgp).push().setValue(donor)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      progressDialog.hide();
                      Toast.makeText(DonateActivity.this, "Thanks for Donating", Toast.LENGTH_SHORT).show();
                  }
              });
    }

    private void checkBox() {
        usemyloc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Toast.makeText(DonateActivity.this, "Please Enable Location", Toast.LENGTH_SHORT).show();
                    }else{
                        p.setCanceledOnTouchOutside(false);
                        p.setMessage("Getting Your Location...");
                        p.show();
                        fetchLocation();
                    }
                }else{
                    locationInput.setText(null);
                }
            }
        });
    }

    private void fetchLocation() {
        locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
             if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                 requestPermissions(new String[]{
                         Manifest.permission.ACCESS_FINE_LOCATION,
                         Manifest.permission.INTERNET,
                         Manifest.permission.ACCESS_COARSE_LOCATION
                 },PERMISSION_CODE);
             }
             return ;
        }
        performTask();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                performTask();

              break;
        }
    }

    private void performTask() {
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Integer.MAX_VALUE, Integer.MIN_VALUE, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lat=location.getLatitude();
                        loongi=location.getLongitude();

                        Geocoder geocoder=new Geocoder(DonateActivity.this, Locale.getDefault());
                        try{
                            List<Address> addressList=geocoder.getFromLocation(lat,loongi,1);
                            String locality=addressList.get(0).getLocality();
                            String city=addressList.get(0).getAddressLine(0);
                            String state="";
                            locationInput.setText(locality+", "+city);
                            p.hide();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Integer.MAX_VALUE, Integer.MIN_VALUE, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat=location.getLatitude();
                    loongi=location.getLatitude();
                    Geocoder geocoder=new Geocoder(DonateActivity.this);
                    try{
                        List<Address> addressList=geocoder.getFromLocation(lat,loongi,1);
                        String locality=addressList.get(0).getLocality();
                        String city=addressList.get(0).getAddressLine(0);
                        locationInput.setText(locality+", "+city);
                        p.hide();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }


    private void setupBloodGroupSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_dropdown_item);
        bgps.setAdapter(arrayAdapter);
    }

    private void initComponent() {
        nameInput = findViewById(R.id.donate_name);
        phoneInput = findViewById(R.id.donate_phone);
        locationInput = findViewById(R.id.donate_address);
        bgps = findViewById(R.id.donate_blood_group_spinner);
        donateButton = findViewById(R.id.donate_submit);
        usemyloc = findViewById(R.id.donate_address_check_box);
        locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        p=new ProgressDialog(this);
        progressDialog=new ProgressDialog(this);
    }
}

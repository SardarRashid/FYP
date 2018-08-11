package com.example.rashidsaddique.fyp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class CustomerMapActivity  extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private Button mLogout, mRequest;
    private LatLng WorkLocation;
    private  Boolean RequestbBol = false;
    private Marker workMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLogout = (Button) findViewById(R.id.EmployeeLogout);
        mRequest = (Button) findViewById(R.id.EmployeeRequestButton);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RequestbBol){
                    geoQuery.removeAllListeners();
                    RequestbBol = false;
                    employeeLocationRef.removeEventListener(employeeLocationRefListener);


                    if(EmployeefoundID != null){
                        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Employee").child(EmployeefoundID);
                        employeeRef.setValue(true);
                        EmployeefoundID = null;

                    }
                    EmployeeFound =false;
                    radius = 1;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerRequest");
                    GeoFire geoFire = new GeoFire(reference);
                    geoFire.removeLocation(userId);

                    if(workMarker != null){
                        workMarker.remove();
                    }
                    mRequest.setText("Call Employee");

                }else {
                    RequestbBol= true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerRequest");
                    GeoFire geoFire = new GeoFire(reference);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                    WorkLocation = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    workMarker = mMap.addMarker(new MarkerOptions().position(WorkLocation).title("Work Here"));

                    mRequest.setText("Getting Employee For You");

                    getClosestEmployee();


                }



            }
        });
    }

    private int radius  = 1;
    private boolean EmployeeFound = false;
    private String EmployeefoundID;
    GeoQuery geoQuery;
    private void getClosestEmployee (){
        DatabaseReference EmployeeLocation = FirebaseDatabase.getInstance().getReference().child("EmployeesAvailable");
        GeoFire geoFire = new GeoFire(EmployeeLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(WorkLocation.latitude,WorkLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!EmployeeFound && RequestbBol) {
                    EmployeeFound = true;
                    EmployeefoundID = key;

                    DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Employee").child(EmployeefoundID);
                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("CustomerWorkId", customerId);
                    employeeRef.updateChildren(map);

                    getEmployeeLocation();
                    mRequest.setText("Looking For Employee Location...");



                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!EmployeeFound){
                    radius++;
                    getClosestEmployee();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


     private Marker mEmployeeMarker;
    private  DatabaseReference employeeLocationRef;
    private ValueEventListener employeeLocationRefListener;
    private void getEmployeeLocation(){
        employeeLocationRef = FirebaseDatabase.getInstance().getReference().child("EmployeesWorking").child(EmployeefoundID).child("l");
        employeeLocationRefListener = employeeLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && RequestbBol){
                    List<Object> map = (List<Object>)dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    mRequest.setText("Employee Found");
                    if(map.get(0)!= null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1)!= null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng employeeLatLng = new LatLng(locationLat,locationLng);
                    if(mEmployeeMarker != null){
                        mEmployeeMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(WorkLocation.latitude);
                    loc1.setLongitude(WorkLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(employeeLatLng.latitude);
                    loc2.setLongitude(employeeLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if(distance<100){
                        mRequest.setText("Employee is here");
                    }else {
                        mRequest.setText("Employee Found" + String.valueOf(distance));
                    }

                    mEmployeeMarker = mMap.addMarker(new MarkerOptions().position(employeeLatLng).title("Your Employee"));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLongitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));



    }

    @SuppressWarnings("deprecation")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}

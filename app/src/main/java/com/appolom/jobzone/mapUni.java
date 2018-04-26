package com.appolom.jobzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class mapUni extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static double lat;
    public static double lng;
    public double latJob;
    public double lngJob;
    private String category;
    private String name;
    private String detailText;
    private String money;
    private String uid;
    private String phoneNumber;
    private String offer_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_uni);

        lat = getIntent().getDoubleExtra("lat",0);
        lng = getIntent().getDoubleExtra("lng",0);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle("JOB ZONE");


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(false);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_offer:
                        Intent intent = new Intent(mapUni.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(mapUni.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(mapUni.this, mapOfferActivity.class);
                        intent3.putExtra("lat", lat);
                        intent3.putExtra("lng", lng);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        };



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);







        //offerActivity bar = new offerActivity();


        //Google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //busca las ofertas de la BD
        offer_key = getIntent().getStringExtra("offer_key");



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query usersRef = rootRef.child("offer").orderByKey().equalTo(offer_key);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    category = ds.child("category").getValue(String.class);
                    name = ds.child("name").getValue(String.class);
                    detailText = ds.child("detailText").getValue(String.class);
                    money = ds.child("money").getValue(String.class);
                    phoneNumber = ds.child("phoneNumber").getValue(String.class);
                    latJob = ds.child("lat").getValue(double.class);
                    lngJob = ds.child("lng").getValue(double.class);

                  //  Log.d("TAG", String.valueOf(category) + "detalle " + detailText + "money " + money + "latJob " + latJob + "lng " + lngJob);
                  //  ((TextView) findViewById(R.id.name)).setText(name);
                  //  ((TextView) findViewById(R.id.detailText)).setText(detailText);
                  //  ((TextView) findViewById(R.id.category)).setText(category);
                  //  ((TextView) findViewById(R.id.money)).setText(money);

                    LatLng uno = new LatLng(latJob, lngJob);
                    mMap.addMarker(new MarkerOptions().position(uno).title(category).snippet("See offer"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(uno));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                    {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            goContact();

                        }


                    });



                }







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersRef.addListenerForSingleValueEvent(eventListener);




    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //    lat = getIntent().getDoubleExtra("lat",0);
        //    lng = getIntent().getDoubleExtra("lng",0);


        // my location
        //   LatLng me = new LatLng(lat, lng);
        //   mMap.addMarker(new MarkerOptions().position(me).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_black_24dp)));
        //   mMap.moveCamera(CameraUpdateFactory.newLatLng(me));

        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {



            case R.id.action_manage:
                goControlOffer();
                return true;




            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }






    public void goControlOffer() {

        Intent intent = new Intent(this, loginManageOffer.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);

    }



    public void goContact(){
        Intent intent = new Intent(this, sms.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("category", category);
        intent.putExtra("detailText", detailText);
        intent.putExtra("money", money);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);

    }





}


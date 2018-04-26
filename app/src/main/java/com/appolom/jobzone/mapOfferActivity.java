package com.appolom.jobzone;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class mapOfferActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {


    private GoogleMap mMap;


    public static double lat;
    public static double lng;
    public String phoneNumber;
    public String category;
    public String detailText;
    public String money;
    public String name;
    public double latJob;
    public double lngJob;
    private Marker locationMarker;
    private String choose;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_offer);

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(getString(R.string.job_places));


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(false);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_offer:
                        Intent intent = new Intent(mapOfferActivity.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(mapOfferActivity.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(mapOfferActivity.this, mapOfferActivity.class);
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

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Offer");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    phoneNumber = ds.child("phoneNumber").getValue(String.class);
                    category = ds.child("category").getValue(String.class);
                    detailText = ds.child("detailText").getValue(String.class);
                    name = ds.child("name").getValue(String.class);
                    money = ds.child("money").getValue(String.class);
                    latJob = ds.child("lat").getValue(double.class);
                    lngJob = ds.child("lng").getValue(double.class);


                    LatLng uno = new LatLng(latJob, lngJob);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(uno).title(category).snippet("See the offer"));
                    final MarkerTag yourMarkerTag = new MarkerTag();
                    yourMarkerTag.setdetailText(detailText);
                    yourMarkerTag.setcategory(category);
                    yourMarkerTag.setName(name);
                    yourMarkerTag.setPhoneNumber(phoneNumber);
                    yourMarkerTag.setchoose("offer");
                    yourMarkerTag.setlat(lat);
                    yourMarkerTag.setlng(lng);
                    yourMarkerTag.setmoney(money);
                    yourMarkerTag.setlatJob(latJob);
                    yourMarkerTag.setlngJob(lngJob);


                    marker.setTag(yourMarkerTag);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(uno));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {


                            MarkerTag yourMarkerTag = ((MarkerTag) marker.getTag());

                            //  Toast.makeText(mapOfferActivity.this, yourMarkerTag.getPhoneNumber(), Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(mapOfferActivity.this, sms.class);

                            choose = "offer";
                            intent.putExtra("choose", yourMarkerTag.getchoose());
                            intent.putExtra("lat", yourMarkerTag.getlat());
                            intent.putExtra("lng", yourMarkerTag.getlng());
                            intent.putExtra("category", yourMarkerTag.getcategory());
                            intent.putExtra("detailText", yourMarkerTag.getdetailText());
                            intent.putExtra("name", yourMarkerTag.getName());
                            intent.putExtra("money", yourMarkerTag.getmoney());
                            intent.putExtra("phoneNumber", yourMarkerTag.getPhoneNumber());
                            intent.putExtra("latJob", yourMarkerTag.getlatJob());
                            intent.putExtra("lngJob", yourMarkerTag.getlngJob());
                            startActivity(intent);




                        }


                    });



                    // my location
                    LatLng me = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(me).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_36dp)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(me));


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersRef.addListenerForSingleValueEvent(eventListener);




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {


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






    public void onSearch(View view) {


        EditText location_tf = (EditText) findViewById(R.id.TFadreess);
        String location = location_tf.getText().toString();
        List<Address> addressList;
        if (location != null || !location.equals("")) ;
        {


            Geocoder geocoder = new Geocoder(this);




                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_36dp)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mapOfferActivity.this, getString(R.string.write_a_place), Toast.LENGTH_SHORT).show();
                }





        }

    }







}




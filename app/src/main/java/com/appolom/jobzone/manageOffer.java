package com.appolom.jobzone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class manageOffer extends AppCompatActivity {




    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient locationProvider;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    public static double lat;
    public static double lng;
    public String topLat;
    public String topLng;
    public String lessLat;
    public int lessLng;
    public double latJob;
    public double lngJob;
    public static  TextView txt, txt2, txt3, txt4;

    private TextView title_offer;
    private TextView joboffert;
    private TextView salary_offer;
    private String category;
    private String detailText;
    private String money;
    private String phoneNumber;
    private String date;
    private String phoneContact;
    public String choose;



    private String uid;
    private String id;




    public static final String TAG = "Firebase";

    RecyclerView recyclerView;
    //DatabaseReference mdatabase;
    Query itemRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_offer);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_v);
        phoneContact = getIntent().getStringExtra("phoneContact");
        choose = getIntent().getStringExtra("choose");


       // Toast.makeText(manageOffer.this, choose, Toast.LENGTH_LONG).show();


        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            Log.d("MainActivity", "no permission");

            // ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        } else {

            locationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();

                        Log.d("MainActivity", "lat: " + lat + " ,long: " + lng);
                    }

                }
            });
        }

        createLocationrequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                //    Log.d("OfferListActivity", "lat: " + lat + " ,long: " + lng);
                }
            }
        };








        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.manage));

       // getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.ic_add_circle_outline_black_24dp);




        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(false);







        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        // Ensure correct menu item is selected (where the magic happens)
       // Menu menu = navigation.getMenu();
       // MenuItem menuItem = menu.getItem(0);
       // menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_offer:
                        Intent intent = new Intent(manageOffer.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(manageOffer.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(manageOffer.this, mapOfferActivity.class);
                        intent3.putExtra("lat", lat);
                        intent3.putExtra("lng", lng);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        };



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


//choose offer or demand to delete
if (new String(choose).equals("offer")) {
    itemRef = FirebaseDatabase.getInstance().getReference().child("offer").orderByChild("phoneNumber").equalTo(phoneContact);

}else{
    itemRef = FirebaseDatabase.getInstance().getReference().child("demand").orderByChild("phoneNumber").equalTo(phoneContact);
}


      //  itemRef = FirebaseDatabase.getInstance().getReference();


        itemRef.keepSynced(true);
// FireBase RecyclerAdapter
        FirebaseRecyclerAdapter<ObjectRecyclerView, OfferViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ObjectRecyclerView, OfferViewHolder>(
                        ObjectRecyclerView.class,
                        R.layout.my_cardview_delete,
                        OfferViewHolder.class,
                        itemRef


                )




                {
                    @Override
                    protected void populateViewHolder(final OfferViewHolder viewHolder, final ObjectRecyclerView model,
                                                      int position) {

                        final String offer_key = getRef(position).getKey();
                        id = offer_key;
                        viewHolder.setCategory(model.getCategory());
                        viewHolder.setDetailText(model.getDetailText());
                        viewHolder.setMoney(model.getMoney());
                        viewHolder.setPhoneNumber(model.getPhoneNumber());
                        phoneNumber = model.getPhoneNumber();
                        viewHolder.setdate(model.getdate());


                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (new String(choose).equals("offer")) {
                                    FirebaseDatabase.getInstance().getReference().child("Offer").child(offer_key).removeValue();
                                }else{
                                    FirebaseDatabase.getInstance().getReference().child("Demand").child(offer_key).removeValue();
                                }

                                Toast.makeText(manageOffer.this, getString(R.string.ready) , Toast.LENGTH_SHORT).show();



                            }
                        });





                    }

                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);





    }

/*
    public void setLocation(Location loc) {
        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    messageTextView2.setText("Mi direcci—n es: \n" + address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

*/



    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResult.length == 1 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);

            } else {
                //permission denied by user
            }
        }
    }

    @SuppressLint("RestrictedApi")
    void createLocationrequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }






    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        View mView;



        public OfferViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setCategory(String category) {
            txt = (TextView) mView.findViewById(R.id.category);
            txt.setText(category);
        }
        public void setDetailText(String detailText) {
            txt2 = (TextView) mView.findViewById(R.id.detailText);
            txt2.setText(detailText);
        }

        public void setMoney(String money) {
            txt3 = (TextView) mView.findViewById(R.id.money);
            txt3.setText(money);
        }

        public void setdate(String date) {
            txt4 = (TextView) mView.findViewById(R.id.date);
            txt4.setText(date);



        }

        public void setPhoneNumber(String phoneNumber) {



        }


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






    }



package com.appolom.jobzone;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class demandListActivity extends AppCompatActivity {





    public static double lat;
    public static double lng;
    public String topLat;
    public String topLng;
    public String lessLat;
    public int lessLng;
    public double latJob;
    public double lngJob;
    public static  TextView txt, txt2, txt3, txt4,txt5;

    private TextView title_offer;
    private TextView joboffert;
    private TextView salary_offer;
    private String category;
    private String detailText;
    private String money;
    private String name;
    private String phoneNumber;
    private String date;
    private String id;
    public ImageButton floatButton;
    private String choose;
    public String postalCode;




    public static final String TAG = "Firebase";

    RecyclerView recyclerView;
    //DatabaseReference mdatabase;
    DatabaseReference itemRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_v);


        lat = getIntent().getDoubleExtra("lat",0);
        lng = getIntent().getDoubleExtra("lng",0);


        floatButton = (ImageButton) findViewById(R.id.imageButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(demandListActivity.this, AddDemandActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivity(intent);
            }



        });




        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (lat != 0.0 && lng != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(lat, lng, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    postalCode = address.getPostalCode();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.demand));
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.ic_add_circle_outline_black_24dp);




        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(false);











        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_offer:
                        Intent intent = new Intent(demandListActivity.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(demandListActivity.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(demandListActivity.this, mapOfferActivity.class);
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



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query itemRef = rootRef.child("Demand").orderByChild("postalCode").equalTo(postalCode);


      //  itemRef = FirebaseDatabase.getInstance().getReference();


        itemRef.keepSynced(true);
// FireBase RecyclerAdapter
        FirebaseRecyclerAdapter<ObjectRecyclerView, DemandViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ObjectRecyclerView, DemandViewHolder>(
                        ObjectRecyclerView.class,
                        R.layout.my_cardview_demand,
                        DemandViewHolder.class,
                        itemRef
                ) {
                    @Override
                    protected void populateViewHolder(final DemandViewHolder viewHolder, final ObjectRecyclerView model,
                                                      int position) {

                        final String offer_key = getRef(position).getKey();
                        id = offer_key;
                        viewHolder.setCategory(model.getCategory());
                        viewHolder.setDetailText(model.getDetailText());
                        viewHolder.setPhoneNumber(model.getPhoneNumber());
                        viewHolder.setdate(model.getdate());
                        viewHolder.setName(model.getName());

                        final String phoneNumber = model.getPhoneNumber();
                        final String category = model.getCategory();
                        final String detailText = model.getDetailText();
                        final double latJob = model.getLat();
                        final double lngJob = model.getLng();
                        final String date = model.getdate();
                        final String name = model.getName();



                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //  goUni();
                                //  Toast.makeText(offerListActivity.this, offer_key, Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(demandListActivity.this, sms.class);

                                // intent.putExtra("offer_key", offer_key);
                                choose = "demand";
                                intent.putExtra("choose", choose);
                                intent.putExtra("lat", lat);
                                intent.putExtra("lng", lng);
                                intent.putExtra("category", category);
                                intent.putExtra("detailText", detailText);
                                intent.putExtra("money", money);
                                intent.putExtra("name", name);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("latJob", latJob);
                                intent.putExtra("lngJob", lngJob);
                                intent.putExtra("date", date);
                                startActivity(intent);




                            }
                        });




                    }

                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }





    public static class DemandViewHolder extends RecyclerView.ViewHolder {
        View mView;



        public DemandViewHolder(View itemView) {
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

        }

        public void setPhoneNumber(String phoneNumber) {

        }

        public void setdate(String date) {
                txt4 = (TextView) mView.findViewById(R.id.date);
                txt4.setText(date);
        }

        public void setName(String name) {
            txt5 = (TextView) mView.findViewById(R.id.name);
            txt5.setText(name);




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

    public void goUni(View view) {
        Intent intent = new Intent(this, mapUni.class);
       // intent.putExtra("uid", uid);
        phoneNumber = txt4.getText().toString();
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
      //  Toast.makeText(offerListActivity.this, uid, Toast.LENGTH_SHORT).show();

    }



    public void goControlOffer() {

        Intent intent = new Intent(this, loginManageOffer.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);

    }



    }

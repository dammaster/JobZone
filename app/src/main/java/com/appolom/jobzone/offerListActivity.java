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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class offerListActivity extends AppCompatActivity {





    public static double lat;
    public static double lng;
    public String topLat;
    public String topLng;
    public String lessLat;
    public int lessLng;

    public static  TextView txt, txt2, txt3, txt4;



    private TextView title_offer;
    private TextView joboffert;
    private TextView salary_offer;
    private String category;
    private String detailText;
    private String money;
    private String name;
    private String date;
    private String phoneNumber;
    private static String phoneContact;
    private String uid;
    private String latJob;
    private String lngJob;
    public ImageButton floatButton;
    private String choose;
    public String postalCode;
    public LinearLayout linearLayout;



    public static final String TAG = "Firebase";

    RecyclerView recyclerView;
    //DatabaseReference mdatabase;
    DatabaseReference itemRef;

    private String id;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_v);



        lat = getIntent().getDoubleExtra("lat",0);
        lng = getIntent().getDoubleExtra("lng",0);

        floatButton = (ImageButton) findViewById(R.id.imageButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(offerListActivity.this, AddOfferActivity.class);
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



       // Toast.makeText(offerListActivity.this, postalCode, Toast.LENGTH_LONG).show();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.search_job_offers));

       // getSupportActionBar().setDisplayShowHomeEnabled(true);
      //  getSupportActionBar().setIcon(R.drawable.ic_add_circle_outline_black_24dp);




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
                        Intent intent = new Intent(offerListActivity.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(offerListActivity.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(offerListActivity.this, mapOfferActivity.class);
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
         Query itemRef = rootRef.child("Offer").orderByChild("postalCode").equalTo(postalCode);




        itemRef.keepSynced(true);
// FireBase RecyclerAdapter
        FirebaseRecyclerAdapter<ObjectRecyclerView, OfferViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ObjectRecyclerView, OfferViewHolder>(
                        ObjectRecyclerView.class,
                        R.layout.my_cardview,
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
                        viewHolder.setdate(model.getdate());
                        viewHolder.setName(model.getName());

                        final String phoneNumber = model.getPhoneNumber();
                        final String category = model.getCategory();
                        final String detailText = model.getDetailText();
                        final String money = model.getMoney();
                        final double latJob = model.getLat();
                        final double lngJob = model.getLng();
                        final String date = model.getdate();
                        final String name = model.getName();



                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                              //  goUni();
                              //  Toast.makeText(offerListActivity.this, offer_key, Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(offerListActivity.this, sms.class);

                               // intent.putExtra("offer_key", offer_key);
                                choose = "offer";
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

        public void setName(String name) { }

        public void setPhoneNumber(String PhoneNumber) { }

        public void setLatJob(String latJob){}

        public void setLngJob(String latJob){}


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


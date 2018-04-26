package com.appolom.jobzone;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class sms extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Circle circle;
    public static double lat;
    public static double lng;
    public static double latJob;
    public static double lngJob;

    EditText etPhone, etMessage;
    Button btnSendSMS;
    private String category;
    private String detailText;
    private String money;
    private String name;
    private String date;
    private String choose;
    private String phoneNumber;
    TextView locate;
    private String locateString;

    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);




        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.contact));

        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_add_circle_outline_black_24dp);




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
                        Intent intent = new Intent(sms.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(sms.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(sms.this, mapOfferActivity.class);
                        intent3.putExtra("lat", lat);
                        intent3.putExtra("lng", lng);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        };



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);






        latJob = getIntent().getDoubleExtra("latJob", 0);
        lngJob = getIntent().getDoubleExtra("lngJob", 0);

        locate = (TextView) findViewById(R.id.locate);
        choose = getIntent().getStringExtra("choose");
        category = getIntent().getStringExtra("category");
        detailText = getIntent().getStringExtra("detailText");
        money = getIntent().getStringExtra("money");
        name = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        date = getIntent().getStringExtra("date");

        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        TextView categoryTextView = findViewById(R.id.category);
        categoryTextView.setText(category);

        TextView detailTextTextView = findViewById(R.id.detailText);
        detailTextTextView.setText(detailText);

        TextView moneyTextView = findViewById(R.id.money);
        moneyTextView.setText(money);

        TextView nameTextView = findViewById(R.id.name);
        nameTextView.setText(getString(R.string.contact_name)+ " " + name);

        TextView dateTextView = findViewById(R.id.date);
        dateTextView.setText(date);



       // etPhone = (EditText) findViewById(R.id.etPhone);

        etMessage = (EditText) findViewById(R.id.etMessage);
       // etMessage.requestFocus();



        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);

        sentPI = PendingIntent.getBroadcast(sms.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(sms.this, 0, new Intent(DELIVERED), 0);



        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                String message = etMessage.getText().toString();
                String telNr = phoneNumber = getIntent().getStringExtra("phoneNumber");

                if (ContextCompat.checkSelfPermission(sms.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(sms.this, new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                } else {
                    SmsManager sms = SmsManager.getDefault();

                    //phone - Recipient's phone number
                    //address - Service Center Address (null for default)
                    //message - SMS message to be sent
                    //piSent - Pending intent to be invoked when the message is sent
                    //piDelivered - Pending intent to be invoked when the message is delivered to the recipient
                    sms.sendTextMessage(telNr, null, message, sentPI, deliveredPI);
                }

                }catch(Exception e)
                {
                    Toast.makeText(sms.this, getString(R.string.write_the_message), Toast.LENGTH_LONG).show();
                }

            }


        });



        //Google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (latJob != 0.0 && lngJob != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(latJob, lngJob, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    locate.setText(address.getAddressLine(0));
                    locateString = locate.getText().toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
            public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's
                // stroke color.
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });






        // my location
           LatLng me = new LatLng(latJob, lngJob);
           mMap.moveCamera(CameraUpdateFactory.newLatLng(me));

           if(choose.equals("offer")) {
               mMap.addMarker(new MarkerOptions().position(me).title(locateString));
               mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
           }if(choose.equals("demand")) {
               // mMap.addMarker(new MarkerOptions().position(me));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(latJob, lngJob))
                        .radius(2000)
                        .strokeWidth(1)
                        .strokeColor(Color.LTGRAY)
                        .fillColor(Color.argb(128, 255, 0, 0))
                        .clickable(true));
           }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //The deliveredPI PendingIntent does not fire in the Android emulator.
        //You have to test the application on a real device to view it.
        //However, the sentPI PendingIntent works on both, the emulator as well as on a real device.

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, getString(R.string.sms_sent_successfully), Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong and there's no way to tell what, why or how.
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, getString(R.string.generic_failure), Toast.LENGTH_SHORT).show();
                        break;

                    //Your device simply has no cell reception. You're probably in the middle of
                    //nowhere, somewhere inside, underground, or up in space.
                    //Certainly away from any cell phone tower.
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, getString(R.string.no_service), Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong in the SMS stack, while doing something with a protocol
                    //description unit (PDU) (most likely putting it together for transmission).
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, getString(R.string.null_pdu), Toast.LENGTH_SHORT).show();
                        break;

                    //You switched your device into airplane mode, which tells your device exactly
                    //"turn all radios off" (cell, wifi, Bluetooth, NFC, ...).
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, getString(R.string.radio_off), Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, getString(R.string.sms_delivered), Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, getString(R.string.sms_not_delivered), Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //register the BroadCastReceivers to listen for a specific broadcast
        //if they "hear" that broadcast, it will activate their onReceive() method
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
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

package com.appolom.jobzone;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddOfferActivity extends AppCompatActivity {

    public static double lat;
    public static double lng;
    public static double latNew;
    public static double lngNew;



    public static final String TAG = "Firebase";

    DatabaseReference itemRef;
    EditText name;
    EditText detailText;
    EditText money;
    EditText codeText;
    int codeInput;
    private String category;
    private RadioGroup categories;
    private RadioButton checkedRadioButton;
    EditText locate;
    public String locateNew;
    public String postalCode;




//para enviar code

    EditText phoneNumber;
    int code;        ;
    Button btnSendSMS;

    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        lat = getIntent().getDoubleExtra("lat",0);
        lng = getIntent().getDoubleExtra("lng",0);
        locate = (EditText) findViewById(R.id.locate);
        // locate.setTag(locate);
        name = findViewById(R.id.name);
        detailText = findViewById(R.id.detailText);
        money = findViewById(R.id.money);
        phoneNumber = findViewById(R.id.phoneNumber);
        codeText = findViewById(R.id.codeText);
        categories = (RadioGroup) findViewById(R.id.categories);
        categories.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                 checkedRadioButton = (RadioButton) findViewById(checkedId);
                category = checkedRadioButton.getText().toString();

            }
        });







        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle("ADD JOB");



        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();


        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(false);




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        // Ensure correct menu item is selected (where the magic happens)
        //Menu menu = navigation.getMenu();
        //MenuItem menuItem = menu.getItem(2);
        //menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_offer:
                        Intent intent = new Intent(AddOfferActivity.this, offerListActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                    case R.id.navigation_demand:
                        Intent intent2 = new Intent(AddOfferActivity.this, demandListActivity.class);
                        intent2.putExtra("lat", lat);
                        intent2.putExtra("lng", lng);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_map:
                        Intent intent3 = new Intent(AddOfferActivity.this, mapOfferActivity.class);
                        intent3.putExtra("lat", lat);
                        intent3.putExtra("lng", lng);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        };


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        itemRef = database.getReference(getString(R.string.offer));

      //  itemRef.child("items").push();




        int id = categories.getCheckedRadioButtonId();




        //para enviar code

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);


        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);

        sentPI = PendingIntent.getBroadcast(AddOfferActivity.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(AddOfferActivity.this, 0, new Intent(DELIVERED), 0);

        code = (int) (Math.random()*(9999-1111 +1 )+1111);

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = String.valueOf(code);
                String telNr = phoneNumber.getText().toString();

                if (ContextCompat.checkSelfPermission(AddOfferActivity.this, android.Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AddOfferActivity.this, new String [] {android.Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
                else
                {
                    SmsManager sms = SmsManager.getDefault();

                    //phone - Recipient's phone number
                    //address - Service Center Address (null for default)
                    //message - SMS message to be sent
                    //piSent - Pending intent to be invoked when the message is sent
                    //piDelivered - Pending intent to be invoked when the message is delivered to the recipient
                    sms.sendTextMessage(telNr, null, message, sentPI, deliveredPI);
                }

            }
        });



        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (lat != 0.0 && lng != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(lat, lng, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                   // postalCode = address.getPostalCode();
                    locate.setText(address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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






           public void adreess(){

               /**
                * to get latitude and longitude of an address
                *
                * @param strAddress address string
                * @return lat and lng in comma separated string
                */



               Geocoder coder = new Geocoder(this, Locale.getDefault());
               List<Address> address;
               locateNew = locate.getText().toString();
               try {
                   address = coder.getFromLocationName(locateNew, 1);
                   if (address == null) {
                       return;
                   }
                   Address location = address.get(0);
                   latNew = location.getLatitude();
                   lngNew = location.getLongitude();
                   postalCode = location.getPostalCode();

                   // return lat + "," + lng;
               } catch (Exception e) {
                   return;
               }



           }

    //controla el codigo


        public void send (View view){

            try {

            codeInput = Integer.valueOf(codeText.getText().toString());

            if(code == codeInput)

            {
                 // new adreess
                adreess();

                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                String date = df.format(Calendar.getInstance().getTime());

        Item item = new Item(name.getText().toString(), category, detailText.getText().toString(), money.getText().toString(), phoneNumber.getText().toString(), latNew, lngNew,postalCode,date);
        itemRef.push().setValue(item);
        goOfferList();

    }else{
                Toast.makeText(AddOfferActivity.this,  getString(R.string.try_again) , Toast.LENGTH_LONG).show();

            }

            }catch(Exception e)
            {
                Toast.makeText(AddOfferActivity.this, getString(R.string.missing_a_field), Toast.LENGTH_LONG).show();
            }



        }






    public void goOfferList() {

        Intent intent = new Intent(this, offerListActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);

    }



    public void goControlOffer() {

        Intent intent = new Intent(this, loginManageOffer.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);

    }

// enviar code

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

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, getString(R.string.code_sent_successfully), Toast.LENGTH_SHORT).show();
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

                switch(getResultCode())
                {
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







}

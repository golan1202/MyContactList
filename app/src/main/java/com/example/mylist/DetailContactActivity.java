package com.example.mylist;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailContactActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_contact_activity);

        int contactPosition = getIntent().getIntExtra("contact",0);

        Contact contact =ContactManager.getInstance(this).getContact(contactPosition);

        TextView nameTv = findViewById(R.id.contact_name_output);
        TextView phoneTv = findViewById(R.id.contact_phone_output);
        ImageView photoIv = findViewById(R.id.contact_photo_output);
        final TextView emailTv = findViewById(R.id.contact_email_output);
        final TextView addressTv = findViewById(R.id.contact_Address_output);

        nameTv.setText(contact.getName());
        phoneTv.setText(contact.getPhone());
        photoIv.setImageBitmap(contact.getPhoto());
        emailTv.setText(contact.getEmail());
        addressTv.setText(contact.getAddress());

        phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = ((TextView)v).getText().toString();
                Intent intent= new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+text));
                startActivity(intent);
            }
        });
        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailTv.getText().toString()});
                intent.setType("text/html");
                startActivity(intent);
            }
        });

        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String address = addressTv.getText().toString();
                    intent.setData(Uri.parse("waze://?q=" + address));
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    if(isNetworkAvailable()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://id=com.waze"));
                        startActivity(intent);
                    }else { Toast.makeText(DetailContactActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();}
                }
            }
        });



    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

package com.example.mylist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddContactActivity extends AppCompatActivity {

    EditText nameEt,phoneEt,emailEt,addressEt;
    Bitmap bitmap;
    ImageView imageView;

    final int CAMERA_REQUEST=1;
    final  int RESULT_LOAD_IMG=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);

        nameEt = findViewById(R.id.name_input);
        phoneEt = findViewById(R.id.phone_input);
        imageView = findViewById(R.id.photo_result);
        emailEt = findViewById(R.id.email_input);
        addressEt = findViewById(R.id.address_input);

        Button takePicBtn= findViewById(R.id.pic_btn);
        Button takePicBtn2 = findViewById(R.id.pic_btn2);
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);

            }
        });

        takePicBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                bitmap=selectedImage;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddContactActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(AddContactActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                if(bitmap==null){
                    Toast.makeText(AddContactActivity.this,"Please choose a photo",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(nameEt.getText().toString().matches("") || phoneEt.getText().toString().matches("")){
                    Toast.makeText(AddContactActivity.this,"Please enter name and phone",Toast.LENGTH_SHORT).show();
                    break;
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle("please confirm")
                        .setMessage("Are you sure you want to save the contact?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(bitmap!=null) {
                                    Contact contact = new Contact(nameEt.getText().toString(), phoneEt.getText().toString(), bitmap,emailEt.getText().toString(),addressEt.getText().toString());
                                    ContactManager manager = ContactManager.getInstance(AddContactActivity.this);
                                    manager.addContact(contact);

                                    nameEt.setText("");
                                    phoneEt.setText("");
                                    addressEt.setText("");
                                    emailEt.setText("");
                                    imageView.setImageBitmap(null);
                                    Toast.makeText(AddContactActivity.this,"Contact Saved",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AddContactActivity.this,"Contact not saved",Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
            case R.id.action_back:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

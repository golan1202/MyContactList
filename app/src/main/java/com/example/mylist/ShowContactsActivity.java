package com.example.mylist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.Collections;

public class ShowContactsActivity extends Activity {
   ContactManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_contacts_activity);

        RecyclerView recyclerView = findViewById(R.id.contact_list);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        manager = ContactManager.getInstance(this);

        final ContactAdapter adapter = new ContactAdapter(manager.getContacts());

        recyclerView.setAdapter(adapter);

        adapter.setListener(new ContactAdapter.ContactsListener() {  // show contacts details in new screen
            @Override
            public void onContactClicked(int position, View view) {

                Intent intent = new Intent(ShowContactsActivity.this, DetailContactActivity.class);
                intent.putExtra("contact", position);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                Collections.swap(manager.getContacts(), viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder,final int i) {
                if (i == ItemTouchHelper.RIGHT || i == ItemTouchHelper.LEFT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowContactsActivity.this);
                    builder.setTitle("please confirm")
                            .setMessage("Are you sure you want to delete the contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    manager.removeContact(viewHolder.getAdapterPosition());
                                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                    Toast.makeText(ShowContactsActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            Toast.makeText(ShowContactsActivity.this, "Contact not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).show();

                }
            }


        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);





    }
    @Override
    protected void onPause() {
        manager.saveContacts();
        super.onPause();
    }
}

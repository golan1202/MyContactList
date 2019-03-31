package com.example.mylist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    interface ContactsListener{
        void onContactClicked(int position,View view);
    }

    ContactsListener listener;

    public void setListener(ContactsListener listener){
        this.listener=listener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView contactIv;
        TextView nameTv;
        TextView numberTv;
        TextView emailTv;
        TextView addressTv;

        public ContactViewHolder(View itemView){
            super(itemView);

            contactIv= itemView.findViewById(R.id.contact_image);
            nameTv= itemView.findViewById(R.id.contact_name);
            numberTv= itemView.findViewById(R.id.contact_phone);
            emailTv = itemView.findViewById(R.id.email_input);
            addressTv = itemView.findViewById(R.id.address_input);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null){
                        listener.onContactClicked(getAdapterPosition(),v);
                    }
                }
            });
        }


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_layout,viewGroup,false);
        ContactViewHolder holder = new ContactViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int i) {
        Contact contact= contacts.get(i);
        holder.contactIv.setImageBitmap(contact.getPhoto());
        holder.nameTv.setText(contact.getName());
        holder.numberTv.setText(contact.getPhone());


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}

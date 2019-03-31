package com.example.mylist;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ContactManager {

    public static ContactManager instance;

    private  Context context;
    private  ArrayList<Contact> contacts= new ArrayList<>();

    static final String FILE_NAME="contacts.dat";

    private ContactManager(Context context){
        this.context=context;

        try {
            FileInputStream fis= context.openFileInput(FILE_NAME);
            ObjectInputStream ois= new ObjectInputStream(fis);
            contacts= (ArrayList<Contact>)ois.readObject();

            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static ContactManager getInstance(Context context){
        if(instance==null)
            instance=new ContactManager(context);

        return instance;
    }
    public Contact getContact(int position){
        if(position<contacts.size())
            return contacts.get(position);

        return null;
    }

    public void addContact(Contact contact){
        contacts.add(contact);
        saveContacts();

    }

    public void removeContact(int position){
        if(position<contacts.size())
            contacts.remove(position);
        saveContacts();
    }

    public void saveContacts(){
        try {
            FileOutputStream fos= context.openFileOutput(FILE_NAME,Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(contacts);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<Contact> getContacts(){
        return contacts;
    }

}

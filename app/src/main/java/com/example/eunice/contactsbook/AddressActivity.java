package com.example.eunice.contactsbook;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;


public class AddressActivity extends AppCompatActivity implements ContactsFragment.ContactsFragmentListener,
 AddEditFragment.AddEditFragmentListener, DetailsFragment.DetailsFragmentListener{

    public static final String CONTACT_URI = "contact_uri";

    private ContactsFragment contactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

//        if (savedInstanceState != null && findViewById(R.id.fragmentContainer) != null){
            contactsFragment = new ContactsFragment();
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactsFragment).commit();

//        else {
//            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
//        }

    }

    @Override
    public void onContactSelected(Uri contactUri){
        if (findViewById(R.id.fragmentContainer) != null){
            displayContact(contactUri, R.id.fragmentContainer);
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onAddContact() {
        if (findViewById(R.id.fragmentContainer) != null){
            displayAddEditFragment(R.id.fragmentContainer, null);
        }

    }

    @Override
    public void onContactDeleted() {
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList();
    }

    @Override
    public void onEditContact(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null){
            displayAddEditFragment(R.id.fragmentContainer, contactUri);
        }
    }

    @Override
    public void onAddEditCompleted(Uri contactUri) {
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList();
    }

    private void displayContact(Uri contactUri, int viewID) {
        DetailsFragment detailsFragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(CONTACT_URI, contactUri);
        detailsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void displayAddEditFragment(int viewID, Uri contactUri) {
        AddEditFragment addEditFragment = new AddEditFragment();

        if (contactUri != null){
        Bundle args = new Bundle();
        args.putParcelable(CONTACT_URI, contactUri);
        addEditFragment.setArguments(args);}

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

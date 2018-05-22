package com.example.eunice.contactsbook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int CONTACT_LOADER = 0;
    private AddEditFragmentListener listener;
    private Uri contactUri;
    private boolean addingNewContact = true;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout streetTextInputLayout;
    private TextInputLayout cityTextInputLayout;
    private TextInputLayout stateTextInputLayout;
    private TextInputLayout zipTextInputLayout;
    private FloatingActionButton saveContactFAB;

    private CoordinatorLayout coordinatorLayout;


    public AddEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddEditFragmentListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface AddEditFragmentListener{
        void onAddEditCompleted(Uri contactUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_add_edit, container, false);
        nameTextInputLayout = (TextInputLayout)v.findViewById(R.id.nameTextInputLayout);
        nameTextInputLayout.getEditText().addTextChangedListener(nameChangedListener);
        phoneTextInputLayout = (TextInputLayout)v.findViewById(R.id.phoneTextInputLayout);
        emailTextInputLayout = (TextInputLayout)v.findViewById(R.id.emailTextInputLayout);
        streetTextInputLayout = (TextInputLayout)v.findViewById(R.id.streetTextInputLayout);
        cityTextInputLayout = (TextInputLayout)v.findViewById(R.id.cityTextInputLayout);
        stateTextInputLayout = (TextInputLayout)v.findViewById(R.id.stateTextInputLayout);
        zipTextInputLayout = (TextInputLayout)v.findViewById(R.id.zipTextInputLayout);

        saveContactFAB = (FloatingActionButton)v.findViewById(R.id.saveFAB);
        saveContactFAB.setOnClickListener(saveContactButtonClicked);
        updateSaveButtonFAB();

        coordinatorLayout = (CoordinatorLayout)v.findViewById(R.id.coordinatorLayout);

        Bundle args = getArguments();

        if (args != null){
            addingNewContact = false;
            contactUri = args.getParcelable(AddressActivity.CONTACT_URI);
        }

        if (contactUri != null){
            getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        }
        return v;
    }

    private final TextWatcher nameChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateSaveButtonFAB();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateSaveButtonFAB(){
        String input = nameTextInputLayout.getEditText().getText().toString();

        if (input.trim().length() != 0)
            saveContactFAB.show();
        else
            saveContactFAB.hide();
    }

    private final View.OnClickListener saveContactButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getView().getWindowToken(), 0);
            saveContact();
        }
    };

    private void saveContact(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseDescription.Contact.COLUMN_NAME, nameTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_PHONE, phoneTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_EMAIL, emailTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_STREET, streetTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_CITY, cityTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_STATE, stateTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_ZIP, zipTextInputLayout.getEditText().getText().toString());

        if (addingNewContact){
            Uri newContactUri = getActivity().getContentResolver().insert(DatabaseDescription.Contact.CONTENT_URI, contentValues);

            if (newContactUri != null){
                Snackbar.make(coordinatorLayout, R.string.contact_added, Snackbar.LENGTH_LONG).show();
                listener.onAddEditCompleted(newContactUri);
            }else {
                Snackbar.make(coordinatorLayout, R.string.contact_not_added, Snackbar.LENGTH_LONG).show();
            }
        }else {
            int updatedRows = getActivity().getContentResolver().update(contactUri, contentValues, null, null);

            if (updatedRows > 0){
                listener.onAddEditCompleted(contactUri);
                Snackbar.make(coordinatorLayout, R.string.contact_updated, Snackbar.LENGTH_LONG);
            }else {
                Snackbar.make(coordinatorLayout, R.string.contact_not_updated, Snackbar.LENGTH_LONG);
            }
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case CONTACT_LOADER:
                return new CursorLoader(getActivity(), contactUri, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()){
            int nameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_PHONE);
            int emailIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_EMAIL);
            int streetIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STREET);
            int cityIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_CITY);
            int stateIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STATE);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);

            nameTextInputLayout.getEditText().setText(data.getString(nameIndex));
            phoneTextInputLayout.getEditText().setText(data.getString(phoneIndex));
            emailTextInputLayout.getEditText().setText(data.getString(emailIndex));
            streetTextInputLayout.getEditText().setText(data.getString(streetIndex));
            cityTextInputLayout.getEditText().setText(data.getString(cityIndex));
            stateTextInputLayout.getEditText().setText(data.getString(stateIndex));
            zipTextInputLayout.getEditText().setText(data.getString(zipIndex));

            updateSaveButtonFAB();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

package com.example.eunice.contactsbook;


import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CONTACT_LOADER = 0;
    private DetailsFragmentListener listener;
    private Uri contactUri;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView streetTextView;
    private TextView cityTextView;
    private TextView stateTextView;
    private TextView zipTextView;
    private DeleteFragment confirmDelete;


    public DetailsFragment() {
        // Required empty public constructor
    }

    public interface DetailsFragmentListener{
        void onContactDeleted();

        void onEditContact(Uri contactUri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailsFragmentListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null){
            contactUri = args.getParcelable(AddressActivity.CONTACT_URI);
        }
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        nameTextView = (TextView)v.findViewById(R.id.nameTextView);
        phoneTextView = (TextView)v.findViewById(R.id.phoneTextView);
        emailTextView = (TextView)v.findViewById(R.id.emailTextView);
        streetTextView = (TextView)v.findViewById(R.id.streetTextView);
        cityTextView = (TextView)v.findViewById(R.id.cityTextView);
        stateTextView = (TextView)v.findViewById(R.id.stateTextView);
        zipTextView = (TextView)v.findViewById(R.id.zipTextView);

        confirmDelete = new DeleteFragment();
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionEdit:
                listener.onEditContact(contactUri);
                return true;
            case R.id.actionDelete:
                deleteContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteContact(){
        Runnable confirm = new Runnable() {
            @Override
            public void run() {
                if (getArguments() != null && getActivity() != null && getActivity().getContentResolver() != null){
                contactUri = getArguments().getParcelable(AddressActivity.CONTACT_URI);
                getActivity().getContentResolver().delete(contactUri, null, null);
                listener.onContactDeleted();}
            }
        };
        confirmDelete.show(getFragmentManager(), "Confirm delete? ");
        confirmDelete.setConfirm(confirm);


    }




//    private final DialogFragment confirmDelete = new DialogFragment(){
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle(R.string.confirm_title).setMessage(R.string.confirm_message)
//                    .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            getActivity().getContentResolver().delete(contactUri, null, null);
//                            listener.onContactDeleted();
//                        }
//                    });
//            builder.setNegativeButton(R.string.button_cancel, null);
//            return builder.create();
//        }
//    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;

        switch (id){
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(), contactUri, null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
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

            nameTextView.setText(data.getString(nameIndex));
            phoneTextView.setText(data.getString(phoneIndex));
            emailTextView.setText(data.getString(emailIndex));
            streetTextView.setText(data.getString(streetIndex));
            cityTextView.setText(data.getString(cityIndex));
            stateTextView.setText(data.getString(stateIndex));
            zipTextView.setText(data.getString(zipIndex));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

package com.example.eunice.contactsbook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Eunice on 4/22/2018.
 */

public class AddressBookContentProvider extends ContentProvider {
    private AddressBookDatabaseHelper databaseHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_CONTACT = 1;
    private static final int CONTACTS = 2;


    static {
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME + "/#", ONE_CONTACT);

        uriMatcher.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME, CONTACTS);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new AddressBookDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                queryBuilder.appendWhere(DatabaseDescription.Contact._ID + "=" + uri.getLastPathSegment());
                break;
            case CONTACTS:
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);

        }

        Cursor cursor = queryBuilder.query(databaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri newContactUri = null;

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                long rowId = databaseHelper.getWritableDatabase().insert(DatabaseDescription.Contact.TABLE_NAME, null, values);

                if (rowId > 0) {
                    newContactUri = DatabaseDescription.Contact.buildContactUri(rowId);
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    throw new SQLException(getContext().getString(R.string.insert_failed) + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);

        }
        return newContactUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)){
            case ONE_CONTACT:
                String id = uri.getLastPathSegment();

                numberOfRowsDeleted = databaseHelper.getWritableDatabase().delete(
                        DatabaseDescription.Contact.TABLE_NAME, DatabaseDescription.Contact._ID
                        + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_delete_uri) + uri);
        }
        if (numberOfRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numberOfRowsUpdated;

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                String id = uri.getLastPathSegment();

                numberOfRowsUpdated = databaseHelper.getWritableDatabase().update(
                        DatabaseDescription.Contact.TABLE_NAME, values, DatabaseDescription.Contact._ID
                                + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_update_uri) + uri);
        }

        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRowsUpdated;
    }
}
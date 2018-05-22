package com.example.eunice.contactsbook;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Eunice on 4/22/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private long rowID;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(DatabaseDescription.Contact.buildContactUri(rowID));
                }
            });
        }

        public void setRowID (long rowID){
            this.rowID = rowID;
        }
    }

    private Cursor cursor = null;
    private final ContactClickListener clickListener;

    public ContactsAdapter(ContactClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public interface ContactClickListener{
        void onClick(Uri contactUri);
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Contact._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME)));
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }
}

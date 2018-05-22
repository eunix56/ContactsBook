package com.example.eunice.contactsbook;


import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteFragment extends DialogFragment {
    private Runnable mConfirm = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Runnable mCancel = new Runnable() {
        @Override
        public void run() {

        }
    };

    public DeleteFragment() {
        // Required empty public constructor
    }

    public void setConfirm (Runnable confirm){
        mConfirm = confirm;
    }

    public void setCancel(Runnable cancel){
        mCancel = cancel;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm_title).setMessage(R.string.confirm_message).
                setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConfirm.run();
                    }
                }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        return builder.create();
    }


}

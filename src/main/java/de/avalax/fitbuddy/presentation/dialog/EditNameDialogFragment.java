package de.avalax.fitbuddy.presentation.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.avalax.fitbuddy.R;

public class EditNameDialogFragment extends DialogFragment {
    private static final String ARGS_NAME = "name";
    private static final String ARGS_HINT = "hint";

    private EditText nameEditText;
    private DialogListener listener;

    public static EditNameDialogFragment newInstance(String name, String hint) {
        EditNameDialogFragment fragment = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_NAME, name);
        args.putString(ARGS_HINT, hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogFragment.DialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_name, null);
        String name = getArguments().getString(ARGS_NAME);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        nameEditText.setText(name);
        nameEditText.setHint(getArguments().getString(ARGS_HINT));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.dialog_change_name)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(EditNameDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditNameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public String getName() {
        return nameEditText.getText().toString();
    }

    public interface DialogListener {
        void onDialogPositiveClick(EditNameDialogFragment editNameDialogFragment);
    }
}
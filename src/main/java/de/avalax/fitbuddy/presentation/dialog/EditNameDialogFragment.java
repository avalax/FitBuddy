package de.avalax.fitbuddy.presentation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        View view = inflater.inflate(R.layout.fragment_edit_name, container, false);
        String name = getArguments().getString(ARGS_NAME);
        Button button = (Button) view.findViewById(R.id.done_button);
        getDialog().setTitle(R.string.dialog_change_name);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        nameEditText.setText(name);
        nameEditText.setHint(getArguments().getString(ARGS_HINT));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onDialogPositiveClick(EditNameDialogFragment.this);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public String getName() {
        return nameEditText.getText().toString();
    }

    public interface DialogListener {
        void onDialogPositiveClick(EditNameDialogFragment editNameDialogFragment);
    }
}
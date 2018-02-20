package de.avalax.fitbuddy.presentation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.exception.DialogListenerException;

public class SupportDialogFragment extends AppCompatDialogFragment {
    private DialogListener listener;

    public static SupportDialogFragment newInstance() {
        SupportDialogFragment fragment = new SupportDialogFragment();
        fragment.setStyle(AppCompatDialogFragment.STYLE_NORMAL, R.style.AppCompatDialogWithTitle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new DialogListenerException(context.toString()
                    + " must implement EditRepsDialogFragment.DialogListener", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.dialog_support_title);
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        Button buttonSupport = view.findViewById(R.id.button_support);
        Button buttonCancel = view.findViewById(R.id.button_cancel);

        buttonSupport.setOnClickListener(onClickListener -> {
            listener.onDialogPositiveClick(this);
            getDialog().dismiss();
        });

        buttonCancel.setOnClickListener(onClickListener -> {
            getDialog().dismiss();
        });

        return view;
    }

    public interface DialogListener {
        void onDialogPositiveClick(SupportDialogFragment editRepsDialogFragment);
    }
}

package de.avalax.fitbuddy.presentation.edit.set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;

public class EditSetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_set, container, false);
        view.findViewById(R.id.set_weight).setOnClickListener(v -> changeWeight());

        view.findViewById(R.id.set_reps).setOnClickListener(v -> changeReps());
        return view;
    }

    private void changeWeight() {
        double weight = 1;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
    }

    private void changeReps() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int reps = 12;
        EditRepsDialogFragment.newInstance(reps).show(fm, "fragment_edit_reps");
    }
}

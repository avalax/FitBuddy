package de.avalax.fitbuddy.presentation.edit.set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseApplicationService;

public class EditSetFragment extends Fragment {

    private Set set;

    @Inject
    protected EditExerciseApplicationService editExerciseApplicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_set, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);

        view.findViewById(R.id.set_weight).setOnClickListener(v -> changeWeight());
        view.findViewById(R.id.set_reps).setOnClickListener(v -> changeMaxReps());
        set = (Set) getActivity().getIntent().getSerializableExtra("set");
        TextView repsTextView = view.findViewById(R.id.set_reps_text_view);
        repsTextView.setText(editExerciseApplicationService.title(set));
        TextView weightTextView = view.findViewById(R.id.set_weight_text_view);
        weightTextView.setText(editExerciseApplicationService.subtitle(set));
        return view;
    }

    private void changeWeight() {
        double weight = set.getWeight();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
    }

    private void changeMaxReps() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int reps = set.getMaxReps();
        EditRepsDialogFragment.newInstance(reps).show(fm, "fragment_edit_max_reps");
    }
}

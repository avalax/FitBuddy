package de.avalax.fitbuddy.presentation.edit.set;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;

public class EditSetViewModel extends ViewModel {
    private static DecimalFormat decimalFormat;

    private MutableLiveData<Set> liveDataSet;
    private Set set;

    static {
        decimalFormat = new DecimalFormat("0.###");
    }

    public void init(Set set) {
        this.set = set;
        this.liveDataSet = new MutableLiveData<>();
        this.liveDataSet.setValue(set);
    }

    public void setReps(int reps) {
        set.setMaxReps(reps);
        liveDataSet.setValue(set);
    }

    public void setWeight(double weight) {
        set.setWeight(weight);
        liveDataSet.setValue(set);
    }

    public LiveData<Set> getSet() {
        return liveDataSet;
    }

    @BindingAdapter("reps")
    public static void repsFromSet(TextView textView, Set set) {
        String label = textView.getContext().getResources()
                .getQuantityString(R.plurals.reps_label, set.getMaxReps(), set.getMaxReps());
        textView.setText(label);
    }

    @BindingAdapter("weight")
    public static void setWeightFromSet(TextView textView, Set set) {
        int quantity = set.getWeight() == 0 ? 0 : 1;
        String value = decimalFormat.format(set.getWeight());
        String label;
        if (quantity == 0) {
            label = textView.getContext().getResources()
                    .getString(R.string.default_set_weight);
        } else {
            label = textView.getContext().getResources()
                    .getQuantityString(R.plurals.weight_label, quantity, value);
        }
        textView.setText(label);
    }
}


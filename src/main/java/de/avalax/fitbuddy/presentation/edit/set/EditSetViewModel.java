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
    private MutableLiveData<Set> liveDataSet;
    private Set set;

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
}


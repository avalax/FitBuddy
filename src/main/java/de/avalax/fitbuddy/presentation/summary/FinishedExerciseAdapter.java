package de.avalax.fitbuddy.presentation.summary;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;

public class FinishedExerciseAdapter
        extends RecyclerView.Adapter<FinishedExerciseAdapter.ExerciseViewHolder> {
    private FinishedExerciseViewHelper finishedExerciseViewHelper;
    private List<FinishedExercise> finishedExercises;
    private Activity activity;

    public FinishedExerciseAdapter(Activity activity,
                                   FinishedExerciseViewHelper finishedExerciseViewHelper,
                                   List<FinishedExercise> finishedExercises) {
        super();
        this.activity = activity;
        this.finishedExerciseViewHelper = finishedExerciseViewHelper;
        this.finishedExercises = finishedExercises;
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        FinishedExercise finishedExercise = finishedExercises.get(position);
        String title = finishedExercise.getName();
        holder.getTitleTextView().setText(title);
        holder.getSubtitleTextView().setText(finishedExerciseViewHelper.subtitle(finishedExercise));
        BarData barData = getBarData(finishedExercise);
        holder.getBarChart().setData(barData);
        holder.getBarChart().getAxisLeft().setAxisMinimum(0);
        holder.getBarChart().getAxisLeft().setAxisMaximum(getMaxReps(finishedExercise));
        holder.getBarChart().invalidate();
    }

    private int getMaxReps(FinishedExercise finishedExercise) {
        int maxReps = 0;
        for (FinishedSet set : finishedExercise.getSets()) {
            if (set.getMaxReps() > maxReps) {
                maxReps = set.getMaxReps();
            }
        }
        return maxReps;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.card_finished_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    private BarData getBarData(FinishedExercise finishedExercise) {
        List<FinishedSet> sets = finishedExercise.getSets();
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sets.size(); i++) {
            int reps = sets.get(i).getReps();
            int maxReps = sets.get(i).getMaxReps();
            int openReps = maxReps - reps;
            entries.add(new BarEntry(i, new float[]{reps, openReps}));
        }
        BarDataSet set = new BarDataSet(entries, "sets");
        int textColor = activity.getResources().getColor(R.color.primaryTextColor);
        set.setValueTextColor(textColor);
        set.setValueTextSize(12);
        set.setColors(getColors());
        BarData barData = new BarData(set);
        barData.setValueFormatter(new ZeroValueFormatter());
        return barData;
    }

    private int[] getColors() {
        return new int[]{
                activity.getResources().getColor(R.color.primaryColor),
                activity.getResources().getColor(R.color.secondaryColor)
        };
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return finishedExercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final BarChart barChart;
        private final TextView subtitleTextView;

        ExerciseViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_title);
            subtitleTextView = v.findViewById(R.id.item_subtitle);
            barChart = v.findViewById(R.id.item_chart);
            int backgroundColor = v.getResources().getColor(R.color.appBarColor);
            initBarChart(backgroundColor);
        }

        private void initBarChart(int backgroundColor) {
            barChart.setFitBars(true);
            barChart.getLegend().setEnabled(false);
            barChart.getDescription().setEnabled(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(false);
            barChart.setHighlightFullBarEnabled(false);
            barChart.setFocusable(false);
            barChart.setDragEnabled(false);
            barChart.setHighlightPerDragEnabled(false);
            barChart.setHighlightPerTapEnabled(false);
            barChart.setTouchEnabled(false);
            barChart.setClickable(false);
            barChart.setMinOffset(0);
            barChart.setBackgroundColor(backgroundColor);

            barChart.getAxisLeft().setEnabled(false);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisRight().setLabelCount(2);
            barChart.getXAxis().setEnabled(false);
            barChart.setAutoScaleMinMaxEnabled(true);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        BarChart getBarChart() {
            return barChart;
        }

        public TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }

    private static class ZeroValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        ZeroValueFormatter() {
            mFormat = new DecimalFormat("###.###");
        }

        @Override
        public String getFormattedValue(
                float value,
                Entry entry,
                int dataSetIndex,
                ViewPortHandler viewPortHandler) {
            if (value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }
}

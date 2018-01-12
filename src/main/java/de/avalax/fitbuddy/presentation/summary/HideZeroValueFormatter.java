package de.avalax.fitbuddy.presentation.summary;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

class HideZeroValueFormatter implements IValueFormatter {
    private DecimalFormat df = new DecimalFormat("###.###");

    @Override
    public String getFormattedValue(
            float value,
            Entry entry,
            int dataSetIndex,
            ViewPortHandler viewPortHandler) {
        if (value > 0) {
            return df.format(value);
        } else {
            return "";
        }
    }
}
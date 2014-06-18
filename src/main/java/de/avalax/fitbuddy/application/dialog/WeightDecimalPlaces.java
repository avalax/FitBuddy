package de.avalax.fitbuddy.application.dialog;

import java.util.Map;
import java.util.TreeMap;

public class WeightDecimalPlaces {
    private static final Map<String, Double> decimalPlaces;

    static {
        decimalPlaces = new TreeMap<>();
        decimalPlaces.put("0", 0.0);
        decimalPlaces.put("125", 0.125);
        decimalPlaces.put("250", 0.25);
        decimalPlaces.put("375", 0.375);
        decimalPlaces.put("500", 0.5);
        decimalPlaces.put("625", 0.625);
        decimalPlaces.put("750", 0.75);
        decimalPlaces.put("875", 0.875);
    }

    public String[] getLabels() {
        return decimalPlaces.keySet().toArray(new String[decimalPlaces.size()]);
    }

    public Double getWeight(int position) {
        Double[] doubleValues = decimalPlaces.values().toArray(new Double[decimalPlaces.size()]);
        return doubleValues[position];
    }

    public int getPosition(double weight) {
        Double[] doubleValues = decimalPlaces.values().toArray(new Double[decimalPlaces.size()]);
        for (int i = 0; i < doubleValues.length; i++) {
            if (doubleValues[i] == weight) {
                return i;
            }
        }
        return 0;
    }
}

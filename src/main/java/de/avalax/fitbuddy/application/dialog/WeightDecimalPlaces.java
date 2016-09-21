package de.avalax.fitbuddy.application.dialog;

import java.util.Map;
import java.util.TreeMap;

public class WeightDecimalPlaces {
    private static final Map<String, Double> DECIMAL_PLACES;

    static {
        DECIMAL_PLACES = new TreeMap<>();
        DECIMAL_PLACES.put("0", 0.0);
        DECIMAL_PLACES.put("125", 0.125);
        DECIMAL_PLACES.put("250", 0.25);
        DECIMAL_PLACES.put("375", 0.375);
        DECIMAL_PLACES.put("500", 0.5);
        DECIMAL_PLACES.put("625", 0.625);
        DECIMAL_PLACES.put("750", 0.75);
        DECIMAL_PLACES.put("875", 0.875);
    }

    public String[] getLabels() {
        return DECIMAL_PLACES.keySet().toArray(new String[DECIMAL_PLACES.size()]);
    }

    public Double getWeight(int position) {
        Double[] doubleValues = DECIMAL_PLACES.values().toArray(new Double[DECIMAL_PLACES.size()]);
        return doubleValues[position];
    }

    public int getPosition(double weight) {
        Double[] doubleValues = DECIMAL_PLACES.values().toArray(new Double[DECIMAL_PLACES.size()]);
        for (int i = 0; i < doubleValues.length; i++) {
            if (doubleValues[i] == weight) {
                return i;
            }
        }
        return 0;
    }
}

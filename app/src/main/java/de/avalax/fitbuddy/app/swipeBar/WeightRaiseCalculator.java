package de.avalax.fitbuddy.app.swipeBar;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class WeightRaiseCalculator {
    private Map<Integer, Double> weightRaiseMapping;

    public WeightRaiseCalculator() {
        weightRaiseMapping = new HashMap<Integer, Double>();
        weightRaiseMapping.put(0, 0.0);
        weightRaiseMapping.put(1, 0.25);
        weightRaiseMapping.put(2, 0.5);
        weightRaiseMapping.put(3, 0.75);
        weightRaiseMapping.put(4, 1.25);
        weightRaiseMapping.put(5, 2.5);
        weightRaiseMapping.put(6, 5.0);
        weightRaiseMapping.put(7, 7.5);
        weightRaiseMapping.put(8, 10.0);
        weightRaiseMapping.put(9, 12.5);
        weightRaiseMapping.put(10, 15.0);
        weightRaiseMapping.put(11, 20.0);
    }

    public double calculate(double currentWeightRaise, int modification) {
        int key = getKey(currentWeightRaise) + modification;
        if (key >= weightRaiseMapping.size()) {
            return weightRaiseMapping.get(weightRaiseMapping.size()-1);
        }
        if (!weightRaiseMapping.containsKey(key)) {
            return weightRaiseMapping.get(0);
        }
        return weightRaiseMapping.get(key);
    }

    private int getKey(double currentWeightRaise) {
        for (Map.Entry<Integer, Double> entry : weightRaiseMapping.entrySet()) {
            if (entry.getValue() == currentWeightRaise) {
                return entry.getKey();
            }
        }
        return 0;
    }
}

package softwaredesign.helperclasses;

public class MinMaxValue {

    public static Double findMax(Double[] values) {
        Double maxValue = values[0];
        for (int i = 1; i < values.length; i++){
            if (values[i] > maxValue) {
                maxValue = values[i];
            }
        }
        return maxValue;
    }

    public static Double findMin(Double[] values) {
        Double minValue = values[0];
        for (int i = 1; i < values.length; i++){
            if (values[i] < minValue) {
                minValue = values[i];
            }
        }
        return minValue;
    }
}

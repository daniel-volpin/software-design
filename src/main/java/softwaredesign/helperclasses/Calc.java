package softwaredesign.helperclasses;

public class Calc {
    public static Double findMax(Double[] values) {
        Double maxValue = values[0];
        for (Double value : values){
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public static Double findMin(Double[] values) {
        Double minValue = values[0];
        for (Double value : values){
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    public static Double findSum(Double[] values) {
        Double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        return sum;
    }
}

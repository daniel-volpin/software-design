package softwaredesign.helperclasses;

public class MinMaxValue {

    public static Double findMaxLat(Double[] latCoordinates) {
        Double maxLat = latCoordinates[0];
        for (int i = 1; i < latCoordinates.length; i++){
            if (latCoordinates[i] > maxLat) {
                maxLat = latCoordinates[i];
            }
        }
        return maxLat;
    }

    public static Double findMaxLong(Double[] longCoordinates) {
        Double maxLon = longCoordinates[0];
        for (int i = 1; i < longCoordinates.length; i++){
            if (longCoordinates[i] > maxLon) {
                maxLon = longCoordinates[i];
            }
        }
        return maxLon;
    }

    public static Double findMinLat(Double[] latCoordinates) {
        Double minLat = latCoordinates[0];
        for (int i = 1; i < latCoordinates.length; i++){
            if (latCoordinates[i] < minLat) {
                minLat = latCoordinates[i];
            }
        }
        return minLat;
    }

    public static Double findMinLong(Double[] longCoordinates) {
        Double minLong = longCoordinates[0];
        for (int i = 1; i < longCoordinates.length; i++){
            if (longCoordinates[i] < minLong) {
                minLong = longCoordinates[i];
            }
        }
        return minLong;
    }

}

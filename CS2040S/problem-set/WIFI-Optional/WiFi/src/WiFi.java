import java.util.Arrays;

class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        double maxDiff = 0;
        for (int i = 0; i < houses.length-1; i++) {
            maxDiff = Math.max(maxDiff, Math.abs(houses[i] - houses[i+1]));
        }
        while (coverable(houses, numOfAccessPoints, maxDiff/2)) {
            maxDiff /= 2;
        }

        return maxDiff;
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        distance *= 2;
        double curr = distance + houses[0];
        for (int i = 0; i < houses.length; i++) {
            if  (houses[i] > curr) {
                curr += distance + houses[i];
                numOfAccessPoints--;
            }
        }

        return numOfAccessPoints >= 1;
    }
}

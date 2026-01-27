public class FuzzyNumber {

    /**
     * Розраховує інтервал [min, max] для гауссової функції приналежності
     * 
     * @param center чітке значення (центр)
     * @param alpha  рівень значущості
     * @return масив {min, max}
     */
    public static double[] getInterval(double center, double alpha) {
        // Формула з Розділу 2.2: ((x - center)/center)^2 = (1/alpha - 1) / 2
        double term = (1.0 / alpha - 1.0) / 2.0;
        double deviation = Math.sqrt(term); // відносне відхилення

        double min = center * (1 - deviation);
        double max = center * (1 + deviation);
        return new double[] { min, max };
    }
}

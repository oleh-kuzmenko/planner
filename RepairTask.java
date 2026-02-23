public class RepairTask {

    private int id;
    private double[] resourceCost; // Профіль витрат (на 3 тижні)
    private int earlyStart;
    private int lateStart;
    private int startWeek; // Змінна рішення

    public RepairTask(int id, double[] resourceCost, int earlyStart, int lateStart) {
        this.id = id;
        this.resourceCost = resourceCost;
        this.earlyStart = earlyStart;
        this.lateStart = lateStart;
    }

    public double getCostAtWeek(int currentWeek, Scenario scenario) {
        if (startWeek == 0)
            return 0;
        int weekIndex = currentWeek - startWeek;

        if (weekIndex >= 0 && weekIndex < resourceCost.length) {
            double baseVal = resourceCost[weekIndex];
            // Використання класу FuzzyNumber для розрахунку меж
            return switch (scenario) {
                case CRISP -> baseVal;
                case FUZZY_OPTIMISTIC -> FuzzyNumber.getInterval(baseVal, 0.8)[0];
                case FUZZY_PESSIMISTIC -> FuzzyNumber.getInterval(baseVal, 0.8)[1];
            };
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getEarlyStart() {
        return earlyStart;
    }

    public int getLateStart() {
        return lateStart;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }
}

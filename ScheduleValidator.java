import java.util.List;

public class ScheduleValidator {
    
    /**
     * Перевіряє, чи задовольняє план обмеженням (імітація перевірки рядків
     * симплекс-таблиці)
     */
    public static boolean checkConstraints(List<RepairTask> tasks, int upToIndex, double limit, Scenario sc) {
        // Перевірка 8-10 тижнів планування
        for (int k = 1; k <= 10; k++) {
            double sumUsage = 0;
            // Сумуємо витрати всіх активних агрегатів
            for (int i = 0; i <= upToIndex; i++) {
                sumUsage += tasks.get(i).getCostAtWeek(k, sc);
            }
            // Якщо хоч в один тиждень сума > ліміту, обмеження порушено
            if (sumUsage > limit + 0.001) { // 0.001 для похибки double
                return false;
            }
        }
        return true;
    }
}

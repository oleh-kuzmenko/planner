import java.util.List;

public class Solver {
    private List<RepairTask> tasks;
    private double resourceLimit;
    private Scenario scenario;

    public Solver(List<RepairTask> tasks, double resourceLimit, Scenario scenario) {
        this.tasks = tasks;
        this.resourceLimit = resourceLimit;
        this.scenario = scenario;
    }

    public boolean solve() {
        return backtrack(0);
    }

    // Метод гілок та меж (адаптований для бінарних змінних)
    private boolean backtrack(int taskIndex) {
        // Якщо дійшли до кінця списку - розв'язок знайдено
        if (taskIndex == tasks.size()) {
            return true;
        }

        RepairTask currentTask = tasks.get(taskIndex);

        // Перебір можливих тижнів початку (від ES до LS)
        for (int week = currentTask.getEarlyStart(); week <= currentTask.getLateStart(); week++) {
            currentTask.setStartWeek(week);

            // Використання ScheduleValidator для перевірки обмежень
            if (ScheduleValidator.checkConstraints(tasks, taskIndex, resourceLimit, scenario)) {
                // Рекурсивний крок
                if (backtrack(taskIndex + 1)) {
                    return true;
                }
            }
        }

        // Якщо жоден варіант не підійшов - відкат (Backtracking)
        currentTask.setStartWeek(0);
        return false;
    }
}

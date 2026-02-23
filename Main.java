import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== СИСТЕМА КАЛЕНДАРНОГО ПЛАНУВАННЯ ===");

            List<RepairTask> tasks = new ArrayList<>();
            tasks.add(new RepairTask(1, new double[] { 4, 6, 3 }, 1, 4));
            tasks.add(new RepairTask(2, new double[] { 3, 2, 5 }, 1, 3));
            tasks.add(new RepairTask(3, new double[] { 7, 1, 1 }, 2, 5));
            tasks.add(new RepairTask(4, new double[] { 1, 3, 6 }, 2, 6));
            tasks.add(new RepairTask(5, new double[] { 5, 3, 4 }, 3, 5));

            System.out.print("Введіть ліміт ресурсів Tk: ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Помилка! Введіть число.");
                scanner.next();
            }
            double limitTk = scanner.nextDouble();

            if (limitTk < 0) {
                System.out.println("Ліміт не може бути від'ємним.");
                return;
            }

            System.out.println("-> Розрахунок для ліміту: " + limitTk);

            // Масив сценаріїв для послідовного виконання
            Scenario[] scenarios = {
                    Scenario.CRISP,
                    Scenario.FUZZY_OPTIMISTIC,
                    Scenario.FUZZY_PESSIMISTIC
            };

            for (Scenario sc : scenarios) {
                solveAndPrint(tasks, limitTk, sc);
            }

            System.out.println("\nРоботу завершено.");
        }
    }

    private static void solveAndPrint(List<RepairTask> tasks, double limit, Scenario scenario) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" СЦЕНАРІЙ: " + scenario.name());
        System.out.println("=".repeat(60));

        // Скидання стану перед новим розрахунком
        for (RepairTask t : tasks)
            t.setStartWeek(0);

        Solver solver = new Solver(tasks, limit, scenario);
        boolean success = solver.solve();

        if (success) {
            System.out.println(">> СТАТУС: ПЛАН ЗНАЙДЕНО");
            printProfessionalGantt(tasks, limit, scenario);
        } else {
            System.out.println(">> СТАТУС: РОЗВ'ЯЗОК НЕ ЗНАЙДЕНО");
            System.out.println("   Неможливо виконати план в межах ліміту " + limit);
        }
    }

    private static void printProfessionalGantt(List<RepairTask> tasks, double limit, Scenario sc) {
        int horizon = 9;
        String colFormat = "%-7s|";
        String numFormat = "%-7.2f|";
        String emptyFormat = "       |";

        System.out.println("\nКАЛЕНДАРНИЙ ПЛАН-ГРАФІК");
        System.out.println("-".repeat(8 + (horizon * 8)));

        // Шапка
        System.out.print(" АГР    |");
        for (int w = 1; w <= horizon; w++) {
            System.out.printf(colFormat, " ТИЖ " + w);
        }
        System.out.println("\n" + "-".repeat(8 + (horizon * 8)));

        // Тіло таблиці
        for (RepairTask t : tasks) {
            System.out.printf(" #%-5d |", t.getId());

            int start = t.getStartWeek();
            int duration = 3;

            for (int w = 1; w <= horizon; w++) {
                if (w >= start && w < start + duration) {
                    double cost = t.getCostAtWeek(w, sc);
                    System.out.printf(numFormat, cost);
                } else {
                    System.out.print(emptyFormat);
                }
            }
            System.out.println();
        }
        System.out.println("-".repeat(8 + (horizon * 8)));

        // Підсумок ресурсів
        System.out.print(" ВСЬОГО |");
        for (int w = 1; w <= horizon; w++) {
            double sum = 0;
            for (RepairTask t : tasks)
                sum += t.getCostAtWeek(w, sc);

            // Якщо є мінімальне перевищення (похибка округлення), підсвічуємо
            if (sum > limit + 0.001) {
                System.out.printf("%-7.2f|", sum); // Можна додати "!" якщо потрібно
            } else {
                System.out.printf(numFormat, sum);
            }
        }
        System.out.println();

        System.out.print(" ЛІМІТ  |");
        for (int w = 1; w <= horizon; w++) {
            System.out.printf(numFormat, limit);
        }
        System.out.println("\n" + "-".repeat(8 + (horizon * 8)));
    }
}

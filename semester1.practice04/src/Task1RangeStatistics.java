package semester1.practice04.homework;
import java.util.Scanner;

public class Task1RangeStatistics {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== АНАЛІЗАТОР ДІАПАЗОНУ ЧИСЕЛ ===");
        int n;

        do {
            System.out.print("Введіть число N: ");
            n = scanner.nextInt();

            if (n <= 0) {
                System.out.println("Помилка: N має бути більшим за 0!");
            }
        } while (n <= 0);

        int totalSum = 0;
        int evenSum = 0;
        int oddSum = 0;
        int evenCount = 0;
        int oddCount = 0;

        for (int i = 1; i <= n; i++) {
            totalSum = totalSum + i;

            if (i % 2 == 0) {
                evenSum = evenSum + i;
                evenCount = evenCount + 1;
            } else {
                oddSum = oddSum + i;
                oddCount = oddCount + 1;
            }
        }

        double average = (double) totalSum / n;

        System.out.println("\n--- РЕЗУЛЬТАТИ ДЛЯ ДІАПАЗОНУ [1 .. " + n + "] ---");
        System.out.printf("Загальна сума чисел:       %d%n", totalSum);
        System.out.printf("Сума парних чисел:         %d (кількість: %d)%n", evenSum, evenCount);
        System.out.printf("Сума непарних чисел:       %d (кількість: %d)%n", oddSum, oddCount);
        System.out.printf("Середнє арифметичне:       %.2f%n", average);
        System.out.println("=========================================");

        scanner.close();
    }
}

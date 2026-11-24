package semester1.practice05.homework;
import java.util.Scanner;

public class Task1DailySales {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] days = {"Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя"};
        double[] sales = new double[7];
        System.out.println("=== ФІНАНСОВИЙ ЗВІТ КАВ'ЯРНІ ЗА ТИЖДЕНЬ ===");
        for (int i = 0; i < sales.length; i++) {
            System.out.print("Введіть виторг за " + days[i] + " (грн): ");
            sales[i] = scanner.nextDouble();
        }

        double totalSales = 0.0;
        for (int i = 0; i < sales.length; i++) {
            totalSales = totalSales + sales[i];
        }

        double averageSales = totalSales / sales.length;
        double maxSales = sales[0];
        String maxDay = days[0];
        double minSales = sales[0];
        String minDay = days[0];

        for (int i = 1; i < sales.length; i++) {

            if (sales[i] > maxSales) {
                maxSales = sales[i];
                maxDay = days[i];
            }

            if (sales[i] < minSales) {
                minSales = sales[i];
                minDay = days[i];
            }
        }

        System.out.println("\n---------------- ПІДСУМКИ ----------------");
        System.out.printf("Загальний тижневий виторг: %.2f грн%n", totalSales);
        System.out.printf("Середній виторг на день:    %.2f грн%n", averageSales);
        System.out.printf("Найкращий день:            %s (%.2f грн)%n", maxDay, maxSales);
        System.out.printf("Найгірший день:            %s (%.2f грн)%n", minDay, minSales);
        System.out.println("\nДні з виторгом вище середнього:");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i] > averageSales) {
                System.out.printf("- %-10s %.2f грн%n", days[i] + ":", sales[i]);
            }
        }
        System.out.println("=========================================");

        scanner.close();
    }
}

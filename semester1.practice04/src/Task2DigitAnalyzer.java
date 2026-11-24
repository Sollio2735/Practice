package semester1.practice04.homework;
import java.util.Scanner;

public class Task2DigitAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== АНАЛІЗАТОР РОЗРЯДІВ ЧИСЛА ===");
        System.out.print("Введіть додатне ціле число: ");
        int number = scanner.nextInt();

        int originalNumber = number;

        int digitCount = 0;
        int digitSum = 0;
        int maxDigit = 0;
        int reversedNumber = 0;

        int temp = number;

        while (temp > 0) {
            int digit = temp % 10;

            digitCount = digitCount + 1;
            digitSum = digitSum + digit;

            if (digit > maxDigit) {
                maxDigit = digit;
            }

            reversedNumber = reversedNumber * 10 + digit;

            temp = temp / 10;
        }

        System.out.println("\n--- СТАТИСТИКА ЧИСЛА ---");
        System.out.printf("Початкове число:     %d%n", originalNumber);
        System.out.printf("Кількість цифр:      %d%n", digitCount);
        System.out.printf("Сума цифр:           %d%n", digitSum);
        System.out.printf("Найбільша цифра:     %d%n", maxDigit);
        System.out.printf("Перевернуте число:   %d%n", reversedNumber);

        if (originalNumber == reversedNumber) {
            System.out.println("Результат:           ✨ Це число є ПАЛІНДРОМОМ!");
        } else {
            System.out.println("Результат:           Це звичайне число (не паліндром).");
        }

        System.out.println("================================");

        scanner.close();
    }
}

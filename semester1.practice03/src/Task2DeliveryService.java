package semester1.practice03.homework;
import java.util.Scanner;

public class Task2DeliveryService {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");
        System.out.print("Введіть вагу відправлення (кг): ");
        double weight = scanner.nextDouble();
        System.out.print("Введіть відстань транспортування (км): ");
        int distance = scanner.nextInt();
        System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
        int deliveryType = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        String premiumInput = scanner.nextLine();
        if (weight <= 0 || weight > 50.0) {
            System.out.println("Помилка: вага має бути більше 0 та не більше 50 кг!");
            return;
        }

        if (distance <= 0) {
            System.out.println("Помилка: відстань має бути більше 0 км!");
            return;
        }

        double baseTariff = switch (deliveryType) {
            case 1 -> 50.0;
            case 2 -> {
                if (weight > 15.0) {
                    System.out.println("Помилка: поштомат не приймає габаритні посилки (вага більше 15 кг)!");
                    yield -1.0;
                } else {
                    yield 60.0;
                }
            }
            case 3 -> 100.0;
            default -> -1.0;
        };

        if (baseTariff == -1.0) {
            System.out.println("Помилка: Некоректний тип доставки або перевищено габарити для поштомату!");
            return;
        }

        double distanceFee = 0.0;
        if (distance <= 50) {
            distanceFee = 0.0;
        } else if (distance <= 200) {
            distanceFee = 35.0;
        } else {
            distanceFee = 80.0;
        }

        boolean isPremium = premiumInput.equalsIgnoreCase("так");
        double subtotal = baseTariff + distanceFee;
        double totalCost = isPremium ? (subtotal * 0.8) : subtotal;
        String deliveryName = switch (deliveryType) {
            case 1 -> "Відділення";
            case 2 -> "Поштомат";
            default -> "Кур'єрська доставка";
        };

        System.out.println("\n------------- НАКЛАДНА ДОСТАВКИ -------------");
        System.out.printf("Тип доставки:              %s (базовий тариф: %.2f грн)%n", deliveryName, baseTariff);
        System.out.printf("Доплата за відстань:       %.2f грн (%d км)%n", distanceFee, distance);
        System.out.printf("Сума до знижки:            %.2f грн%n", subtotal);
        System.out.printf("Статус клієнта:            %s%n", isPremium ? "Premium (-20%)" : "Звичайний");
        System.out.println("---------------------------------------------");
        System.out.printf("РАЗОМ ДО СПЛАТИ:           %.2f грн%n", totalCost);
        System.out.println("=============================================");

        scanner.close();
    }
}

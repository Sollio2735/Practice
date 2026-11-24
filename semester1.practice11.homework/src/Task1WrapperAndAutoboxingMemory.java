package semester1.practice10.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Task1WrapperAndAutoboxingMemory {

    public static void inspectIntegerCache(int[] testValues) {
        System.out.println("[1. ЕКСПЕРИМЕНТ: INTEGER CACHE [-128..127] (== проти .equals())]:");
        for (int val : testValues) {
            Integer first = Integer.valueOf(val);
            Integer second = Integer.valueOf(val);

            boolean sameReference = (first == second);
            boolean sameValue = first.equals(second);
            String source = sameReference ? "IntegerCache" : "Новий об'єкт у Heap!";

            System.out.printf("  Значення: %5d | (a == b): %-5b | a.equals(b): %-5b | Джерело: %s%n",
                    val, sameReference, sameValue, source);
        }
        System.out.println();
    }

    public static void demonstrateAutoboxingUnderTheHood() {
        System.out.println("[2. AUTOBOXING / UNBOXING ПІД КАПОТОМ ТА ЗАХИСТ ВІД NULL]:");

        Integer autoInt = 42;
        Integer explicitInt = Integer.valueOf(42);
        int unboxedInt = autoInt;
        int explicitUnboxedInt = explicitInt.intValue();

        Double explicitDouble = Double.valueOf(3.14);
        double unboxedDouble = explicitDouble.doubleValue();

        Boolean explicitBoolean = Boolean.valueOf(true);
        boolean unboxedBoolean = explicitBoolean.booleanValue();

        System.out.printf(Locale.US,
                "  Явне пакування: Integer.valueOf(%d) -> %d | Розпакування: .intValue() -> %d%n",
                unboxedInt, explicitInt, explicitUnboxedInt);
        System.out.printf(Locale.US,
                "  Додаткові обгортки: Double.valueOf(%.2f).doubleValue() -> %.2f | Boolean.valueOf(%b).booleanValue() -> %b%n",
                explicitDouble, unboxedDouble, explicitBoolean, unboxedBoolean);

        List<Integer> sensorReadings = Arrays.asList(45, null, -12, 88, null, 102);
        System.out.println("  Вхідний список із датчиків (з null): " + sensorReadings);

        int sum = 0;
        List<Integer> nullIndices = new ArrayList<>();

        for (int i = 0; i < sensorReadings.size(); i++) {
            Integer boxedReading = sensorReadings.get(i);
            if (boxedReading == null) {
                nullIndices.add(i);
                continue;
            }
            sum += boxedReading.intValue();
        }

        if (!nullIndices.isEmpty()) {
            String indicesText = String.join(" та ",
                    nullIndices.stream().map(String::valueOf).toList());
            System.out.printf("  ⚠️ Пропущено null-елемент на індексі %s (запобігання NullPointerException при intValue()!)%n",
                    indicesText);
        }
        System.out.println("  Сума валідних розпакованих елементів: " + sum);
        System.out.println();
    }

    public static List<Integer> parseTelemetryStream(String[] rawPackets) {
        System.out.println("[3. ПАРСИНГ РЯДКІВ (Integer.parseInt) ТА ЕКСТРЕМУМИ (MAX_VALUE / MIN_VALUE)]:");
        System.out.printf("  Початкові межі пошуку: min = %d (MAX_VALUE), max = %d (MIN_VALUE)%n",
                Integer.MAX_VALUE, Integer.MIN_VALUE);

        List<Integer> validData = new ArrayList<>();
        if (rawPackets == null) {
            return validData;
        }

        for (String packet : rawPackets) {
            if (packet == null || packet.trim().isEmpty()) {
                System.out.println("  ❌ Порожній або null-пакет відхилено");
                continue;
            }

            String cleaned = packet.trim();
            try {
                int parsedValue = Integer.parseInt(cleaned);
                validData.add(Integer.valueOf(parsedValue));
            } catch (NumberFormatException e) {
                System.out.printf("  ❌ Пакет '%s' відхилено (NumberFormatException)%n", cleaned);
            }
        }

        System.out.println("  Успішно розпізнаний список Integer: " + validData);
        return validData;
    }

    public static void printExtremesAndStats(List<Integer> validData) {
        if (validData == null || validData.isEmpty()) {
            System.out.println("  Список даних порожній — обчислення екстремумів неможливе.");
            return;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0L;

        for (Integer item : validData) {
            if (item == null) continue;
            int val = item.intValue();
            if (val < min) min = val;
            if (val > max) max = val;
            sum += val;
        }

        System.out.printf("  Знайдений мінімум: %d | Знайдений максимум: %d | Сума елементів: %d%n",
                min, max, sum);

        List<Integer> editableList = new ArrayList<>(validData);
        editableList.remove(Integer.valueOf(-30));
        System.out.println("  Після remove(Integer.valueOf(-30)) (за значенням): " + editableList);

        editableList.remove(0);
        System.out.println("  Після remove(0) (видалення за індексом 0): " + editableList);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        System.out.println("================ ДОСЛІДЖЕННЯ WRAPPER CLASSES ТА INTEGER CACHE ================");

        int[] cacheProbeValues = {-128, -50, 0, 127, 128, 500, 2026};
        inspectIntegerCache(cacheProbeValues);

        demonstrateAutoboxingUnderTheHood();

        String[] rawTelemetry = {"  145 ", "-30", "ERR_404", "980", "", null, "2147483647", "-85"};
        List<Integer> parsedTelemetry = parseTelemetryStream(rawTelemetry);
        printExtremesAndStats(parsedTelemetry);

        System.out.println("==============================================================================");
    }
}

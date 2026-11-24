package semester1.practice10.homework;

import java.util.ArrayList;

class CustomArrayCapacityEmulator<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elementData;
    private int size = 0;
    private int resizeCount = 0;
    private int totalElementsCopied = 0;

    public CustomArrayCapacityEmulator() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    private void ensureCapacityInternal() {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            Object[] newBuffer = new Object[newCapacity];
            System.arraycopy(elementData, 0, newBuffer, 0, size);
            elementData = newBuffer;
            resizeCount++;
            totalElementsCopied += size;

            System.out.printf("  ⚡ [GROW #%d] Переповнення при додаванні %d-го елемента! Ємність: %d -> %d (System.arraycopy скопіював %d ел.)%n",
                    resizeCount, size + 1, oldCapacity, newCapacity, size);
        }
    }

    public void add(E element) {
        ensureCapacityInternal();
        elementData[size++] = element;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacityInternal();
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + 1, numMoved);
        }
        elementData[index] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
        return oldValue;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elementData[index];
    }

    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    public int size() {
        return size;
    }

    public int getCapacity() {
        return elementData.length;
    }

    public int getResizeCount() {
        return resizeCount;
    }

    public int getTotalElementsCopied() {
        return totalElementsCopied;
    }

    public void trimToSize() {
        if (size < elementData.length) {
            Object[] trimmedBuffer = new Object[size];
            System.arraycopy(elementData, 0, trimmedBuffer, 0, size);
            elementData = trimmedBuffer;
        }
    }
}

public class Task2ArrayListCapacityAndDynamicGrowth {

    public static void main(String[] args) {
        var emulator = new CustomArrayCapacityEmulator<String>();

        var rawUnsafeList = new ArrayList<>();
        rawUnsafeList.add("UnsafeObject");

        System.out.println("================ ЕМУЛЯТОР ВНУТРІШНЬОЇ БУДОВИ ARRAYLIST (ПІД КАПОТОМ) ================");
        System.out.printf("[ІНІЦІАЛІЗАЦІЯ]: Створено CustomArrayCapacityEmulator<String> | size = %d | capacity = %d%n%n",
                emulator.size(), emulator.getCapacity());

        System.out.println("--- Додавання 35 елементів у список ---");
        for (int i = 1; i <= 35; i++) {
            emulator.add("Block-" + i);
        }

        int freeSlots = emulator.getCapacity() - emulator.size();
        System.out.printf("Стан після додавання 35 елементів: size = %d | capacity = %d (Вільний резерв: %d комірок null)%n%n",
                emulator.size(), emulator.getCapacity(), freeSlots);

        System.out.println("--- Тестування вставки add(0, 'HEADER') та видалення remove(5) ---");
        int shiftedRight = emulator.size();
        emulator.add(0, "HEADER");
        System.out.printf("  -> Вставлено 'HEADER' на індекс 0 (зсунуто праворуч %d елементів через System.arraycopy)%n",
                shiftedRight);

        int shiftedLeft = emulator.size() - 5 - 1;
        String removedElement = emulator.remove(5);
        System.out.printf("  -> Видалено елемент '%s' з індексу 5 (зсунуто ліворуч %d елементів, хвіст занулено для GC)%n",
                removedElement, shiftedLeft);

        System.out.printf("  Поточний стан: size = %d | capacity = %d | Перший: '%s' | Останній: '%s'%n%n",
                emulator.size(), emulator.getCapacity(), emulator.get(0), emulator.get(emulator.size() - 1));

        System.out.println("--- Оптимізація пам'яті trimToSize() ---");
        int oldCap = emulator.getCapacity();
        emulator.trimToSize();
        System.out.printf("  -> Викликано trimToSize(): буфер Object[%d] стиснуто рівно до Object[%d]!%n",
                oldCap, emulator.size());
        System.out.printf("  Фінальна телеметрія: size = %d | capacity = %d | Кількість розширень: %d%n",
                emulator.size(), emulator.getCapacity(), emulator.getResizeCount());
        System.out.println("=====================================================================================");
    }
}


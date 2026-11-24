package semestr1.practice02;

public class Task2NumberAnalysis {
    static void main() {
        int number = 742;
        int hundreds = (number / 100) % 10;
        int tens = (number / 10) % 10;
        int units = number % 10;
        int summNumbers = hundreds + tens + units;
        int dobutokNumbers = hundreds * tens * units;
        int reversedNumber = units * 100 + tens * 10 + hundreds;
        boolean isSumEven = summNumbers % 2 == 0;
        System.out.println("Початкове число:\t\t\t" + number);
        System.out.println("Розряди:\t\t\t\t\t" + "Сотні = " + hundreds + ", Десятки = " + tens + ", Одиниці = " + units);
        System.out.println("Сума цифр:\t\t\t\t\t" + summNumbers);
        System.out.println("Добуток цифр:\t\t\t\t" + dobutokNumbers);
        System.out.println("Перевернуте число:\t\t\t" + reversedNumber);
        System.out.println("Чи є сума цифр парною?\t\t" + isSumEven);


    }
}

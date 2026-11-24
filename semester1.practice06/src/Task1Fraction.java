package semester1.practice06.homework;

class Fraction {
    private int numerator;
    private int denominator;

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setValues(int num, int den) {
        if (den == 0) {
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            this.numerator = num;
            this.denominator = 1;
            return;
        }

        if (den < 0) {
            this.numerator = -num;
            this.denominator = -den;
        } else {
            this.numerator = num;
            this.denominator = den;
        }
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public void simplify() {
        int commonDivisor = gcd(this.numerator, this.denominator);
        this.numerator = this.numerator / commonDivisor;
        this.denominator = this.denominator / commonDivisor;
    }

    public double toDecimal() {
        return (double) this.numerator / this.denominator;
    }

    public void add(int otherNum, int otherDen) {
        if (otherDen == 0) {
            return;
        }

        int newNumerator = (this.numerator * otherDen) + (otherNum * this.denominator);
        int newDenominator = this.denominator * otherDen;

        this.numerator = newNumerator;
        this.denominator = newDenominator;

        simplify();
    }

    public void printFraction() {
        if (this.denominator == 1) {
            System.out.print(this.numerator);
        } else {
            System.out.print(this.numerator + "/" + this.denominator);
        }
    }
}

public class Task1Fraction {
    public static void main(String[] args) {
        System.out.println("=== КАЛЬКУЛЯТОР ЗВИЧАЙНИХ ДРОБІВ ===");
        Fraction fraction1 = new Fraction();
        fraction1.setValues(8, 12);
        System.out.print("Дріб 1: ");
        fraction1.printFraction();
        System.out.println();
        fraction1.simplify();
        System.out.print("Після скорочення: ");
        fraction1.printFraction();
        System.out.printf(" (десятковий вигляд: %.4f)%n", fraction1.toDecimal());
        System.out.println("\nДодаємо дріб 3/4 до 2/3...");
        System.out.println("Формула: 2/3 + 3/4 = (2*4 + 3*3) / (3*4) = 17/12");
        fraction1.add(3, 4);
        System.out.print("Результат додавання: ");
        fraction1.printFraction();
        System.out.printf(" (десятковий вигляд: %.4f)%n", fraction1.toDecimal());
        System.out.println("=====================================");
    }
}

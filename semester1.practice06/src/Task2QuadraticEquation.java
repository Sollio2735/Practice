package semester1.practice06.homework;
class QuadraticEquation {

    private double a;
    private double b;
    private double c;

    public void setCoefficients(double a, double b, double c) {
        if (a == 0) {
            System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0 (це лінійне рівняння)!");
            this.a = 1.0;
        } else {
            this.a = a;
        }
        this.b = b;
        this.c = c;
    }

    public double getDiscriminant() {
        return Math.pow(this.b, 2) - 4 * this.a * this.c;
    }

    public boolean hasRealRoots() {
        return getDiscriminant() >= 0;
    }

    public double getRoot1() {
        double d = getDiscriminant();
        if (d < 0) {
            return Double.NaN;
        }
        return (-this.b + Math.sqrt(d)) / (2 * this.a);
    }

    public double getRoot2() {
        double d = getDiscriminant();
        if (d < 0) {
            return Double.NaN;
        }
        return (-this.b - Math.sqrt(d)) / (2 * this.a);
    }

    public void printSolution() {
        System.out.printf("Рівняння: %.1f*x^2 + (%.1f)*x + %.1f = 0%n", this.a, this.b, this.c);
        double d = getDiscriminant();
        System.out.printf("Дискримінант D = %.2f%n", d);

        if (d > 0) {
            System.out.println("Рівняння має 2 дійсні корені:");
            System.out.printf("  x1 = %.2f%n", getRoot1());
            System.out.printf("  x2 = %.2f%n", getRoot2());
        } else if (d == 0) {
            System.out.println("Рівняння має один дійсний корінь:");
            System.out.printf("  x = %.2f%n", getRoot1());
        } else {
            System.out.println("Рівняння не має дійсних коренів у множині R.");
        }
    }
}

public class Task2QuadraticEquation {
    public static void main(String[] args) {
        System.out.println("=== РОЗВ'ЯЗУВАЧ КВАДРАТНИХ РІВНЯНЬ ===");
        System.out.println("Тест 1:");
        QuadraticEquation eq1 = new QuadraticEquation();
        eq1.setCoefficients(1.0, -5.0, 6.0);
        eq1.printSolution();
        System.out.println("--------------------------------------");

        System.out.println("Тест 2:");
        QuadraticEquation eq2 = new QuadraticEquation();
        eq2.setCoefficients(1.0, -4.0, 4.0);
        eq2.printSolution();
        System.out.println("--------------------------------------");

        System.out.println("Тест 3:");
        QuadraticEquation eq3 = new QuadraticEquation();
        eq3.setCoefficients(1.0, 2.0, 5.0);
        eq3.printSolution();
        System.out.println("=====================================");
    }
}

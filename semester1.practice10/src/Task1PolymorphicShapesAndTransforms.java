package semester1.practice10.homework;

import java.util.Locale;

interface Scalable2D {
    double MIN_SCALE_FACTOR = 0.1;

    void scale(double factor);
}

interface Rotatable2D {
    void rotate(double deltaDegrees);

    double getRotationAngle();
}

abstract class AbstractShape2D {
    private final String name;
    private double centerX;
    private double centerY;

    public AbstractShape2D(String name, double centerX, double centerY) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getName() {
        return name;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    public final double getIsoperimetricQuotient() {
        double p = getPerimeter();
        if (p == 0) return 0.0;
        return (4.0 * Math.PI * getArea()) / (p * p);
    }

    protected String getMetricsSummary() {
        return String.format(Locale.US, "S=%.2f, P=%.2f, Q=%.4f",
                getArea(), getPerimeter(), getIsoperimetricQuotient());
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s['%s', center=(%.1f; %.1f) | %s]",
                getClass().getSimpleName(), name, centerX, centerY, getMetricsSummary());
    }
}

class CircleShape extends AbstractShape2D implements Scalable2D {
    private double radius;

    public CircleShape(String name, double centerX, double centerY, double radius) {
        super(name, centerX, centerY);
        if (radius <= 0) {
            throw new IllegalArgumentException("Радіус має бути більшим за 0");
        }
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2.0 * Math.PI * radius;
    }

    @Override
    public void scale(double factor) {
        double effectiveFactor = Math.max(factor, MIN_SCALE_FACTOR);
        this.radius *= effectiveFactor;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "CircleShape['%s', center=(%.1f; %.1f), r=%.2f | %s]",
                getName(), getCenterX(), getCenterY(), radius, getMetricsSummary());
    }
}

class RectangleShape extends AbstractShape2D implements Scalable2D, Rotatable2D {
    private double width;
    private double height;
    private double rotationAngle;

    public RectangleShape(String name, double centerX, double centerY, double width, double height) {
        super(name, centerX, centerY);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Сторони прямокутника мають бути більшими за 0");
        }
        this.width = width;
        this.height = height;
        this.rotationAngle = 0.0;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2.0 * (width + height);
    }

    @Override
    public void scale(double factor) {
        double effectiveFactor = Math.max(factor, MIN_SCALE_FACTOR);
        this.width *= effectiveFactor;
        this.height *= effectiveFactor;
    }

    @Override
    public void rotate(double deltaDegrees) {
        this.rotationAngle = ((this.rotationAngle + deltaDegrees) % 360.0 + 360.0) % 360.0;
    }

    @Override
    public double getRotationAngle() {
        return rotationAngle;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "RectangleShape['%s', center=(%.1f; %.1f), %.2fx%.2f, angle=%.1f° | %s]",
                getName(), getCenterX(), getCenterY(), width, height, rotationAngle, getMetricsSummary());
    }
}

final class ImmutableReferenceSquare extends AbstractShape2D {
    private final double side;

    public ImmutableReferenceSquare(String name, double centerX, double centerY, double side) {
        super(name, centerX, centerY);
        if (side <= 0) {
            throw new IllegalArgumentException("Сторона квадрата має бути більшою за 0");
        }
        this.side = side;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public double getPerimeter() {
        return 4.0 * side;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "ImmutableReferenceSquare['%s', center=(%.1f; %.1f), side=%.2f | %s]",
                getName(), getCenterX(), getCenterY(), side, getMetricsSummary());
    }
}

public class Task1PolymorphicShapesAndTransforms {

    public static double calculateTotalArea(AbstractShape2D[] shapes) {
        double total = 0.0;
        for (AbstractShape2D shape : shapes) {
            if (shape != null) {
                total += shape.getArea();
            }
        }
        return total;
    }

    public static void transformScene(AbstractShape2D[] shapes, double scaleFactor, double rotateDeg) {
        System.out.printf(Locale.US, "-> Виконуємо пакетну трансформацію (scale x%.1f, rotate %+.1f°) через instanceof...%n",
                scaleFactor, rotateDeg);

        for (AbstractShape2D shape : shapes) {
            if (shape == null) continue;

            boolean transformed = false;

            // Pattern Matching for instanceof (Java 16+)
            if (shape instanceof Scalable2D s) {
                s.scale(scaleFactor);
                transformed = true;

                if (shape instanceof CircleShape c) {
                    System.out.printf(Locale.US, "   [Scalable2D] '%s' масштабовано в %.1f разів (новий r=%.2f)%n",
                            shape.getName(), scaleFactor, c.getRadius());
                } else if (shape instanceof RectangleShape r) {
                    System.out.printf(Locale.US, "   [Scalable2D] '%s' масштабовано в %.1f разів (%.2fx%.2f)%n",
                            shape.getName(), scaleFactor, r.getWidth(), r.getHeight());
                }
            }

            if (shape instanceof Rotatable2D r) {
                r.rotate(rotateDeg);
                transformed = true;
                System.out.printf(Locale.US, "   [Rotatable2D] '%s' повернуто на %+.1f° (поточний кут: %.1f°)%n",
                        shape.getName(), rotateDeg, r.getRotationAngle());
            }

            if (!transformed) {
                System.out.printf("   [Info] '%s' (final class) пропущено — не підтримує трансформації.%n",
                        shape.getName());
            }
        }
    }

    private static void printScene(AbstractShape2D[] shapes) {
        for (int i = 0; i < shapes.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, shapes[i]);
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        AbstractShape2D[] shapes = {
                new CircleShape("Коло-Лінза", 0.0, 0.0, 5.0),
                new RectangleShape("Рамка-А", 2.0, 3.0, 4.0, 6.0),
                new ImmutableReferenceSquare("Еталон-Квадрат", 10.0, 10.0, 5.0)
        };

        System.out.println("================ ПОЛІМОРФНА ГЕОМЕТРІЯ ТА ІНТЕРФЕЙСИ ТРАНСФОРМАЦІЙ ================");
        System.out.println("[СТАН СЦЕНИ ДО ТРАНСФОРМАЦІЇ]:");
        printScene(shapes);
        System.out.printf("Сумарна площа сцени: %.2f кв.од.%n%n", calculateTotalArea(shapes));

        transformScene(shapes, 2.0, 45.0);

        System.out.println("\n[СТАН СЦЕНИ ПІСЛЯ ТРАНСФОРМАЦІЇ]:");
        printScene(shapes);
        System.out.printf("Нова сумарна площа сцени: %.2f кв.од.%n", calculateTotalArea(shapes));
        System.out.println("==================================================================================");
    }
}

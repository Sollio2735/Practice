package semester1.practice09.homework;

class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        if (other != null) {
            this.x = other.x;
            this.y = other.y;
        } else {
            this.x = 0;
            this.y = 0;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double distanceTo(Point2D other) {
        if (other == null) return 0;
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }
}

abstract class AbstractShape2D {
    private final String name;
    private final Point2D anchorPoint;

    public AbstractShape2D(String name, Point2D anchorPoint) {
        this.name = name;
        this.anchorPoint = (anchorPoint != null) ? new Point2D(anchorPoint) : new Point2D(0, 0);
    }

    public String getName() {
        return name;
    }

    public Point2D getAnchorPoint() {
        return new Point2D(anchorPoint);
    }

    public void moveBy(double dx, double dy) {
        anchorPoint.translate(dx, dy);
    }

    public abstract double getArea();
    public abstract double getPerimeter();

    @Override
    public String toString() {
        return String.format("%s[name='%s', center=%s | S=%.2f, P=%.2f]",
                getClass().getSimpleName(), name, anchorPoint, getArea(), getPerimeter());
    }
}

class Circle2D extends AbstractShape2D {
    private double radius;

    public Circle2D(String name, Point2D center, double radius) {
        super(name, center);
        this.radius = (radius > 0) ? radius : 1.0;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return String.format("Circle2D[name='%s', center=%s, r=%.2f | S=%.2f, P=%.2f]",
                getName(), getAnchorPoint(), radius, getArea(), getPerimeter());
    }
}

class Rectangle2D extends AbstractShape2D {
    private double width;
    private double height;

    public Rectangle2D(String name, Point2D center, double width, double height) {
        super(name, center);
        this.width = (width > 0) ? width : 1.0;
        this.height = (height > 0) ? height : 1.0;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return String.format("Rectangle2D[name='%s', center=%s, %.2fX%.2f | S=%.2f, P=%.2f]",
                getName(), getAnchorPoint(), width, height, getArea(), getPerimeter());
    }
}

class RightTriangle2D extends AbstractShape2D {
    private double legA;
    private double legB;

    public RightTriangle2D(String name, Point2D center, double legA, double legB) {
        super(name, center);
        this.legA = (legA > 0) ? legA : 1.0;
        this.legB = (legB > 0) ? legB : 1.0;
    }

    public double getHypotenuse() {
        return Math.sqrt(legA * legA + legB * legB);
    }

    @Override
    public double getArea() {
        return 0.5 * legA * legB;
    }

    @Override
    public double getPerimeter() {
        return legA + legB + getHypotenuse();
    }

    @Override
    public String toString() {
        return String.format("RightTriangle2D[name='%s', center=%s, a=%.2f, b=%.2f, c=%.2f | S=%.2f, P=%.2f]",
                getName(), getAnchorPoint(), legA, legB, getHypotenuse(), getArea(), getPerimeter());
    }
}

class GeometryCanvas {
    private final String title;
    private final AbstractShape2D[] shapes;
    private int count;

    public GeometryCanvas(String title, int capacity) {
        this.title = title;
        this.shapes = new AbstractShape2D[capacity];
        this.count = 0;
    }

    public boolean addShape(AbstractShape2D shape) {
        if (shape == null || count >= shapes.length) {
            return false;
        }
        shapes[count++] = shape;
        return true;
    }

    public double getTotalArea() {
        double totalArea = 0;
        for (int i = 0; i < count; i++) {
            totalArea += shapes[i].getArea();
        }
        return totalArea;
    }

    public double getTotalPerimeter() {
        double totalPerimeter = 0;
        for (int i = 0; i < count; i++) {
            totalPerimeter += shapes[i].getPerimeter();
        }
        return totalPerimeter;
    }

    public AbstractShape2D findLargestShape() {
        if (count == 0) return null;
        AbstractShape2D largest = shapes[0];
        for (int i = 1; i < count; i++) {
            if (shapes[i].getArea() > largest.getArea()) {
                largest = shapes[i];
            }
        }
        return largest;
    }

    public void moveAllShapes(double dx, double dy) {
        for (int i = 0; i < count; i++) {
            shapes[i].moveBy(dx, dy);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Полотно: '%s' (Фігур: %d/%d)\n", title, count, shapes.length));
        for (int i = 0; i < count; i++) {
            sb.append(String.format("  %d. %s\n", i + 1, shapes[i].toString()));
        }
        return sb.toString();
    }
}

public class Task1CompositeShapesCanvas {
    public static void main(String[] args) {
        System.out.println("================ ГЕОМЕТРИЧНЕ ПОЛОТНО (КОМПОЗИЦІЯ + ABSTRACT) ================");

        GeometryCanvas canvas = new GeometryCanvas("Інженерне креслення №1", 5);

        canvas.addShape(new Circle2D("Коло-А", new Point2D(0, 0), 5.0));
        canvas.addShape(new Rectangle2D("Плита-Б", new Point2D(10, 5), 4.0, 6.0));
        canvas.addShape(new RightTriangle2D("Клин-В", new Point2D(-3, 2), 3.0, 4.0));

        System.out.print(canvas);
        System.out.println("-----------------------------------------------------------------------------");

        System.out.printf("Сумарна площа креслення:    %.2f кв.од.\n", canvas.getTotalArea());
        System.out.printf("Сумарний периметр:          %.2f од.\n", canvas.getTotalPerimeter());

        AbstractShape2D largest = canvas.findLargestShape();
        if (largest != null) {
            System.out.printf("Найбільша фігура:           %s (S = %.2f)\n", largest.getName(), largest.getArea());
        }

        double dx = 5.0, dy = -2.0;
        System.out.printf("-> Переміщуємо всі фігури полотна на вектор (+%.1f; %.1f)...\n", dx, dy);
        canvas.moveAllShapes(dx, dy);

        AbstractShape2D largestRef = canvas.findLargestShape();
        System.out.printf("Новий центр 'Коло-А': %s\n", largestRef.getAnchorPoint());

        System.out.println("=============================================================================");
    }
}

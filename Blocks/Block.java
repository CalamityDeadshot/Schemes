package Schemes.Blocks;

import Schemes.Utils.Pair;
import Schemes.Utils.Utils;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;


// Самый важный класс - базовый класс блока.
// Наследуется от класса Polygon, представляющего собой многоугольник.
public class Block extends Polygon {
    protected String text;
    protected final transient Graphics2D g;
    public final int width, height, pivotX, pivotY;

    protected final int x1, x2, x3, x4;
    protected final int y1, y2, y3, y4;
    public Point TOP;
    public Point BOTTOM;
    public Point LEFT;
    public Point RIGHT;
    protected final transient Rectangle bounds;
    public int type;

    public static final int TYPE_BRANCHING = 1;
    public static final int TYPE_CALL = 2;
    public static final int TYPE_CONNECTOR = 3;
    public static final int TYPE_IO = 4;
    public static final int TYPE_LOOP_START = 5;
    public static final int TYPE_LOOP_END = 6;
    public static final int TYPE_OPERATION = 7;
    public static final int TYPE_PREPARATION = 8;
    public static final int TYPE_TERMINATOR = 9;

    public ArrayList<Pair<Point>> connections = new ArrayList<>();

    // Так как блоки Терминатора и Соединителя не являются многоугольниками, этот конструктор нужен
    // чтобы не отрисовывать ограничивающий их многоугольник.
    public Block(int width, int height, int pivotX, int pivotY, String text, Graphics2D g, boolean wtf) {
        super(
                new int[] {pivotX - width / 2, pivotX + width / 2, pivotX + width / 2, pivotX - width / 2},
                new int[] {pivotY - height / 2, pivotY - height / 2, pivotY + height / 2, pivotY + height / 2},
                4);

        this.text = text;
        this.g = g;
        this.height = height;
        this.width = width;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        bounds = this.getBounds();
        x1 = bounds.x; x2 = bounds.x + bounds.width; x3 = x2; x4 = x1;
        y1 = bounds.y; y2 = y1; y3 = bounds.y + bounds.height; y4 = y3;
        TOP = new Point(pivotX, pivotY - height / 2, Point.TOP);
        BOTTOM = new Point(pivotX, pivotY + height / 2, Point.BOTTOM);
        LEFT = new Point(x1, pivotY, Point.LEFT);
        RIGHT = new Point(x2, pivotY, Point.RIGHT);
        drawCenteredString();
    }

    // Конструктор для не прямоугольных блоков
    public Block(int pivotX, int pivotY, int[] xs, int[] ys, String text, Graphics2D g) {
        super(xs, ys, xs.length);
        this.text = text;
        this.g = g;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        bounds = this.getBounds();
        this.width = bounds.width;
        this.height = bounds.height;
        x1 = bounds.x; x2 = bounds.x + bounds.width; x3 = x2; x4 = x1;
        y1 = bounds.y; y2 = y1; y3 = bounds.y + bounds.height; y4 = y3;
        TOP = new Point(pivotX, pivotY - height / 2, Point.TOP);
        BOTTOM = new Point(pivotX, pivotY + height / 2, Point.BOTTOM);
        LEFT = new Point(x1, pivotY, Point.LEFT);
        RIGHT = new Point(x2, pivotY, Point.RIGHT);
        drawShape();
        drawCenteredString();
    }

    // Базовый конструктор, генерирующий прямоугольник. Напрямую используется блоком Операция.
    public Block(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(
            new int[] {pivotX - width / 2, pivotX + width / 2, pivotX + width / 2, pivotX - width / 2},
            new int[] {pivotY - height / 2, pivotY - height / 2, pivotY + height / 2, pivotY + height / 2},
            4);

        this.text = text;
        this.g = g;
        this.height = height;
        this.width = width;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        bounds = this.getBounds();
        x1 = bounds.x; x2 = bounds.x + bounds.width; x3 = x2; x4 = x1;
        y1 = bounds.y; y2 = y1; y3 = bounds.y + bounds.height; y4 = y3;
        TOP = new Point(pivotX, pivotY - height / 2, Point.TOP);
        BOTTOM = new Point(pivotX, pivotY + height / 2, Point.BOTTOM);
        LEFT = new Point(x1, pivotY, Point.LEFT);
        RIGHT = new Point(x2, pivotY, Point.RIGHT);
        drawShape();
        drawCenteredString();
    }

    // Класс Точка, позволяющий соединить точку на холсте с блоком.
    // Полезно при развитии программы далее, а также в блоке условного оператора.
    public class Point {
        int x, y;
        public int side = -1;
        public final static int TOP = 0;
        public final static int BOTTOM = 1;
        public final static int LEFT = 2;
        public final static int RIGHT = 3;
        transient Block owner = Block.this;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(int x, int y, int side) {
            this.x = x;
            this.y = y;
            this.side = side;
        }

        /**
         * Данный метод соединияет точку на холсте с серединой верхней стороны блока
         * @param another блок, с которым нужно соединить точку
         * @return another
         */
        public Block connectTo(Block another) {
            this.connectTo(another.TOP);
            connections.add(new Pair<>(this, another.TOP));
            return another;
        }

        /**
         * Данный метод соединяет точку на холсте с другой точкой
         * @param another точка, с которой нужно соединить данную точку
         * @return Блок, которому принадлежит точка another
         */
        public Block connectTo(Point another) {
            switch (another.side) {
                case TOP, BOTTOM -> drawVerticalConnection(another);
                case LEFT, RIGHT -> drawHorizontalConnection(another);
            }
            switch (another.side) {
                case TOP -> drawArrowDown(another.x, another.y);
                case BOTTOM -> drawArrowUp(another.x, another.y);
                case LEFT -> drawArrowRight(another.x, another.y);
                case RIGHT -> drawArrowLeft(another.x, another.y);
            }
            connections.add(new Pair<>(this, another));
            return another.owner;
        }

        public Block restoreConnection(Point another) {
            switch (another.side) {
                case TOP, BOTTOM -> drawVerticalConnection(another);
                case LEFT, RIGHT -> drawHorizontalConnection(another);
            }
            switch (another.side) {
                case TOP -> drawArrowDown(another.x, another.y);
                case BOTTOM -> drawArrowUp(another.x, another.y);
                case LEFT -> drawArrowRight(another.x, another.y);
                case RIGHT -> drawArrowLeft(another.x, another.y);
            }
            return another.owner;
        }

        private void drawVerticalConnection(Point another) {
            if (this.x == another.x) {
                g.drawLine(x, y, another.x, another.y);
            } else {
                int breakY = (y + another.y) / 2;
                g.drawLine(x, y, x, breakY);
                g.drawLine(x, breakY, another.x, breakY);
                g.drawLine(another.x, breakY, another.x, another.y);
            }
        }

        private void drawHorizontalConnection(Point another) {
            if (this.y == another.y) {
                g.drawLine(x, y, another.x, another.y);
            } else {
                int breakX = (x + another.x) / 2;
                g.drawLine(x, y, breakX, y);
                g.drawLine(breakX, y, breakX, another.y);
                g.drawLine(breakX, another.y, another.x, another.y);
            }
        }
    }

    // Метод, рисующий на холсте данный блок
    private void drawShape() {
        GeneralPath path = new GeneralPath();
        path.append(this, false);
        g.draw(path);
    }
    // Метод для отрисовки текста внутри блока
    private void drawCenteredString() {
        Font font = Utils.FONT;
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = bounds.x + (bounds.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = bounds.y + ((bounds.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    // Методы для отрисовки стрелок на конце соединения.
    protected void drawArrowDown(int x, int y) {
        Polygon arrow = new Polygon(
                new int[] {x, x - 5, x + 5},
                new int[] {y, y - 10, y - 10},
                3
        );
        g.fill(arrow);
        g.drawPolygon(arrow);
    }
    protected void drawArrowUp(int x, int y) {
        Polygon arrow = new Polygon(
                new int[] {x, x - 5, x + 5},
                new int[] {y, y + 10, y + 10},
                3
        );
        g.fill(arrow);
        g.drawPolygon(arrow);
    }
    protected void drawArrowLeft(int x, int y) {
        Polygon arrow = new Polygon(
                new int[] {x, x + 10, x + 10},
                new int[] {y, y - 5, y + 5},
                3
        );
        g.fill(arrow);
        g.drawPolygon(arrow);
    }
    protected void drawArrowRight(int x, int y) {
        Polygon arrow = new Polygon(
                new int[] {x, x - 10, x - 10},
                new int[] {y, y - 5, y + 5},
                3
        );
        g.fill(arrow);
        g.drawPolygon(arrow);
    }

    // Метод для соединения двух блоков.
    // Низ одного блока соединяется с верхом другого.
    public Block connectTo(Block another) {
        this.BOTTOM.connectTo(another.TOP);
        return another;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        drawCenteredString();
    }

}

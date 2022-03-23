package Schemes.Blocks;

import java.awt.*;

// Блок ввода-вывода
public class IO extends Block {
    public IO(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(pivotX, pivotY,
                new int[] {pivotX - width / 2 + 7, pivotX + width / 2 + 7, pivotX + width / 2 - 7, pivotX - width / 2 - 7},
                new int[] {pivotY - height / 2, pivotY - height / 2, pivotY + height / 2, pivotY + height / 2},
                text, g);
        LEFT = new Point(pivotX - width / 2, pivotY, Point.LEFT);
        RIGHT = new Point(pivotX + width / 2, pivotY, Point.RIGHT);
        type = Block.TYPE_IO;
    }
}

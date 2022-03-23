package Schemes.Blocks;

import Schemes.Utils.Utils;

import java.awt.*;


// Условный оператор (ветвление)
public class Branching extends Block {
    public Branching(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(pivotX, pivotY,
                new int[] {pivotX, pivotX + width / 2, pivotX, pivotX - width / 2},
                new int[] {pivotY - height / 2, pivotY, pivotY + height / 2, pivotY},
                text, g);
        drawText();
        type = Block.TYPE_BRANCHING;
    }

    // Метод для отрисовки текста Да/Нет по сторонам от блока
    private void drawText() {
        Font font = Utils.FONT;
        FontMetrics metrics = g.getFontMetrics(font);
        int x1 = bounds.x; int y = bounds.y + metrics.getHeight() / 2;
        int x2 = bounds.x - metrics.stringWidth("Нет") + width;
        g.drawString("Да", x1, y);
        g.drawString("Нет", x2, y);
    }

    // Метод соединения ветки Да с другим блоком
    public Block connectTrueTo(Block another) {
        int x1 = bounds.x, y1 = bounds.y + height / 2;
        return new Point(x1, y1).connectTo(another);
    }

    // Метод соединения ветки Нет с другим блоком
    public Block connectFalseTo(Block another) {
        int x1 = bounds.x + width, y1 = bounds.y + height / 2;
        return new Point(x1, y1).connectTo(another);
    }
}

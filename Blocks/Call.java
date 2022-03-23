package Schemes.Blocks;

import java.awt.*;

// Вызов внешней процедуры
public class Call extends Block {
    public Call(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(width, height, pivotX, pivotY, text, g);
        g.drawLine(bounds.x + 10, bounds.y, bounds.x + 10, bounds.y + height);
        g.drawLine(bounds.x + width - 10, bounds.y, bounds.x + width - 10, bounds.y + height);
        type = Block.TYPE_CALL;
    }
}

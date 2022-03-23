package Schemes.Blocks;

import java.awt.*;

// Подготовка данных
public class Preparation extends Block {
    public Preparation(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(pivotX, pivotY,
                new int[] {
                        pivotX - width / 2 + 10,
                        pivotX + width / 2 - 10,
                        pivotX + width / 2,
                        pivotX + width / 2 - 10,
                        pivotX - width / 2 + 10,
                        pivotX - width / 2
                },
                new int[] {
                        pivotY - height / 2,
                        pivotY - height / 2,
                        pivotY,
                        pivotY + height / 2,
                        pivotY + height / 2,
                        pivotY
                },
                text, g);
        type = Block.TYPE_PREPARATION;
    }
}

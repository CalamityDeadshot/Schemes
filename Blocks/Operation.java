package Schemes.Blocks;

import java.awt.*;

// Блок операции
public class Operation extends Block {
    public Operation(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(width, height, pivotX, pivotY, text, g);
        type = Block.TYPE_OPERATION;
    }
}

package Schemes.Blocks;

import java.awt.*;
import java.awt.geom.Ellipse2D;

// Терминатор
public class Terminator extends Block {
    public Terminator(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
        super(width, height, pivotX, pivotY, text, g, false);
        Ellipse2D.Double ellipse = new Ellipse2D.Double(
                pivotX - width / 2,
                pivotY - height / 2,
                width, height
        );
        g.draw(ellipse);
        type = Block.TYPE_TERMINATOR;
    }
}

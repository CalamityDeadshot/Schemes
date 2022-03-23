package Schemes.Blocks;

import java.awt.*;
import java.awt.geom.Ellipse2D;

// Соединитель
public class Connector extends Block {
    public Connector(int size, int pivotX, int pivotY, String text, Graphics2D g) {
        super(size, size, pivotX, pivotY, text, g, false);
        Ellipse2D.Double ellipse = new Ellipse2D.Double(
                pivotX - width / 2,
                pivotY - height / 2,
                size, size
        );
        g.draw(ellipse);
        type = Block.TYPE_CONNECTOR;
    }
}

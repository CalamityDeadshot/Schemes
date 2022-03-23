package Schemes;

import Schemes.Blocks.*;
import Schemes.Utils.Diagram;
import Schemes.Utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

// Точка входа
public class DiagramDraw {

    JPanel mainPanel;
    final JFrame fr = new JFrame("Block diagram");

    // Подготовительная работа: создание окна, привязка слушателей
    public void run() {
        fr.setPreferredSize(new
                Dimension(500, 500));
        DrawingPanel panel = new DrawingPanel();
        fr.add(panel);
        fr.setVisible(true);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // При закрытии окна видимая часть блок-схемы сохранится в shot.png в директории src
        fr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                BufferedImage im = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
                panel.paint(im.getGraphics());
                try {
                    Utils.diagram.save();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });

        AtomicBoolean shiftPressed = new AtomicBoolean(false);

        // При зажатии Shift можно прокручивать схему горизонтально
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
            switch (ke.getID()) {
                case KeyEvent.KEY_PRESSED:
                    if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                        shiftPressed.set(true);
                    }
                    break;

                case KeyEvent.KEY_RELEASED:
                    if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                        shiftPressed.set(false);
                    }
                    break;
            }
            return false;

        });

        // Прокручивание схемы
        panel.addMouseWheelListener(e -> {
            if (shiftPressed.get()) panel.startX += e.getWheelRotation() * -20;
            else panel.startY += e.getWheelRotation() * -20;
            panel.repaint();
        });

        fr.pack();
    }

    public static void main(String[] args) {
        DiagramDraw lab = new DiagramDraw();
        lab.run();
    }

    // Холст, на котором рисуется блок-схема
    class DrawingPanel extends JPanel {
        int startY = 0;
        int startX = 0;
        public DrawingPanel() {
            // Белый фон
            this.setBackground (Color.WHITE);
        }

        // Здесь происходит собственно рисование блок-схемы
        // Каждый блок организован как наследник класса Block, который реализует базовое отображение
        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            Graphics2D gr = (Graphics2D) g;
            gr.setFont(Utils.FONT);
            Utils.diagram.draw(gr);
            // Каждый блок принимает в качестве аргументов ширину, высоту, координаты X и У своего геометрического центра,
            // текст, отображаемый внутри блока, и объект gr, который внутри класса производит рисование.
            // Это позволяет избавиться от необходимости вызывать метод отрисовки для каждого созданного в этом методе
            // объекта.
            Terminator begin = new Terminator(100, 50, 250 + startX, 30 + startY, "Begin", gr);
            Operation input = new Operation(70, 40, 250 + startX, 100 + startY, "i := 0", gr);
            Loop.Start ls = new Loop.Start(100, 40, 250 + startX, 180 + startY, "Loop 1; i < 100", gr);
            IO output = new IO(70, 40, 250 + startX, 260 + startY, "Write i", gr);

            Operation inc = new Operation(70, 40, 350 + startX, 260 + startY, "i := i + 1", gr);
            Loop.End le = new Loop.End(70, 40, 350 + startX, 180 + startY, "Loop 1", gr);
            IO output2 = new IO(70, 40, 350 + startX, 100 + startY, "Write i", gr);
            Terminator end = new Terminator(70, 40, 350 + startX, 30 + startY, "End", gr);


            // Соединения между блоками реализованы чейнингом методов,
            // где каждый последующий блок доступен для соединения с другим
            // без необходимости обращаться к нему как к переменной
            begin.connectTo(input)
                    .connectTo(ls)
                    .connectTo(output)
                    .RIGHT.connectTo(inc.LEFT)
                    .TOP.connectTo(le.BOTTOM)
                    .TOP.connectTo(output2.BOTTOM)
                    .TOP.connectTo(end.BOTTOM);

            Utils.diagram = new Diagram(begin, input, ls, output, inc, le, output2, end);
        }

    }
}

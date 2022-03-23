package Schemes.Blocks;

import java.awt.*;

// Цикл.
// Так как цикл состоит из двух блоков, внутри есть классы для верхнего и нижнего блоков.
public class Loop {
    public static class Start extends Block{
        public Start(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
            super(pivotX, pivotY,
                    new int[] {
                            pivotX - width / 2 + 10,
                            pivotX + width / 2 - 10,
                            pivotX + width / 2,
                            pivotX + width / 2,
                            pivotX - width / 2,
                            pivotX - width / 2
                    },
                    new int[] {
                            pivotY - height / 2,
                            pivotY - height / 2,
                            pivotY - height / 2 + 10,
                            pivotY + height / 2,
                            pivotY + height / 2,
                            pivotY - height / 2 + 10
                    },
                    text, g);
            type = Block.TYPE_LOOP_START;

        }
    }

    public static class End extends Block{
        public End(int width, int height, int pivotX, int pivotY, String text, Graphics2D g) {
            super(pivotX, pivotY,
                    new int[] {
                            pivotX - width / 2,
                            pivotX + width / 2,
                            pivotX + width / 2,
                            pivotX + width / 2 - 10,
                            pivotX - width / 2 + 10,
                            pivotX - width / 2
                    },
                    new int[] {
                            pivotY - height / 2,
                            pivotY - height / 2,
                            pivotY + height / 2 - 10,
                            pivotY + height / 2,
                            pivotY + height / 2,
                            pivotY + height / 2 - 10
                    },
                    text, g);

            type = Block.TYPE_LOOP_END;

        }
    }
}

package Schemes.Utils;

import Schemes.Blocks.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Данный класс нужен для сохранения и восстановления блок-схемы
 */
public class Diagram {
    ArrayList<GsonBlock> blocks = new ArrayList<>();
    public Diagram(Block... blocks) {
        for (Block b : blocks) this.blocks.add(new GsonBlock(b));
    }

    /**
     * Данный метод сохраняет блок-схему в файл scheme.txt в формате JSON
     * @throws IOException
     */
    public void save() throws IOException {

        File f = new File("scheme.txt");
        f.createNewFile();
        FileWriter w = new FileWriter("scheme.txt");
        Gson gson = new Gson();
        String json = gson.toJson(this);
        json = json.replaceAll(" ", "___");
        w.write(json);
        w.close();
    }

    /**
     * Данный метод рисует сохранённую схему
     * @param g объект графики для рисования на холсте
     */
    public void draw(Graphics2D g) {
        ArrayList<Block> toDraw = new ArrayList<>();
        blocks.forEach(b -> {
            b.text = b.text.replaceAll("___", " ");
            switch (b.type) {
                case Block.TYPE_BRANCHING -> toDraw.add(new Branching(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_CALL -> toDraw.add(new Call(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_CONNECTOR -> toDraw.add(new Connector(b.width, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_IO -> toDraw.add(new IO(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_LOOP_START -> toDraw.add(new Loop.Start(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_LOOP_END -> toDraw.add(new Loop.End(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_OPERATION -> toDraw.add(new Operation(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_PREPARATION -> toDraw.add(new Preparation(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
                case Block.TYPE_TERMINATOR -> toDraw.add(new Terminator(b.width, b.height, b.pivotX, b.pivotY, b.text, g));
            }
            toDraw.get(toDraw.size() - 1).connections = b.connections;
        });

        toDraw.forEach(block -> {
            block.connections.forEach(connection -> {
                switch (connection.first.side) {
                    case Block.Point.TOP -> block.TOP.restoreConnection(connection.second);
                    case Block.Point.BOTTOM -> block.BOTTOM.restoreConnection(connection.second);
                    case Block.Point.LEFT -> block.LEFT.restoreConnection(connection.second);
                    case Block.Point.RIGHT -> block.RIGHT.restoreConnection(connection.second);
                    default -> connection.first.restoreConnection(connection.second);
                }
            });
        });
    }

    /**
     * Данный метод читает схему из файла scheme.txt
     * @return Объект сохранённой схемы или пустую схему, если произошла ошибка
     */
    public static Diagram read() {
        try {
            Scanner s = new Scanner(new File("scheme.txt"));
            String json = s.next();
            JsonReader reader = new JsonReader(new StringReader(json.replaceAll(" ", "___")));
            reader.setLenient(true);
            return new Gson().fromJson(reader, Diagram.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Diagram();
    }

    /**
     * Так как сохранять напрямую объект Block не представляется возможным (из-за огромной цепи наследования),
     * этот класс хранит в себе все важные свойства этого объекта, и сериализуется именно он
     */
    class GsonBlock {
        protected String text;
        protected final int width, height, pivotX, pivotY;

        protected final int x1, x2, x3, x4;
        protected final int y1, y2, y3, y4;
        public Block.Point TOP;
        public Block.Point BOTTOM;
        public Block.Point LEFT;
        public Block.Point RIGHT;
        public ArrayList<Pair<Block.Point>> connections;

        public int type;
        GsonBlock(Block block) {
            this.text = block.getText();
            this.height = block.height;
            this.width = block.width;
            this.pivotX = block.pivotX;
            this.pivotY = block.pivotY;
            x1 = pivotX - width / 2; x2 = pivotX + width / 2; x3 = x2; x4 = x1;
            y1 = pivotY - height / 2; y2 = y1; y3 = pivotY + height / 2; y4 = y3;
            TOP = block.TOP;
            BOTTOM = block.BOTTOM;
            LEFT = block.LEFT;
            RIGHT = block.RIGHT;
            connections = block.connections;
            type = block.type;
        }
    }
}

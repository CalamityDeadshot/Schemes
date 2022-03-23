# Schemes
This is a fun little project about drawing flowcharts through code. It provides a nice interface for generating a flowchart.
```java
public void paintComponent (Graphics g) {
    super.paintComponent(g);
    Graphics2D gr = (Graphics2D) g;
    gr.setFont(Utils.FONT);
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
```
This code will generate a simple flowchart.

![image](https://user-images.githubusercontent.com/44675043/159743322-018e0207-0f0d-42e3-a821-6398c071292a.png)

The main idea of this project is to use OOP to its fullest extent.

## Saving to JSON
The flowchart can also be saved in a JSON format, like [this](https://github.com/CalamityDeadshot/Schemes/blob/master/scheme.txt)

import java.awt.*;

public class Component {
    int x, y, shape, rotation;
    Color color;
    Component(int x, int y, int shape, Color color, int r) {
        this.x = x;
        this.y = y;
        this.rotation = r;
        this.shape = shape;
        this.color = color;
    }

    public Component(Component c) {
        this(c.x, c.y, c.shape, c.color, c.rotation);
    }
}

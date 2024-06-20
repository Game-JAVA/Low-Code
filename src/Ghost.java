import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ghost extends Character {
    private Color color;

    public Ghost(int x, int y, int tileSize, Color color) {
        super(x, y, tileSize);
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public void moveTowards(int pacManX, int pacManY, MazeBlock[][] map) {
        int dx = Integer.compare(pacManX, x);
        int dy = Integer.compare(pacManY, y);

        if (Math.random() > 0.5) {
            move(dx, 0, map);
        } else {
            move(0, dy, map);
        }
    }
}
import javafx.scene.canvas.GraphicsContext;

public abstract class Character {

    // Attributes

    protected int x;
    protected int y;
    protected int tileSize;

    // Constructors
    public Character(int x, int y, int tileSize) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
    }

    // Methods

    public abstract void draw(GraphicsContext gc);

    public abstract void move(int dx, int dy, MazeBlock[][] map);

    // Getters and Setters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
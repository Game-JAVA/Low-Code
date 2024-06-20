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

    public void move(int dx, int dy, MazeBlock[][] map) {
        int newX = x + dx;
        int newY = y + dy;

        if (newX >= 0 && newX < map[0].length && newY >= 0 && newY < map.length && !map[newY][newX].isWall()) {
            x = newX;
            y = newY;
        }
    }

    // Getters and Setters

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

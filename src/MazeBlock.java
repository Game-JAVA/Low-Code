import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeBlock {

    // Attributes

    private int type;
    private int x;
    private int y;
    private int tileSize;

    // Constructor

    public MazeBlock(int type, int x, int y, int tileSize) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
    }

    // Methods

    public void draw(GraphicsContext gc) {
        switch (type) {
            case 0:
                gc.setFill(Color.BLUE); // Wall
                break;
            default:
                gc.setFill(Color.BLACK); // Empty space
                break;
        }
        gc.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);

        if (type == 5) {
            gc.setFill(Color.WHITE);
            gc.fillOval(x * tileSize + tileSize / 4, y * tileSize + tileSize / 4, tileSize / 4, tileSize / 4);
        }
    }

    public boolean isWall() {
        return type == 0;
    }

    public boolean isPellet() {
        return type == 5;
    }

    // Getters and Setters
    public void setType(int type) {
        this.type = type;
    }
}
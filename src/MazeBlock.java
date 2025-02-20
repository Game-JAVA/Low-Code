import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MazeBlock {

    // Attributes

    private int type;
    private final int x;
    private final int y;
    private final int tileSize;
    private Image image;

    // Constructor

    public MazeBlock(int type, int x, int y, int tileSize, Image image) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.image = image;
    }

    // Methods
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public boolean isWall() {
        return type != 0 && type != 5 && type != 10 && type != 1 && type != 2 && type != 3 && type != 4;
    }

    public boolean isPellet() {
        return type == 5;
    }

    public boolean isSuperPellet() {
        return type == 10;
    }

    // Getters and Setters
    public void setType(int type) {
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }
}

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PacMan extends Character {

    // Attributes

    private Image image;

    // Constructor

    public PacMan(int x, int y, int tileSize, Image image) {
        super(x, y, tileSize);
        this.image = image;
    }

    // Methods

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public void collectPellet(MazeBlock[][] map) {
        if (map[y][x].isPellet()) {
            map[y][x].setType(-1); // Limpar o pellet
        }
    }

    // Getters and Setters

    public void setImage(Image image) {
        this.image = image;
    }
}
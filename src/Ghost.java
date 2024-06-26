import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ghost extends Character {

    // Attributes
    private Image image;
    private String direction;
    private long lastMoveTime;
    private long moveInterval; // Intervalo de movimento em milissegundos

    // Constructor
    public Ghost(int x, int y, int tileSize, Image image) {
        super(x, y, tileSize);
        this.image = image;
        this.direction = "Right"; // Direção inicial
        this.moveInterval = 200; // Intervalo de movimento em milissegundos
        this.lastMoveTime = System.currentTimeMillis();
    }

    // Methods
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize);
    }

    @Override
    public void move(int dx, int dy, MazeBlock[][] map) {

    }

    // Getters and Setters
    public void setImage(Image image) {
        this.image = image;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setMoveInterval(long moveInterval) {
        this.moveInterval = moveInterval;
    }
}

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class PacMan extends Character {

    // Attributes
    private Image image;
    private String direction;
    private long lastMoveTime;
    private long moveInterval; // Intervalo de movimento em milissegundos
    private Image Empty = new Image("/assets/maze/0-Empty.png");;
    private int score; // Atributo para armazenar o score

    private DoubleProperty translateX;
    private DoubleProperty translateY;
    private GameBoard gameBoard; // Adicionar referência à GameBoard
    private double bufferX = 0; // Buffer para posição X
    private double bufferY = 0; // Buffer para posição Y

    // Constructor
    public PacMan(int x, int y, int tileSize, Image image) {
        super(x, y, tileSize);
        this.image = image;
        this.direction = "Right"; // Direção inicial
        this.moveInterval = 175; // Intervalo de movimento em milissegundos
        this.lastMoveTime = System.currentTimeMillis();
        this.gameBoard = gameBoard; // Inicializar referência à GameBoard
        this.translateX = new SimpleDoubleProperty(x * tileSize);
        this.translateY = new SimpleDoubleProperty(y * tileSize);
        this.score = 0; // Inicializa o score
    }

    // Methods
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, translateX.get(), translateY.get(), tileSize, tileSize);
    }

    @Override
    public void move(int dx, int dy, MazeBlock[][] map) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= moveInterval) {
            int newX = x;
            int newY = y;

            switch (direction) {
                case "Left-Up":
                case "Up":
                case "Right-Up":
                    newY = y - 1;
                    break;
                case "Left-Down":
                case "Down":
                case "Right-Down":
                    newY = y + 1;
                    break;
                case "Left":
                    newX = x - 1;
                    break;
                case "Right":
                    newX = x + 1;
                    break;
            }

            if (newX >= 0 && newX < map[0].length && newY >= 0 && newY < map.length && !map[newY][newX].isWall()) {
                animateMove(newX, newY);
                x = newX;
                y = newY;
            }
            lastMoveTime = currentTime;
        }
    }

    //Suaviazar movimento do PacMan
    public void animateMove(int newX, int newY){
        double targetX = newX * tileSize;
        double targetY = newY * tileSize;

        Timeline timeline = new Timeline();
        KeyValue kvX = new KeyValue(translateX, targetX);
        KeyValue kvY = new KeyValue(translateY, targetY);
        KeyFrame kf = new KeyFrame(Duration.millis(moveInterval), kvX, kvY);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public void collectPellet(MazeBlock[][] map) {
        if (map[y][x].isPellet()) {
            map[y][x].setType(0); // Limpar o superPellet
            map[y][x].setImage(Empty); // Limpar o superPellet
            score += 10; // Adiciona 10 pontos ao score
        }
    }


    public void collectSuperPellet(MazeBlock[][] map) {
        if (map[y][x].isSuperPellet()) {
            map[y][x].setType(0); // Limpar o superPellet
            map[y][x].setImage(Empty); // Limpar o superPellet
            score += 50; // Adiciona 50 pontos ao score
        }
    }

    // Getters and Setters
    public Image getImage(){
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setMoveInterval(long moveInterval) {
        this.moveInterval = moveInterval;
    }

    public int getScore() {
        return score; // Método para obter o score
    }

}
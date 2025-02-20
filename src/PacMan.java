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
    private final long moveInterval; // Intervalo de movimento em milissegundos
    private final Image Empty = new Image("/assets/maze/0-Empty.png");
    private int score; // Atributo para armazenar o score

    private boolean isHuntingGhosts = false; //indica se PacMan está no modo de caça aos fantasmas
    private long huntStartTime; //armazena o tempo de início do modo de caça aos fantasmas em nanossegundos

    private final DoubleProperty translateX;
    private final DoubleProperty translateY;

    private GameBoard gameBoard; // Declarar a variável gameBoard

    // Constructor
    public PacMan(int x, int y, int tileSize, Image image, GameBoard gameBoard) {
        super(x, y, tileSize);
        this.image = image;
        this.direction = "Right"; // Direção inicial
        this.moveInterval = 175; // Intervalo de movimento em milissegundos
        this.lastMoveTime = System.currentTimeMillis();
        this.translateX = new SimpleDoubleProperty(x * tileSize);
        this.translateY = new SimpleDoubleProperty(y * tileSize);
        this.score = 0; // Inicializa o score
        this.gameBoard = gameBoard; // Inicializa a variável gameBoard
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
                if (map[newY][newX].getType() == 2) {
                    score += 10;
                    gameBoard.setScore(score);
                    map[newY][newX].setImage(Empty);
                    map[newY][newX].setType(0);
                } else if (map[newY][newX].getType() == 3) {
                    score += 50;
                    gameBoard.setScore(score);
                    gameBoard.makeGhostsVulnerable();
                    map[newY][newX].setImage(Empty);
                    map[newY][newX].setType(0);
                    isHuntingGhosts = true;
                    huntStartTime = System.nanoTime();
                }

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

    //verifica se um movimento em uma determinada direção é possível
    public boolean canMoveToDirection(String newDirection, MazeBlock[][] map) {
        int newX = x;
        int newY = y;

        switch (newDirection) {
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

        // Verifica se a nova posição está dentro dos limites do mapa e se não é uma parede
        return newX >= 0 && newX < map[0].length && newY >= 0 && newY < map.length && !map[newY][newX].isWall();
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
            startHuntMode(); // Inicia o modo de caça aos fantasmas
            makeGhostsVulnerable(); // Torna todos os fantasmas vulneráveis
        }
    }

    // Este método torna todos os fantasmas vulneráveis
    private void makeGhostsVulnerable() {
        for (Ghost ghost : gameBoard.getGhosts()) {
            ghost.setVulnerable(true);
        }
    }

    // Este método retorna o estado de caça aos fantasmas
    public boolean isHuntingGhosts() {
        return isHuntingGhosts;
    }

    // Este método inicia o modo de caça
    private void startHuntMode() {
        isHuntingGhosts = true;
        huntStartTime = System.nanoTime();
    }

    public Image getImage() {
        return image;
    }

    // Getters and Setters

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score; // Método para obter o score
    }

    // Este método define a pontuação do jogo
    public void setScore(int score) {
        this.score = score;
    }

    // Este método define se PacMan está no modo de caça aos fantasmas
    public void setHuntingGhosts(boolean huntingGhosts) {
        isHuntingGhosts = huntingGhosts; // Atribui o valor passado como parâmetro à variável de instância 'isHuntingGhosts'
    }

    // Este método retorna o tempo de início do modo de caça
    public long getHuntStartTime() {
        return huntStartTime;// Retorna o valor da variável de instância 'huntStartTime'
    }

}
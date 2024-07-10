import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
public class GameBoard extends Application {

    // Attributes
    private PacMan pacMan;
    private Ghost[] ghosts;
    private int dx = 0;
    private int dy = 0;
    private boolean isGameOver = false;
    private boolean gameRunning = true;
    private long startTime;
    private int lives = 3;
    private List<int[]> collectedPellets = new ArrayList<>();
    private  int score;
    private final int[][] mazeLayout = {
            { 61,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  62 },
            { 60,  54,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  53,  54,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  53,  54,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  53,  54,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  41,  53,  60 },
            { 60,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  60 },
            { 60,  32,   5,  11,  42,  42,  12,   5,  11,  42,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  42,  12,   5,  11,  42,  42,  12,   5,  31,  60 },
            { 60,  32,  10,  31,  60,  60,  32,   5,  31,  60,  60,  60,  32,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  31,  60,  60,  60,  32,   5,  31,  60,  60,  32,  10,  31,  60 },
            { 60,  32,   5,  21,  41,  41,  22,   5,  21,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  22,   5,  21,  41,  41,  22,   5,  31,  60 },
            { 60,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  60 },
            { 60,  32,   5,  11,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  12,   5,  31,  60 },
            { 60,  32,   5,  21,  41,  41,  22,   5,  31,  32,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  31,  32,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  31,  32,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  31,  32,   5,  21,  41,  41,  22,   5,  31,  60 },
            { 60,  32,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,  31,  60 },
            { 60,  52,  42,  42,  42,  42,  12,   5,  31,  32,   5,  11,  12,   5,  31,  32,   5,  11,  42,  42,  51,  52,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  51,  52,  42,  42,  12,   5,  31,  32,   5,  11,  12,   5,  31,  32,   5,  11,  42,  42,  42,  42,  51,  60 },
            { 60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  31,  32,   5,  21,  22,   5,  21,  41,  41,  53,  54,  41,  41,  22,   0,  21,  22,   0,  21,  41,  41,  53,  54,  41,  41,  22,   5,  21,  22,   5,  31,  32,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60 },
            { 60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,  31,  32,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  31,  32,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60 },
            { 60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  31,  32,   5,  11,  42,  42,  42,  12,   5,  31,  32,   0,  81,  91,  95,   0,   0,  96,  91,  82,   0,  31,  32,   5,  11,  42,  42,  42,  12,   5,  31,  32,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60 },
            { 41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  22,   5,  31,  54,  41,  41,  22,   5,  21,  22,   0,  93,   0,   0,   0,   0,   0,   0,  94,   0,  21,  22,   5,  21,  41,  41,  53,  32,   5,  21,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41 },
            { 70,   0,   0,   0,   0,   0,   0,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   0,   0,  93,   0,   0,   0,   0,   0,   0,  94,   0,   0,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   0,   0,   0,   0,   0,   0,  70 },
            { 42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  51,  32,   5,  11,  42,  42,  42,  12,   0,  93,   0,   1,   2,   3,   4,   0,  94,   0,  11,  42,  42,  42,  12,   5,  31,  52,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42 },
            { 60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  21,  41,  41,  41,  22,   5,  21,  41,  41,  53,  32,   0,  83,  92,  92,  92,  92,  92,  92,  84,   0,  31,  54,  41,  41,  22,   5,  21,  41,  41,  41,  22,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60 },
            { 60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60 },
            { 60,  60,  60,  60,  60,  60,  32,   5,  31,  32,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  31,  32,   0,  11,  42,  42,  42,  42,  42,  42,  12,   0,  31,  32,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  31,  32,   5,  31,  60,  60,  60,  60,  60,  60 },
            { 60,  54,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  53,  60 },
            { 60,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  60 },
            { 60,  32,   5,  11,  42,  42,  12,   5,  11,  42,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  42,  12,   5,  11,  42,  42,  12,   5,  31,  60 },
            { 60,  32,   5,  21,  41,  53,  32,   5,  21,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  22,   5,  31,  54,  41,  22,   5,  31,  60 },
            { 60,  32,  10,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,  10,  31,  60 },
            { 60,  52,  42,  12,   5,  31,  32,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  11,  42,  42,  42,  42,  42,  42,  12,   5,  11,  12,   5,  31,  32,   5,  11,  42,  51,  60 },
            { 60,  54,  41,  22,   5,  21,  22,   5,  31,  32,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  31,  32,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  31,  32,   5,  21,  41,  41,  53,  54,  41,  41,  22,   5,  31,  32,   5,  21,  22,   5,  21,  41,  53,  60 },
            { 60,  32,   5,   5,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,  31,  32,   5,   5,   5,   5,   5,   5,  31,  60 },
            { 60,  32,   5,  11,  42,  42,  42,  42,  51,  52,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  51,  52,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  51,  52,  42,  42,  12,   5,  31,  32,   5,  11,  42,  42,  51,  52,  42,  42,  42,  42,  12,   5,  31,  60 },
            { 60,  32,   5,  21,  41,  41,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  22,   5,  21,  22,   5,  21,  41,  41,  41,  41,  41,  41,  41,  41,  22,   5,  31,  60 },
            { 60,  32,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  31,  60 },
            { 60,  52,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  51,  60 },
            { 63,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  60,  64 }
    };

    private final int TILE_SIZE = 30;
    private final int MAP_HEIGHT = mazeLayout.length;
    private final int MAP_WIDTH = mazeLayout[0].length;

    private final MazeBlock[][] map = new MazeBlock[MAP_HEIGHT][MAP_WIDTH];

    private Image pacmanRightUp;
    private Image pacmanRight;
    private Image pacmanRightDown;
    private Image pacmanLeftUp;
    private Image pacmanLeft;
    private Image pacmanLeftDown;

    private Image staticGhostUp;

    private Image Empty;
    private Image Pellet;
    private Image SuperPellet;
    private Image UpperLeftCornerObstacle;
    private Image ObstacleTopRightCorner;
    private Image LowerLeftCornerObstacle;
    private Image ObstacleLowerRightCorner;
    private Image LeftVerticalHalfObstacleOrRightVerticalCornerWall;
    private Image RightVerticalHalfObstacleOrLeftVerticalCornerWall;
    private Image LowerHalfHorizontalObstacleOrUpperHalfHorizontalWall;
    private Image UpperHalfHorizontalObstacleOrLowerHalfHorizontalWall;
    private Image LowerRightCurveObstacleOrLowerRightCornerWall;
    private Image LowerLeftCurveObstacleOrLowerLeftCornerWall;
    private Image ObstacleUpperRightCornerOrWallUpperRightCorner;
    private Image ObstacleUpperLeftCornerOrWallUpperLeftCorner;
    private Image FullBlock;
    private Image FullBlockTopLeftCornerRounded;
    private Image FullBlockTopRightCornerRounded;
    private Image FullBlockBottomLeftCornerRounded;
    private Image FullBlockBottomRightCornerRounded;
    private Image Portal;
    private Image CageUpperLeftCorner;
    private Image CageUpperRightCorner;
    private Image LowerLeftCornerCage;
    private Image CageLowerRightCorner;
    private Image UpperHorizontalHalfCage;
    private Image LowerHorizontalHalfCage;
    private Image LeftHalfVerticalCage;
    private Image RightHalfVerticalCage;
    private Image UpperHorizontalHalfCageWithRightDoorCorners;
    private Image UpperHorizontalHalfCageWithDoorCornersOnTheLeft;
    private Image DoorCage;

    private Image gameOverImage;
    private Image lifeImage;

    @Override
    public void start(Stage primaryStage) {
        loadImages();

        Pane root = new Pane();
        Canvas canvas = new Canvas(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        initializeMap();
        initializeCharacters();

        drawMap(gc);
        drawCharacters(gc);

        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        //TRAVA A TELA (REDIMENSIONAR)
        primaryStage.setResizable(false);


        primaryStage.setTitle("PacMan Game - Low Code");
        primaryStage.setScene(scene);
        primaryStage.show();

        startTime = System.nanoTime();

        AnimationTimer timer = new AnimationTimer() {
            private boolean gameOverShown = false;
            private long gameOverStartTime;

            @Override
            public void handle(long now) {
                if (gameRunning) {
                    update(now);
                    gc.clearRect(0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE); // Limpa o canvas
                    drawMap(gc);
                    drawCharacters(gc);
                    drawScore(gc); // Desenha o score

                    if (!isGameOver) {
                        pacMan.move(dx, dy, map);
                        checkCollision(); // Verificar colisão após cada movimento do PacMan

                        // Movimento dos fantasmas
                        for (Ghost ghost : ghosts) {
                            ghost.move(0, 0, map);
                        }
                        checkVulnerabilityTimeout(now);

                    } else {
                        if (!gameOverShown) {
                            gameOverStartTime = System.nanoTime();
                            gameOverShown = true;
                            //ACREDITO QUE AQUI DEVE ARMAZENAR OS SCORES E PELLETS COLETADOS
                        }

                        long elapsedTime = System.nanoTime() - gameOverStartTime;
                        double secondsToShowGameOver = 0.1; // Tempo desejado em segundos
                        if (elapsedTime >= secondsToShowGameOver * 1e9) {
                            // Reduz a imagem de Game Over após o tempo desejado
                            gc.drawImage(gameOverImage, (canvas.getWidth() - gameOverImage.getWidth() / 2) / 2,
                                    (canvas.getHeight() - gameOverImage.getHeight() / 2) / 2,
                                    gameOverImage.getWidth() / 2, gameOverImage.getHeight() / 2);

                            // Define o jogo como não rodando após mostrar Game Over
                            gameRunning = false;
                            PauseTransition delay = new PauseTransition(Duration.seconds(3));
                            delay.setOnFinished(e -> {
                                resetGame(); // Inicializar o estado do jogo
                            });
                            delay.play();
                        }
                    }
                }
            }
        };
        timer.start();
    }

    // Este método verifica se o tempo de vulnerabilidade dos fantasmas expirou
    private void checkVulnerabilityTimeout(long currentTime) {
        long vulnerabilityDuration = 10 * 1_000_000_000L; // 10 segundos em nanossegundos
        for (Ghost ghost : ghosts) {
            if (ghost.isVulnerable() && currentTime - ghost.getVulnerableStartTime() > vulnerabilityDuration) {
                ghost.setVulnerable(false);
            }
        }
    }

    // Este método retorna o array de fantasmas
    public Ghost[] getGhosts() {
        return ghosts;
    }

    // Incializando as imagens
    private void loadImages() {
        pacmanRightUp = new Image("/assets/pacman-Right-Up.gif");
        pacmanRight = new Image("/assets/pacman-Right.gif");
        pacmanRightDown = new Image("/assets/pacman-Right-Down.gif");
        pacmanLeftUp = new Image("/assets/pacman-Left-Up.gif");
        pacmanLeft = new Image("/assets/pacman-Left.gif");
        pacmanLeftDown = new Image("/assets/pacman-Left-Down.gif");

        staticGhostUp = new Image("/assets/staticGhostUp.gif");

        lifeImage = new Image("/assets/heart.png"); // Nova imagem para vida
        gameOverImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/game-over.png")));

        Empty  = new Image("/assets/maze/0-Empty.png");
        Pellet = new Image("/assets/maze/5-Pellet.png");
        SuperPellet = new Image("/assets/maze/10-Super-Pellet.png");
        UpperLeftCornerObstacle = new Image("/assets/maze/11-Upper-Left-Corner-Obstacle.png");
        ObstacleTopRightCorner = new Image("/assets/maze/12-Obstacle-Top-Right-Corner.png");
        LowerLeftCornerObstacle = new Image("/assets/maze/21-Lower-Left-Corner-Obstacle.png");
        ObstacleLowerRightCorner = new Image("/assets/maze/22-Obstacle-Lower-Right-Corner.png");
        LeftVerticalHalfObstacleOrRightVerticalCornerWall = new Image("/assets/maze/31-Left-Vertical-Half-Obstacle-Or-Right-Vertical-Corner-Wall.png");
        RightVerticalHalfObstacleOrLeftVerticalCornerWall = new Image("/assets/maze/32-Right-Vertical-Half-Obstacle-Or-Left-Vertical-Corner-Wall.png");
        LowerHalfHorizontalObstacleOrUpperHalfHorizontalWall = new Image("/assets/maze/41-Lower-Half-Horizontal-Obstacle-Or-Upper-Half-Horizontal-Wall.png");
        UpperHalfHorizontalObstacleOrLowerHalfHorizontalWall = new Image("/assets/maze/42-Upper-Half-Horizontal-Obstacle-Or-Lower-Half-Horizontal-Wall.png");
        LowerRightCurveObstacleOrLowerRightCornerWall = new Image("/assets/maze/51-Lower-Right-Curve-Obstacle-Or-Lower-Right-Corner-Wall.png");
        LowerLeftCurveObstacleOrLowerLeftCornerWall = new Image("/assets/maze/52-Lower-Left-Curve-Obstacle-Or-Lower-Left-Corner-Wall.png");
        ObstacleUpperRightCornerOrWallUpperRightCorner = new Image("/assets/maze/53-Obstacle-Upper-Right-Corner-Or-Wall-Upper-Right-Corner.png");
        ObstacleUpperLeftCornerOrWallUpperLeftCorner = new Image("/assets/maze/54-Obstacle-Upper-Left-Corner-Or-Wall-Upper-Left-Corner.png");
        FullBlock = new Image("/assets/maze/60-Full-Block.png");
        FullBlockTopLeftCornerRounded = new Image("/assets/maze/61-Full-Block-Top-Left-Corner-Rounded.png");
        FullBlockTopRightCornerRounded = new Image("/assets/maze/62-Full-Block-Top-Right-Corner-Rounded.png");
        FullBlockBottomLeftCornerRounded = new Image("/assets/maze/63-Full-Block-Bottom-Left-Corner-Rounded.png");
        FullBlockBottomRightCornerRounded = new Image("/assets/maze/64-Full-Block-Bottom-Right-Corner-Rounded.png");
        Portal = new Image("/assets/maze/70-Portal.png");
        CageUpperLeftCorner = new Image("/assets/maze/81-Cage-Upper-Left-Corner.png");
        CageUpperRightCorner = new Image("/assets/maze/82-Cage-Upper-Right-Corner.png");
        LowerLeftCornerCage = new Image("/assets/maze/83-Lower-Left-Corner-Cage.png");
        CageLowerRightCorner = new Image("/assets/maze/84-Cage-Lower-Right-Corner.png");
        UpperHorizontalHalfCage = new Image("/assets/maze/91-Upper-Horizontal-Half-Cage.png");
        LowerHorizontalHalfCage = new Image("/assets/maze/92-Lower-Horizontal-Half-Cage.png");
        LeftHalfVerticalCage = new Image("/assets/maze/93-Left-Half-Vertical-Cage.png");
        RightHalfVerticalCage = new Image("/assets/maze/94-Right-Half-Vertical-Cage.png");
        UpperHorizontalHalfCageWithRightDoorCorners = new Image("/assets/maze/95-Upper-Horizontal-Half-Cage-With-Right-Door-Corners.png");
        UpperHorizontalHalfCageWithDoorCornersOnTheLeft = new Image("/assets/maze/96-Upper-Horizontal-Half-Cage-With-Door-Corners-On-The-Left.png");
        DoorCage = new Image("/assets/maze/100-Door-Cage.png");
    }

    public Image getImageForType(int type) {
        switch (type) {
            case 0: return Empty;
            case 5: return Pellet;
            case 10: return SuperPellet;
            case 11: return UpperLeftCornerObstacle;
            case 12: return ObstacleTopRightCorner;
            case 21: return LowerLeftCornerObstacle;
            case 22: return ObstacleLowerRightCorner;
            case 31: return LeftVerticalHalfObstacleOrRightVerticalCornerWall;
            case 32: return RightVerticalHalfObstacleOrLeftVerticalCornerWall;
            case 41: return LowerHalfHorizontalObstacleOrUpperHalfHorizontalWall;
            case 42: return UpperHalfHorizontalObstacleOrLowerHalfHorizontalWall;
            case 51: return LowerRightCurveObstacleOrLowerRightCornerWall;
            case 52: return LowerLeftCurveObstacleOrLowerLeftCornerWall;
            case 53: return ObstacleUpperRightCornerOrWallUpperRightCorner;
            case 54: return ObstacleUpperLeftCornerOrWallUpperLeftCorner;
            case 60: return FullBlock;
            case 61: return FullBlockTopLeftCornerRounded;
            case 62: return FullBlockTopRightCornerRounded;
            case 63: return FullBlockBottomLeftCornerRounded;
            case 64: return FullBlockBottomRightCornerRounded;
            case 70: return Portal;
            case 81: return CageUpperLeftCorner;
            case 82: return CageUpperRightCorner;
            case 83: return LowerLeftCornerCage;
            case 84: return CageLowerRightCorner;
            case 91: return UpperHorizontalHalfCage;
            case 92: return LowerHorizontalHalfCage;
            case 93: return LeftHalfVerticalCage;
            case 94: return RightHalfVerticalCage;
            case 95: return UpperHorizontalHalfCageWithRightDoorCorners;
            case 96: return UpperHorizontalHalfCageWithDoorCornersOnTheLeft;
            case 100: return DoorCage;
            default: return null;
        }
    }

    // Inicializando mapa
    private void initializeMap() {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                Image img = getImageForType(mazeLayout[y][x]);
                map[y][x] = new MazeBlock(mazeLayout[y][x], x, y, TILE_SIZE, img);
            }
        }
    }

    // Inicializando personagens
    private void initializeCharacters() {
        pacMan = new PacMan(2, 2, TILE_SIZE, pacmanRight, this);
        pacMan.setScore(score);
        ghosts = new Ghost[] {
                new Ghost(GameBoard.ghostsLocation(1, mazeLayout)[1], GameBoard.ghostsLocation(1, mazeLayout)[0], TILE_SIZE, staticGhostUp),
                new Ghost(GameBoard.ghostsLocation(2, mazeLayout)[1], GameBoard.ghostsLocation(2, mazeLayout)[0], TILE_SIZE, staticGhostUp),
                new Ghost(GameBoard.ghostsLocation(3, mazeLayout)[1], GameBoard.ghostsLocation(3, mazeLayout)[0], TILE_SIZE, staticGhostUp),
                new Ghost(GameBoard.ghostsLocation(4, mazeLayout)[1], GameBoard.ghostsLocation(4, mazeLayout)[0], TILE_SIZE, staticGhostUp)
        };
    }

    // Desenhando
    private void drawMap(GraphicsContext gc) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x].draw(gc);
            }
        }
    }

    // Desenhando personagens
    private void drawCharacters(GraphicsContext gc) {
        pacMan.draw(gc);
        for (Ghost ghost : ghosts) {
            ghost.draw(gc);
        }
    }

    // Verificando teclas pressionadas pelo usuário
    private void handleKeyPressed(KeyEvent event) {
        if (isGameOver && event.getCode() == KeyCode.SPACE) {
            resetGame();
            return;
        }


        switch (event.getCode()) {
            case W:
            case UP:
                dx = 0;
                dy = -1;

                if (pacMan.getDirection().equals("Right") || pacMan.getDirection().equals("Left-Down")) {
                    pacMan.setImage(pacmanRightUp);
                    pacMan.setDirection("Right-Up");
                } else if (pacMan.getDirection().equals("Left") || pacMan.getDirection().equals("Right-Down")) {
                    pacMan.setImage(pacmanLeftUp);
                    pacMan.setDirection("Left-Up");
                }

                break;
            case S:
            case DOWN:
                dx = 0;
                dy = 1;

                if (pacMan.getDirection().equals("Right") || pacMan.getDirection().equals("Left-Up")) {
                    pacMan.setImage(pacmanRightDown);
                    pacMan.setDirection("Right-Down");
                } else if (pacMan.getDirection().equals("Left") || pacMan.getDirection().equals("Right-Up")) {
                    pacMan.setImage(pacmanLeftDown);
                    pacMan.setDirection("Left-Down");
                }

                break;
            case A:
            case LEFT:
                dx = -1;
                dy = 0;
                pacMan.setImage(pacmanLeft);
                pacMan.setDirection("Left");
                break;
            case D:
            case RIGHT:
                dx = 1;
                dy = 0;
                pacMan.setImage(pacmanRight);
                pacMan.setDirection("Right");
                break;
            default:
                break;
        }
    }


    private void handleKeyReleased(KeyEvent event) {
        if (!isGameOver) {
            dx = 0;
            dy = 0;
        }
    }

    // checkCollision() para verificar colisão entre PacMan e fantasmas
    // checkCollision() para verificar colisão entre PacMan e fantasmas
    private void checkCollision() {
        // Obter as coordenadas do PacMan
        int pacManX = pacMan.getX();
        int pacManY = pacMan.getY();

        // Verificar colisão com cada fantasma
        for (Ghost ghost : ghosts) {
            int ghostX = ghost.getX();
            int ghostY = ghost.getY();

            if (pacManX == ghostX && pacManY == ghostY) {
                // Colisão detectada
                if (pacMan.isHuntingGhosts()) {
                    // PacMan está no modo de caça
                    ghost.resetPosition();
                    pacMan.setScore(pacMan.getScore() + 200); // Pontuação ao comer um fantasma
                } else {
                    // PacMan não está no modo de caça
                    lives--;
                    if (lives > 0) {
                        savePelletsState();
                        score = pacMan.getScore();
                        initializeCharacters();
                    } else {
                        isGameOver = true;
                    }
                }
                break;
            }
        }
    }

    private void update(long currentTime) {
        pacMan.move(dx, dy, map);
        pacMan.collectPellet(map);
        pacMan.collectSuperPellet(map);
        checkCollision();

        if (pacMan.isHuntingGhosts()) {
            long elapsedTime = currentTime - pacMan.getHuntStartTime();
            if (elapsedTime >= 10_000_000_000L) { // 10 segundos em nanossegundos
                pacMan.setHuntingGhosts(false); // Fim do modo de caça
                for (Ghost ghost : ghosts) {
                    ghost.setVulnerable(false); // Fim da vulnerabilidade dos fantasmas
                }
            }
        }


    if (currentTime - startTime > 5_000_000_000L) { // 5 segundos em nanossegundos
            for (Ghost ghost : ghosts) {
                ghost.setChasing(true);
            }
        }

        for (Ghost ghost : ghosts) {
            ghost.move(pacMan, map, currentTime);
        }
    }


    private void drawScore(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Define o tamanho da fonte para 20 e negrito
        gc.fillText("Score: " + pacMan.getScore(), 50, 40);

        // Desenhar o placar, incluindo as vidas
        int startX = MAP_WIDTH * TILE_SIZE - 150; // Posição inicial X no canto superior direito
        int startY = 20; // Posição inicial Y no canto superior

        // Desenha as vidas restantes
        for (int i = 0; i < lives; i++) {
            gc.drawImage(lifeImage, startX + i * 40, startY, 20, 20); // Ajusta o tamanho para 20x20
        }
    }


    // Obter a posição que os fantasmas devem estar
    private static int[] ghostsLocation(int ghostNumber, int[][] mazeLayout) {
        int[] result = new int[2]; // Array para armazenar a posição [i, j]

        for (int i = 0; i < mazeLayout.length; i++) {
            for (int j = 0; j < mazeLayout[0].length; j++) {
                if (mazeLayout[i][j] == ghostNumber) {
                    result[0] = i; // Armazena a posição Y
                    result[1] = j; // Armazena a posição X
                    return result; // Retorna a posição assim que encontra o fantasma
                }
            }
        }

        // Se o fantasma não for encontrado retorna [-1, -1] como uma posição inválida.
        return new int[]{-1, -1};
    }
    // Função para salvar o estado dos pellets
    private void savePelletsState() {
        collectedPellets.clear();
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (map[y][x].getType() == 0 && map[y][x].getImage() == Pellet) {
                    collectedPellets.add(new int[]{x, y});
                }
            }
        }
    }

    // Função para restaurar o estado dos pellets
    private void restorePelletState() {
        for (int[] pellet : collectedPellets) {
            map[pellet[1]][pellet[0]].setImage(Pellet);
        }
    }

    private void resetGame() {
        gameRunning = true;
        isGameOver = false;
        System.out.println("Game Resetado");
        lives = 3;
        collectedPellets.clear();
        score = 0;
        initializeMap();
        initializeCharacters();
        dx = 0;
        dy = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Este método torna todos os fantasmas vulneráveis
    public void makeGhostsVulnerable() {
        for (Ghost ghost : ghosts) {
            ghost.setVulnerable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameBoard extends Application {

    // Attributes

    private static final int TILE_SIZE = 64;
    private static final int MAP_WIDTH = 15;
    private static final int MAP_HEIGHT = 15;

    private MazeBlock[][] map = new MazeBlock[MAP_HEIGHT][MAP_WIDTH];
    private PacMan pacMan;
    //    private Ghost[] ghosts;
    private int dx = 0;
    private int dy = 0;

    private int[][] mazeLayout = {
            {19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22},
            {17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20},
            {25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20},
            {0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20},
            {19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20},
            {17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,  0, 21},
            {17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,  0, 21},
            {17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,  0, 21},
            {17, 16, 16, 20, 0,  17, 16, 16, 16, 16, 18, 18, 18, 18, 20},
            {17, 24, 24, 28, 0,  25, 24, 24, 16, 16, 16, 16, 16, 16, 20},
            {21, 0,  0,  0,  0,  0,  0,  0,  17, 16, 16, 16, 16, 16, 20},
            {17, 18, 18, 22, 0,  19, 18, 18, 16, 16, 16, 16, 16, 16, 20},
            {17, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 20},
            {17, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 20},
            {25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28}
    };

    private Image pacmanUp;
    private Image pacmanDown;
    private Image pacmanLeft;
    private Image pacmanRight;

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
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        primaryStage.setTitle("PacMan Game - Low Code");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                drawMap(gc);
                drawCharacters(gc);
            }
        };
        timer.start();
    }

    // Incializando as imagens
    private void loadImages() {
        pacmanUp = new Image("/assets/up.gif");
        pacmanDown = new Image("/assets/down.gif");
        pacmanLeft = new Image("/assets/left.gif");
        pacmanRight = new Image("/assets/right.gif");
    }

    // Inicializando mapa
    private void initializeMap() {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x] = new MazeBlock(mazeLayout[y][x], x, y, TILE_SIZE);
            }
        }
    }

    // Inicializando personagens
    private void initializeCharacters() {
        pacMan = new PacMan(1, 1, TILE_SIZE, pacmanRight);
//         ghosts = new Ghost[] { new Ghost(14, 15, TILE_SIZE, Color.RED), new Ghost(13,
//         15, TILE_SIZE, Color.PINK), new Ghost(14, 14, TILE_SIZE, Color.CYAN), new Ghost(13,
//         14, TILE_SIZE, Color.ORANGE) };
    }

    // Desenhando mapa
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
//         for (Ghost ghost : ghosts) {
//         ghost.draw(gc);
//         }
    }


    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W:
            case UP:
                dx = 0;
                dy = -1;
                pacMan.setImage(pacmanUp);
                break;
            case S:
            case DOWN:
                dx = 0;
                dy = 1;
                pacMan.setImage(pacmanDown);
                break;
            case A:
            case LEFT:
                dx = -1;
                dy = 0;
                pacMan.setImage(pacmanLeft);
                break;
            case D:
            case RIGHT:
                dx = 1;
                dy = 0;
                pacMan.setImage(pacmanRight);
                break;
            default:
                break;
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        dx = 0;
        dy = 0;
    }

    private void update() {
        pacMan.move(dx, dy, map);
        pacMan.collectPellet(map);
//         for (Ghost ghost : ghosts) {
//         ghost.moveTowards(pacMan.getX(), pacMan.getY(), map);
//         }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

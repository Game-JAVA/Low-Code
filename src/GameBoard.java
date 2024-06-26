import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameBoard extends Application {

    // Attributes
    private PacMan pacMan;
    private Ghost[] ghosts;
    private int dx = 0;
    private int dy = 0;

    private int[][] mazeLayout = {
            {  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5,  5,  5,  5,  5,  5,  5,  0,  0,  5,  5,  5,  5,  5,  5, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5 },
            {  5,  0,  0,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0,  0,  0,  5 },
            { 10,  0,  0,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0,  0,  0, 10 },
            {  5,  0,  0,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0,  0,  0,  5 },
            {  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5 },
            {  5,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,  0,  0,  5,  0,  0,  0,  0,  0,  0,  0,  0,  5,  0,  0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  5 },
            {  5,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,  0,  0,  5,  0,  0,  0,  0,  0,  0,  0,  0,  5,  0,  0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  5 },
            {  5,  5,  5,  5,  5, 5, 0, 0, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5,  0,  0,  5,  5,  5,  5,  0,  0,  5,  5,  5,  5,  0,  0, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5, 0, 0, 5,  5,  5,  5,  5,  5 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0, 20,  0,  0, 20,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0, 20,  0,  0, 20,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 5, 5, 5, 5, 5, 5, 5,  0,  0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,  0,  0, 5, 5, 5, 5, 5, 5, 5, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0, 20,  0,  0,  0, 20, 20,  0,  0,  0, 20,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0, 20,  0, 20, 20, 20, 20, 20, 20,  0, 20,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            { 20, 20, 20, 20, 20, 5, 5, 5, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5, 20, 20, 20,  0, 20, 20, 20, 20, 20, 20,  0, 20, 20, 20, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5, 5, 5, 5, 20, 20, 20, 20, 20 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 0,  0,  0, 20,  0, 20,  1,  2,  3,  4, 20,  0, 20,  0,  0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 0,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0, 20,  0,  0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  0,  0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,  0,  0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0, 20,  0,  0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0, 20,  0,  0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  0,  0,  0 },
            {  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5,  5,  5,  5,  5,  5,  5,  0,  0,  5,  5,  5,  5,  5,  5, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5 },
            {  5,  0,  0,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0,  0,  0,  5 },
            {  5,  0,  0,  0,  0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 5,  0,  0,  0,  0,  5 },
            { 10,  5,  5,  0,  0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  0,  0,  5,  5, 10 },
            {  0,  0,  5,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,  0,  0,  5,  0,  0,  0,  0,  0,  0,  0,  0,  5,  0,  0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  5,  0,  0 },
            {  0,  0,  5,  0,  0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,  0,  0,  5,  0,  0,  0,  0,  0,  0,  0,  0,  5,  0,  0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5,  0,  0,  5,  0,  0 },
            {  5,  5,  5,  5,  5, 5, 0, 0, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5,  0,  0,  5,  5,  5,  5,  0,  0,  5,  5,  5,  5,  0,  0, 5, 5, 5, 5, 0, 0, 5, 5, 5, 5, 0, 0, 5,  5,  5,  5,  5,  5 },
            {  5,  0,  0,  0,  0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  5 },
            {  5,  0,  0,  0,  0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0,  0,  0,  0,  0,  0,  5,  0,  0,  5,  0,  0,  0,  0,  0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  5 },
            {  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,  5,  5,  5,  5,  5 }
    };

    private final int TILE_SIZE = 30;
    private final int MAP_HEIGHT = mazeLayout.length;
    private final int MAP_WIDTH = mazeLayout[0].length;

    private MazeBlock[][] map = new MazeBlock[MAP_HEIGHT][MAP_WIDTH];

    private Image pacmanRightUp;
    private Image pacmanRight;
    private Image pacmanRightDown;
    private Image pacmanLeftUp;
    private Image pacmanLeft;
    private Image pacmanLeftDown;

    private Image staticGhostUp;

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
        pacmanRightUp = new Image("/assets/pacman-Right-Up.gif");
        pacmanRight = new Image("/assets/pacman-Right.gif");
        pacmanRightDown = new Image("/assets/pacman-Right-Down.gif");
        pacmanLeftUp = new Image("/assets/pacman-Left-Up.gif");
        pacmanLeft = new Image("/assets/pacman-Left.gif");
        pacmanLeftDown = new Image("/assets/pacman-Left-Down.gif");

        staticGhostUp = new Image("/assets/staticGhostUp.gif");
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
        pacMan = new PacMan(0, 0, TILE_SIZE, pacmanRight);
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
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].draw(gc);
        }
    }

    // Verificando teclas pressionadas pelo usuário
    private void handleKeyPressed(KeyEvent event) {
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
        dx = 0;
        dy = 0;
    }

    private void update() {
        pacMan.move(dx, dy, map);
        pacMan.collectPellet(map);
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

    public static void main(String[] args) {
        launch(args);
    }
}

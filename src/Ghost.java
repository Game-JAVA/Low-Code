import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost extends Character {

    // Atributos
    private int initialX; // Coordenada x inicial do fantasma
    private int initialY; // Coordenada y inicial do fantasma
    private Image image; // Imagem do fantasma
    private long lastMoveTime; // Tempo do último movimento
    private double moveInterval = 185_000_000; // Intervalo de meio segundo (em nanossegundos)
    private double speed = 0.004;
    private boolean isChasing = false; // Indica se o fantasma está perseguindo o Pac-Man
    private boolean isExitingBase = true; // Indica se o fantasma está saindo da base
    private boolean hasExitedBase = false; // Indica se o fantasma já saiu da base
    private static final int CHASE_RANGE = 5; // Distância de perseguição em blocos
    private int lastDirection = -1; // Última direção do movimento

    private DoubleProperty translateX;
    private DoubleProperty translateY;

    // Construtor
    public Ghost(int x, int y, int tileSize, Image image) {
        super(x, y, tileSize);
        this.initialX = x;
        this.initialY = y;
        this.image = image;
        this.lastMoveTime = System.nanoTime(); // Inicializa o tempo do último movimento com o tempo atual
        this.translateX = new SimpleDoubleProperty(x * tileSize);
        this.translateY = new SimpleDoubleProperty(y * tileSize);
    }

    // Métodos
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, translateX.get(), translateY.get(), tileSize, tileSize); // Desenha o fantasma na tela
    }

    @Override
    public void move(int dx, int dy, MazeBlock[][] map) {}

    public void move(PacMan pacMan, MazeBlock[][] map, long currentTime) {
        if (currentTime - lastMoveTime > moveInterval) { // Verifica se o intervalo de tempo para o próximo movimento já passou
            lastMoveTime = currentTime; // Atualiza o tempo do último movimento

            if (isExitingBase) {
                exitBase(map); // Se o fantasma estiver saindo da base, executa a lógica para sair da base
            } else if (pacMan != null && isWithinChaseRange(pacMan)) {
                isChasing = true; // Se o Pac-Man estiver dentro da distância de perseguição, ativa o modo de perseguição
                chasePacMan(pacMan, map); // Executa a lógica de perseguição
            } else {
                isChasing = false; // Caso contrário, desativa o modo de perseguição
                moveRandomly(map); // Executa a lógica de movimento aleatório
            }
        }
    }

    private boolean isWithinChaseRange(PacMan pacMan) {
        int dx = Math.abs(pacMan.getX() - x); // Calcula a diferença absoluta na coordenada x
        int dy = Math.abs(pacMan.getY() - y); // Calcula a diferença absoluta na coordenada y
        return dx + dy <= CHASE_RANGE; // Verifica se a soma das diferenças está dentro da distância de perseguição
    }

    private void chasePacMan(PacMan pacMan, MazeBlock[][] map) {
        int targetX = pacMan.getX(); // Coordenada x do Pac-Man
        int targetY = pacMan.getY(); // Coordenada y do Pac-Man

        List<int[]> possibleMoves = new ArrayList<>(); // Lista de movimentos possíveis

        // Verifica os movimentos possíveis (cima, baixo, esquerda, direita)
        if (targetX < x && !map[y][x - 1].isWall()) {
            possibleMoves.add(new int[]{-1, 0}); // Adiciona movimento para a esquerda
        }
        if (targetX > x && !map[y][x + 1].isWall()) {
            possibleMoves.add(new int[]{1, 0}); // Adiciona movimento para a direita
        }
        if (targetY < y && !map[y - 1][x].isWall()) {
            possibleMoves.add(new int[]{0, -1}); // Adiciona movimento para cima
        }
        if (targetY > y && !map[y + 1][x].isWall()) {
            possibleMoves.add(new int[]{0, 1}); // Adiciona movimento para baixo
        }

        // Move na direção do Pac-Man em uma das direções possíveis
        if (!possibleMoves.isEmpty()) {
            int[] move = possibleMoves.get(new Random().nextInt(possibleMoves.size())); // Escolhe uma direção aleatória
            animateMove(x + move[0], y + move[1]);
            x += move[0]; // Atualiza a coordenada x
            y += move[1]; // Atualiza a coordenada y
        }
    }

    private void exitBase(MazeBlock[][] map) {
        if (y > 0 && (map[y - 1][x].getType() == 100 || !map[y - 1][x].isWall())) {
            animateMove(x, y - 1);
            y--; // Move o fantasma para cima se não houver parede
        } else {
            isExitingBase = false; // Indica que o fantasma saiu da base
            hasExitedBase = true; // Indica que o fantasma já saiu da base
        }
    }

    public void moveRandomly(MazeBlock[][] map) {
        Random random = new Random();
        List<Integer> possibleDirections = new ArrayList<>(); // Lista de direções possíveis

        // Adiciona todas as direções possíveis, exceto a oposta do último movimento
        if (lastDirection != 1 && y > 0 && (map[y - 1][x].getType() == 100 || !map[y - 1][x].isWall()) && (!hasExitedBase || y - 1 >= 0) && !(x == initialX && y - 1 == initialY)) {
            possibleDirections.add(0); // Adiciona movimento para cima
        }
        if (lastDirection != 0 && y < map.length - 1 && (map[y + 1][x].getType() == 100 || !map[y + 1][x].isWall()) && !isExitingBase && !(x == initialX && y + 1 == initialY)) {
            possibleDirections.add(1); // Adiciona movimento para baixo
        }
        if (lastDirection != 3 && x > 0 && (map[y][x - 1].getType() == 100 || !map[y][x - 1].isWall()) && !(x - 1 == initialX && y == initialY)) {
            possibleDirections.add(2); // Adiciona movimento para a esquerda
        }
        if (lastDirection != 2 && x < map[0].length - 1 && (map[y][x + 1].getType() == 100 || !map[y][x + 1].isWall()) && !(x + 1 == initialX && y == initialY)) {
            possibleDirections.add(3); // Adiciona movimento para a direita
        }

        if (!possibleDirections.isEmpty()) {
            int direction = possibleDirections.get(random.nextInt(possibleDirections.size())); // Escolhe uma direção aleatória
            lastDirection = direction; // Atualiza a última direção

            switch (direction) {
                case 0: // Cima
                    animateMove(x, y - 1);
                    y--;
                    break;
                case 1: // Baixo
                    animateMove(x, y + 1);
                    y++;
                    break;
                case 2: // Esquerda
                    animateMove(x - 1, y);
                    x--;
                    break;
                case 3: // Direita
                    animateMove(x + 1, y);
                    x++;
                    break;
                default:
                    break;
            }
        } else {
            // Se não houver direções possíveis, escolhe uma direção aleatória para evitar ficar preso
            lastDirection = -1;
        }
    }

    // Método para animar o movimento do fantasma
    private void animateMove(int newX, int newY) {
        double targetX = newX * tileSize;
        double targetY = newY * tileSize;

        Timeline timeline = new Timeline();
        KeyValue kvX = new KeyValue(translateX, targetX);
        KeyValue kvY = new KeyValue(translateY, targetY);
        KeyFrame kf = new KeyFrame(Duration.millis(moveInterval / 1_000_000), kvX, kvY);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    // Getters e Setters
    public int getX() {
        return x; // Retorna a coordenada x
    }

    public int getY() {
        return y; // Retorna a coordenada y
    }

    public void setChasing(boolean chasing) {
        isChasing = chasing; // Define se o fantasma está perseguindo
    }
}

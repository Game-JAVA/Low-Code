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

import java.util.*;

public class Ghost extends Character {

    // Atributos
    private int initialX; // Coordenada x inicial do fantasma
    private int initialY; // Coordenada y inicial do fantasma
    private Image image; // Imagem do fantasma
    private long lastMoveTime; // Tempo do último movimento
    private double moveInterval = 185_000_000; // Intervalo de meio segundo (em nanossegundos)
    private boolean isChasing = false; // Indica se o fantasma está perseguindo o Pac-Man
    private boolean isExitingBase = true; // Indica se o fantasma está saindo da base
    private boolean hasExitedBase = false; // Indica se o fantasma já saiu da base
    private static final int CHASE_RANGE = 10; // Distância de perseguição em blocos
    private int lastDirection = -1; // Última direção do movimento

    private boolean isVulnerable = false;
    private long vulnerableStartTime;


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

            if (isVulnerable) {
                // Lógica para fazer o fantasma fugir do Pac-Man
                fleeFromPacMan(pacMan, map);
            }else if (isExitingBase) {
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

        List<int[]> path = findPath(x, y, targetX, targetY, map);
        if (!path.isEmpty()) {
            int[] nextMove = path.get(0);
            animateMove(nextMove[0], nextMove[1]);
            x = nextMove[0];
            y = nextMove[1];
        }
    }

    private void fleeFromPacMan(PacMan pacMan, MazeBlock[][] map) {
        int targetX = pacMan.getX();
        int targetY = pacMan.getY();
        List<int[]> possibleMoves = new ArrayList<>();

        if (targetX > x && !map[y][x - 1].isWall()) possibleMoves.add(new int[]{-1, 0});
        if (targetX < x && !map[y][x + 1].isWall()) possibleMoves.add(new int[]{1, 0});
        if (targetY > y && !map[y - 1][x].isWall()) possibleMoves.add(new int[]{0, -1});
        if (targetY < y && !map[y + 1][x].isWall()) possibleMoves.add(new int[]{0, 1});

        if (!possibleMoves.isEmpty()) {
            int[] move = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
            animateMove(x + move[0], y + move[1]);
            x += move[0];
            y += move[1];
        }
    }

    private List<int[]> findPath(int startX, int startY, int targetX, int targetY, MazeBlock[][] map) {
        Queue<int[]> queue = new LinkedList<>();
        Map<String, int[]> predecessors = new HashMap<>();
        queue.add(new int[]{startX, startY});
        String targetKey = targetX + "," + targetY;
        boolean found = false;

        while (!queue.isEmpty() && !found) {
            int[] current = queue.poll();
            int currentX = current[0];
            int currentY = current[1];

            for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int newX = currentX + dir[0];
                int newY = currentY + dir[1];
                String key = newX + "," + newY;

                if (newX >= 0 && newX < map[0].length && newY >= 0 && newY < map.length && !map[newY][newX].isWall() && !predecessors.containsKey(key)) {
                    predecessors.put(key, new int[]{currentX, currentY});
                    queue.add(new int[]{newX, newY});
                    if (key.equals(targetKey)) {
                        found = true;
                        break;
                    }
                }
            }
        }

        List<int[]> path = new ArrayList<>();
        if (found) {
            int[] step = new int[]{targetX, targetY};
            while (!Arrays.equals(step, new int[]{startX, startY})) {
                path.add(0, step);
                step = predecessors.get(step[0] + "," + step[1]);
            }
        }

        return path;
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

    public void resetPosition() {
        x = initialX;
        y = initialY;
        isExitingBase = true;
        hasExitedBase = false;
        isVulnerable = false; // Reseta o estado de vulnerabilidade
    }

    public boolean isVulnerable() {
        return isVulnerable;
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

    public long getVulnerableStartTime() {
        return vulnerableStartTime;
    }

    public void setVulnerable(boolean vulnerable) {
        isVulnerable = vulnerable;
        if (vulnerable) {
            vulnerableStartTime = System.nanoTime();
        }
    }
}

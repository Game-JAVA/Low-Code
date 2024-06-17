# Game-JAVA 📜
This repository is a teaching project developed during the Internship at the Systems Analysis and Development faculty.

## Description

The PACMAN game takes place through a maze, with the objective of eating all the lozenges and super lozenges scattered along the way. The player must avoid being captured by the ghosts that patrol the maze. When colliding with a tablet, PACMAN consumes it and it disappears from the maze. If the player manages to eat all the gum, he wins the game.

The development of this game was done in JavaFX.

# Game Class Diagram

```

---
title: UML Diagram
---

classDiagram

Characters <|-- Pacman
Characters <|-- Ghost
MazeBlock <|-- GameBoard
Pellets <|-- GameBoard
Pellets <|-- Powerup
GameBoard <|-- EventRunner

class GameBoard{
    - titleScreenImage : Image
    - endGameImage : Image
    - winScreenImage: Image
    - pacman : Pacman
    - ghosts : Ghost[]
    - score : int
    - numLives : int
    - mazeBlockPositions : int[][]
    - mazeBlocks: MazeBlock[][]
    - tempPellets : Pellet[][]
    - showTitleScreen : boolean
    - showWinScreen : boolean
    - showEndGameScreen : boolean
    - initiateNewGame : int
    - pacmanDying : int
    - timer : long
    - font : Font
    - font2 : Font
    + GameBoard()
    + reset() void
    + repositionCharacters() void
    + paintComponent(Graphics) void
    + collided() boolean
    + pacmanCanEatGhost() int
    + eatGhost() void
    + decrementLives() void
    + getLives() int
    + eatPellets() void
    + incrementScore(int) void
    + getScore() int
}

class Characters{
    - up : int
    - down : int
    - left : int
    - right : int
    + getX() int
    + getY() int
    + setX(int) void
    + setY(int) void
    + setDirection(String) void
    + move() void
    + isFacingBlock() boolean
    + respawn() void
    + paintCharacter(Graphics) void
}

class Pacman{
    - PACMAN_WIDTH : int
    - PACMAN_HEIGHT : int
    - x : int
    - y: int
    - counter : int
    - pelletsEaten : int
    - xIncrement : int
    - yIncrement : int
    - characterDirection : String
    - hasPower : boolean
    + Pacman(int, int) void
    + getX() int
    + getY() int
    + setX(int) void
    + setY(int) void
    + setDirection(String) void
    + isFacingBlock() boolean
    + respawn() void
    + colOnMaze() int
    + rowOnMaze() int
    + incrementPelletsEaten()  void
    + numPelletsEaten() int
    + hasPower() boolean
    + givePower() void
    + resetPower() void
    + move() void
    + moveRight() void
    + moveLeft() void
    + paintCharacter(Graphics) void
    + moveUp() void
    + moveDown() void
}

class Ghost{
    - GHOST_WIDTH : int
    - GHOST_HEIGHT : int
    - x : int
    - y : int
    - characterDirection: String
    + Ghost(int, int)
    + getX() int
    + getY() int
    + setX(int) void
    + setY(int) void
    + setDirection(String) void
    + getDirection() String
    + isFacingBlock() boolean
    + isAtIntersection()boolean
    + reverse() void
    + respawn() void
    + paintCharacter(Graphics) void
}

class MazeBlock{
    - BLOCK_WIDTH : int
    - BLOCK_HEIGHT : int
    - x : int
    - y : int
    + MazeBlock(int, int)
    + paintBlock(Graphics) void
}

class Pellets{
    - PELLET_WIDTH : int
    - PELLET_WIDTH : int
    - x : int
    - y : int
    + Pellet(int, int)
    + paintPellet(Graphics) void
}

class Powerup{
    + Powerup(int, int)
    + paintPellet(Graphics) void
}

class EventRunner{
    - gameBoard : GameBoard
    - frameTimer : Timer
    + EventRunner()
    + performActions(boolean) void
    + keyPressed(KeyEvent) void
    + keyReleased(KeyEvent) void
    + keyTyped(KeyEvent) void
    + main(String[]) void
}

```
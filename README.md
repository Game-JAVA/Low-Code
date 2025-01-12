# Game-JAVA 📜
This repository is a teaching project developed during the Internship at the Systems Analysis and Development faculty.

## 📖 Description

The PACMAN game takes place through a maze, with the objective of eating all the lozenges and super lozenges scattered along the way. The player must avoid being captured by the ghosts that patrol the maze. When colliding with a tablet, PACMAN consumes it and it disappears from the maze. If the player manages to eat all the gum, he wins the game.

The development of this game was done in JavaFX.

## 🔍 Instructions

### 1. Welcome Screen:
* There will be two buttons, one to start the game and the other to see the controls. Both will be in the middle of the screen, the start game button will be on top of the controls button.

### 2. Gameplay

* PACMAN will spawn in the middle of the screen, his goal is to eat all the pellets that will be scattered around the map while escaping from the GHOSTS that will try to catch him.

* The score increases as PACMAN consumes the pellets, when PACMAN consumes the larger pellet the GHOSTS will be vulnerable and can also be consumed by PACMAN, there will be some fruits scattered around the map, if these fruits are consumed by PACMAN it will gain more score.

* When the game starts, four GHOSTS will spawn in the base, but only two will leave the base at first, only after 5 seconds, the other two GHOSTS will leave the base.

* The game ends only when PACMAN has consumed all the pellets scattered around the map. When the game ends, a new level begins. With each new level, there will be some changes making the game more difficult:
    > By consuming a large pellet, the vulnerability time of the GHOSTS will be shorter.

    > The movement speed of GHOSTS is increased.
    
    > The view distance of GHOSTS is increased.


## 🕹️ Controls
* W-A-S-D or arrow keys to move the character.
* Press 'ESC' to pause the game.

## 💻 Requirements
* Java IDE or Compiler required to run the game.

## 📥 Installation
To install the game you must have the JAVA Project Execution Software on your computer, the recommended software is
[🧷JetBrains IntelliJ IDEA Community Edition 2024.1.3](https://www.jetbrains.com/idea/download/?section=windows), which is what we used to develop the game.

After installing the software, the next step is to clone our project. To do this, follow this step by step:
 > Click: "<> Code" > HTTPS > Copy url to clipboard.

 > Open IntelliJ IDEA.
 
 > Click: Projects > Get from VCS > Repository URL > Git(Version Control) > Paste the url in "URL:" > Select the local directory in "Directory" > Clone.

#

# Game Class Diagram

```mermaid
---
title: UML Diagram
---

classDiagram

Character <|-- PacMan
Character <|-- Ghost
MazeBlock <|-- GameBoard

class GameBoard {
    - pacMan: PacMan
    - ghosts: Ghost[]
    - dx: int
    - dy: int

    - isGameOver: boolean
    - gameRunning: boolean
    - startTime: long

    - mazeLayout: int[][]

    - TILE_SIZE: int
    - MAP_HEIGHT: int
    - MAP_WIDTH: int

    - map: MazeBlock[][]

    - pacmanRightUp: Image
    - pacmanRight: Image
    - pacmanRightDown: Image
    - pacmanLeftUp: Image
    - pacmanLeft: Image
    - pacmanLeftDown: Image

    - staticGhostUp: Image

    - Empty: Image
    - Pellet: Image
    - SuperPellet: Image
    - UpperLeftCornerObstacle: Image
    - ObstacleTopRightCorner: Image
    - LowerLeftCornerObstacle: Image
    - ObstacleLowerRightCorner: Image
    - LeftVerticalHalfObstacleOrRightVerticalCornerWall: Image
    - RightVerticalHalfObstacleOrLeftVerticalCornerWall: Image
    - LowerHalfHorizontalObstacleOrUpperHalfHorizontalWall: Image
    - UpperHalfHorizontalObstacleOrLowerHalfHorizontalWall: Image
    - LowerRightCurveObstacleOrLowerRightCornerWall: Image
    - LowerLeftCurveObstacleOrLowerLeftCornerWall: Image
    - ObstacleUpperRightCornerOrWallUpperRightCorner: Image
    - ObstacleUpperLeftCornerOrWallUpperLeftCorner: Image
    - FullBlock: Image
    - FullBlockTopLeftCornerRounded: Image
    - FullBlockTopRightCornerRounded: Image
    - FullBlockBottomLeftCornerRounded: Image
    - FullBlockBottomRightCornerRounded: Image
    - Portal: Image
    - CageUpperLeftCorner: Image
    - CageUpperRightCorner: Image
    - LowerLeftCornerCage: Image
    - CageLowerRightCorner: Image
    - UpperHorizontalHalfCage: Image
    - LowerHorizontalHalfCage: Image
    - LeftHalfVerticalCage: Image
    - RightHalfVerticalCage: Image
    - UpperHorizontalHalfCageWithRightDoorCorners: Image
    - UpperHorizontalHalfCageWithDoorCornersOnTheLeft: Image
    - DoorCage: Image

    - gameOverImage: Image
    - lifeImage: Image

    + start(Stage) void
    + loadImages() void
    + getImageForType(int) Image
    + initializeMap() void
    + initializeCharacters() void
    + drawMap(GraphicsContext) void
    + drawCharacters(GraphicsContext) void
    + handleKeyPressed(KeyEvent) void
    + handleKeyReleased(KeyEvent) void
    + checkCollision() void
    + update(long) void
    + drawScore(GraphicsContext) void
    + ghostLocation(int, int[][]) int[]
    + resetGame() void
    + main(String[]) void
}

class Character {
    - x: int
    - y: int
    - tileSize: int

    + Character(int, int, int)

    + draw(GraphicsContext) void
    + move(int, int, MazeBlock[][]) void

    + getX() int
    + getY() int
}

class PacMan {
    - image: Image
    - direction: String
    - lastMoveTime: long
    - moveInterval: long
    - Empty: Image
    - score: int

    - translateX: DoubleProperty
    - translateY: DoubleProperty

    - gameBoard: GameBoard
    - speed: double
    - bufferX: double
    - bufferY: double

    + PacMan(int, int, int, Image, Image)

    + draw(GraphicsContext) void
    + move(int, int, MazeBlock[][]) void
    + animateMove(int, int) void
    + collectPellet(MazeBlock[][]) void
    + collectSuperPellet(MazeBlock[][]) void

    + setImage(Image) void
    + getDirection() String
    + setDirection(String) void
    + getScore() int
}

class Ghost {
    - initialX: int
    - initialY: int
    - image: Image
    - lastMoveTime: long
    - moveInterval: double
    - speed: double
    - isChasing: boolean
    - isExitingBase: boolean
    - hasExitedBase: boolean
    - CHASE_RANGE: int
    - lastDirection: int

    + Ghost(int, int, int, Image)

    + draw(GraphicsContext) void
    + move(int, int, MazeBlock[][]) void
    + move(PacMan, MazeBlock[][], long) void
    + isWithinChaseRange(PacMan) boolean
    + chasePacMan(PacMan, MazeBlock[][]) void
    + exitBase(MazeBlock[][]) void
    + moveRandomly(MazeBlock[][]) void

    + getX() int
    + getY() int
    + setChasing(boolean) void
}

class MazeBlock {
    - type: int
    - x: int
    - y: int
    - tileSize: int
    - image: Image

    + MazeBlock(int, int, int, int, Image)

    + draw(GraphicsContext) void

    + isWall() boolean
    + isPellet() boolean
    + isSuperPellet() boolean

    + setType(int) void
    + setImage(Image) void
    + getType() int
}

```
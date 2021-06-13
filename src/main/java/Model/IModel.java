package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Observer;

public interface IModel
{
    /**
     * @param rows - the number of rows of the maze
     * @param cols - the number of cols of the maze
     */
    void generateMaze(int rows, int cols);

    /**
     * @return the maze matrix of the model.
     */
    int[][] getMaze();

    /**
     * solves the maze of the IModel
     */
    void solveMaze();

    /**
     * @return the solution of the IModel's maze
     */
    Solution getSolution();

    /**
     * moves the player to the movementDirection if the move is legal.
     * @param movementDirection the Direction the player wants to move
     */
    void updatePlayerPosition(Direction movementDirection);

    /**
     * @return the row index of the player
     */
    int getPlayerRow();

    /**
     * @return the column index of the player
     */
    int getPlayerCol();

    /**
     * @param o add observer o
     */
    void assignObserver(Observer o);

    /**
     * @param filePath the path where you want to create the maze file into.
     */
    void saveMaze(String filePath);

    /**
     * @param filePath the file path of the maze file you want to load
     */
    void loadMaze(String filePath);

    /**
     * @return the row index of the goal position of the maze
     */
    int getGoalRowPosition();

    /**
     * @return the column index of the goal position of the maze
     */
    int getGoalColPosition();

    /**
     * start the servers
     */
    void startServers();

    /**
     * stop the servers
     */
    void stopServers();


}

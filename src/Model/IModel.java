package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.File;
import java.util.Observer;

public interface IModel
{
    void generateMaze(int rows, int cols);
    int[][] getMaze();
    void solveMaze();
    Solution getSolution();
    void updatePlayerPosition(Direction movementDirection);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void saveMaze(String filePath);
    void loadMaze(String filePath);
    int getGoalRowPosition();
    int getGoalColPosition();
    int getStartRowPosition();
    int getStartColPosition();
    void setMaze(Maze newMaze);
    void startServers();
    void stopServers();

}

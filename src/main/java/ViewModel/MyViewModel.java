package ViewModel;

import Model.Direction;
import Model.IModel;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import com.sun.glass.ui.View;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer

{
    private IModel model;
    public MyViewModel(IModel model)
    {
        this.model = model;
        this.model.assignObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * @return the maze of the model
     */
    public int[][] getMaze()
    {
        return model.getMaze();
    }

    /**
     * @return the row index of the player in the IModel
     */
    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    /**
     * @return the column index of the player in the IModel
     */
    public int getPlayerCol(){
        return model.getPlayerCol();
    }


    /**
     * @return the solution of the IModel's maze
     */
    public Solution getSolution(){
        return model.getSolution();
    }

    /**
     * @param rows a string representing the rows of the maze
     * @param cols a string representing the cols of the maze
     */
    public void generateMaze(String rows, String cols)
    {
        if(checkMazeGenarationParams(rows,cols) == true)
        {
            model.generateMaze(Integer.parseInt(rows), Integer.parseInt(cols));
        }
        else
        {
            setChanged();
            notifyObservers("invalid params");
        }

    }

    /**
     * asks the IModel to solve it's maze.
     */
    public void solveMaze(){
        model.solveMaze();
    }


    /**
     * @param keyCode the keyCode representing the direction you want the player to move to.
     */
    public void movePlayer(KeyCode keyCode){
        Direction direction;
        switch (keyCode){
            case UP, NUMPAD8 -> direction = Direction.UP;
            case DOWN, NUMPAD2 -> direction = Direction.DOWN;
            case LEFT, NUMPAD4 -> direction = Direction.LEFT;
            case RIGHT, NUMPAD6 -> direction = Direction.RIGHT;
            case NUMPAD1 -> direction = Direction.DOWNLEFT;
            case NUMPAD3 -> direction = Direction.DOWNRIGHT;
            case NUMPAD7 -> direction = Direction.UPLEFT;
            case NUMPAD9 -> direction = Direction.UPRIGHT;
            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerPosition(direction);
    }

    /**
     * @param filePath the path where you want to create the maze file into.
     */
    public void saveMaze(String filePath)
    {
        if (filePath != null)
            model.saveMaze(filePath);
    }


    /**
     * @param filePath the file path of the maze file you want to load
     */
    public void loadMaze(String filePath)
    {
        if (filePath != null)
            model.loadMaze(filePath);
    }

    /**
     * start the IModel's servers
     */
    public void startServers()
    {
        model.startServers();
    }

    /**
     * stops the IModel's servers
     */
    public void stopServers()
    {
        model.stopServers();
    }


    /**
     * @param rows the string representing the rows of the maze
     * @param cols the string representing the cols of the maze
     * @return true if the strings represent integer in number format between 2 to 1000, false otherwise
     */
    public boolean checkMazeGenarationParams(String rows, String cols)
    {
        if(numberOrNot(rows) == false ||numberOrNot(cols) == false )
        {
            return false;
        }
        if(Integer.parseInt(rows) <=1 || Integer.parseInt(cols) <= 1 || Integer.parseInt(rows) >1000 || Integer.parseInt(cols) >1000)
        {
            return false;
        }
        return true;
    }

    /**
     * @param input the string you want to check if it is in number format
     * @return true if the string is in number format, false otherwise
     */
    private boolean numberOrNot(String input)
    {
        try
        {
            Integer.parseInt(input);
        }
        catch(NumberFormatException ex)
        {
            return false;
        }
        return true;
    }


    /**
     * @return the goal position of the IModel's maze
     */
    public Position getGoalPosition()
    {
        Position goalPos = new Position( model.getGoalRowPosition(), model.getGoalColPosition());
        return goalPos;
    }


}

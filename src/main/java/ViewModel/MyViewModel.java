package ViewModel;

import Model.Direction;
import Model.IModel;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import com.sun.glass.ui.View;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer

{
    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
    public int[][] getMaze()
    {
        return model.getMaze();
    }
    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }
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
    public void solveMaze(){
        model.solveMaze();
    }
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
    public void saveMaze(String filePath)
    {
        if (filePath != null)
            model.saveMaze(filePath);
    }
    public void loadMaze(String filePath)
    {
        if (filePath != null)
            model.loadMaze(filePath);
    }
    public void startServers()
    {
        model.startServers();
    }
    public void stopServers()
    {
        model.stopServers();
    }
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
    public Position getGoalPosition()
    {
        Position goalPos = new Position( model.getGoalRowPosition(), model.getGoalColPosition());
        return goalPos;
    }
}
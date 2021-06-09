package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel
{


    private Server generateMazeServer;
    private Server solveMazeServer;
    private Maze maze;
    private Solution solution;
    private boolean startServers;
    private int startPositionRow;
    private int startPositionCol;
    private int playerRow;
    private int playerCol;
    private int goalPositionRow;
    private int goalPositionCol;
    private boolean reachedGoal;
    public MyModel()
    {
        generateMazeServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveMazeServer = new Server(5401 , 1000, new ServerStrategySolveSearchProblem());
        playerRow = 0 ;
        playerCol = 0 ;
        goalPositionCol = 0;
        goalPositionRow= 0 ;
        startPositionRow = 0 ;
        startPositionCol = 0 ;
        reachedGoal = false;

    }

    private void serverGenerateMaze(int rows, int cols)
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows * cols +20 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze newMaze = new Maze(decompressedMaze);
                        maze = newMaze;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void generateMaze(int rows, int cols)
    {
        serverGenerateMaze(rows,cols);
        goalPositionRow = maze.getGoalPosition().getRowIndex();
        goalPositionCol = maze.getGoalPosition().getColumnIndex();
        startPositionRow = maze.getStartPosition().getRowIndex();
        startPositionCol = maze.getStartPosition().getColumnIndex();
        playerRow = startPositionRow;
        playerCol = startPositionCol;
        reachedGoal = false;
        setChanged();
        notifyObservers("maze generated");
    }

    private void serverSolveMaze()
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        solution = mazeSolution;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Solution getSolution()
    {
        return solution;
    }
    @Override
    public void solveMaze()
    {
        serverSolveMaze();
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public void updatePlayerPosition(Direction movementDirection)
    {
        switch (movementDirection)
        {
            case UP:
                if(playerRow > 0 && maze.getMazeMatrix()[playerRow-1][playerCol] == 0 )
                    playerRow -- ;
                break;
            case DOWN:
                if(playerRow < maze.getHeight()-1 && maze.getMazeMatrix()[playerRow+1][playerCol] == 0 )
                    playerRow ++ ;
                break;

            case LEFT:
                if(playerCol > 0 && maze.getMazeMatrix()[playerRow][playerCol-1] == 0 )
                    playerCol-- ;
                break;

            case RIGHT:
                if(playerCol < maze.getWidth()-1 && maze.getMazeMatrix()[playerRow][playerCol+1] == 0 )
                    playerCol ++ ;
                break;

            case UPLEFT:
                if(playerRow > 0 && playerCol >0 && maze.getMazeMatrix()[playerRow-1][playerCol-1] == 0 &&
                        (maze.getMazeMatrix()[playerRow-1][playerCol] == 0 || maze.getMazeMatrix()[playerRow][playerCol-1] == 0 ))
                {
                    playerRow -- ;
                    playerCol -- ;
                }
                break;

            case UPRIGHT:
                if(playerRow > 0 && playerCol < maze.getWidth()-1 && maze.getMazeMatrix()[playerRow-1][playerCol+1] == 0 &&
                        (maze.getMazeMatrix()[playerRow-1][playerCol] == 0 || maze.getMazeMatrix()[playerRow][playerCol+1] == 0 ))
                {
                    playerRow -- ;
                    playerCol++;
                }
                break;

            case DOWNLEFT:
                if(playerRow < maze.getHeight() -1 && playerCol > 0 && maze.getMazeMatrix()[playerRow+1][playerCol-1] == 0 &&
                        (maze.getMazeMatrix()[playerRow][playerCol-1] == 0 || maze.getMazeMatrix()[playerRow+1][playerCol] == 0 ))
                {
                    playerRow++ ;
                    playerCol--;
                }
                break;

            case DOWNRIGHT:
                if(playerRow < maze.getHeight() -1 && playerCol < maze.getWidth()-1  && maze.getMazeMatrix()[playerRow+1][playerCol+1] == 0 &&
                        (maze.getMazeMatrix()[playerRow+1][playerCol] == 0 || maze.getMazeMatrix()[playerRow][playerCol+1] == 0 ))
                {
                    playerRow ++ ;
                    playerCol ++;
                }
                break;

        }
        if(playerCol == goalPositionCol && playerRow == goalPositionRow && reachedGoal == false)
        {
            reachedGoal = true;
            setChanged();
            notifyObservers("reached goal position"); //todo: remmember to move the player in this case also


        }
        else
        {
            setChanged();
            notifyObservers("updated player position");
        }
    }
    @Override
    public void saveMaze(String filePath)
    {
        try
        {
            Position playerPosition = new Position(playerRow,playerCol);
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(maze);
            objectOutputStream.writeObject(playerPosition);
            objectOutputStream.flush();
            fileOutputStream.close();
            objectOutputStream.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void loadMaze(String filePath)
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            maze = (Maze) objectInputStream.readObject();
            Position playerPosition = (Position) objectInputStream.readObject();
            playerRow = playerPosition.getRowIndex();
            playerCol = playerPosition.getColumnIndex();
            goalPositionRow = maze.getGoalPosition().getRowIndex();
            goalPositionCol = maze.getGoalPosition().getColumnIndex();
            startPositionRow = maze.getStartPosition().getRowIndex();
            startPositionCol = maze.getStartPosition().getColumnIndex();
            fileInputStream.close();
            objectInputStream.close();
            reachedGoal = false;
            setChanged();
            notifyObservers("loaded a maze");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public int[][] getMaze() {
        return maze.getMazeMatrix();
    }







    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }
    public void setPlayerRow(int playerRow) {
        this.playerRow = playerRow;
    }

    public void setPlayerCol(int playerCol) {
        this.playerCol = playerCol;
    }


    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }



    @Override
    public int getGoalRowPosition() {
        return goalPositionRow;
    }

    @Override
    public int getGoalColPosition() {
        return goalPositionCol;
    }
    public void setGoalPositionRow(int goalPositionRow) {
        this.goalPositionRow = goalPositionRow;
    }

    public void setGoalPositionCol(int goalPositionCol) {
        this.goalPositionCol = goalPositionCol;
    }


    @Override
    public int getStartRowPosition() {
        return maze.getStartPosition().getRowIndex();
    }

    @Override
    public int getStartColPosition() {
        return maze.getStartPosition().getColumnIndex();
    }

    @Override
    public void setMaze(Maze newMaze) {
        this.maze = newMaze;

    }
    public void startServers() {
        startServers = true;
        generateMazeServer.start();
        solveMazeServer.start();
    }

    public void stopServers() {
        if(startServers){
            generateMazeServer.stop();
            solveMazeServer.stop();
        }
    }
}

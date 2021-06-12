package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final Logger LOG= LogManager.getLogger();

    public MyModel()
    {
        generateMazeServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        LOG.info("Generate maze server initialized at port 5400");
        solveMazeServer = new Server(5401 , 1000, new ServerStrategySolveSearchProblem());
        LOG.info("Solve maze server initialized at port 5401");
        playerRow = 0 ;
        playerCol = 0 ;
        goalPositionCol = 0;
        goalPositionRow= 0 ;
        startPositionRow = 0 ;
        startPositionCol = 0 ;
        reachedGoal = false;

    }

    /**
     * helper fun to generate the maze using the server
     * @param rows the numer of rows of the maze
     * @param cols the number of cols of the maze
     */
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
                        LOG.info("User " +  InetAddress.getLocalHost() + "requested to generate a maze of size [" + rows + "] [" + cols + "]" );
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows * cols +20 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze newMaze = new Maze(decompressedMaze);
                        maze = newMaze;
                    } catch (Exception e) {
                        LOG.error("Caught exception: " + e);

                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            LOG.error("Caught UnknownHostException: " + e);
        }
    }

    /**
     * generates a maze
     * @param rows - the number of rows of the maze
     * @param cols - the number of cols of the maze
     */
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

    /**
     * helper fun to solve the maze using the server
     */
    private void serverSolveMaze()
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        LOG.info("User " + InetAddress.getLocalHost() + " requested to solve a maze");
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        solution = mazeSolution;
                        LOG.info("Server succeeded to solve the maze of user " + InetAddress.getLocalHost()+ "the length of the solution is " + solution.getSolutionPath().size() );
                    } catch (Exception e) {
                        LOG.error("Caught exception: " + e);
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            LOG.error("Caught UnknownHostException: " + e);
        }
    }

    /**
     * @return the soultion of the IModel's maze
     */
    @Override
    public Solution getSolution()
    {
        return solution;
    }

    /**
     * solves the maze of the IModel
     */
    @Override
    public void solveMaze()
    {
        serverSolveMaze();
        setChanged();
        notifyObservers("maze solved");
    }

    /**
     * moves the player to the movementDirection if the move is legal.
     * @param movementDirection the Direction the player wants to move
     */
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
            notifyObservers("reached goal position");


        }
        else
        {
            setChanged();
            notifyObservers("updated player position");
        }
    }

    /**
     * @param filePath the path where you want to create the maze file into.
     */
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
            LOG.info("saved a maze to the disk");
        }
        catch (Exception e)
        {
            LOG.error("Caught exception: " + e);
        }

    }

    /**
     * @param filePath the file path of the maze file you want to load
     */
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
            LOG.info("loaded a maze from the disk");
            setChanged();
            notifyObservers("loaded a maze");
        }
        catch (Exception e)
        {
            LOG.error("Caught exception: " + e);
        }
    }

    /**
     * @return the maze matrix of the model.
     */
    @Override
    public int[][] getMaze() {
        return maze.getMazeMatrix();
    }

    /**
     * @return the row index of the player
     */
    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * @return the column index of the player
     */
    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * @param o add observer o
     */
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    /**
     * @return the row index of the goal position of the maze
     */
    @Override
    public int getGoalRowPosition() {
        return goalPositionRow;
    }

    /**
     * @return the column index of the goal position of the maze
     */
    @Override
    public int getGoalColPosition() {
        return goalPositionCol;
    }

    /**
     * start the servers
     */
    public void startServers() {
        startServers = true;
        generateMazeServer.start();
        solveMazeServer.start();
        LOG.info("started servers");
    }

    /**
     * stop the servers
     */
    public void stopServers() {
        if(startServers){
            generateMazeServer.stop();
            solveMazeServer.stop();
            LOG.info("stopped servers");
        }
    }
}

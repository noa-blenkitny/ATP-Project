package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class mazeWindowController extends AView implements Initializable, Observer {

    public Slider soundBar;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseMusic("maze");
        soundBar.setValue(mp.getVolume() * 100);
        soundBar.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable observable) {
                mp.setVolume(soundBar.getValue() / 100);
            }
        });

        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        mazeDisplayer.changePlayer(choosePlayerController.getChosenPlayer());

    }

    /**
     * @param actionEvent the event - click Generate button
     */
    public void generateMaze(ActionEvent actionEvent) {
        String rows = textField_mazeRows.getText();
        String cols = textField_mazeColumns.getText();
        myViewModel.generateMaze(rows, cols);
    }

    /**
     * @param actionEvent the event - click Solve button
     */
    public void solveMaze(ActionEvent actionEvent) {
        myViewModel.solveMaze();
    }

    /**
     * @param keyEvent - the event - key pressed to move the player
     */
    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.movePlayer(keyEvent.getCode());
        keyEvent.consume();
    }

    /**
     * @param row new row after moving in the maze
     * @param col new column after moving in the maze
     */
    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    /**
     * @param mouseEvent when the mouse clicked on the maze displayer the focus will be on iy
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /**
     * @param mouseEvent moving the player in the maze according to mouse dragging
     */
    public void mouseDragged(MouseEvent mouseEvent){
            int maxSize = Math.max(myViewModel.getMaze()[0].length, myViewModel.getMaze().length);
            double cellHeight = mazeDisplayer.getHeight() / myViewModel.getMaze().length;
            double cellWidth = mazeDisplayer.getWidth() /myViewModel.getMaze()[0].length;
            double canvasHeight = mazeDisplayer.getHeight();
            double canvasWidth = mazeDisplayer.getWidth();
            int rowMazeSize = myViewModel.getMaze().length;
            int colMazeSize = myViewModel.getMaze()[0].length;
            double startRow = (canvasHeight / 2 - (cellHeight * rowMazeSize / 2)) / cellHeight;
            double startCol = (canvasWidth / 2 -(cellWidth * colMazeSize / 2)) / cellWidth;
            double mouseX =(int) ((mouseEvent.getX()) / (mazeDisplayer.getWidth()  / maxSize) - startCol);
            double mouseY =(int) ((mouseEvent.getY()) / (mazeDisplayer.getHeight() / maxSize) - startRow);

                if (mouseY < myViewModel.getPlayerRow() && mouseX == myViewModel.getPlayerCol()) {
                    myViewModel.movePlayer(KeyCode.UP);
                }
                if (mouseY > myViewModel.getPlayerRow() && mouseX == myViewModel.getPlayerCol()) {
                    myViewModel.movePlayer(KeyCode.DOWN);
                }
                if (mouseX < myViewModel.getPlayerCol() && mouseY == myViewModel.getPlayerRow()) {
                    myViewModel.movePlayer(KeyCode.LEFT);
                }
                if (mouseX > myViewModel.getPlayerCol() && mouseY == myViewModel.getPlayerRow()) {
                    myViewModel.movePlayer(KeyCode.RIGHT);
                }
                if (mouseY < myViewModel.getPlayerRow() && mouseX > myViewModel.getPlayerCol()) {
                    myViewModel.movePlayer(KeyCode.NUMPAD9); //UPRIGHT
                }
                if (mouseY > myViewModel.getPlayerRow() && mouseX > myViewModel.getPlayerCol()) {
                    myViewModel.movePlayer(KeyCode.NUMPAD3); //DOWNRIGHT
                }
                if (mouseX < myViewModel.getPlayerCol() && mouseY < myViewModel.getPlayerRow()) {
                    myViewModel.movePlayer(KeyCode.NUMPAD7); //UPLEFT
                }
                if (mouseX < myViewModel.getPlayerCol() && mouseY > myViewModel.getPlayerRow()) {
                    myViewModel.movePlayer(KeyCode.NUMPAD1); //DOWNLEFT
                }

            mouseEvent.consume();
        }

    /**
     * @param scroll to manage zoom in and zoom out event
     */
    public void scroll(ScrollEvent scroll) {

        double zoom_fac =0.1 ;
        if (scroll.isControlDown())
        {
           if(scroll.getDeltaY()<0)
           {
               zoom_fac=-0.1;
           }
            Scale newScale = new Scale();
            newScale.setX(mazeDisplayer.getScaleX() + zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() + zoom_fac);
            newScale.setPivotX(mazeDisplayer.getScaleX());
            newScale.setPivotY(mazeDisplayer.getScaleY());
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }

    /**
     * @param o the observable class to update
     * @param arg the update notification when operation is complete
     */
    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "loaded a maze" -> mazeGenerated();
            case "updated player position" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "invalid params" -> invalidParamAlert("Invalid parameter entered.\nPlease enter an integer between 2 to 1000.");
            case "reached goal position" -> reachedGoal();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    /**
     * the function that passes the solution to the maze displayer
     */
    private void mazeSolved()
    {
        mazeDisplayer.setSolution(myViewModel.getSolution());
    }

    /**
     * the function that passes when reached the goal to the maze displayer
     */
    private void reachedGoal()
    {
        try{
            mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(),myViewModel.getPlayerCol());
            chooseMusic("goal");
            Stage newStage = new Stage();
            newStage.setTitle("Good Game!");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("reachedGoalPosition.fxml"));
            Scene scene = new Scene(root, 800, 500);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setOnHiding((new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                  chooseMusic("maze");
                }}));
            newStage.showAndWait();
        }catch (Exception e){ }
    }

    /**
     * the function that passes that player moved and the now position to the maze displayer
     */
    private void playerMoved() {
        setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
    }

    /**
     * the function that passes that the new maze is generated to the maze displayer
     */
    protected void mazeGenerated() {
        mazeDisplayer.setSolution(null);
        mazeDisplayer.drawMaze(myViewModel.getMaze(),   myViewModel.getGoalPosition());
        mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(),myViewModel.getPlayerRow());
    }
    public void choosePlayer(String player)
    {
        mazeDisplayer.changePlayer(player);
    }

    /**
     * @param event on save button click
     */
    public void save(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze")
        );
        fileChooser.setInitialFileName("mazeGame");
        File file = fileChooser.showSaveDialog(getStage());
        if (file != null) {
            myViewModel.saveMaze(file.getPath());
        }
    }

    /**
     * @param event on load button click
     */
    public void load(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null)
        {
            myViewModel.loadMaze(file.getPath());
        }
    }

    /**
     * @param event on new button click
     */
    public void New(ActionEvent event)
    {
        try{
            switchScene("generate.fxml",getStage());

        }catch (Exception e){ }

    }
    /**
     * getters and setters
     */
    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }
}


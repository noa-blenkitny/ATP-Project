package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class mazeWindowController extends AView implements Initializable, Observer {
    //public MyViewModel myViewModel;


    public void setViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
        this.myViewModel.addObserver(this);
    }

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        mazeDisplayer.changePlayer(choosePlayerController.getChosenPlayer());

    }

    public void generateMaze(ActionEvent actionEvent) {
        String rows = textField_mazeRows.getText();
        String cols = textField_mazeColumns.getText();
        myViewModel.generateMaze(rows, cols);
    }

    //public void MazeByHardness(int rows, int cols) {
        //myViewModel.generateMaze(rows, cols);
   // }
    public void solveMaze(ActionEvent actionEvent) {
        myViewModel.solveMaze();
    }



    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.movePlayer(keyEvent.getCode());
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
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
           // mazeDisplayer.requestFocus();
        }

    public void scroll(ScrollEvent scroll) {
//        if (scroll.isControlDown()) {
//            double zoom_fac = 1.05;
//            if (scroll.getDeltaY() < 0)
//            {
//                zoom_fac = 2.0 - zoom_fac;
//            }
//            Scale newScale = new Scale();
//            newScale.setPivotX(scroll.getX());
//            newScale.setPivotY(scroll.getY());
//            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
//            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
//            mazeDisplayer.getTransforms().add(newScale);
//            scroll.consume();
//        }
        //namas version- zooms to top left corner
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

    private void mazeSolved()
    {
        mazeDisplayer.setSolution(myViewModel.getSolution());
    }

    private void reachedGoal()
    {
        try{
            mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(),myViewModel.getPlayerCol());
            Stage newStage = new Stage();
            newStage.setTitle("Good Game!");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("reachedGoalPosition.fxml"));
            Scene scene = new Scene(root, 800, 500);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }catch (Exception e){ }
    }
    private void playerMoved() {
        setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
    }


    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;
            }
            Scale newScale = new Scale();
            newScale.setPivotX(scroll.getX());
            newScale.setPivotY(scroll.getY());
            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }
    protected void mazeGenerated() {
        mazeDisplayer.setSolution(null);
        mazeDisplayer.drawMaze(myViewModel.getMaze(),   myViewModel.getGoalPosition());
        mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(),myViewModel.getPlayerRow());
    }
    public void choosePlayer(String player)
    {
        mazeDisplayer.changePlayer(player);
    }

    public void save(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(("./resources")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze")
        );
        fileChooser.setInitialFileName("mazeGame");
        File file = fileChooser.showSaveDialog(getStage());
        if (file != null) {
            myViewModel.saveMaze(file.getPath());
        }
    }

    public void load(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./resources"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null)
        {
            myViewModel.loadMaze(file.getPath());
        }
    }

    public void New(ActionEvent event)
    {
        try{
            switchScene("generate.fxml",getStage());

        }catch (Exception e){ }

    }
}


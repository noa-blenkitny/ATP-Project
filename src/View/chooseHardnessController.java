package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Random;
import java.util.Observable;

public class chooseHardnessController extends AView{

    public void genareteMazeByHardness(int rows, int cols)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MazeWindow.fxml"));
        try {
            myViewModel.startServers();
            Parent root = fxmlLoader.load();
            mazeWindowController newView = fxmlLoader.getController();
            newView.MazeByHardness(rows, cols);
            newView.setViewModel(this.myViewModel);
            Scene newScene = new Scene(root,1000,650);

            getStage().setScene(newScene);
            getStage().show();
        }
        catch (Exception e)
        {}
    }





    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findFirst().getAsInt();
    }

    public void update(Observable o, Object arg) {

    }



    public void hardMaze(ActionEvent actionEvent)
    {
        genareteMazeByHardness(getRandomNumber(100,500), getRandomNumber(100,500));
        //switchScene("MazeWindow.fxml", getStage());

    }

    public void mediumMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        genareteMazeByHardness(getRandomNumber(20,100), getRandomNumber(20,100));

    }

    public void easyMaze(ActionEvent actionEvent)
    {
        genareteMazeByHardness(getRandomNumber(2,20), getRandomNumber(2,10));
        switchScene("MazeWindow.fxml", getStage());
    }

}

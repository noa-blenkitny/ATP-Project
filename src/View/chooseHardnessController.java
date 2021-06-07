package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Random;
import java.util.Observable;

public class chooseHardnessController extends AView{
    

    public String getRandomNumber(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.ints(min, max).findFirst().getAsInt());
    }

    public void update(Observable o, Object arg) {

    }



    public void hardMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        myViewModel.startServers();
        myViewModel.generateMaze(getRandomNumber(100,200), getRandomNumber(100,200));
        //genareteMazeByHardness(getRandomNumber(100,500), getRandomNumber(100,500));

    }

    public void mediumMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        myViewModel.startServers();
        myViewModel.generateMaze(getRandomNumber(30,100), getRandomNumber(30,100));
        //switchScene("MazeWindow.fxml", getStage());
        //genareteMazeByHardness(getRandomNumber(20,100), getRandomNumber(20,100));

    }

    public void easyMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        myViewModel.startServers();
        myViewModel.generateMaze(getRandomNumber(10,30), getRandomNumber(10,30));
       // genareteMazeByHardness(getRandomNumber(2,20), getRandomNumber(2,10));
        //switchScene("MazeWindow.fxml", getStage());
    }


    public void costumMaze(ActionEvent actionEvent)
    {
        switchScene("generate.fxml", getStage());
    }
}

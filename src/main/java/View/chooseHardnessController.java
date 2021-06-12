package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Random;
import java.util.Observable;

public class chooseHardnessController extends AView{


    /**
     * @param min the lower bound of the number you want to generate
     * @param max the upper bound of the number you want to generate
     * @return a string in number format of a random int in range (min,max)
     */
    public String getRandomNumber(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.ints(min, max).findFirst().getAsInt());
    }


    public void update(Observable o, Object arg) {

    }


    /**
     * generates a maze of random size in the range (50,100)
     * @param actionEvent the event that occurs when you press the hard button
     */
    public void hardMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        myViewModel.startServers();
        myViewModel.generateMaze(getRandomNumber(50,100), getRandomNumber(50,100));
    }
    /**
     * generates a maze of random size in the range (30,50)
     * @param actionEvent the event that occurs when you press the medium button
     */
    public void mediumMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        myViewModel.startServers();
        myViewModel.generateMaze(getRandomNumber(30,50), getRandomNumber(30,50));
    }

    /**
     * generates a maze of random size in the range (10,30)
     * @param actionEvent the event that occurs when you press the easy button
     */
    public void easyMaze(ActionEvent actionEvent)
    {
        switchScene("MazeWindow.fxml", getStage());
        myViewModel.startServers();
        myViewModel.generateMaze(getRandomNumber(10,30), getRandomNumber(10,30));
    }


    /**
     * transform to a new scene letting you enter the size of the maze
     * @param actionEvent the event that occurs when you press the custom button
     */
    public void costumMaze(ActionEvent actionEvent)
    {
        switchScene("generate.fxml", getStage());
    }
}

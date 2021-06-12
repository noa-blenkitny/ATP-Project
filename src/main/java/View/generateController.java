package View;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class generateController extends AView {

    public TextField rowText;
    public TextField colText;

    /**
     * generates a maze in the size you enter, show alert if the size is not an integer between 2 to 1000
     * @param actionEvent the event that occurs when you press the generate button
     */
    public void customSizeMazeGenerator(ActionEvent actionEvent) {
        String rows = rowText.getText();
        String cols = colText.getText();
        if(myViewModel.checkMazeGenarationParams(rows, cols) == true)
        {
            switchScene("MazeWindow.fxml", getStage());
            myViewModel.startServers();
            myViewModel.generateMaze(rows, cols);
        }
        else
        {
            invalidParamAlert("Invalid parameter entered.\nPlease enter an integer between 2 to 1000.");
        }

    }
    @Override
    public void update(Observable o, Object arg) {

    }


}

package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Observable;

public class choosePlayerController extends AView{


    public static String chosenPlayer;
    @Override
    public void update(Observable o, Object arg) {

    }
    public void changePlayer(String player)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MazeWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            mazeWindowController newView = fxmlLoader.getController();
            newView.choosePlayer(player);
            chosenPlayer = player;
        }
        catch (Exception e)
        {}
    }

    public static String getChosenPlayer() {
        return chosenPlayer;
    }

    public void women(ActionEvent actionEvent) {
        chosenPlayer = "women";
        switchScene("chooseHardness.fxml",getStage());
      // changePlayer("women");
    }

    public void monkey(ActionEvent actionEvent) {
        chosenPlayer = "monkey";
        switchScene("chooseHardness.fxml",getStage());
        //changePlayer("monkey");
    }

    public void fox(ActionEvent actionEvent) {
        chosenPlayer = "fox";
        switchScene("chooseHardness.fxml",getStage());
        //changePlayer("fox");
    }

    public void gazelle(ActionEvent actionEvent) {
        chosenPlayer = "gazelle";
        switchScene("chooseHardness.fxml",getStage());
        //changePlayer("gazelle");
    }

    public void child(ActionEvent actionEvent) {
        chosenPlayer = "child";
        switchScene("chooseHardness.fxml",getStage());
        //changePlayer("child");
    }

    public void man(ActionEvent actionEvent) {
        chosenPlayer = "man";
        switchScene("chooseHardness.fxml",getStage());
       // changePlayer("man");
    }
}

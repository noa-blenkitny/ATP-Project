package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Observable;

public class choosePlayerController extends AView{

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
        }
        catch (Exception e)
        {}
    }


    public void women(ActionEvent actionEvent) {
       switchScene("chooseHardness.fxml",getStage());
       changePlayer("women");
    }

    public void monkey(ActionEvent actionEvent) {
        switchScene("chooseHardness.fxml",getStage());
        changePlayer("monkey");
    }

    public void fox(ActionEvent actionEvent) {
        switchScene("chooseHardness.fxml",getStage());
        changePlayer("fox");
    }

    public void gazelle(ActionEvent actionEvent) {
        switchScene("chooseHardness.fxml",getStage());
        changePlayer("gazelle");
    }

    public void child(ActionEvent actionEvent) {
        switchScene("chooseHardness.fxml",getStage());
        changePlayer("child");
    }

    public void man(ActionEvent actionEvent) {
        switchScene("chooseHardness.fxml",getStage());
        changePlayer("man");
    }
}

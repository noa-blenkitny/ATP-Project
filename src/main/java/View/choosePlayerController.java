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

    /**
     * @return the chosen player
     */
    public static String getChosenPlayer() {
        return chosenPlayer;
    }

    /**
     * choose the women as the player
     * @param actionEvent the event that occurs when you press the women button
     */
    public void women(ActionEvent actionEvent) {
        chosenPlayer = "women";
        switchScene("chooseHardness.fxml",getStage());
    }

    /**
     * choose the monkey as the player
     * @param actionEvent the event that occurs when you press the monkey button
     */
    public void monkey(ActionEvent actionEvent) {
        chosenPlayer = "monkey";
        switchScene("chooseHardness.fxml",getStage());
    }

    /**
     * choose the fox as the player
     * @param actionEvent the event that occurs when you press the fox button
     */
    public void fox(ActionEvent actionEvent) {
        chosenPlayer = "fox";
        switchScene("chooseHardness.fxml",getStage());
    }

    /**
     * choose the gazelle as the player
     * @param actionEvent the event that occurs when you press the gazelle button
     */
    public void gazelle(ActionEvent actionEvent) {
        chosenPlayer = "gazelle";
        switchScene("chooseHardness.fxml",getStage());

    }

    /**
     * choose the child as the player
     * @param actionEvent the event that occurs when you press the child button
     */
    public void child(ActionEvent actionEvent) {
        chosenPlayer = "child";
        switchScene("chooseHardness.fxml",getStage());
    }

    /**
     * choose the man as the player
     * @param actionEvent the event that occurs when you press the man button
     */
    public void man(ActionEvent actionEvent) {
        chosenPlayer = "man";
        switchScene("chooseHardness.fxml",getStage());
    }
}

package View;


import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.Observable;

public class MyViewController extends AView  {

    @Override
    public void update(Observable o, Object arg) {

    }

    public void startScene(ActionEvent actionEvent)
    {
        switchScene("choosePlayer.fxml",getStage());
    }

}
